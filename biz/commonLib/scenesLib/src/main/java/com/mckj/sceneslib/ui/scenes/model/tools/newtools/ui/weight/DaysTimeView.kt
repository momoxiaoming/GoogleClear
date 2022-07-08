package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.mckj.baselib.helper.showToast
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.DaysDialogTimeBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.DateUtil
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.DaysHelper
import java.util.*

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight
 * @data  2022/4/13 17:44
 */
class DaysTimeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    companion object{
        const val EVENT_CANCEL=-1L
    }

    private val binding = DataBindingUtil.inflate<DaysDialogTimeBinding>(
        LayoutInflater.from(context),
        R.layout.days_dialog_time,
        this,
        true
    )

    private var view: View?=null
    private var clickCallback:(Long)->Unit={}
    private var currentDate = Date()


    init {
        binding.apply {
            daysTimeDate.text ="${DateUtil.formatNowDateFormat()}${DateUtil.getWeekOfDate()}"

            daysTimeCancel.setOnClickListener {
                clickCallback(EVENT_CANCEL)
            }

            daysTimeSure.setOnClickListener {
                if (currentDate.time-Date().time<0){
                    showToast("设置的纪念日应该大于当前时间")
                }else{
                    clickCallback(currentDate.time)
                }
            }
        }

    }

    private fun showTimePicker(){
        val showTimePicker = DaysHelper.showTimePicker(
            context,
            Calendar.getInstance(),
            binding.daysTimeContainer,
            {
                view=it
            },
            {
                currentDate=it
            })

        binding.daysTimeNl.setOnCheckedChangeListener { CheckBoxView, isChecked ->
            if (CheckBoxView.isPressed) {
                showTimePicker.isLunarCalendar = !showTimePicker.isLunarCalendar
                view?.let {
                    DaysHelper.setTimePickerChildWeight(it,
                        if (isChecked) 1.5f else 1.0f,
                        if (isChecked) 1f else 1.1f)
                }
                showTimePicker.show()
            }
        }
    }


    fun setCheckBoxState(state:Boolean=false){
        binding.daysTimeNl.isChecked=state
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility==View.VISIBLE) {
            showTimePicker()
        }
    }




    fun setClickCallback(action:(Long)->Unit){
        clickCallback = action
    }

}