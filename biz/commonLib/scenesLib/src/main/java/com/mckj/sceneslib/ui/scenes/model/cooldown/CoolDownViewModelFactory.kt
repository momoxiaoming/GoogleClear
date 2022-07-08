package com.mckj.sceneslib.ui.scenes.model.cooldown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class CoolDownViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoolDownViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoolDownViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}