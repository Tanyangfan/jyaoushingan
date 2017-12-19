package mushi.rikka.jyaoushingan.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Activity基类
 * Created by Tan.Yangfan on 2017/12/19.
 */
open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(getTag(), "onCreate")

        lifecycle.addObserver(BaseObserver())
    }

    fun getTag(): String? {
        return javaClass.name
    }
}
