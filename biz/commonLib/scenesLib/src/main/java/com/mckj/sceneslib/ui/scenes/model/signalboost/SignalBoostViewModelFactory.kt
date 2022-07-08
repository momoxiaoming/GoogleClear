package com.mckj.sceneslib.ui.scenes.model.signalboost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.sceneslib.data.RamDataRepository

/**
 * Describe:SignalBoostViewModelFactory
 *
 * Created By yangb on 2020/9/25
 */
class SignalBoostViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignalBoostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignalBoostViewModel(com.mckj.sceneslib.data.RamDataRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}