package com.mckj.sceneslib.ui.scenes.model.networktest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class NetworkTestViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NetworkTestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NetworkTestViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}