package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.DatabindingFragment
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.NotifyFragmentAppBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter.AppStateAdapter
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AppInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify.AppStateProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify
 * @data  2022/3/4 15:52
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_NOTIFY_APP)
class NotifyAppFragment : DatabindingFragment<NotifyFragmentAppBinding>() {

    private val mAppStateAdapter by lazy { AppStateAdapter() }

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): NotifyFragmentAppBinding {
        return NotifyFragmentAppBinding.inflate(inflater, container, false)
    }


    override fun initLayout() {
        super.initLayout()
        viewScope.launch(Dispatchers.Default){
            AppStateProvider.initSourceData()
        }
        initView()
        initEvent()
        initObserver()

    }

    private fun initView() {

        binding.apply {
            notifyAppNewTitleBar.apply {
                setTitle("通知栏清理")
                setTitleBarBgColor(R.color.notify_color)
                setTitleBarListener({ requireActivity().finish() }, {})
            }

            notifyAppContainer.layoutManager = LinearLayoutManager(requireContext())
            notifyAppContainer.adapter =mAppStateAdapter
        }
    }

    private fun initEvent() {
        mAppStateAdapter.setOnItemChildClickListener { adapter, view, position ->
            val appInfo = adapter.data[position] as AppInfo
            appInfo.isFilter = !appInfo.isFilter
            AppStateProvider.saveAppState(appInfo)
            mAppStateAdapter.notifyItemChanged(position)
        }
    }

    private fun initObserver() {
        AppStateProvider.appStateLiveData.observe(this){
            mAppStateAdapter.setList(it)
        }
    }


}