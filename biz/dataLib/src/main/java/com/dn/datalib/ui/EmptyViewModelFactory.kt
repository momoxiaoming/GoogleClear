package com.dn.datalib.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Describe:EmptyViewModelFactory
 *
 * Created By yangb on 2020/9/25
 */
class EmptyViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmptyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmptyViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}