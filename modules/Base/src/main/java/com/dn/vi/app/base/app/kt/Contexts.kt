package com.dn.vi.app.base.app.kt

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dn.vi.app.cm.log.VLog

/**
 * Android 组件相关的一些扩展
 * Created by holmes on 2020/5/19.
 **/

/**
 * 递归出，context对应的activity。
 * 如果context不是来自activity，则返回null.
 *
 * 多用于 从View的context 中dump activity
 *
 * @param context 如果为null，只dump 调用者[Context]
 */
fun Context.ofActivity(context: Context? = null): AppCompatActivity? {
    var activity: Context? = context ?: this
    do {
        if (activity is AppCompatActivity) {
            return activity
        } else if (activity is Application) {
            activity = null
            break
        } else if (activity is Service) {
            activity = null
            break
        } else if (activity is ContextWrapper) {
            activity = activity.baseContext
        } else {
            VLog.w("wtf class? ${activity?.javaClass?.simpleName}")
            activity = null
        }
    } while (activity != null)

    return null
}

/**
 * 显示一个Toast
 */
fun Context.tip(msg: CharSequence) {
    val context = ofActivity() ?: this.applicationContext
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

/**
 * 显示一个Toast
 */
fun Context.tip(@StringRes msgRes: Int) {
    val context = ofActivity() ?: this.applicationContext
    Toast.makeText(context, msgRes, Toast.LENGTH_SHORT).show()
}

/**
 * 显示一个Toast
 */
fun Fragment.tip(msg: CharSequence) {
    val activity = activity ?: return
    val context = activity.ofActivity(activity) ?: activity.applicationContext
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

/**
 * 显示一个Toast
 */
fun Fragment.tip(@StringRes msgRes: Int) {
    val activity = activity ?: return
    val context = activity.ofActivity(activity) ?: activity.applicationContext
    Toast.makeText(context, msgRes, Toast.LENGTH_SHORT).show()
}

