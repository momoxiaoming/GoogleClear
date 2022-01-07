package com.dn.openlib.ui

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.dn.openlib.OpenLibARouterPath
import com.dn.vi.app.base.app.kt.arouter

/**
 * Describe:
 *
 * Created By yangb on 2020/12/22
 */
fun Context.startFragment(fragmentPath: String) {
    startFragment(OpenLibARouterPath.ACTIVITY_CONTAINER, fragmentPath)
}

fun Context.startFragment(fragmentPath: String, block: (Postcard) -> Unit) {
    startFragment(OpenLibARouterPath.ACTIVITY_CONTAINER, fragmentPath, block)
}

fun Context.startTitleFragment(fragmentPath: String) {
    startFragment(OpenLibARouterPath.ACTIVITY_CONTAINER_TITLE, fragmentPath)
}

fun Context.startTitleFragment(fragmentPath: String, block: (Postcard) -> Unit) {
    startFragment(OpenLibARouterPath.ACTIVITY_CONTAINER_TITLE, fragmentPath, block)
}

fun Context.startFullFragment(fragmentPath: String) {
    startFragment(OpenLibARouterPath.ACTIVITY_CONTAINER_FULL, fragmentPath)
}

fun Context.startFullFragment(fragmentPath: String, block: (Postcard) -> Unit) {
    startFragment(OpenLibARouterPath.ACTIVITY_CONTAINER_FULL, fragmentPath, block)
}

/**
 * 通过Activity容器，跳转Activity
 *
 * @param activityPath activity路径
 * @param fragmentPath fragment路径
 */
fun Context.startFragment(activityPath: String, fragmentPath: String) {
    arouter().build(activityPath)
        .withString(OpenLibARouterPath.EXTRA_FRAGMENT_PATH, fragmentPath)
        .navigation(this)
}

fun Context.startFragment(activityPath: String, fragmentPath: String, block: (Postcard) -> Unit) {
    val postcard = arouter().build(activityPath)
        .withString(OpenLibARouterPath.EXTRA_FRAGMENT_PATH, fragmentPath)
    block(postcard)
    postcard.navigation(this)
}