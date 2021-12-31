package com.mckj.module.wifi.ui.detail

import android.os.Build
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.ResourceUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiFragmentDetailBinding
import com.mckj.module.wifi.entity.WifiDetailEntity
import com.mckj.module.wifi.ui.viewBinder.WifiDetailViewBinder
import com.mckj.module.wifi.utils.Log
import com.mckj.openlib.helper.onceClick
import com.mckj.sceneslib.manager.network.WifiInfo
import org.jetbrains.anko.textColorResource

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
@Route(path = ARouterPath.Wifi.FRAGMENT_DETAIL)
class WifiDetailFragment :
    DataBindingFragment<WifiFragmentDetailBinding, WifiDetailViewModel>() {

    companion object {
        const val TAG = "WifiDetailFragment"
    }

    private lateinit var mWifiInfo: WifiInfo
    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        val wifiDetailViewBinder = WifiDetailViewBinder()
        adapter.register(WifiDetailEntity::class, wifiDetailViewBinder)
        adapter
    }

    override fun getViewModel() =
        ViewModelProvider(requireActivity(), WifiDetailViewModelFactory()).get(
            WifiDetailViewModel::class.java
        )

    override fun getLayoutId() = R.layout.wifi_fragment_detail

    override fun initData() {
        val bundle = arguments ?: return
        mWifiInfo = bundle.getParcelable<WifiInfo>("wifi_info")!!
        Log.i(TAG, "initData: wifiInfo:$mWifiInfo")
        mModel.loadWifiDetails(mWifiInfo)
    }

    override fun initView() {
        //状态栏字体改成黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window?.let {
                it.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        //初始化标题
        mBinding.headerLayout.headerLayout.setBackgroundResource(R.color.white)
        mBinding.headerLayout.headerToolbar.apply {
            title = "热点信息"
            setTitleTextColor(ResourceUtil.getColor(R.color.OpenColorTextBlack))
            navigationIcon = ResourceUtil.getDrawable(R.drawable.wifi_icon_back_black)
            setNavigationOnClickListener {
                mModel.isFinish.value = true
            }
        }
        val manager = LinearLayoutManager(requireContext())
        mBinding.detailRecycler.layoutManager = manager

        mBinding.detailBtn.let {
            if (mWifiInfo.isConnect) {
                it.setBackgroundResource(R.drawable.wifi_shape_round_bg_gray)
                it.text = "忘记WiFi"
                it.textColorResource = R.color.base_text_black
            } else {
                it.setBackgroundResource(R.drawable.scenes_shape_btn_green)
                it.text = "连接WiFi"
                it.textColorResource = R.color.base_text_white
            }
            it.onceClick {
                if (mWifiInfo.isConnect) {
                    //忘记密码
                    mModel.removeConnect(requireActivity(), mWifiInfo)
                } else {
                    //连接WIFi
                    mModel.connect(requireActivity(), mWifiInfo)
                }
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        mModel.mDetailLiveData.observe(this, Observer {
            setWifiDetailAdapter(it)
        })
    }

    private fun setWifiDetailAdapter(list: List<WifiDetailEntity>) {
        if (mBinding.detailRecycler.adapter == null) {
            mBinding.detailRecycler.adapter = mAdapter
        }
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }


}