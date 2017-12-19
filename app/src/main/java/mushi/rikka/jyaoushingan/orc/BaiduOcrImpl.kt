package mushi.rikka.jyaoushingan.orc

import android.content.Context
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken

/**
 * ocr接口试试
 * Created by Tan.Yangfan on 2017/12/19.
 */
class BaiduOcrImpl:IOcr {

    override fun init(context: Context) {
        OCR.getInstance().initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
            }

            override fun onError(error: OCRError) {
                error.printStackTrace()
            }
        }, context)
    }

    override fun hasInit(): Boolean {
        return !OCR.getInstance().hasGotToken()
    }

    override fun release() {
        OCR.getInstance().release()
    }

}