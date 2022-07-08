package com.mckj.module.bean


import android.app.Activity
import com.mckj.api.client.JunkConstants
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkInfo
import com.mckj.module.viewmodel.MediaCleanViewModel

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/2 18:33
 * @desc
 */

data class UIDealData(
    var selectedSize: Long = 0,
    var totalSize: Long = 0,
    var selectedJunkList: MutableList<GroupJunkInfo>? = null,
    var expand: Boolean = false
)

data class TxMenuItem(
    var type: Int = 0,
    var iconRes: Int = 0,
    var title: String = "",
    var desc: String = "",
    var appJunk: AppJunk? = null
)

data class UIMediaCleanBean(
    var mimeType: Int = JunkConstants.JunkFileType.IMG,
    var timeGroup: Int,
    var dataList: List<JunkInfo>
)

data class UiCacheRelativeData(
//    var scanLottieImage: String = "",
//    var scanLottieName: String = "",
    var scanLottieIdResRaw:Int = 0,
    var toolBarTitle: String = "",
    var remindTip: String = "",
    var cacheDesc: String = "",
    var logDesc: String = "",
    var otherDesc: String = ""
)

data class GroupJunkInfo(
    var selected: Boolean = true,
    var name: String = "",
    var junkSize: Long = 0,
    var list: MutableList<JunkInfo>? = null
)

data class SortBean(
    val sortName: String = "",
    var selected: Boolean = false,
    var block: () -> Unit
)

data class Category(var isExpand: Boolean, var category: Int, var childList: List<JunkInfo>)

data class QAuthorBean(
    var tag: ChildFragment,
    var result: Int
) {
    fun isOk(): Boolean {
        return result == Activity.RESULT_OK
    }
}

enum class ChildFragment {
    IMG, VIDEO, DOCUMENT
}