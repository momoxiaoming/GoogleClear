package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import android.graphics.Color
import android.util.TypedValue
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.FontAdapterRvItemBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.FontScaleHelper
import kotlin.math.roundToInt

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter
 * @data  2022/4/12 09:49
 */
class FontScaleAdapter :
    BaseQuickAdapter<Float, BaseDataBindingHolder<FontAdapterRvItemBinding>>(R.layout.font_adapter_rv_item) {

    init {
        addChildClickViewIds(R.id.font_scale_do)
    }

    override fun convert(holder: BaseDataBindingHolder<FontAdapterRvItemBinding>, item: Float) {
        holder.dataBinding?.apply {
            val isDiff = holder.adapterPosition == 0
            fontScaleDo.isVisible = !isDiff
            fontScaleTitle.setTextColor(
                Color.parseColor(
                    if (isDiff) "#333333" else "#14B4FF"
                )
            )
            fontScaleTitle.text = if (isDiff) "当前系统使用字号(${(item*100).roundToInt()}%)" else "字号放大-${(item*100).roundToInt()}%"
            fontScaleTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17f)
            fontScaleHint.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16f)
            fontScaleDo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16f)
            FontScaleHelper.setTextFontScale(fontScaleValue,item)
        }
    }
}