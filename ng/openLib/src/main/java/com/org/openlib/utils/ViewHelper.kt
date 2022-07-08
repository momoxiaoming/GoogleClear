package com.org.openlib.utils

import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import java.util.concurrent.TimeUnit

/**
 * Describe:
 *
 * Created By yangb on 2020/10/28
 */

fun View.onceClick(listener: (view: View) -> Unit) {
    this.clicks()
        .throttleFirst(500L, TimeUnit.MILLISECONDS)
        .subscribe {
            listener(this)
        }
}

fun View.onceClick(listener: View.OnClickListener) {
    this.clicks()
        .throttleFirst(500L, TimeUnit.MILLISECONDS)
        .subscribe {
            listener.onClick(this)
        }
}