package com.mckj.baselib.view

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.mckj.baselib.R
import com.mckj.baselib.helper.getApplicationContext
import com.mckj.baselib.util.ResourceUtil

/**
 * Describe:ToastUtil
 *
 * Created By yangb on 2020/9/25
 */
object ToastUtil {

    private val mainHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * 安全显示土司
     */
    fun postShow(@StringRes resId: Int) {
        mainHandler.post { show(resId) }
    }

    /**
     * 安全显示土司
     */
    fun postShow(msg: CharSequence?) {
        mainHandler.post { show(msg) }
    }

    /**
     * 安全显示土司
     */
    fun postShowLong(msg: CharSequence?) {
        mainHandler.post { showLong(msg) }
    }

    fun show(@StringRes resId: Int) {
        show(ResourceUtil.getText(resId))
    }

    fun show(msg: CharSequence?) {
        show(msg, Toast.LENGTH_SHORT)
    }

    fun showLong(msg: CharSequence?) {
        show(msg, Toast.LENGTH_LONG)
    }

    /**
     * 显示土司
     *
     * @param msg 显示内容
     * @param duration 显示时长
     */
    fun show(msg: CharSequence?, duration: Int) {
        if (msg == null) {
            return
        }
        showBlackBgToast(msg, Gravity.CENTER, duration)
//        Toast.makeText(getApplication(), msg, duration).show()
    }

    fun postShowBlackBgToast(msg: CharSequence?, gravity: Int, duration: Int) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showBlackBgToast(msg, gravity, duration)
        } else {
            mainHandler.post { showBlackBgToast(msg, gravity, duration) }
        }
    }

    private fun showBlackBgToast(msg: CharSequence?, gravity: Int, duration: Int) {
        val toast = Toast(getApplicationContext())
        val view =
            LayoutInflater.from(getApplicationContext()).inflate(R.layout.base_black_toast, null)
        view.let {
            (it as TextView).text = msg
            toast.view = it
            toast.duration = duration
            toast.setGravity(gravity, 0, 0)
            toast.show()
        }

    }
}
