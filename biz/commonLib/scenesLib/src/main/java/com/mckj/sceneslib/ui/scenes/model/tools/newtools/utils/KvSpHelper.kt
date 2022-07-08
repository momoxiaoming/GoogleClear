package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import com.tencent.mmkv.MMKV

object KvSpHelper {


    private val kvSp by lazy {
        MMKV.defaultMMKV()
    }

    fun putString(key:String,value:String){
        kvSp.encode(key,value)
    }
    fun getString(key: String)= kvSp.decodeString(key)


    fun putBoolean(key:String,value:Boolean){
        kvSp.encode(key,value)
    }

    fun getBoolean(key: String)= kvSp.decodeBool(key,false)


    fun putLong(key: String,value: Long){
        kvSp.encode(key,value)
    }

    fun getLong(key: String) = kvSp.decodeLong(key,-1L)
}