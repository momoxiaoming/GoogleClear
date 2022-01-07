package com.mckj.cleancore.impl.junk

import com.mckj.cleancore.db.entity.JunkDbEntity


/**
 * @author leix
 * @version 1
 * @createTime 2021/8/7 16:10
 * @desc 数据库的实现类，对外抛出的数据获取的唯一入口
 */
class JunkDbImpl : IJunkDb {

    companion object {
        val instance: JunkDbImpl by lazy { JunkDbImpl() }
    }

    private val mJunkDbImpWrap = JunkDbImplWrap()

    override fun getAllList(): List<JunkDbEntity>? {
        return mJunkDbImpWrap.getAllList()
    }

    override fun getListByPackageNames(names: List<String>): List<JunkDbEntity>? {
        return mJunkDbImpWrap.getListByPackageNames(names)
    }

    override fun getListByPackageNames(
        names: List<String>,
        cate: Int,
    ): List<JunkDbEntity>? {
        return mJunkDbImpWrap.getListByPackageNames(names, cate)
    }

    override fun getListByExcludePackageNames(names: List<String>): List<JunkDbEntity>? {
        return mJunkDbImpWrap.getListByExcludePackageNames(names)
    }

    override fun getListByExcludePackageNames(
        names: List<String>,
        cate: Int,
    ): List<JunkDbEntity>? {
        return mJunkDbImpWrap.getListByExcludePackageNames(names, cate)
    }
}