package com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm

import androidx.lifecycle.MutableLiveData
import com.mckj.baselib.base.databinding.AbstractViewModel

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm
 * @data  2022/3/4 19:05
 */
class NotifyContentViewModel: AbstractViewModel() {

    private var currentAllState = false

    val selectState = MutableLiveData<Boolean>()


    val selectAllState  = MutableLiveData<Boolean>()


    fun getCurrentState() = currentAllState


    fun setClickAllState(state: Boolean) {
        currentAllState=state
        selectAllState.value= state
    }


    fun setClickState(state:Boolean=true){
        selectState.value=state
    }




}