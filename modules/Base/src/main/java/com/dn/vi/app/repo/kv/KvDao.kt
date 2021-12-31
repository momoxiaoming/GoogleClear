package com.dn.vi.app.repo.kv

import androidx.room.*

/**
 * K-V dao
 * Created by holmes on 2020/6/29.
 **/
@Dao
abstract class KvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addKv(kvEntity: KvEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addKvBat(kvEntities: List<KvEntity>)

    @Update
    abstract fun updateKv(kvEntity: KvEntity)

    @Update
    abstract fun updateKvBat(kvEntity: List<KvEntity>): Int

    @Delete
    abstract fun deleteKv(kvEntity: KvEntity): Int

    @Query("SELECT * FROM kvlite WHERE keys = :key LIMIT 1")
    abstract fun getKv(key: String): KvEntity?

    @Query("SELECT COUNT(*) FROM kvlite")
    abstract fun poolSize(): Int

    /**
     * 更新或添加
     */
    @Transaction
    open fun updateOrAdd(kvEntity: KvEntity): KvEntity {
        val now = System.currentTimeMillis()
        if (kvEntity.createdAt == 0L) {
            kvEntity.createdAt = now
        }
        if (kvEntity.updatedAt == 0L) {
            kvEntity.updatedAt = now
        }

        val exists = getKv(kvEntity.key)
        if (exists != null) {
            kvEntity.createdAt = exists.createdAt
            updateKv(kvEntity)
        } else {
            addKv(kvEntity)
        }
        return kvEntity
    }

    /**
     * 批量更新或添加
     */
    @Transaction
    open fun updateOrAddBat(kvEntity: List<KvEntity>): List<KvEntity> {
        kvEntity.forEach { kve ->
            updateOrAdd(kve)
        }
        return kvEntity
    }

}