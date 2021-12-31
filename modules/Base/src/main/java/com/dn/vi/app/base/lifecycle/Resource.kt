@file:Suppress("NOTHING_TO_INLINE")

package com.dn.vi.app.base.lifecycle

/**
 * A generic class that contains data and status about loading this data.
 *
 * 用于在LiveData中包装状态数据
 *
 * From (jetpack guide)[https://developer.android.com/jetpack/docs/guide#show-in-progress-operations]
 * Created by holmes on 2020/5/20.
 **/
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    open val hasData: Boolean
        get() = data != null

    abstract val isSuccess: Boolean
    abstract val isLoading: Boolean
    abstract val isError: Boolean

    class Success<T>(data: T) : Resource<T>(data) {
        override val isSuccess: Boolean = true
        override val isLoading: Boolean = false
        override val isError: Boolean = false
    }

    class Loading<T>(data: T? = null) : Resource<T>(data) {
        override val isSuccess: Boolean = false
        override val isLoading: Boolean = true
        override val isError: Boolean = false
    }

    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message) {
        override val isSuccess: Boolean = false
        override val isLoading: Boolean = false
        override val isError: Boolean = true
    }
}

/**
 * 给对象[T]，包装一个 success 的 Resource状态
 */
inline fun <T> T.wrapSuccess(): Resource<T> = Resource.Success<T>(this)

/**
 * 给对象[T]，包装一个 loading 的 Resource状态
 */
inline fun <T> T.wrapLoading(): Resource<T> = Resource.Loading<T>(this)

/**
 * 给对象[T]，包装一个 error 的 Resource状态
 */
inline fun <T> T.wrapError(msg: String): Resource<T> = Resource.Error<T>(msg, this)


/**
 * 方便[Resource]区分三种状态,并分别处理
 */
inline fun <reified T> Resource<T>.withCond(
    loading: (l: Resource.Loading<T>) -> Unit,
    error: (e: Resource.Error<T>) -> Unit,
    success: (s: Resource.Success<T>) -> Unit
) {
    when (this) {
        is Resource.Success -> {
            success(this)
        }
        is Resource.Loading -> {
            loading(this)
        }
        is Resource.Error -> {
            error(this)
        }
    }
}