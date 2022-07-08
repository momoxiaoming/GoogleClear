package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.dn.vi.app.cm.utils.DAttrUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.LayoutNewTitleBarBinding

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight
 * @data  2022/3/2 16:24
 */
class NewTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding = DataBindingUtil.inflate<LayoutNewTitleBarBinding>(
        LayoutInflater.from(context),
        R.layout.layout_new_title_bar,
        this,
        true
    )

    private var backAction ={}
    private var setAction ={}

    init {
        binding.titleBack.setOnClickListener {
            backAction()
        }

        binding.titleSet.setOnClickListener {
            setAction()
        }
    }

    fun setTitleBarListener(backAction:()->Unit={},setAction:()->Unit={}){
        this.backAction =backAction
        this.setAction = setAction
    }

    fun setTitle(title:String,color:Int=R.color.white){
        binding.titleText.apply {
            text = title
            setTextColor(ContextCompat.getColor(context,color))
        }
    }

    fun setTitleBarBgColor(color:Int=R.color.white,isTheme:Boolean=false){
        val baColor = if (isTheme) {
            color
        } else {
            ContextCompat.getColor(context, color)
        }
        binding.titleBarContainer.setBackgroundColor(baColor)
    }

    fun setBackColor(color: Int=R.color.back){
        val valueOf = ColorStateList.valueOf(ContextCompat.getColor(context, color))
        binding.titleBack.imageTintList=valueOf
    }

    fun showSetIcon(){
        binding.titleSet.isVisible=true
    }

}