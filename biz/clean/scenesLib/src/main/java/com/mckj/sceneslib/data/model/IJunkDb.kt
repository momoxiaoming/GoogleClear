package com.mckj.sceneslib.data.model

import com.mckj.cleancore.db.entity.JunkDbEntity


/**
 * Describe:
 *
 * Created By yangb on 2021/3/8
 */
interface IJunkDb {

    /**
     * 获取缓存列表-通过包名
     *
     * @param names 包名列表
     *
     */
    fun getListByPackageNames(names: List<String>): List<JunkDbEntity>?

    /**
     * 获取缓存列表-通过包名
     *
     * @param names 包名列表
     * @param cate 缓存类别
     *
     */
    fun getListByPackageNames(names: List<String>, cate: Int): List<JunkDbEntity>?

    /**
     * 获取缓存列表-剔除包名
     *
     * @param names 包名列表
     *
     */
    fun getListByExcludePackageNames(names: List<String>): List<JunkDbEntity>?

    /**
     * 获取缓存列表-剔除包名
     *
     * @param names 包名列表
     * @param cate 缓存类别
     */
    fun getListByExcludePackageNames(names: List<String>, cate: Int): List<JunkDbEntity>?

}