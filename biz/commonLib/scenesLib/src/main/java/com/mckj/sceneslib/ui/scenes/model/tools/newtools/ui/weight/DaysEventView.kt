package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.mckj.baselib.helper.showToast
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.DaysDialogEventBinding

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight
 * @data  2022/4/13 17:44
 */
class DaysEventView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    companion object{
        const val EVENT_CLOSE = 0
        const val EVENT_TIME = 1
    }
    private val binding = DataBindingUtil.inflate<DaysDialogEventBinding>(
        LayoutInflater.from(context),
        R.layout.days_dialog_event,
        this,
        true
    )
    
    private var clickEvent:(Int)->Unit = {}

    init {
        binding.apply { 
            daysClose.setOnClickListener { 
                clickEvent(EVENT_CLOSE)
            }


            daysEventTime.setOnClickListener {
                val eventStr = daysEventName.text.toString()
                if (eventStr.isNotBlank()) {
                    clickEvent(EVENT_TIME)
                }else{
                    showToast("事件名称不能为空")
                }
            }

        }

    }


    fun getDaysEventName() = binding.daysEventName.text.toString()

    fun setEditEmpty(){
        binding.daysEventName.setText("")
    }

    fun setClickEvent(action:(Int)->Unit){
        clickEvent = action
    }
    
}