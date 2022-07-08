package com.mckj.module.cleanup.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Method

object CleanSpUtil {
    private const val FILE_NAME = "clean_sp"
    private var sp: SharedPreferences? = null

    private fun init(context: Context): SharedPreferences {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).also {
            sp = it
        }
    }


    /**
     * 插件间和宿主共用数据 必须 传入context
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    fun <E> put(context: Context, key: String, value: E) {
        val editor = init(context).edit()
        if (value == null) {
            editor.putString(key, "")
            SPCompat.apply(editor)
            return
        }
        if (value is String || value is Int || value is Boolean || value is Float || value is Long || value is Double) {
            editor.putString(key, value.toString())
        } else {
            editor.putString(key, Gson().toJson(value))
        }
        SPCompat.apply(editor)
    }


    /**
     * 插件间和宿主共用数据 必须 传入context
     *
     * @param key
     * @param defaultValue
     * @return
     */
    operator fun <E> get(context: Context, key: String, defaultValue: E): E {
        val value = init(context).getString(key, defaultValue.toString())
        if (defaultValue is String) {
            return value as E
        }
        if (defaultValue is Int) {
            return Integer.valueOf(value!!) as E
        }
        if (defaultValue is Boolean) {
            return java.lang.Boolean.valueOf(value) as E
        }
        if (defaultValue is Float) {
            return java.lang.Float.valueOf(value!!) as E
        }
        if (defaultValue is Long) {
            return java.lang.Long.valueOf(value!!) as E
        }
        return if (defaultValue is Double) {
            java.lang.Double.valueOf(value!!) as E
        } else Gson().fromJson(value, defaultValue!!::class.java) as E

    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    fun remove(context: Context, key: String?) {
        val editor = init(context).edit()
        editor.remove(key)
        SPCompat.apply(editor)
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    fun clear(context: Context) {
        val editor = init(context).edit()
        editor.clear()
        SPCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    fun contains(context: Context, key: String?): Boolean {
        return init(context).contains(key)
    }


    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    fun getAll(context: Context): Map<String?, *>? {
        return init(context).all
    }

    /**
     * 保存对象到sp文件中 被保存的对象须要实现 Serializable 接口
     *
     * @param key
     * @param value
     */
    fun saveObject(context: Context, key: String, value: Any?) {
        put(context, key, value)
    }

    /**
     * desc:获取保存的Object对象
     *
     * @param key
     * @return modified:
     */
    fun <T> readObject(context: Context, key: String, clazz: Class<T>): T? {
        try {
            return get(context, key, clazz.newInstance()) as T
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author
     */
    private object SPCompat {
        private val S_APPLY_METHOD = findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (S_APPLY_METHOD != null) {
                    S_APPLY_METHOD.invoke(editor)
                    return
                }
            } catch (e: Exception) {
            }
            editor.commit()
        }
    }
}