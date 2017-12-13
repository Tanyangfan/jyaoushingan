package mushi.rikka.jyaoushingan

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import java.io.File

/**
 * App
 * Created by Tan.Yangfan on 2017/12/11.
 */
class App : Application() {

    private var captureBitmap: Bitmap? = null

    companion object {
        fun getSaveFile(context: Context): File {
            return File(context.filesDir, "pic.jpg")
        }
    }

    fun getCaptureBitmap(): Bitmap? {
        return captureBitmap
    }

    fun setCaptureBitmap(bitmap: Bitmap) {
        this.captureBitmap = bitmap
    }
}
