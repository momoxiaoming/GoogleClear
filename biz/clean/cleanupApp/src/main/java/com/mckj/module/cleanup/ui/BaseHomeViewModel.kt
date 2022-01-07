package com.mckj.module.cleanup.ui

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.PackageManager


import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.app.kt.transportData
import com.google.android.material.appbar.AppBarLayout

import com.dn.baselib.anim.AnimatorHelper
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.ext.launch
import com.dn.baselib.util.LocationUtil
import com.dn.datalib.ARouterPath
import com.dn.openlib.ui.startTitleFragment
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.manager.junk.JunkManager
import com.mckj.cleancore.tools.JunkType
import com.mckj.module.cleanup.data.HomeRepository
import com.mckj.module.cleanup.entity.HomeMenuData
import com.mckj.module.cleanup.gen.CleanupSp
import com.mckj.module.cleanup.util.Log
import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.ScanData
import com.mckj.sceneslib.manager.AutoScanManager
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType

import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.delay

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
    fun loadHomeData() {
        launch {
            mHomeDataLiveData.value = repository.getHomeMenuData()
        }
    }

    /**
     * 菜单监听
     */
    fun menuClickListener(context: Context, menuItem: MenuItem) {
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
     * 扫描垃圾
     */
    fun scanJunk(delayTime: Long = 0) {
        launch {
            AutoScanManager.getInstance().setState(ScanData.STATE_SCANNING)
            val result = JunkManager.getInstance().scan(JunkType.AUTO_SCAN) { entity ->
                AutoScanManager.getInstance().postAdd(entity)
            }
            delay(delayTime)
            Log.i(TAG, "scanJunk: result:$result")
            AutoScanManager.getInstance().setState(ScanData.STATE_SCANNED)
            //记录扫描时间
            CleanupSp.instance.cleanupHomeScanTime = System.currentTimeMillis()
        }
    }

    /**
     * 停止扫描垃圾
     */
    fun stopScanJunk() {
        JunkManager.getInstance().stop()
    }

    /**
     * 显示清理垃圾
     */
    fun showCleanDetail(context: Context) {
        context.startTitleFragment(ARouterPath.Cleanup.FRAGMENT_JUNK_DETAIL)
    }

    /**
     * 显示清理垃圾
     */
    fun showCleanJunk(context: Context, type: Int = ScenesType.TYPE_JUNK_CLEAN) {
        val value: List<IJunkEntity>? = AutoScanManager.getInstance().scanDataLiveData.value?.list
        if (value != null) {
            transportData {
                put("junk_list", value)
            }
            ScenesManager.getInstance().jumpPage(context, type)
        }
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

    /**
     * 我能做什么？
     */
    fun whatCanIDo(context: Context, appBarLayout: AppBarLayout) {
        when {
            System.currentTimeMillis() - CleanupSp.instance.cleanupHomeScanTime >= 5 * 60 * 1000 -> {
                //扫描间隔大于5分钟
                Log.i(TAG, "whatCanIDo: AutoScan")
                if (isStoragePermission(context)) {
                    scanJunk()
                }
            }
            else -> {
//                Log.i(TAG, "whatCanIDo: scrollToTheTop")
//                scrollMenuToTop(appBarLayout)
            }
        }
    }

    /**
     * 移动菜单项到顶部
     */
    private fun scrollMenuToTop(appBarLayout: AppBarLayout) {
        val data = AutoScanManager.getInstance().scanDataLiveData.value ?: return
        if (data.state != ScanData.STATE_NORMAL) {
            return
        }
        val layoutParams: CoordinatorLayout.LayoutParams =
            appBarLayout.layoutParams as? CoordinatorLayout.LayoutParams ?: return
        val behavior: AppBarLayout.Behavior =
            layoutParams.behavior as? AppBarLayout.Behavior ?: return
        val from: Int = behavior.topAndBottomOffset
        val to: Int = -appBarLayout.height + appBarLayout.minimumHeight
        Log.i(TAG, "scrollMenuToTop: from:$from to:$to")
        mScrollMenuAnim?.cancel()
        mScrollMenuAnim = AnimatorHelper.playUpdate(from, to) {
            behavior.topAndBottomOffset = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        mScrollMenuAnim?.cancel()
    }


}