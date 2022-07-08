package com.mckj.sceneslib.ui.scenes.model.networkcheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.sceneslib.data.NetworkCheckRepository

/**
 * Describe:WifiCheckViewModelFactory
 *
 * Created By yangb on 2020/9/25
 */
class NetworkCheckViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NetworkCheckViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NetworkCheckViewModel(com.mckj.sceneslib.data.NetworkCheckRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}