package com.mckj.sceneslib.ui.junk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class JunkViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JunkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JunkViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}