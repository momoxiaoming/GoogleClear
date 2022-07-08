package com.mckj.baselib.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics
import java.util.*

/**
 * @author leix
 * @version 1
 * @createTime 2022/1/18 15:34
 * @desc 应用内语言适配
 */
object LocaleUtil {

    fun changeLanguage(context: Context, lang: String, area: String) {
        if (lang.isNullOrEmpty() || area.isNullOrEmpty()) {
            return
        }
        val locale = Locale(lang, area)
        setAppLanguage(context, locale)
    }


    private fun setAppLanguage(context: Context, locale: Locale) {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        val configuration: Configuration = resources.configuration
        //Android 7.0以上的方法
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale)
            configuration.setLocales(LocaleList(locale))
            context.createConfigurationContext(configuration)
            resources.updateConfiguration(configuration, metrics)
        } else
            //Android 4.1 以上方法
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, metrics)
    }
}