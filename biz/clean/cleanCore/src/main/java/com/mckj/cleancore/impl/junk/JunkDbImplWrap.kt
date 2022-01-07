package com.mckj.cleancore.impl.junk

import com.dn.baselib.util.AESUtil
import com.mckj.cleancore.db.JunkDatabase
import com.mckj.cleancore.db.dao.JunkDbDao
import com.mckj.cleancore.db.entity.JunkDbEntity


/**
 * @author leix
 * @version 1
 * @createTime 2021/8/9 18:12
 * @desc
 */
class JunkDbImplWrap : IJunkDb {
    private val junkDao: JunkDbDao by lazy { JunkDatabase.getInstance().junkDbDao() }


    override fun getAllList(): List<JunkDbEntity>? {
        val allList = junkDao.getAllList()
        return decrypt(allList)
    }

    override fun getListByPackageNames(names: List<String>): List<JunkDbEntity>? {
        return decrypt(junkDao.getListByPackageNames(names))
    }

    override fun getListByPackageNames(
        names: List<String>,
        cate: Int,
    ): List<JunkDbEntity>? {
        return decrypt(junkDao.getListByPackageNames(names, cate))
    }

    override fun getListByExcludePackageNames(names: List<String>): List<JunkDbEntity>? {
        return decrypt(junkDao.getListByExcludePackageNames(names))
    }

    override fun getListByExcludePackageNames(
        names: List<String>,
        cate: Int,
    ): List<JunkDbEntity>? {
        return decrypt(junkDao.getListByExcludePackageNames(names, cate))
    }

    private fun decrypt(list: List<JunkDbEntity>?): List<JunkDbEntity>? {
        list?.let {
            for (bean in it) {
                val filePath = bean.filePath
                val rootPath = bean.rootPath
                if (!filePath.isNullOrEmpty()) {
                    bean.filePath = AESUtil.d(filePath).replace("\"","")
                }
                if (!rootPath.isNullOrEmpty()) {
                    bean.rootPath = AESUtil.d(rootPath).replace("\"","")
                }
            }
        }
        return list
    }
}