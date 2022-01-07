package com.dn.openlib.callback

/**
 * LiveDataResponse
 *
 * @author mmxm
 * @date 2022/1/5 11:09
 */
data class LiveDataResponse<T>(
    var code: Int, var msg: String? = null, var t: T? = null
) {

    companion object {

        /**
         * 成功
         */
        val CODE_SUCCESS = 0

        /**
         * 失败
         */
        val CODE_FAILED = 1

        fun <T> success(t: T): LiveDataResponse<T> {
            return LiveDataResponse(CODE_SUCCESS, "success", t)
        }

        fun <T> failed(code: Int = CODE_FAILED, msg: String? = "failed"): LiveDataResponse<T> {
            return LiveDataResponse(code, msg)
        }

    }

    fun isSuccess(): Boolean {
        return code == CODE_SUCCESS
    }

}