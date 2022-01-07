package com.dn.baselib.ext

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.base.app.AppMod

/**
 * Describe:
 *
 * Created By yangb on 2020/9/25
 */
var mApplication: Application? = null

fun getApplication(): Application = mApplication ?: AppMod.app

fun getApplicationContext(): Context = getApplication().applicationContext

fun getGlobalResource(): Resources = getApplication().resources

fun showToast(msg: String?) {
    Toast.makeText(getApplication(),msg,Toast.LENGTH_SHORT).show()
}

fun Context.toActivity(): FragmentActivity? {
    if (this is FragmentActivity) {
        return this
    } else if (this is ContextWrapper) {
        return this.baseContext as? FragmentActivity
    }
    return null
}



/**
 * if else 扩展
 * @receiver Boolean
 * @param yesBlock Function0<T>
 * @param noBlock Function0<T>
 * @return T
 */
inline fun <T> Boolean.IE(yesBlock: (() -> T), noBlock: (() -> T)): T {
    return if (this) {
        yesBlock.invoke()
    } else {
        noBlock.invoke()
    }
}