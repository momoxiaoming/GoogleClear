package com.dn.vi.app.cm.kt

import java.util.concurrent.Callable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * kotlin 的 委托。
 * 针对一个值，在空的时候，返回一个默认值
 * @param defaultValueProvider 返回值 不为null
 * @param valueProvider 返回值为null时，则使用[defaultValueProvider]
 * Created by holmes on 2021/4/25.
 **/
open class DefaultValueDelegate<T>(
    private val defaultValueProvider: Callable<T>,
    private val valueProvider: Callable<T?>
) : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null) {
            value = valueProvider.call()
        }
        if (value == null) {
            value = defaultValueProvider.call()
        }
        return value!!
    }

}

/**
 * 委托默认值
 */
inline fun <T> defaultValue(
    defValue: T,
    crossinline provider: () -> T?
): ReadWriteProperty<Any?, T> {
    return DefaultValueDelegate({ defValue },
        { provider() })
}

/**
 * 委托默认值with Provider
 */
inline fun <T> defaultValue(
    defProvider: Callable<T>,
    crossinline provider: () -> T?
): ReadWriteProperty<Any?, T> {
    return DefaultValueDelegate(defProvider,
        Callable<T?> { provider() })
}
