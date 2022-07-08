package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.FontDialogSuccessBinding

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight
 * @data  2022/4/12 12:13
 */
class FontSuccessView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {


    private val binding = DataBindingUtil.inflate<FontDialogSuccessBinding>(LayoutInflater.from(context),
        R.layout.font_dialog_success,this,true)


    private var dismissCallback = {}



    init {
        binding.fontSure.setOnClickListener {
            dismissCallback()
        }
    }



   fun setDismissCallback(action:()->Unit){
       dismissCallback = action
   }

}