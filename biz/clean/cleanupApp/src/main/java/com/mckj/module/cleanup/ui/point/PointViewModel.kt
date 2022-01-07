package com.mckj.module.cleanup.ui.point

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.baselib.base.databinding.AbstractViewModel
import com.mckj.sceneslib.data.ToolRepository
import com.mckj.sceneslib.entity.OptItem
import kotlinx.coroutines.launch

/**
 * PointViewModel
 *
 * @author mmxm
 * @date 2021/3/3 16:44
 */
class PointViewModel : AbstractViewModel() {

    private val repository: ToolRepository by lazy { ToolRepository() }

    private val _menuList = MutableLiveData<MutableList<OptItem>>()
    var menuList = _menuList


    fun getMenuList() {
        viewModelScope.launch {
            _menuList.value = repository.getMenuList()
        }
    }



}