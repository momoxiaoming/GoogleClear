package com.mckj.sceneslib.ui.scenes.landing.header

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter

import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentOneKeyCleanBinding
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentSpeedBinding
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderOneKeyCleanBinding
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingHeaderOneKeyCleanFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderOneKeyCleanBinding, ScenesViewModel>() {

    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
//        adapter.register(NativeAdEntity::class, NativeAdViewBinder(requireActivity()) { adStatus ->
//        })
        adapter
    }

    override fun getLayoutId() = R.layout.scenes_fragment_landing_header_one_key_clean

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
//        val list = mutableListOf<Any>()
//        //添加广告位
//        val render = TwoButtonImageTemplate(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                // tip("否,下一步")
//                activity?.onBackPressed()
//            }
//        }).toRender()
    }

    override fun initView() {
    }

}