package com.dn.baselib.util

import android.util.Log

/**
 * Describe:ListUtil
 *
 * Created By yangb on 2020/10/10
 */
object ListUtil {

    fun <T> getItem(iterable: Iterable<T>?, index: Int): T? {
        if (isEmpty(iterable)) {
            return null
        }
        try {
            return iterable?.elementAt(index)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> getSize(iterable: Iterable<T>?) = iterable?.count() ?: 0

    fun <T> isEmpty(iterable: Iterable<T>?) = getSize(iterable) <= 0

    fun <T> print(tag: String, iterable: Iterable<T>?) {
        Log.i(tag, "---------start:size=" + iterable?.count() + "-------")
        if (iterable != null) {
            for (item in iterable) {
                Log.i(tag, item.toString())
            }
        }
        Log.i(tag, "---------end----------")
    }

    inline fun <reified T> isFirst(iterable: Iterable<Any>?, item : T) : Boolean{
        if(iterable == null){
            return false
        }
        var result = false
        for (i in iterable) {
            if (i is T) {
                result = i == item
                break
            }
        }
        return result
    }

    fun <T> getListClass() : Class<List<T>>{
        val list = ArrayList<T>()
        return list.javaClass
    }

}