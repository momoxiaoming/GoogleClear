package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AudioAdapterItemBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.VoiceLevel

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter
 * @data  2022/3/3 18:32
 */
class AudioAdapter:BaseQuickAdapter<VoiceLevel,BaseDataBindingHolder<AudioAdapterItemBinding>>(R.layout.audio_adapter_item) {

    init {
        addChildClickViewIds(R.id.audio_do)
    }

    override fun convert(holder: BaseDataBindingHolder<AudioAdapterItemBinding>, item: VoiceLevel) {
           holder.dataBinding?.apply {
               audioIcon.setImageResource(item.icon)
               audioTitle.text="${item.title}"
               audioHint.text="${item.hint}"
           }
    }

}