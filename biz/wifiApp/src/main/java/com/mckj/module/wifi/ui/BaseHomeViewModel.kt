package com.mckj.module.wifi.ui

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.LifeQueue
import com.mc.cpyr.lib_common.dialog.vest.AwardDialog
import com.mc.cpyr.lib_common.game.bank.BankProxy
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.entity.LoadingItem
import com.mckj.baselib.util.launch
import com.mckj.module.wifi.data.HomeRepository
import com.mckj.module.wifi.data.model.impl.MakeMoneyImpl
import com.mckj.module.wifi.entity.HomeMenuData
import com.mckj.module.wifi.utils.Log
import com.mckj.module.wifi.utils.WifiNotifyUtil
import com.mckj.module.wifi.widget.FloatGoldView
import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.MenuJumpItem
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.manager.network.NetworkReceiver
import com.tz.gg.appproxy.AppProxy
import com.tz.gg.appproxy.EvAgent
import com.tz.gg.appproxy.evs.AuditListener
import org.jetbrains.anko.toast

/**
 * Describe:
 *
 * Created By yangb on 2021/5/19
 */
open class BaseHomeViewModel(open val repository: HomeRepository) : AbstractViewModel() {

    companion object {
        const val TAG = "BaseHomeViewModel"
    }

    val iMakeMoney by lazy { MakeMoneyImpl() }
    var connectInfo: ConnectInfo? = null

    /**
     * 主菜单列表
     */
    val mHomeDataLiveData = MutableLiveData<HomeMenuData>()
    private val auditListener = object : AuditListener {
        override fun statusChange(audit: Boolean) {
            connectInfo?.let {
                WifiNotifyUtil.showWifiSpeed(it)
            }
        }
    }

    /**
     * 加载home数据
     */
    fun loadHomeData() {
        launch {
            mHomeDataLiveData.value = repository.getHomeMenuData()
        }
        //监听网络响应广播
        NetworkReceiver.register(AppMod.app)
        NetworkData.getInstance().connectInfoLiveData.observeForever {
            connectInfo = it
            WifiNotifyUtil.showWifiSpeed(it)
            EvAgent.sendEvent("notify_show")
        }
        AppProxy.addAuditListener(auditListener)
    }

    /**
     * 更新配置
     */
    fun updateMenuConfig(adapter: RecyclerView.Adapter<*>, position: Int, menuItem: MenuItem) {
        menuItem.update = System.currentTimeMillis()
        if (menuItem.isRecommend) {
            menuItem.isRecommend = false
            adapter.notifyItemChanged(position)
        }
        launch {
            when (menuItem) {
                is MenuJumpItem -> {
                    val list: List<MenuJumpItem> =
                        mHomeDataLiveData.value?.jumpList ?: return@launch
                    repository.iHomeData.saveJumpMenuList(list)
                }
                is MenuBusinessItem -> {
                    val list: List<MenuBusinessItem> =
                        mHomeDataLiveData.value?.businessList ?: return@launch
                    repository.iHomeData.saveBusinessMenuList(list)
                }
                else -> {
                    val list: List<MenuItem> =
                        mHomeDataLiveData.value?.homeList ?: return@launch
                    repository.iHomeData.saveHomeMenuList(list)
                }
            }
        }
    }

    /**
     * 请求红包
     */
    fun requestRedPacket(
        activity: FragmentActivity,
        floatGoldView: FloatGoldView,
        lifeQueue: LifeQueue
    ) {
        launch {
            mLoadingDialog.value = LoadingItem(true, "正在领取中")
            val result = iMakeMoney.requestEnvelope()
            mLoadingDialog.value = LoadingItem(false)
            lifeQueue.submit {
                if (result) {
                    makeMoney(activity)
                    floatGoldView.refreshTime()
                    floatGoldView.start()
                } else {
                    activity.toast("红包领取失败:网络异常")
                }
            }
        }
    }

    /**
     * 赚钱
     */
    private fun makeMoney(activity: FragmentActivity) {
        val awardMoney = BankProxy.instance.drawMoney()
        Log.i(TAG, "makeMoney: awardMoney:$awardMoney")
        showDoublePickupMoney(activity, awardMoney)
    }

    /**
     * 双倍领钱
     */
    private fun showDoublePickupMoney(activity: FragmentActivity, money: Float) {
        AwardDialog.newInstance(money, AwardDialog.AWARD_BEFORE)
            .rxShow(activity.supportFragmentManager, AwardDialog.TAG)
            .subscribe {
                BankProxy.instance.plusMoney(money)
            }
    }

}