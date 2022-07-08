package com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm

import androidx.lifecycle.MutableLiveData
import com.mckj.baselib.base.databinding.AbstractViewModel

class FontScaleViewModel : AbstractViewModel() {

    val isFontSuccessState by lazy { MutableLiveData<Boolean>()}


     fun changeSuccessDialogState(state: Boolean){
         isFontSuccessState.value=state
    }


    fun getSuccessDialogState() = isFontSuccessState.value?:false

}