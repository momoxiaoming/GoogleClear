package com.mckj.module.cleanup.ui.junkDetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.dn.vi.app.base.app.kt.transportData
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.manager.junk.JunkToolManager
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.dn.baselib.base.databinding.AbstractViewModel
import com.mckj.module.cleanup.entity.MenuJunkChild
import com.mckj.module.cleanup.entity.MenuJunkParent

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

    fun init(list: List<IJunkEntity>?) {
        mDetailList.clear()
        if (list.isNullOrEmpty()) {
            return
        }
        //垃圾分类
        val map = mutableMapOf<Int, MutableList<IJunkEntity>>()
        list.forEach { entity ->
            val type = entity.getJunkType()
            var items = map[type]
            if (items == null) {
                items = mutableListOf()
                map[type] = items
            }
            items.add(entity)
        }
        //垃圾组装
        for ((key, value) in map) {
            val name = JunkToolManager.getInstance().getTool(key)?.getName() ?: "未知"
            val size = getSize(value)
            val parent = MenuJunkParent(
                name,
                size,
                isExpand = false,
                select = true
            )
            val childList = mutableListOf<MenuJunkChild>()
            for (item in value) {
                childList.add(MenuJunkChild(true, item, parent))
            }
            parent.childList = childList
            mDetailList.add(parent)
        }
        resetList()
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
                    totalSize += it.iJunkEntity.getJunkSize()
                }
            }
        }
        mDetailLiveData.value = list
        mSelectSizeLiveData.value = totalSize
    }

    /**
     * 获取大小
     */
    fun getSize(list: List<IJunkEntity>): Long {
        var size = 0L
        for (item in list) {
            size += item.getJunkSize()
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
        val list = getSelectList()
        if (list.isNotEmpty()) {
            transportData {
                put("junk_list", list)
            }
//            ScenesManager.getInstance().jump(context, ScenesType.TYPE_JUNK_CLEAN)
            ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_JUNK_CLEAN)
        }
        isFinish.value = true
    }

    /**
     * 获取选中列表
     */
    fun getSelectList(): List<IJunkEntity> {
        val list = mutableListOf<IJunkEntity>()
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