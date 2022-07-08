package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify

import android.app.ActivityManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.DatabindingFragment
import com.dn.vi.app.base.app.kt.lifeQueue
import com.org.openlib.help.Consumer
import com.mckj.baselib.view.ToastUtil
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.helper.startFragment
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AudioFragmentBinding
import com.mckj.sceneslib.databinding.NotifyFragmentCoverBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify.NotifyHelper
import com.org.openlib.utils.FragmentUtil
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify
 * @data  2022/3/4 15:52
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_NOTIFY_COVER)
class NotifyCoverFragment : DatabindingFragment<NotifyFragmentCoverBinding>() {

    private val mNotifyPermissionDialog by lazy { NotifyPermissionDialogFragment.newInstance() }

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): NotifyFragmentCoverBinding {
        return NotifyFragmentCoverBinding.inflate(inflater, container, false)
    }

    private var mConsumer: Consumer<Boolean>? = null
    private var parentId = 0

    companion object{
        fun newInstance(consumer: Consumer<Boolean>,id:Int): NotifyCoverFragment {
            return NotifyCoverFragment().also {
                it.mConsumer = consumer
                it.parentId = id
            }
        }
    }


    override fun initLayout() {
        binding.notifyCoverTitleBar.apply {
            setTitle("")
            setTitleBarBgColor(R.color.trans)
            setTitleBarListener({
                requireActivity().finish()
            })
        }

        NotifyHelper.toggleNotificationListenerService(requireContext())
        if (NotifyHelper.isNotificationListenerEnable(AppMod.app)) {
            toContentPage()
        }


        binding.openNotifyBt.setOnClickListener {
            lifeQueue.submit {
                mNotifyPermissionDialog.rxShow(childFragmentManager,NotifyPermissionDialogFragment.TAG).subscribeBy{
                    toCheckPermission()
                }
            }
        }
    }


    private fun toCheckPermission(){
        if (NotifyHelper.isNotificationListenerEnable(AppMod.app)) {
            toContentPage()
        } else {
            NotifyHelper.gotoNotificationAccessSetting(AppMod.app)
            checkNotifyPermission()
        }
    }

    private fun toContentPage() {
        if (isAdded) {
            val fragment = NotifyContentFragment.newInstance {
                mConsumer?.accept(true)
            }
            if (parentId!=0) {
                FragmentUtil.show(requireActivity().supportFragmentManager, fragment, parentId)
            }
        }
    }

    private var isOpen =false

    override fun onResume() {
        super.onResume()
        if (isOpen){
            if (!NotifyHelper.isNotificationListenerEnable(AppMod.app)) {
                isOpen=false
                if (isAdded) {
                    St.stPermissionSysDialogDismiss("通知读写权限","拒绝")
                    ToastUtil.show("打开通知栏监听权限失败")
                    requireActivity().finish()
                }
            }
        }
    }


    private fun checkNotifyPermission() {
        isOpen=true
        viewScope.launch(Dispatchers.Default){
            while (true) {
                if (NotifyHelper.isNotificationListenerEnable(AppMod.app)) {
                    St.stPermissionSysDialogShow("通知读写权限","允许")
                    toContentPage()
                    break
                }
            }
        }
    }
}