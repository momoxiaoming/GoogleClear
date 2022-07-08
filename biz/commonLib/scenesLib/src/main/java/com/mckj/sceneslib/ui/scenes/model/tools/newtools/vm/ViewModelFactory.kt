package com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner


/**
 * ViewModelFactory
 *
 * @author mmxm
 * @date 2021/3/12 14:50
 */

/**
 * 获取一个viewModel,只要是持有ViewModelStoreOwner的类都可以获取
 */
inline fun <reified T : ViewModel, reified S : ViewModelStoreOwner> S.obtainViewModel() =
    ViewModelProvider(this, ViewModelFactory()).get(T::class.java)


class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(DustViewModel::class.java) ->
                    DustViewModel()
                isAssignableFrom(NotifyContentViewModel::class.java) ->
                    NotifyContentViewModel()
                isAssignableFrom(MagnifierViewModel::class.java) ->
                    MagnifierViewModel()
                isAssignableFrom(NetworkViewModel::class.java) ->
                    NetworkViewModel()
                isAssignableFrom(FontScaleViewModel::class.java) ->
                    FontScaleViewModel()
                isAssignableFrom(FontScaleViewModel::class.java) ->
                    FontScaleViewModel()
                isAssignableFrom(AccountViewModel::class.java) ->
                    AccountViewModel()
                isAssignableFrom(DaysViewModel::class.java) ->
                    DaysViewModel()
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}