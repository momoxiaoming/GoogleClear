package com.dn.vi.app.cm.utils

import com.google.gson.Gson
import java.lang.reflect.Type


/**
 * Describe: json工具
 *
 * Created By yangb on 2020/9/4
 */
object JsonUtil {

    fun toJson(obj: Any): String? {
        try {
            return Gson().toJson(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> fromJson(str: String, type: Type): T? {
        try {
            return Gson().fromJson(str, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> fromJson(str: String, clazz: Class<T>): T? {
        try {
            return Gson().fromJson(str, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}