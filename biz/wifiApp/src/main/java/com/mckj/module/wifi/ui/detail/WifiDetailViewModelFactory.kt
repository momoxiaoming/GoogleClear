package com.mckj.module.wifi.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.module.wifi.data.WifiRepository

/**
 * Describe:WifiViewModelFactory
 *
 * Created By yangb on 2020/9/25
 */
class WifiDetailViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WifiDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WifiDetailViewModel(WifiRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}