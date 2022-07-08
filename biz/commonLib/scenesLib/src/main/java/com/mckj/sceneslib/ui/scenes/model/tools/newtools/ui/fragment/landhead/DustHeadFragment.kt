package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.landhead

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dn.vi.app.base.app.DatabindingFragment
import com.dn.vi.app.base.app.kt.transportData

import com.mckj.sceneslib.databinding.DustFragmentHeadBinding
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust
 * @data  2022/3/8 11:13
 */
class DustHeadFragment:DatabindingFragment<DustFragmentHeadBinding>(){

    companion object{
        const val DUST_AGAIN = "dust_again"
        fun getInstance()=DustHeadFragment()
    }

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DustFragmentHeadBinding {
        return DustFragmentHeadBinding.inflate(inflater,container,false)
    }

    override fun initLayout() {
        binding.dustAgain.setOnClickListener {
            transportData {
                put(DUST_AGAIN,true)
            }
            ScenesManager.getInstance().jumpPage(requireContext(), ScenesType.TYPE_DUST)
        }
    }
}