package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.network

import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.helper.showToast
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.NetworkFragmentBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter.NetworkAdapter
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.CheckNetWorkHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.NetworkViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.obtainViewModel

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.newwork
 * @data  2022/4/11 15:54
 */
@RequiresApi(Build.VERSION_CODES.M)
@Route(path = ARouterPath.NewTools.NEW_TOOLS_CHECK_NET_WORK)
class NetworkFragment:DataBindingFragment<NetworkFragmentBinding,NetworkViewModel>() {

    companion object{
        private const val TITLE ="网络监控"
    }
    private val mNetworkAdapter by lazy { NetworkAdapter() }
    private  val registerForActivityResult =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (CheckNetWorkHelper.hasPermissionToReadNetworkStats(requireContext())) {
            mModel.showChangeNetwork()
        }else{
           showToast("没有被授权")
        }
    }

    override fun getLayoutId(): Int =R.layout.network_fragment

    override fun getViewModel(): NetworkViewModel = obtainViewModel()

    override fun initData() {
        St.stSetMonitorClick()
        St.stSetMonitorShow()
        if (CheckNetWorkHelper.hasPermissionToReadNetworkStats(requireContext())) {
            mModel.showChangeNetwork()
        }else{
            val requestReadNetworkIntent = CheckNetWorkHelper.requestReadNetworkIntent()
            registerForActivityResult.launch(requestReadNetworkIntent)
        }
    }

    override fun initView() {
        mBinding.apply {
            networkTitleBar.apply {
                setTitle(TITLE, R.color.white)
                setBackColor(R.color.white)
                setTitleBarBgColor(R.color.trans)
                setTitleBarListener(backAction = {
                    requireActivity().finish()
                })
            }

            networkContainer.layoutManager = LinearLayoutManager(context)
            networkContainer.adapter = mNetworkAdapter
        }
    }

    override fun initObserver() {
        super.initObserver()
        val that = this
        mBinding.apply {
            mModel.apply {
                currentNetWork.observe(that){
                    currentNetwork.text = it
                }

                currentAppList.observe(that) {
                    mNetworkAdapter.setList(it)
                }

                currentNetSpeed.observe(that){
                    networkUp.text = CheckNetWorkHelper.renderFileSize(it[1])
                    networkDown.text = CheckNetWorkHelper.renderFileSize(it[0])
                }
            }
        }

    }
}