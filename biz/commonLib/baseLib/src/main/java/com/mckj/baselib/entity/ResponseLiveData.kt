package com.mckj.baselib.entity

/**
 * Describe:
 *
 * Created By yangb on 2020/11/13
 */
data class ResponseLiveData<T>(
    val code: Int, val msg: String? = null, val t: T? = null
) {

    companion object {

        const val SUCCESS = 0
        const val ERROR = -1

        fun <T> success(data: T): ResponseLiveData<T> {
            return ResponseLiveData<T>(SUCCESS, "成功", data)
        }

        fun <T> error(code: Int = ERROR, msg: String? = null): ResponseLiveData<T> {
            return ResponseLiveData<T>(code, msg)
        }
    }

}

