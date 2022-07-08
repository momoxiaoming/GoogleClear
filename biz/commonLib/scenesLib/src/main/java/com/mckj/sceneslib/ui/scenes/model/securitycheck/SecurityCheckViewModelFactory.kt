package com.mckj.sceneslib.ui.scenes.model.securitycheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.sceneslib.data.SecurityCheckRepository

/**
 * Describe:WifiCheckViewModelFactory
 *
 * Created By yangb on 2020/9/25
 */
class SecurityCheckViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecurityCheckViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SecurityCheckViewModel(com.mckj.sceneslib.data.SecurityCheckRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}