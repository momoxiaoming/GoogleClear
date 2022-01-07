package com.dn.baselib.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Describe:
 *
 * Created By yangb on 2020/9/25
 */
fun <T> ViewModel.launch(block: suspend () -> T, error: (Throwable) -> Unit) =
    viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            e.printStackTrace()
            error(e)
        }
    }

fun <T> ViewModel.launch(block: suspend () -> T) = viewModelScope.launch {
    try {
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}