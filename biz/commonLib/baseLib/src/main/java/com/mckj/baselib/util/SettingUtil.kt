package com.mckj.baselib.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Describe:
 *
 * Created By yangb on 2020/12/8
 */
object SettingUtil {

    /**
     * 打开外部浏览器
     */
    fun openUrl(context: Context, url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        val resolve = intent.resolveActivity(context.packageManager)
        if (resolve != null) {
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
            return true
        }
        return false
    }

}