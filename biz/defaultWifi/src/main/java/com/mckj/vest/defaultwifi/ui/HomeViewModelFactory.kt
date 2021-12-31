package com.mckj.vest.defaultwifi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.module.wifi.data.HomeRepository
import com.mckj.vest.defaultwifi.data.HomeDataImpl

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class HomeViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(HomeRepository(HomeDataImpl())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}