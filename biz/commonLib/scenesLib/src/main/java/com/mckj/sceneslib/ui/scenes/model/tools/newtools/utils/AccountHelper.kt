package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.os.Build
import android.text.InputFilter
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountTypeInfo
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors


object AccountHelper {
    private  const val SP_ACCOUNT_INFO = "sp_account_info"
     const val FORMAT_YMD="yyyy年MM月dd日"
     const val FORMAT_YM="yyyy年MM月"
     const val FORMAT_YMDHMS="yyyy-MM-dd HH:mm:ss"

    private val payTypeList by lazy { arrayListOf(
        AccountTypeInfo(R.drawable.ic_account_catering,"餐饮"),
        AccountTypeInfo(R.drawable.ic_account_shopping,"购物"),
        AccountTypeInfo(R.drawable.ic_account_traffic,"交通"),
        AccountTypeInfo(R.drawable.ic_account_daily,"日用"),
        AccountTypeInfo(R.drawable.ic_account_medical,"医疗"),
        AccountTypeInfo(R.drawable.ic_account_dress,"服饰"),
        AccountTypeInfo(R.drawable.ic_account_online_shopping,"网购"),
        AccountTypeInfo(R.drawable.ic_account_beauty,"美容"),
        AccountTypeInfo(R.drawable.ic_account_communication,"通讯"),
        AccountTypeInfo(R.drawable.ic_account_entertainment,"娱乐"),
        AccountTypeInfo(R.drawable.ic_account_housing,"住房"),
        AccountTypeInfo(R.drawable.ic_account_work,"工作"),
        AccountTypeInfo(R.drawable.ic_account_fruit,"水果"),
        AccountTypeInfo(R.drawable.ic_account_vegetables,"蔬菜"),
    ) }

    private val accountAllList = arrayListOf<AccountInfo>()
    private val accountMonthList = arrayListOf<AccountInfo>()
    val accountMonthLiveData by lazy { MutableLiveData<List<AccountInfo>>() }
    private val iconMap = hashMapOf<String,Int>()

    init {
        payTypeList.forEach {
            iconMap[it.payType] = it.payIcon
        }
    }

    fun getPayTypeIcon():HashMap<String,Int>{
        return iconMap
    }


    fun getNowDateFormat(format:String= FORMAT_YMD):String{
        val simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
        return simpleDateFormat.format(Date())
    }

    fun getCurrentDate():Array<String>{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return arrayOf(year.toString(),month.toString(),day.toString())
    }


    fun queryAllAccountInfo() {
        val cacheStr = KvSpHelper.getString(SP_ACCOUNT_INFO)
        if (!cacheStr.isNullOrEmpty()) {
            val type = object : TypeToken<List<AccountInfo>>() {}.type
            GsonHelper.jsonToObj<List<AccountInfo>>(cacheStr, type)?.let { list ->
                if (list.isNotEmpty()) {
                    accountAllList.clear()
                    accountAllList.addAll(list)
                    val groupList = groupMonthList(accountAllList)
                    groupList[getCurrentDate()[1]]?.let { it ->
                        accountMonthList.clear()
                        accountMonthList.addAll(it)
                        accountMonthList.sortByDescending { it.payTimeDate }
                        accountMonthLiveData.value = accountMonthList
                    }
                }
            }
        }else{
            accountMonthLiveData.value = accountMonthList
        }

    }


    fun getAllAccountInfo() = accountAllList


    fun addAccountInfo(accountInfo: AccountInfo){
//        val accountInfo1 = AccountInfo(
//            "交通",
//            BigDecimal(100).setScale(1, BigDecimal.ROUND_HALF_UP),
//            System.currentTimeMillis(),
//            "2022年05月14日",
//            "2022年05月",
//            "2022",
//            "5",
//        )
//        accountAllList.add(accountInfo1)

        accountAllList.add(accountInfo)
        accountMonthList.add(accountInfo)
        accountMonthList.sortByDescending { it.payTimeDate }
        accountMonthLiveData.value = accountMonthList
        KvSpHelper.putString(SP_ACCOUNT_INFO,GsonHelper.toJson(accountAllList))
    }

    fun getAccountTypeData() = payTypeList

    //分组
    //年
     fun groupYearList(list: List<AccountInfo>): MutableMap<String, MutableList<AccountInfo>> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.stream().collect(Collectors.groupingBy(AccountInfo::payYear))
        } else {
            groupUnder24(list) {accountInfo -> accountInfo.payYear }
        }

    // 月
     fun groupMonthList(list: List<AccountInfo>): MutableMap<String, MutableList<AccountInfo>> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.stream().collect(Collectors.groupingBy(AccountInfo::payMonth))
        } else {
            groupUnder24(list) {accountInfo -> accountInfo.payMonth }
        }
    //日
    fun groupDayList(list: List<AccountInfo>): MutableMap<String, MutableList<AccountInfo>> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.stream().collect(Collectors.groupingBy(AccountInfo::payDayDate))
        } else {
            groupUnder24(list) {accountInfo -> accountInfo.payDayDate }
        }

    private inline fun <T> groupUnder24(list: List<T>,action:(T)->String): HashMap<String, MutableList<T>> {
        val map = hashMapOf<String, MutableList<T>>()
        list.forEach {
            val key = action(it)
            var tmpList = map[key]
            if (tmpList == null) {
                tmpList = arrayListOf()
                tmpList.add(it)
                map[key] = tmpList
            } else {
                tmpList.add(it)
            }
        }
        return map
    }

    //求和
    fun sumMoney(list: List<AccountInfo>):BigDecimal{
      return  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sumOver24(list)
        }else{
            sumUnder24(list)
        }
    }


    fun sumUnder24(list: List<AccountInfo>):BigDecimal{
        var sum =BigDecimal(0)
        for (accountInfo in list) {
            val payValue = accountInfo.payMoney
            sum+=payValue
        }
        return sum
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun sumOver24(list: List<AccountInfo>):BigDecimal{
       return list.stream().map(AccountInfo::payMoney).reduce(BigDecimal.ZERO, BigDecimal::add)
    }

    private const val DECIMAL_DIGITS = 1

    fun setEditRule(editText: EditText){
        val lengthFilter = InputFilter { source, start, end, dest, dstart, dend -> // 删除等特殊字符，直接返回
            if ("" == source.toString()) {
                return@InputFilter null
            }
            val dValue = dest.toString()
            val splitArray = dValue.split("//.").toTypedArray()
            if (splitArray.size > 1) {
                val dotValue = splitArray[1]
                val diff: Int = dotValue.length + 1 - DECIMAL_DIGITS
                if (diff > 0) {
                    return@InputFilter source.subSequence(start, end - diff)
                }
            }
            null
        }
        editText.filters = arrayOf(lengthFilter);
    }




}