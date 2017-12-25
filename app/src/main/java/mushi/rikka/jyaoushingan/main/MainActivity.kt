package mushi.rikka.jyaoushingan.main

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import com.baidu.ocr.ui.camera.CameraActivity
import kotlinx.android.synthetic.main.activity_main.*
import mushi.rikka.jyaoushingan.App
import mushi.rikka.jyaoushingan.ProgressDialog
import mushi.rikka.jyaoushingan.R
import mushi.rikka.jyaoushingan.RecognizeService
import mushi.rikka.jyaoushingan.base.BaseActivity
import mushi.rikka.jyaoushingan.orc.OcrManager
import mushi.rikka.jyaoushingan.screencapture.FloatWindowsService
import mushi.rikka.jyaoushingan.utils.showToast
import java.io.File


class MainActivity : BaseActivity() {

    private val REQUEST_CODE_ACCURATE_BASIC = 101
    private val REQUEST_MEDIA_PROJECTION = 102
    private val REQUEST_OVERLAY = 103

    private var dialog: ProgressDialog? = null

    companion object {
        class CaptureEvent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iniAccessToken()

        goBtn.setOnClickListener({
            takePhoto()
        })

        captureBtn.setOnClickListener({
            if (!OcrManager.instance.hasInit()) {
                return@setOnClickListener
            }
            requestCapturePermission()
        })

        closeBtn.setOnClickListener({
            stopService(Intent(applicationContext, FloatWindowsService::class.java))
        })

        copyBtn.setOnClickListener({
            doCopy()
        })

        resultTxt.movementMethod = ScrollingMovementMethod.getInstance()

        cropPhoto()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        cropPhoto()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ACCURATE_BASIC) { // 识别成功回调，通用文字识别（高精度版）
                doRecognize()
            } else if (requestCode == REQUEST_MEDIA_PROJECTION && data != null) {
                FloatWindowsService.setResultData(data)
                checkFloatWindowPermission()
//                startService(Intent(applicationContext, FloatWindowsService::class.java))
            }
        }

        if (requestCode == REQUEST_CODE_ACCURATE_BASIC) {
            var file: File = App.getSaveFile(applicationContext)
            if (file.exists()) {
                file.delete()
            }
        } else if (requestCode == REQUEST_OVERLAY) {
            checkFloatWindowPermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        OcrManager.instance.release()
    }

    private fun takePhoto() {
        if (!OcrManager.instance.hasInit()) {
            return
        }

        val intent = Intent(this@MainActivity, CameraActivity::class.java)
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                App.getSaveFile(applicationContext).absolutePath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL)
        startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC)
    }

    private fun cropPhoto() {
//        if (!hasGotToken) {
//            return
//        }

        val file: File = App.getSaveFile(applicationContext)
        if (!file.exists()) {
            return
        }
        val intent = Intent(this@MainActivity, CameraActivity::class.java)
        intent.putExtra(CameraActivity.KEY_MODE, CameraActivity.MODE_SHOW_CROP)
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                App.getSaveFile(applicationContext).absolutePath)
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL)
        startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC)
    }

    private fun showResult(result: String) {
        cancelProcess()
        resultTxt.text = result
    }

    /**
     * 初始化Token
     */
    private fun iniAccessToken() {
        OcrManager.instance.init(applicationContext)
    }

    private fun doCopy() {
        val cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", resultTxt.text)
        cmb.primaryClip = clip
        showToast("copy success!")
    }

    private fun showProcess() {
        if (dialog == null) {
            dialog = ProgressDialog(this)
        }
        if (dialog!!.isShowing) {
            dialog?.dismiss()
        }
        dialog?.show()
    }

    private fun cancelProcess() {
        dialog?.dismiss()
    }

    private fun requestCapturePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }

        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION)
    }

    private fun checkFloatWindowPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }

        if (Settings.canDrawOverlays(this)) {
            startService(Intent(applicationContext, FloatWindowsService::class.java))
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + packageName))
            startActivityForResult(intent, REQUEST_OVERLAY)
        }
    }

    private fun doRecognize() {
        var file: File = App.getSaveFile(applicationContext)
        if (file.exists()) {
            showProcess()
            RecognizeService
                    .recAccurateBasic(App.getSaveFile(applicationContext).absolutePath)
                    { result ->
                        run {
                            showResult(result)
                            file.delete()
                        }
                    }
        }
    }
}
