package com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm

import androidx.lifecycle.MutableLiveData
import com.mckj.baselib.base.databinding.AbstractViewModel

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm
 * @data  2022/3/3 11:03
 */
class DustViewModel: AbstractViewModel() {

    companion object{
        const val DUST_MODE_SPEAKER =0
        const val DUST_MODE_EARPIECE =1
    }

    val dustModel by lazy { MutableLiveData<Int>() }


    fun changeDustModel(model:Int){
        dustModel.value = model
    }


}