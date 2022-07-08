package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import com.google.gson.Gson
import java.lang.reflect.Type

object GsonHelper {

    val gson by lazy { Gson() }

    /**
     * 对象转json
     * @param any Any
     * @return (String..String?)
     */
    fun toJson(any: Any)= gson.toJson(any)


    /**
     * json转object
     * @param data String
     * @return T?
     */
    inline fun <reified T> jsonToObj(data: String):T?{
        return try {
          return  gson.fromJson(data, T::class.java)
        }catch (e:Exception){
             null
        }

    }

    /**
     * json转 list、map类型
     * @param data String
     * @param type Type
     * @return T?
     */
    fun <T> jsonToObj(data: String, type: Type): T? {
        return  try {
             gson.fromJson(data, type)
        }catch (e:Exception){
           null
        }
    }

}