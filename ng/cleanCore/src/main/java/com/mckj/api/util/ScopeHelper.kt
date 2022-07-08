package com.mckj.api.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Describe:
 *
 * Created By yangb on 2021/3/30
 */
object ScopeHelper {

    private val mMainScope: CoroutineScope by lazy { MainScope() }

    fun <T> launch(block: suspend () -> T, error: (Throwable) -> Unit) =
        mMainScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                e.printStackTrace()
                error(e)
            }
        }

    fun <T> launch(block: suspend () -> T) = mMainScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

}