package com.mckj.sceneslib.ui.junkDetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.dn.vi.app.base.app.kt.transportData
import com.mckj.api.client.JunkConstants
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkInfo
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.MenuJunkChild
import com.mckj.sceneslib.entity.MenuJunkParent

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class JunkDetailViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "JunkDetailViewModel"
    }

    /**
     * 列表详情
     */
    val mDetailLiveData = MutableLiveData<List<Any>>()

    /**
     * 选中大小
     */
    val mSelectSizeLiveData = MutableLiveData<Long>()

    private val mDetailList: MutableList<MenuJunkParent> by lazy { mutableListOf() }

    fun init(list: MutableList<AppJunk>?) {
        mDetailList.clear()
        if (list.isNullOrEmpty()) {
            return
        }
        val allJunks = mutableListOf<JunkInfo>()//全部的缓存颗粒
        list.forEach {
            it.junks?.apply {
                allJunks.addAll(this)
            }
        }
        //缓存颗粒根据垃圾类型重新分类
        val map = allJunks.groupBy {
            it.junkType
        }
        //重新组装数据
        for ((key, value) in map) {
            val name = getParentName(key)//垃圾名称：@Linked  JunkConstants.JunkType
            val size = getSize(value)
            val parent = MenuJunkParent(
                name,
                size,
                isExpand = false,
                select = true
            )
            val childList = mutableListOf<MenuJunkChild>()
            //重新分组数据:将相同的包名的垃圾分组
            val groupMap = value.groupBy { junkInfo ->
                junkInfo.parent
            }
            for ((key, value) in groupMap) {
                buildAppJunk(key, list)?.let {
                    it.junks = value as MutableList<JunkInfo>
                    it.junkSize = getSize(value)
                    childList.add(
                        MenuJunkChild(
                            select = true,
                            iJunkEntity = it, parent
                        )
                    )
                }
            }
            parent.childList = childList
            mDetailList.add(parent)
        }
        resetList()
    }

    private fun buildAppJunk(packageName: String, list: MutableList<AppJunk>?): AppJunk? {
        var appJunk: AppJunk?
        list?.let {
            for (bean in it) {
                if (packageName == bean.packageName) {
                    val temp = mutableListOf<JunkInfo>()
                    temp.addAll(bean.junks!!)
                    appJunk = AppJunk(
                        type = bean.type,
                        appName = bean.appName,
                        packageName = bean.packageName,
                        icon = bean.icon,
                        junkSize = bean.junkSize,
                        junkDescription = bean.junkDescription,
                        junks = temp
                    )
                    return appJunk
                }
            }
        }
        return null
    }


    private fun getParentName(junkType: Int): String {
        return when (junkType) {
            JunkConstants.JunkType.CACHE -> ResourceUtil.getString(R.string.scenes_app_cache)
            JunkConstants.JunkType.AD_CACHE -> ResourceUtil.getString(R.string.scenes_ad_cache)
            JunkConstants.JunkType.LOG -> ResourceUtil.getString(R.string.scenes_log_cache)
            JunkConstants.JunkType.DOWNLOAD -> ResourceUtil.getString(R.string.scenes_download_cache)
            else -> ResourceUtil.getString(R.string.scenes_other)
        }
    }

    private fun resetList() {
        val list = mutableListOf<Any>()
        var totalSize = 0L
        for (item in mDetailList) {
            list.add(item)
            val childList = item.childList ?: continue
            if (item.isExpand) {
                list.addAll(childList)
            }
            childList.forEach {
                if (it.select) {
                    totalSize += it.iJunkEntity.junkSize
                }
            }
        }
        mDetailLiveData.value = list
        mSelectSizeLiveData.value = totalSize
    }

    /**
     * 获取大小
     */
    fun getSize(list: List<JunkInfo>): Long {
        var size = 0L
        for (item in list) {
            size += item.junkSize
        }
        return size
    }

    fun select(item: MenuJunkParent) {
        item.select = !item.select
        item.childList?.forEach {
            it.select = item.select
        }
        resetList()
    }

    fun select(item: MenuJunkChild) {
        item.select = !item.select
        val parent = item.parent
        var select = true
        parent.childList?.let {
            for (child in it) {
                if (!child.select) {
                    select = false
                    break
                }
            }
        }
        parent.select = select
        resetList()
    }

    fun expand(item: MenuJunkParent) {
        item.isExpand = !item.isExpand
        resetList()
    }

    fun clean(context: Context) {
        val junkList = mutableListOf<JunkInfo>()
        getSelectList().forEach {
            if (it.junks != null) {
                junkList.addAll(it.junks!!)
            }
        }
        if (junkList.isNotEmpty()) {
            transportData {
                put("junk_list", junkList)
            }
            ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_JUNK_CLEAN)
        }
        isFinish.value = true
    }

    /**
     * 获取选中列表
     */
    private fun getSelectList(): List<AppJunk> {
        val list = mutableListOf<AppJunk>()
        for (item in mDetailList) {
            val childList = item.childList ?: continue
            childList.forEach {
                if (it.select) {
                    list.add(it.iJunkEntity)
                }
            }
        }
        return list
    }

}