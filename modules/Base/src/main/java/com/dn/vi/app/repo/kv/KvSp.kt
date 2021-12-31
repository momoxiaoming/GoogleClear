package com.dn.vi.app.repo.kv

import android.content.Context
import android.content.SharedPreferences
import com.dn.vi.app.base.app.AppMod
import com.google.gson.Gson

/**
 * 使用内置 SharePreferences 来实现的
 * 兼容性高。性能一般。
 *
 * 应用场景，不跨进程，需要高兼容性，稳定性，小数据的时候使用
 *
 * 大部分场景使用[KvLite]优先，里面有更多参数属性。
 *
 * Created by holmes on 2020/10/29.
 **/
object KvSp {


    private val sp: SharedPreferences by lazy {
        AppMod.app.getSharedPreferences("kv-sp-lite", Context.MODE_PRIVATE)
    }

    private val editor: SharedPreferences.Editor
        get() = sp.edit()

    private val gson: Gson
        get() = AppMod.appComponent.getGson()

    private inline fun edit(b: SharedPreferences.Editor.() -> Unit) {
        editor.also{
            it.b()
            it.apply()
        }
    }

    /**
     * 获取一个key的值
     *
     * @return 不存的时候也会返回""
     */
    fun getKv(key: String): String {
        return sp.getString(key, "") ?: ""
    }

    /**
     * 添加，或修改一个key的值。 值为string
     * @return [text]
     */
    fun putKv(key: String, text: String): String {
        edit {
            putString(key, text)
        }
        return text
    }

    /**
     * 添加，或添加一个key的值
     * @param obj 值， 如果是string,则直接修改。其它类似，则会序列化为json，再保存
     */
    fun putObj(key: String, obj: Any): String {
        val text = when (obj) {
            is CharSequence -> obj.toString()
            else -> gson.toJson(obj)
        }
        return putKv(key, text)
    }

    /**
     * 单独设置[key]的int值
     * 会盖掉其它值
     */
    fun putInt(key: String, i: Int): Int {
        edit {
            putInt(key, i)
        }
        return i
    }

    /**
     * 获取一个key的int值
     *
     * @return 不存的时候也会返回 [defVal]
     */
    fun getInt(key: String, defVal: Int = 0): Int {
        return sp.getInt(key, defVal)
    }

    /**
     * 单独设置[key]的bool值
     * 会盖掉其它值
     */
    fun putBool(key: String, b: Boolean): Boolean {
        edit {
            putInt(key, if (b) 1 else 0)
        }
        return b
    }

    /**
     * 获取一个key的bool值
     *
     * @return 不存的时候也会返回 [defVal]
     */
    fun getBool(key: String, defVal: Boolean = false): Boolean {
        return sp.getInt(key, if (defVal) 1 else 0) != 0 ?: defVal
    }

    /**
     * 单独设置[key]的long值
     * 会盖掉其它值
     */
    fun putLong(key: String, l: Long): Long {
        edit { putLong(key, l) }
        return l
    }

    /**
     * 获取一个key的long值
     *
     * @return 不存的时候也会返回 [defVal]
     */
    fun getLong(key: String, defVal: Long = 0L): Long {
        return sp.getLong(key, defVal)
    }

    /**
     * 删除key
     */
    fun delete(key: String): Boolean {
        edit {
            remove(key)
        }
        return true
    }


}