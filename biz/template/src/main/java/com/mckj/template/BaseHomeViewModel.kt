package com.mckj.template

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.CacheJunk
import com.org.openlib.help.Consumer2
import com.mckj.api.entity.JunkInfo
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.helper.startTitleFragment
import com.mckj.template.style.DefaultMenuStyle
import com.mckj.template.abs.AbsMenuView
import com.mckj.template.entity.TepConstants
import com.mckj.template.entity.DataCenter
import com.mckj.template.entity.MenuData
import com.mckj.template.style.DefaultDayUsedStyle
import com.mckj.template.style.MenuPlusStyle
import com.mckj.template.template.MenuPlusTemplate
import com.mckj.template.template.MenuTemplate
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.permission.DPermissionUtils
import com.mckj.template.header.clean.AbsCleanHeader
import com.org.openlib.utils.DateUtil
import com.org.proxy.AppProxy
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.await
import java.util.function.Consumer

/**
 * @author xx
 * @version 1
 * @createTime 2022/2/24 20:13
 * @desc
 */
open class BaseHomeViewModel : AbstractViewModel() {

    companion object {
        private const val TAG = "BaseOsHomeViewModel"
        const val NORMAL = 1
        const val END = 2
        const val WARN = 3
    }

    var mCleanHeader: AbsCleanHeader? = null


    val mHomeDataLiveData = MutableLiveData<MenuData>()

    private val mDataCenter = DataCenter()

    val uiStatus = MutableLiveData<Int>()

    fun loadHomeData(
        menuList: MutableList<MenuItem>?,
        busMenuList: MutableList<MenuItem>?,
        busPlusMenuList: MutableList<MenuItem>?,
        recommendMenuList: MutableList<MenuItem>?
    ) {
        ScenesManager.getInstance().initCleaner()
        val list = mutableListOf<MenuItem>()
        menuList?.forEach {
            if (!AppProxy.requireAudit()) {
                list.add(it)
            }
            val scene = ScenesManager.getInstance().getScenes(it.type)
            if (scene != null && (AppProxy.requireAudit() && !scene.getData().followAudit)) {
                list.add(it)
            }
        }
        busMenuList?.forEach {
            if (!AppProxy.requireAudit()) {
                list.add(it)
            }
            val scene = ScenesManager.getInstance().getScenes(it.type)
            if (scene != null && (AppProxy.requireAudit() && !scene.getData().followAudit)) {
                list.add(it)
            }
        }
        busPlusMenuList?.forEach {
            if (!AppProxy.requireAudit()) {
                list.add(it)
            }
            val scene = ScenesManager.getInstance().getScenes(it.type)
            if (scene != null && (AppProxy.requireAudit() && !scene.getData().followAudit)) {
                list.add(it)
            }
        }
        recommendMenuList?.forEach {
            if (!AppProxy.requireAudit()) {
                list.add(it)
            }
            val scene = ScenesManager.getInstance().getScenes(it.type)
            if (scene != null && (AppProxy.requireAudit() && !scene.getData().followAudit)) {
                list.add(it)
            }
        }
        mDataCenter.getRecommendConfig()?.let {
            for (bean in list) {
                if (!bean.isRecommend) {
                    continue
                }
                for (recordType in it) {
                    if (bean.type == recordType.recordType) {
                        bean.isRecommend = false
                    }
                }
            }
        }

        val menuData = MenuData(
            homeList = list,
            useDays = 0
        )
        mHomeDataLiveData.value = menuData
    }


    fun startFlow(context: Context, consumer: Consumer2<Int, CacheJunk?>) {
        if (DPermissionUtils.hasFileScanPermission(context)) {
            viewModelScope.launch {
                consumer.accept(TepConstants.CleanHeaderStatus.SCAN, null)
            }
        } else {
            consumer.accept(TepConstants.CleanHeaderStatus.DENY, null)
        }
    }


    fun forwardCleanDetail(
        context: Context,
        appJunks: List<JunkInfo>,
        type: Int = ScenesType.TYPE_JUNK_CLEAN
    ) {
        transportData {
            put("junk_list", appJunks)
        }
        ScenesManager.getInstance().jumpPage(context, type)

    }

    fun lookDetail(
        context: Context,
        appJunks: List<AppJunk>
    ) {
        transportData {
            put("junk_list", appJunks)
        }
        context.startTitleFragment(ARouterPath.Cleanup.FRAGMENT_JUNK_DETAIL)
    }


    fun requestPermission(context: Context, consumer: Consumer<Boolean>) {
        DPermissionUtils.requestFileScanPermission(context, "main", consumer)
    }

    fun hasPermission(context: Context): Boolean {
        return DPermissionUtils.hasFileScanPermission(context)
    }


    /**
     * 获取主菜单数据
     */
    fun getMenuItem(
        type: Int,
        isRecommend: Boolean = false,
        isAuditConfig: Boolean = false
    ): MenuItem {
        return mDataCenter.getMenuItem(
            type,
            isRecommend,
            isAuditConfig,
            TepConstants.Menu.HOME_MENU_MAIN
        )
    }

    /**
     * 获取特色菜单数据
     */
    fun getBusMenuItem(
        type: Int,
        isRecommend: Boolean = false,
        isAuditConfig: Boolean = false
    ): MenuItem {
        return mDataCenter.getMenuItem(
            type,
            isRecommend,
            isAuditConfig,
            TepConstants.Menu.HOME_MENU_BUS
        )
    }

    /**
     * 获取特色plus菜单数据
     */
    fun getBusPlusMenuItem(
        type: Int,
        isRecommend: Boolean = false,
        isAuditConfig: Boolean = false
    ): MenuItem {
        return mDataCenter.getMenuItem(
            type,
            isRecommend,
            isAuditConfig,
            TepConstants.Menu.HOME_MENU_BUS_PLUS
        )
    }

    /**
     * 获取推荐菜单数据
     */
    fun getRecommendMenuItem(
        type: Int,
        isRecommend: Boolean = false,
        isAuditConfig: Boolean = false
    ): MenuItem {
        return mDataCenter.getMenuItem(
            type,
            isRecommend,
            isAuditConfig,
            TepConstants.Menu.HOME_MENU_RECOMMEND
        )
    }


    /**
     * 默认的陪伴样式
     */
    fun getDefaultDayUsedStyle(context: Context): View? {
        val style = DefaultDayUsedStyle(context)
        viewModelScope.launch {
            val useDays = try {
                val time = AppProxy.bootTime
                DateUtil.getIntervalDays(time)
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
            style.setData(useDays)
        }
        return style.rootView
    }

    /**
     * 默认的主菜单样式
     */
    fun getDefaultMenu(context: Context): AbsMenuView {
        val menuView = MenuTemplate(context)
        menuView.bindStyle(DefaultMenuStyle())
        menuView.setDecoration(getItemDecoration(context, SizeUtil.dp2px(7f)))
        return menuView
    }

    /**
     * 默认的
     */
    fun getDefaultBusMenu(context: Context): AbsMenuView {
        val busMenu = MenuPlusTemplate(context)
        busMenu.bindStyle(MenuPlusStyle())
        busMenu.setDecoration(getItemDecoration(context, SizeUtil.dp2px(7f)))
        return busMenu
    }


    fun getItemDecoration(context: Context, divider: Int): RecyclerView.ItemDecoration {
        return DividerItemDecoration(
            context,
            RecyclerView.VERTICAL
        ).also { decor ->
            decor.setDrawable(DividerDrawable(divider).also { })
        }
    }

    fun updateStatus(status: Int) {
        uiStatus.postValue(status)
    }

}