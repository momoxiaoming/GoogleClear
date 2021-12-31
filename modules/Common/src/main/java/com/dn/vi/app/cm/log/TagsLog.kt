package com.dn.vi.app.cm.log

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 根据接口定义名。
 * 生成对应tag的Logger对象
 *
 * 定义 日志 tag接口。
 *
 * 接口方法 名为 tag, 返回值为 VLog.Logger
 *
 * ```
 * interface Logs {
 *      fun home(): VLog.Logger
 *      fun background(): VLog.Logger
 * }
 * ```
 *
 * 再调用 [TagsLog.create]，得到一上面定义 的实例
 *
 * ```
 *  val log = TagsLog.create(Logs::class.java)
 *  log.home.d("sfsdfsf")
 * ```
 *
 * Created by holmes on 2020/6/6.
 **/
object TagsLog {

    @Suppress("UNCHECKED_CAST")
    fun <T> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(
            clazz.classLoader, arrayOf(clazz),
            TagCreator()
        ) as T
    }

    private class TagCreator : InvocationHandler {

        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {

            // 如果是object的方法，就直接响应了。
            // equals, hashCode等这些
            if (method.declaringClass == Object::class.java) {
                return method.invoke(this, args)
            }

            val name = method.name
            if (method.returnType == VLog.Logger::class.java) {
                return VLog.scoped(name)
            }
            return Unit
        }

    }

}