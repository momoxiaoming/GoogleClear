package com.dn.baselib.ext

import android.content.Context
import com.dn.vi.app.base.app.AppMod

/**
 * ResourceExt
 *
 * @author mmxm
 * @date 2022/1/6 9:49
 */

fun Context.idToString(idRes: Int): String {
    return resources.getString(idRes)
}
fun idToString(idRes: Int): String {
    return AppMod.app.resources.getString(idRes)
}