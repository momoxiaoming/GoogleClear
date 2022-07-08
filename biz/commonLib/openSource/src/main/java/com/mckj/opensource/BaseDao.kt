package com.mckj.opensource

import androidx.room.*

/**
 * Describe:
 *
 * Created By yangb on 2020/10/16
 */
@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t : T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<T>)

    @Delete
    fun deleteList(elements:List<T>)

    @Delete
    fun delete(vararg elements:T)

    @Update
    fun update(element: T)

}