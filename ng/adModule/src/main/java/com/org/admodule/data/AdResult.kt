package com.org.admodule.data

/**
 * AdResult
 *
 * @author mmxm
 * @date 2022/7/4 23:09
 */
data class AdResult<T>(val item: T, val adStatus: AdStatus, val desc: String = "")