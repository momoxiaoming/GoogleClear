package com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm

import androidx.lifecycle.MutableLiveData
import com.mckj.baselib.base.databinding.AbsViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountYearInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.AccountHelper

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm
 * @data  2022/4/12 16:53
 */
class AccountViewModel : AbsViewModel() {

    companion object {
        const val MODEL_MONTH = 0
        const val MODEL_YEAR = 1

    }


    val currentTimeModel by lazy { MutableLiveData(MODEL_MONTH) }

    val showTimeModelView by lazy { MutableLiveData<Boolean>() }


    val totalMoney by lazy { MutableLiveData<String>() }

    val yearList by lazy { MutableLiveData<ArrayList<AccountYearInfo>>() }


    fun setShowTimeModelView(state: Boolean) {
        showTimeModelView.value = state
    }

    fun changeTimeModel() {
        currentTimeModel.value =
            if (currentTimeModel.value == MODEL_MONTH) MODEL_YEAR else MODEL_MONTH
    }


    fun getAccountYearData() {
        val everyMonth = arrayListOf<AccountYearInfo>()
        val allAccountInfo = AccountHelper.getAllAccountInfo()
        val groupYearList = AccountHelper.groupYearList(allAccountInfo)
        groupYearList[AccountHelper.getCurrentDate()[0]]?.let { it ->
            setTotalMoney(AccountHelper.sumMoney(it).toDouble())
            val groupMonthList = AccountHelper.groupMonthList(it)
            groupMonthList.forEach {
                val list = it.value
                if (list.isNotEmpty()) {
                    everyMonth.add(
                        AccountYearInfo(
                            list[0].payMonthDate,
                            AccountHelper.sumMoney(list).toDouble()
                        )
                    )
                }
            }
            yearList.value = everyMonth
        }
    }


    fun setTotalMoney(money:Double){
        totalMoney.value = "$money"
    }

}