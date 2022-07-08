package com.mckj.module.utils

import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.base.app.AppMod
import com.mckj.api.entity.JunkInfo
import com.mckj.module.bean.SortBean
import com.mckj.module.widget.TxTab
import com.mckj.module.widget.dialog.NormalDialogFragment

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/15 16:31
 * @desc
 */
class UIHelper {
    fun buildTabItem(
        tabName: String,
        sortedList: MutableList<SortBean>
    ): TxTab {
        val tabItem = TxTab(AppMod.app)
        tabItem.setupWithTabLayout(
            TxTab.TabEntity(tabName, sortedList)
        )
        return tabItem
    }

    fun buildDateSortList(newest: () -> Unit, last: () -> Unit): MutableList<SortBean> {
        val sortList = mutableListOf<SortBean>()
        sortList.add(SortBean("最新", true, newest))
        sortList.add(SortBean("最久", false, last))
        return sortList
    }

    fun buildSizeSortList(max: () -> Unit, min: () -> Unit): MutableList<SortBean> {
        val sortList = mutableListOf<SortBean>()
        sortList.add(SortBean("最大", true, max))
        sortList.add(SortBean("最小", false, min))
        return sortList
    }

    fun buildMimeTypeSortList(
        word: () -> Unit,
        excel: () -> Unit,
        ppt: () -> Unit,
        pdf: () -> Unit,
        txt: () -> Unit,
        other: () -> Unit
    ): MutableList<SortBean> {
        val sortList = mutableListOf<SortBean>()
        sortList.add(SortBean("Word文档", false, word))
        sortList.add(SortBean("Excel", false, excel))
        sortList.add(SortBean("PPT演示文稿", false, ppt))
        sortList.add(SortBean("PDF", false, pdf))
        sortList.add(SortBean("TXT文件", false, txt))
        sortList.add(SortBean("其他类型", false, other))
        return sortList
    }

    fun getDateSortList(): ArrayList<Int> {
        val dateSort = ArrayList<Int>()
        dateSort.add(McConstants.Sort.SORT_NEWEST)
        dateSort.add(McConstants.Sort.SORT_LAST)
        return dateSort
    }

    fun getSizeSortList(): ArrayList<Int> {
        val dateSort = ArrayList<Int>()
        dateSort.add(McConstants.Sort.SORT_MAX)
        dateSort.add(McConstants.Sort.SORT_MIN)
        return dateSort
    }

    fun getFileTypeFilterList(): ArrayList<Int> {
        val fileFilterList = ArrayList<Int>()
        fileFilterList.add(McConstants.Filter.FILTER_ALL)
        fileFilterList.add(McConstants.Filter.FILTER_WORD)
        fileFilterList.add(McConstants.Filter.FILTER_EXCEL)
        fileFilterList.add(McConstants.Filter.FILTER_PDF)
        fileFilterList.add(McConstants.Filter.FILTER_TXT)
        fileFilterList.add(McConstants.Filter.FILTER_PPT)
        fileFilterList.add(McConstants.Filter.FILTER_OTHER)
        return fileFilterList
    }

    fun showImgDeleteDialog(
        activity: FragmentActivity,
        selectedList: List<JunkInfo>,
        positive: () -> Unit,
        negative: () -> Unit
    ) {
        showVideoDeleteDialog(activity, selectedList, positive, negative, "张图片")
    }

    fun showVideoDeleteDialog(
        activity: FragmentActivity,
        selectedList: List<JunkInfo>,
        positive: () -> Unit,
        negative: () -> Unit,
        contentTip: String = "个视频"
    ) {
        val spanString = SpannableStringBuilder()
        spanString.append("您选择了")
            .append(
                McUtils.getColorSpan(
                    "${selectedList.size}$contentTip", Color.parseColor("#15B464")
                )
            )
            .append(McUtils.getColorSpan("，删除后7天内可在回收站找回", Color.parseColor("#333333")))
        NormalDialogFragment.newInstance(
            NormalDialogFragment.DialogBean(
                title = "确认删除？",
                content = spanString,
                positiveDesc = "删除",
                negativeDesc = "取消",
                positive = positive,
                negative = negative
            )
        ).show(activity.supportFragmentManager, "NormalDialogFragment")
    }

    fun showDocumentDeleteDialog(
        activity: FragmentActivity,
        selectedList: List<JunkInfo>,
        positive: () -> Unit,
        negative: () -> Unit
    ) {
        val spanString = SpannableStringBuilder()
        spanString.append("文件删除后将无法恢复")
        NormalDialogFragment.newInstance(
            NormalDialogFragment.DialogBean(
                title = "确认删除？",
                content = spanString,
                positiveDesc = "删除",
                negativeDesc = "取消",
                positive = positive,
                negative = negative
            )
        ).show(activity.supportFragmentManager, "NormalDialogFragment")
    }

    fun showDocumentFindDialog(
        activity: FragmentActivity,
        positive: () -> Unit
    ) {
        val spanString = SpannableStringBuilder()
        spanString.append("可以跳转至手机中保存该文件的位置，在那里进行保存、转发等操作")
        NormalDialogFragment.newInstance(
            NormalDialogFragment.DialogBean(
                title = "查找文件文件",
                content = spanString,
                positiveDesc = "查找",
                negativeDesc = "取消",
                positive = positive,
                negative = {})
        ).show(activity.supportFragmentManager, "NormalDialogFragment")
    }
}