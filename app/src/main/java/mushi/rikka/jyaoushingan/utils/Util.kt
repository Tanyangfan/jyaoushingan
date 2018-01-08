package mushi.rikka.jyaoushingan.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import android.widget.Toast

/**
 * 工具类
 * Created by Tan.Yangfan on 2017/12/25.
 */

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun SharedPreferences.saveSp(f: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    f(editor)
    editor.apply()
}

fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            context.resources.displayMetrics
    )
}