package com.dn.vi.app.cm.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * 获取 Manifest meta 信息
 * Created by holmes on 17-9-12.
 */
object MetaData {

    private val cache = HashMap<String, String>(8)

    private fun fetchCache(key: String): String? {
        return cache[key]
    }

    /**
     * 获取指定key的meta值
     *
     * @param context
     * @param key
     * @param defaultValue 如果key不存在，或者key的值为null, 则返回这个默认值
     * @return
     */
    fun getMetaValue(context: Context, key: String, defaultValue: String?): String? {
        val c = fetchCache(key)
        if (c != null) {
            return c
        }

        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            val metaData = appInfo.metaData
            if (metaData != null) {
                val value = metaData.get(key)?.toString()
                if (value != null) {
                    cache[key] = value
                    return value
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return defaultValue
    }

    fun getMetaValueNoNull(context: Context, key: String, defaultValue: String = ""): String {
        return getMetaValue(context, key, defaultValue) ?: defaultValue
    }

}