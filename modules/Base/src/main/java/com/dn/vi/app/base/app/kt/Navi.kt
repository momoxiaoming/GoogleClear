@file:Suppress("NOTHING_TO_INLINE")

package com.dn.vi.app.base.app.kt

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.base.app.ContainerActivity
import com.dn.vi.app.base.helper.DataTransport

/**
 * 跳转 路由 导航
 * Created by holmes on 2020/5/21.
 **/

/**
 * get arouter
 */
inline fun arouter(): ARouter = ARouter.getInstance()

/**
 * 通过 ARouter拿 Fragment
 */
inline fun <reified T : Fragment> ARouter.fragment(path: String): T? =
    ARouter.getInstance().build(path).navigation() as? T

/**
 * 通过 ARouter拿 Fragment
 * 并可以通过ext，操作中间的 [Postcard]
 */
inline fun <reified T : Fragment> ARouter.fragment(path: String, ext: Postcard.() -> Unit): T? =
    ARouter.getInstance().build(path).also { it.ext() }.navigation() as? T

/**
 * 获取ARouter的extra, 并应用
 */
inline fun Postcard.withExtras(block: Bundle.() -> Unit): Postcard {
    this.extras.block()
    return this
}

/**
 * 直接通过 [activityPath]的 [ContainerActivity]，来显示 [build]出的Fragment
 */
@Deprecated("使用[ArouterContext]来跳")
inline fun ARouter.naviToFragment(activityPath: String, build: () -> Fragment) {
    val fragment = build()
    transportData {
        put(ContainerActivity.EXTRA_FRAGMENT, fragment)
    }
    ARouter.getInstance().build(activityPath).navigation()
}

/**
 * 直接通过 [activityPath]的 [ContainerActivity]，来显示 [fragmentPath]注册的Fragment
 */
@Deprecated("使用[ArouterContext]来跳")
fun ARouter.naviToFragment(activityPath: String, fragmentPath: String) {
    val fragment = arouter().fragment<Fragment>(fragmentPath) ?: return
    transportData {
        put(ContainerActivity.EXTRA_FRAGMENT, fragment)
    }
    ARouter.getInstance().build(activityPath).navigation()
}

/**
 * 带有context的arouter
 * 如果是跳转activity，则会以context为参数来跳。如果context为activity，则会以activity来启动。
 */
inline fun Context.arouterCtx(): ARouterContext = ARouterContext(this)

/**
 * 带有context的arouter
 * 如果是跳转activity，则会以context为参数来跳。如果context为activity，则会以activity来启动。
 */
inline fun Fragment.arouterCtx(): ARouterContext = ARouterContext(this.requireActivity())

/**
 * 直接通过 [activityPath]的 [ContainerActivity]，来显示 [build]出的Fragment
 */
inline fun ARouterContext.naviToFragment(activityPath: String, build: () -> Fragment) {
    val fragment = build()
    transportData {
        put(ContainerActivity.EXTRA_FRAGMENT, fragment)
    }
    this.navigation(activityPath) {}
}

/**
 * 直接通过 [activityPath]的 [ContainerActivity]，来显示 [fragmentPath]注册的Fragment
 */
fun ARouterContext.naviToFragment(activityPath: String, fragmentPath: String) {
    val fragment = arouter().fragment<Fragment>(fragmentPath) ?: return
    transportData {
        put(ContainerActivity.EXTRA_FRAGMENT, fragment)
    }
    this.navigation(activityPath) {}
}

/**
 * 进程时，临时共享(提取)数据。
 * 主要用于UI之间跳转时，数据传输
 *
 * ```
 * activityA.transportData {
 *  put("abc", AbcObj(hash=1234))
 * }
 *
 * // ... do navigate
 *
 * activityB.transportData {
 *  val obj = get("abc") as AbcObj
 *  print("${obj.hash}")    // print: 1234
 * }
 *
 * ```
 */
inline fun transportData(block: DataTransport.() -> Unit) {
    DataTransport.getInstance().block()
}

/**
 * 从DataTransport里面直接拿出对应[key]的类型为 [T]的数据。
 * @return 如果没有[key]的数据或者，数据类型不是 [T], 则返回 null
 */
inline fun <reified T> DataTransport.getAs(key: Any): T? {
    return DataTransport.getInstance().get(key) as? T
}

/**
 * 获取一个值(如果不为null)，但不马上移除(刷新GC时间)
 *
 * same , get() then put() back
 */
fun <T> DataTransport.peek(key: Any): T? {
    val instance = DataTransport.getInstance()
    val v = instance.get(key) as? T
    if (v != null) {
        instance.put(key, v)
    }
    return v
}
