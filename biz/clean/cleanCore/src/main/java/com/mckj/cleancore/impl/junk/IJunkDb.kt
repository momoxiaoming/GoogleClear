package com.mckj.cleancore.impl.junk

import com.mckj.cleancore.db.entity.JunkDbEntity


/**
 * @author leix
 * @version 1
 * @createTime 2021/8/7 16:12
 * @desc
 */
interface IJunkDb {

    /**
     * 数据库全部数据
     */
    fun getAllList(): List<JunkDbEntity>?

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