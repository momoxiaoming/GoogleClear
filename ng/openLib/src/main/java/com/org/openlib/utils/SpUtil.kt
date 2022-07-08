package com.org.openlib.utils

import android.content.Context
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil

/**
 * SpUtil
 *
 * @author mmxm
 * @date 2022/7/5 20:06
 */
object SpUtil {

    private const val SHARED_NAME = "openapi_pref"


    private val shareReadAble by lazy {
        AppMod.app.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    private val shareWriteAble by lazy {
        shareReadAble.edit()
    }

    fun <T> put(key: String, value: T) {
        when (value) {
            is Int -> {
                shareWriteAble.putInt(key, value)
            }
            is Long -> {
                shareWriteAble.putLong(key, value)
            }
            is Boolean -> {
                shareWriteAble.putBoolean(key, value)
            }
            is String -> {
                shareWriteAble.putString(key, value)
            }
            is Float -> {
                shareWriteAble.putFloat(key, value)
            }
        }
        shareWriteAble.commit()
    }

    fun getInt(key: String, default: Int = 0): Int {
        return shareReadAble.getInt(key, default)
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return shareReadAble.getLong(key, default)
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return shareReadAble.getFloat(key, default)
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return shareReadAble.getBoolean(key, default)
    }

    fun getString(key: String, default: String = ""): String {
        return shareReadAble.getString(key, default) ?: default
    }

}