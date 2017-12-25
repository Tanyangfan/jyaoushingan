package mushi.rikka.jyaoushingan.orc

import android.content.Context

/**
 * 图像识别
 * Created by Tan.Yangfan on 2017/12/19.
 */
object OcrManager {

    private var ocrImpl: IOcr = BaiduOcrImpl()

    fun init(context: Context) {
        ocrImpl.init(context)
    }

    fun hasInit(): Boolean {
        return ocrImpl.hasInit()
    }

    fun release() {
        ocrImpl.release()
    }

}