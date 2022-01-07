package com.mckj.module.cleanup.ui.junkDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class JunkDetailViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JunkDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JunkDetailViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}