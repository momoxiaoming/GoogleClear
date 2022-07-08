package com.mckj.module.cleanup.ui

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.kt.transportData
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkInfo
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.LocationUtil
import com.mckj.baselib.util.launch
import com.mckj.datalib.entity.ARouterPath
import com.mckj.module.cleanup.data.HomeRepository
import com.mckj.module.cleanup.entity.HomeMenuData
import com.mckj.module.cleanup.util.Log
import com.org.openlib.helper.startTitleFragment
import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.network.NetworkReceiver
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.manager.scenes.model.*
import com.mckj.sceneslib.manager.strategy.helper.StrategyManager
import com.tbruyelle.rxpermissions3.RxPermissions

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
open class BaseHomeViewModel(open val repository: HomeRepository) : AbstractViewModel() {

    companion object {
        const val TAG = "BaseHomeViewModel"
    }


    /**
     * 主菜单列表
     */
    val mHomeDataLiveData = MutableLiveData<HomeMenuData>()

    /**
     * 菜单滑动动画
     */
    private var mScrollMenuAnim: ValueAnimator? = null

    /**
     * 加载home数据
     */
    open fun loadHomeData() {
        ScenesManager.getInstance().initCleaner()
        StrategyManager.getInstance()
        launch {
            mHomeDataLiveData.value = repository.getHomeMenuData()
        }
        //监听网络响应广播
        NetworkReceiver.register(AppMod.app)
    }

    /**
     * 菜单监听
     */
    fun menuClickListener(context: Context, menuItem: MenuItem) {
        val scenes = ScenesManager.getInstance().getScenes(ScenesType.TYPE_JUNK_CLEAN)
        scenes?.enterFlag = "首页"
        ScenesManager.getInstance().jumpPage(context, menuItem.type)
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
     * 显示清理垃圾
     */
    fun showCleanDetail(context: Context, appJunks: List<AppJunk>) {
        transportData {
            put("junk_list", appJunks)
        }
        context.startTitleFragment(ARouterPath.Cleanup.FRAGMENT_JUNK_DETAIL)
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

    /**
     * 是否有sd卡的读写权限
     */
    fun isStoragePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    }

    /**
     * 申请sd卡权限
     */
    fun requestStoragePermission(activity: FragmentActivity) {
        Log.i(TAG, "requestLocationPermission: ")
        val rxPermissions = RxPermissions(activity)
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { result ->
                if (result) {
                    //申请成功,刷新数据
                } else {
                    //权限被拒绝 且禁止询问
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        //不能否再次请求
                        LocationUtil.openSelfSetting()
                    }
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        mScrollMenuAnim?.cancel()
    }
}