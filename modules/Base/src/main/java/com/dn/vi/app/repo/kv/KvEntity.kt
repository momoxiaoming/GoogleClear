package com.dn.vi.app.repo.kv

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 一个简单的kv. 由sqlite来存储
 * Created by holmes on 2020/6/29.
 **/
@Entity(tableName = "kvlite")
data class KvEntity(

    @PrimaryKey
    @ColumnInfo(name = "keys")
    val key: String,

    @ColumnInfo(name = "text1", defaultValue = "''")
    var text: String? = null,

    /**
     * int, boolean
     */
    @ColumnInfo(name = "int1", defaultValue = "0")
    var int1: Int = 0,

    /**
     * long
     */
    @ColumnInfo(name = "int2", defaultValue = "0")
    var int2: Long = 0L,

    @ColumnInfo(name = "created_at", defaultValue = "0")
    var createdAt: Long = 0L,

    @ColumnInfo(name = "update_at", defaultValue = "0")
    var updatedAt: Long = 0L

) {

    /**
     * bool, int1的代理(0, 1)
     */
    var bool1: Boolean
        get() = int1 != 0
        set(value) {
            int1 = if (value) 1 else 0
        }

}