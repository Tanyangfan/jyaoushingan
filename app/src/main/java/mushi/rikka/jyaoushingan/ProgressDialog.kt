package mushi.rikka.jyaoushingan

import android.R
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar


/**
 * 进度条对话框
 * Created by Tan.Yangfan on 2017/12/11.
 */
class ProgressDialog : AlertDialog {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, theme: Int) : super(context, theme) {
        init()
    }

    private fun init() {

        val window = window
        requestWindowFeature(Window.FEATURE_NO_TITLE)  //去掉dialog的title
        window!!.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)  //全屏的flag
        window.setBackgroundDrawableResource(android.R.color.transparent) //设置window背景透明
        val lp = window.attributes
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        lp.alpha = 1.0f
        lp.dimAmount = 0.0f //dimAmount在0.0f和1.0f之间，0.0f完全不暗，1.0f全暗
        window.attributes = lp

        val root = LinearLayout(context)
        root.setHorizontalGravity(Gravity.CENTER_HORIZONTAL)
        val rootLp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        root.layoutParams = rootLp

        val progressBar = ProgressBar(context, null, R.attr.progressBarStyle)
        val barLp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        progressBar.layoutParams = barLp
        root.addView(progressBar)

        setView(root)
    }
}