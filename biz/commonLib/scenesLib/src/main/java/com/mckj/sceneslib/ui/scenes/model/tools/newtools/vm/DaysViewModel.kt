package com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm

import androidx.lifecycle.MutableLiveData
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.DaysInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.DaysHelper
import java.util.*

class DaysViewModel: AbstractViewModel() {


    val daysInfo by lazy { MutableLiveData<DaysInfo>()  }

    val editState by lazy { MutableLiveData<Boolean>() }


    fun changeEditState(state: Boolean){
        editState.value = state
    }



    fun queryDaysInfo(){
        val queryDaysCacheMsg = DaysHelper.queryDaysCacheMsg()
        if (queryDaysCacheMsg != null) {
            daysInfo.value =queryDaysCacheMsg
        }else{
            daysInfo.value = DaysInfo("纪念日",Date().time)
        }
    }


    fun setDaysInfo(msg: DaysInfo) {
        daysInfo.value = msg
        DaysHelper.addDaysCacheMsg(msg)
    }


}