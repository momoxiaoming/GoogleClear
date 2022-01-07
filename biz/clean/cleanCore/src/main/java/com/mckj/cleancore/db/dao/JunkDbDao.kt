package com.mckj.cleancore.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.mckj.cleancore.db.entity.JunkDbEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/4 10:32
 * @desc
 */
@Dao
interface JunkDbDao {

    @Query("SELECT * FROM CleanDB WHERE _id = :id")
    fun getJunkDbEntityById(id: Long): JunkDbEntity?

    /**
     * 获取所有列表
     */
    @Query("SELECT * FROM CleanDB")
    fun getAllList(): List<JunkDbEntity>?

    /**
     * 获取列表-分类
     */
    @Query("SELECT * FROM CleanDB WHERE junk_type = :cate")
    fun getListByCate(cate: Int): List<JunkDbEntity>?

    /**
     * 获取列表-类型
     */
    @Query("SELECT * FROM CleanDB WHERE file_type = :type")
    fun getListByType(type: Int): List<JunkDbEntity>?

    /**
     * 获取列表-分类和类型
     */
    @Query("SELECT * FROM CleanDB WHERE junk_type = :cate AND file_type = :type")
    fun getListByCateAndType(cate: Int, type: Int): List<JunkDbEntity>?

    /**
     * 获取列表-根据包名
     */
    @Query("SELECT * FROM CleanDB WHERE pkg_name = :packageName")
    fun getListByPackageName(packageName: String): List<JunkDbEntity>?

    /**
     * 获取列表-根据包名
     */
    @Query("SELECT * FROM CleanDB WHERE pkg_name IN (:names)")
    fun getListByPackageNames(names: List<String>): List<JunkDbEntity>?

    /**
     * 获取列表-排除包名
     */
    @Query("SELECT * FROM CleanDB WHERE pkg_name NOT IN (:names) ")
    fun getListByExcludePackageNames(names: List<String>): List<JunkDbEntity>?

    /**
     * 获取列表-根据包名和分类
     */
    @Query("SELECT * FROM CleanDB WHERE junk_type = :cate AND pkg_name IN (:names)")
    fun getListByPackageNames(names: List<String>, cate: Int): List<JunkDbEntity>?

    /**
     * 获取列表-排除包名
     */
    @Query("SELECT * FROM CleanDB WHERE junk_type = :cate AND pkg_name NOT IN (:names)")
    fun getListByExcludePackageNames(names: List<String>,  cate: Int): List<JunkDbEntity>?

    /**
     * 获取缓存列表-根据app名
     */
    @Query("SELECT * FROM CleanDB WHERE app_name = :appName")
    fun getListByAppName(appName: String): List<JunkDbEntity>?

    /**
     * 获取列表-根据app名
     */
    @Query("SELECT * FROM CleanDB WHERE app_name IN (:names)")
    fun getListByAppNames(names: List<String>): List<JunkDbEntity>?
}