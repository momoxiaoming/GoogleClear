package com.mckj.module.wifi.ui.wifi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.module.wifi.data.WifiRepository

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class WifiViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WifiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WifiViewModel(WifiRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}