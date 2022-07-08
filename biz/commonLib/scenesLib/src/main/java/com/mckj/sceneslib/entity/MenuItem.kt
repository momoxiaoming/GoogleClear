package com.mckj.sceneslib.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
@SuppressLint("ParcelCreator")
@Parcelize
open class MenuItem() : Parcelable {

    constructor(
        type: Int,
        update: Long = 0L,
        isRecommend: Boolean = false,
        isAuditConfig: Boolean = false,
        resId: Int = 0,menuType:Int = 0
    ) : this() {
        this.type = type
        this.update = update
        this.isRecommend = isRecommend
        this.isAuditConfig = isAuditConfig
        this.resId = resId
        this.menuType = menuType
    }

    @SerializedName("t")
    var type: Int = 0

    /**
     * 更新时间
     */
    @SerializedName("update")
    var update: Long = 0L

    /**
     * 能否推荐
     */
    @SerializedName("ra")
    var recommendAble: Boolean = false

    /**
     * 是否开启审核配置
     */
    @SerializedName("iac")
    var isAuditConfig: Boolean = false

    /**
     * 是否推荐
     */
    @Transient
    var isRecommend: Boolean = false

    /**
     * 资源id
     */
    @Transient
    var resId: Int = 0

    /**
     * 菜单类型
     */
    @Transient
    var menuType: Int = 0
}

@SuppressLint("ParcelCreator")
class MenuBusinessItem(
    type: Int,
    update: Long = 0L,
    recommendAble: Boolean = false,
    isAuditConfig: Boolean = false,
    resId: Int = 0
) : MenuItem(type, update, recommendAble, isAuditConfig, resId) {

}

@SuppressLint("ParcelCreator")
class MenuJumpItem(
    type: Int,
    update: Long = 0L,
    recommendAble: Boolean = false,
    isAuditConfig: Boolean = false
) : MenuItem(type, update, recommendAble, isAuditConfig) {

}

data class Category(var category: String)