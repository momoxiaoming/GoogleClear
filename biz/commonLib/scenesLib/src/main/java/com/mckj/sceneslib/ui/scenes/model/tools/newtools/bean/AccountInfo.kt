package com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean

import java.math.BigDecimal

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util.main.account
 * @data  2022/4/1 14:45
 */
data class AccountInfo(val payType:String, val payMoney: BigDecimal, val payTimeDate:Long, val payDayDate:String, val payMonthDate:String, val payYear:String,val payMonth:String)

