package com.mckj.sceneslib.ui.scenes.model.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsDataImpl

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class ToolsViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToolsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToolsViewModel(ToolsDataImpl()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}