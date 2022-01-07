package com.mckj.sceneslib.ui.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Describe:
 *
 * Created By yangb on 2020/9/25
 */
class ScenesViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScenesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScenesViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}