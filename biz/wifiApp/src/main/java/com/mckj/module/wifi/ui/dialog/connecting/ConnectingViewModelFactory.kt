package com.mckj.module.wifi.ui.dialog.connecting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.module.wifi.data.WifiRepository

/**
 * Describe:ConnectingViewModelFactory
 *
 * Created By yangb on 2020/9/25
 */
class ConnectingViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConnectingViewModel(WifiRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}