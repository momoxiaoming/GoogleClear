package com.dn.vi.app.base.app

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

/**
 *  provide Context
 * Created by holmes on 2020/7/16.
 **/
interface ContextProvider {

    fun getContext(): Context

}

/**
 *  provide Context which is a activity
 */
interface ActivityProvider : ContextProvider


fun Activity.toActivityProvider(): ActivityProvider {
    return ActivityProviderImpl(this)
}

fun Context.toActivityProvider(): ActivityProvider {
    return ActivityProviderImpl(this as Activity)
}

fun Fragment.toActivityProvider(): ActivityProvider {
    return LazyActivityProviderImpl(lazy { this.requireActivity() })
}

private class ActivityProviderImpl(private val a: Activity) : ActivityProvider {
    override fun getContext(): Context {
        return a
    }
}

private class LazyActivityProviderImpl(private val a: Lazy<Activity>) : ActivityProvider {
    override fun getContext(): Context {
        return a.value
    }
}
