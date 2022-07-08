package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.DaysInfo
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util.main.countdown
 * @data  2022/4/7 14:28
 */
object DaysHelper {


        private const val KEY_MEMORIAL_MSG = "key_memorial_msg"

        fun queryDaysCacheMsg():DaysInfo?{
            val cacheStr = KvSpHelper.getString(KEY_MEMORIAL_MSG)
            return  if (!cacheStr.isNullOrEmpty()) GsonHelper.jsonToObj<DaysInfo>(cacheStr) else null
        }


        fun addDaysCacheMsg(daysInfo: DaysInfo){
            KvSpHelper.putString(KEY_MEMORIAL_MSG,GsonHelper.toJson(daysInfo))
        }


        fun showTimePicker(context: Context, selectTime:Calendar, container: ViewGroup, viewAction:(View)->Unit, onTimeChange: (Date) -> Unit): TimePickerView
                /**
                 * @description
                 *
                 * 注意事项：
                 * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
                 * 具体可参考demo 里面的两个自定义layout布局。
                 * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
                 * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
                 */
        // val selectedDate = Calendar.getInstance() //系统当前时间
        //时间选择器 ，自定义布局
                = TimePickerBuilder(context) { date, v -> }
            .isCyclic(true)
            .setDate(selectTime)
            .setLayoutRes(R.layout.pickerview_custom_time) { v ->
                viewAction(v)
            }
            .setTimeSelectChangeListener {
                onTimeChange(it)
            }
            .setContentTextSize(13)
            .isAlphaGradient(true)
            .setTextColorCenter(Color.parseColor("#14B4FF"))//设置选中项的颜色
            .setTextColorOut(Color.BLACK)
            .setType(booleanArrayOf( true, true, true,false, false, false,))
            .setLabel("年", "月", "日", "时", "分", "秒")
            .setLineSpacingMultiplier(3.0f)
            .setTextXOffset(0, 0, 0, 0, 0, 0)
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .setDividerColor(Color.parseColor("#DEE0E3"))
            .setItemVisibleCount(5)
            .setDecorView(container)
            .setOutSideCancelable(false)
            .build().apply {
                setKeyBackCancelable(false)
                show(false)
            }

        fun setTimePickerChildWeight(v: View, yearWeight: Float, weight: Float) {
            val timePicker = v.findViewById<View>(R.id.timepicker) as ViewGroup
            val year = timePicker.getChildAt(0)
            val lp = year.layoutParams as LinearLayout.LayoutParams
            lp.weight = yearWeight
            year.layoutParams = lp
            for (i in 1 until timePicker.childCount) {
                val childAt = timePicker.getChildAt(i)
                val childLp = childAt.layoutParams as LinearLayout.LayoutParams
                childLp.weight = weight
                childAt.layoutParams = childLp
            }
        }



}