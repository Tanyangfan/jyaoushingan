package mushi.rikka.jyaoushingan.orc

import android.content.Context

/**
 * ocr接口
 * Created by Tan.Yangfan on 2017/12/19.
 */
interface IOcr {

    /**
     * 初始化
     */
    fun init(context: Context)

    /**
     * 是否初始化
     */
    fun hasInit(): Boolean

    /**
     * release
     */
    fun release()
}