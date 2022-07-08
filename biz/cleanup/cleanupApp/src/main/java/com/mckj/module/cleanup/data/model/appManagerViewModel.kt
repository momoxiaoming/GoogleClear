package com.mckj.module.cleanup.data.model


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mckj.module.cleanup.entity.AppInfoHolder
import com.mckj.module.cleanup.entity.ApplicationLocal
import com.mckj.module.cleanup.ui.adapter.AppRecyclerAdapter
import com.mckj.module.cleanup.util.Log
import com.org.proxy.AppProxy

import java.math.BigDecimal

class appManagerViewModel : ViewModel() {
    val map: MutableMap<Int, Boolean> = mutableMapOf()

    var sharedAdapter: MutableMap<Int, AppRecyclerAdapter> = mutableMapOf()

    var checkList: MutableLiveData<MutableMap<Int, Boolean>> = MutableLiveData(mutableMapOf())

    var checkedSizeByte: MutableLiveData<BigDecimal> = MutableLiveData(BigDecimal(0L))

    var checkedSize: MutableLiveData<String> = MutableLiveData("")

    // 用来控制CheckBox的选中状况
    val checkedMap: MutableMap<String, Boolean> = mutableMapOf()

    var appAllList: MutableLiveData<MutableSet<ApplicationLocal>> = MutableLiveData(mutableSetOf())

    var isUninstallClick: MutableLiveData<Boolean> = MutableLiveData(false)

    var mEditTextLivaData: MutableLiveData<String> = MutableLiveData("")

    fun checkClick(index: Int, bl: Boolean) {
        map[index] = bl
        checkList.value = map
    }

    fun setAppList(list: MutableSet<ApplicationLocal>) {
        val tempList = mutableSetOf<ApplicationLocal>()
        list.forEach {
            if ((it.packageName != AppProxy.packageName) && (it.apkSize!=0L)) {
                tempList.add(it)
            }
        }
        appAllList.value = tempList
    }


}

