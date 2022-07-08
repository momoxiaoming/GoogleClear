package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.FontDialogSetBinding
import com.mckj.sceneslib.databinding.FontDialogSuccessBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.FontScaleHelper
import kotlin.math.roundToInt

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight
 * @data  2022/4/12 15:36
 */
class FontSetView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    companion object{
        private const val ADD_PROCESS = 50
    }

    private val binding = DataBindingUtil.inflate<FontDialogSetBinding>(
        LayoutInflater.from(context),
        R.layout.font_dialog_set, this, true
    )


    private var mCurrentScale = 0

    private var useCallback: (Int) -> Unit = {}

    init {
        binding.apply {
            mCurrentScale = (FontScaleHelper.getFontScale(context) * 100).roundToInt()
            fontScaleSeekbar.progress = mCurrentScale-ADD_PROCESS
            fontSetValue.text ="中文 Aa ${mCurrentScale}%"

            fontScaleSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mCurrentScale = progress+ADD_PROCESS
                        fontSetValue.text ="中文 Aa ${mCurrentScale}%"
                        FontScaleHelper.setTextFontScale(fontSetValue, mCurrentScale / 100f)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            fontSetSure.setOnClickListener {
                useCallback(mCurrentScale)
            }

            fontSetClose.setOnClickListener {
                useCallback(-1)
            }

        }
    }

    fun setCallback(useAction: (Int) -> Unit) {
        useCallback = useAction
    }


}