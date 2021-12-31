package com.dn.vi.app.repo.kv

import android.content.Context
import androidx.room.Room
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.repo.kv.KvLite.async
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import okio.ByteString.Companion.toByteString

/**
 * 内置 一个简单的 K-V 结构
 *
 * 性能要求不高的场景, 以及数据量相对大，不适合一值放内存的场景
 *
 * [async]是对操作的rx封装,异步调用。
 *
 * 同步方法，不能在主线程调用。主线程一定要用[async]
 *
 * Created by holmes on 2020/6/29.
 **/
object KvLite {
    const val databaseName = "lite-db"

    // === 外部依赖 ===

    private val context: Context
        get() = AppMod.app

    private val gson: Gson
        get() = AppMod.appComponent.getGson()

    // === $ ===

    private val kvDb: KvDatabase by lazy {
        Room.databaseBuilder(context, KvDatabase::class.java, databaseName)
            .enableMultiInstanceInvalidation()
            .addMigrations(Migration1to2())
            .build()
    }

    /**
     * 原始 DAO, 一般不直接使用
     */
    val dao: KvDao by lazy {
        kvDb.kvDao()
    }

    /**
     * 异步操作接口: rxjava
     * 和[rx]一样
     */
    val async
        get() = rx

    /**
     * 异步操作接口: rxjava
     */
    val rx: RxDelegate by lazy { RxDelegate() }

    /**
     * 异步操作接口: coroutine
     */
    val coroutine: CoroutineDelegate by lazy { CoroutineDelegate() }


    /**
     * 如果不存在，则为null
     */
    fun getKvRaw(key: String): KvEntity? = dao.getKv(key)

    /**
     * 获取一个key的值
     *
     * @return 不存的时候也会返回""
     */
    fun getKv(key: String): String {
        val entity = dao.getKv(key)
        return entity?.text ?: ""
    }

    /**
     * 添加，或修改一个key的值。 值为string
     * @return 如果 [KvEntity.createdAt] == [KvEntity.updatedAt]则说明是添加。
     */
    fun putKv(key: String, text: String): KvEntity {
        val entity = KvEntity(key).also {
            val now = System.currentTimeMillis()
            it.text = text
            it.createdAt = now
            it.updatedAt = now
        }
        dao.updateOrAdd(entity)
        return entity
    }

    /**
     * 添加，或添加一个key的值
     * @param obj 值， 如果是string,则直接修改。其它类似，则会序列化为json，再保存
     */
    fun putObj(key: String, obj: Any): KvEntity {
        val text = when (obj) {
            is CharSequence -> obj.toString()
            else -> gson.toJson(obj)
        }
        return putKv(key, text)
    }

    /**
     * 单独设置[key]的int值
     * 会盖掉其它值
     */
    fun putInt(key: String, i: Int): KvEntity {
        val entity = KvEntity(key).also {
            val now = System.currentTimeMillis()
            it.int1 = i
            it.createdAt = now
            it.updatedAt = now
        }
        dao.updateOrAdd(entity)
        return entity
    }

    /**
     * 获取一个key的int值
     *
     * @return 不存的时候也会返回 [defVal]
     */
    fun getInt(key: String, defVal: Int = 0): Int {
        val entity = dao.getKv(key)
        return entity?.int1 ?: defVal
    }

    /**
     * 单独设置[key]的bool值
     * 会盖掉其它值
     */
    fun putBool(key: String, b: Boolean): KvEntity {
        val entity = KvEntity(key).also {
            val now = System.currentTimeMillis()
            it.bool1 = b
            it.createdAt = now
            it.updatedAt = now
        }
        dao.updateOrAdd(entity)
        return entity
    }

    /**
     * 获取一个key的bool值
     *
     * @return 不存的时候也会返回 [defVal]
     */
    fun getBool(key: String, defVal: Boolean = false): Boolean {
        val entity = dao.getKv(key)
        return entity?.bool1 ?: defVal
    }

    /**
     * 单独设置[key]的long值
     * 会盖掉其它值
     */
    fun putLong(key: String, l: Long): KvEntity {
        val entity = KvEntity(key).also {
            val now = System.currentTimeMillis()
            it.int2 = l
            it.createdAt = now
            it.updatedAt = now
        }
        dao.updateOrAdd(entity)
        return entity
    }

    /**
     * 获取一个key的long值
     *
     * @return 不存的时候也会返回 [defVal]
     */
    fun getLong(key: String, defVal: Long = 0L): Long {
        val entity = dao.getKv(key)
        return entity?.int2 ?: defVal
    }

    /**
     * 删除key
     */
    fun delete(key: String): Boolean {
        val entity = KvEntity(key)
        val count = dao.deleteKv(entity)
        return count > 0
    }


    /**
     * 将string的转一个hash hex字符
     * 方便缩减字符串长度
     */
    @JvmStatic
    fun buildHash(string: String): String {
        val hashCode = string.hashCode()
        // 32 | 24 | 16 | 8
        val byteArray: ByteArray = byteArrayOf(
            ((hashCode shr 24) and 0xFF).toByte(),
            ((hashCode shr 16) and 0xFF).toByte(),
            ((hashCode shr 8) and 0xFF).toByte(),
            (hashCode and 0xFF).toByte()
        )
        return byteArray.toByteString().hex()
    }

    /**
     * 通用的key, 分段写法
     *
     * @return scope:key[:sub]
     */
    @JvmStatic
    fun joinKeys(scope: String, key: String, sub: String? = null): String {
        if (sub.isNullOrEmpty()) {
            return "$scope:$key"
        } else {
            return "$scope:$key:$sub"
        }
    }

    /**
     * 异步调用
     *
     * rx封装
     */
    class RxDelegate internal constructor() {

        private val scheduler: Scheduler
            get() = Schedulers.io()

        /**
         * 如果不存在，则为null, maybe触发[onComplete]
         */
        fun getKvRaw(key: String): Maybe<KvEntity> {
            return Maybe.create<KvEntity> { emitter ->
                KvLite.getKvRaw(key).also { entity ->
                    if (!emitter.isDisposed) {
                        if (entity != null) {
                            emitter.onSuccess(entity)
                        } else {
                            emitter.onComplete()
                        }
                    }
                }
            }.subscribeOn(scheduler)
        }

        fun getKv(key: String): Single<String> {
            return Single.fromCallable {
                return@fromCallable KvLite.getKv(key)
            }.subscribeOn(scheduler)
        }

        fun putKv(key: String, text: String): Single<KvEntity> {
            return Single.fromCallable {
                return@fromCallable KvLite.putKv(key, text)
            }.subscribeOn(scheduler)
        }

        fun putObj(key: String, obj: Any): Single<KvEntity> {
            return Single.fromCallable {
                return@fromCallable KvLite.putObj(key, obj)
            }.subscribeOn(scheduler)
        }

        /**
         * 单独设置[key]的int值
         * 会盖掉其它值
         */
        fun putInt(key: String, i: Int): Single<KvEntity> {
            return Single.fromCallable {
                return@fromCallable KvLite.putInt(key, i)
            }.subscribeOn(scheduler)
        }

        /**
         * 获取一个key的int值
         *
         * @return 不存的时候也会返回 [defVal]
         */
        fun getInt(key: String, defVal: Int = 0): Single<Int> {
            return Single.fromCallable {
                return@fromCallable KvLite.getInt(key, defVal)
            }.subscribeOn(scheduler)
        }

        /**
         * 单独设置[key]的bool值
         * 会盖掉其它值
         */
        fun putBool(key: String, b: Boolean): Single<KvEntity> {
            return Single.fromCallable {
                return@fromCallable KvLite.putBool(key, b)
            }.subscribeOn(scheduler)
        }

        /**
         * 获取一个key的bool值
         *
         * @return 不存的时候也会返回 [defVal]
         */
        fun getBool(key: String, defVal: Boolean = false): Single<Boolean> {
            return Single.fromCallable {
                return@fromCallable KvLite.getBool(key, defVal)
            }.subscribeOn(scheduler)
        }

        /**
         * 单独设置[key]的long值
         * 会盖掉其它值
         */
        fun putLong(key: String, l: Long): Single<KvEntity> {
            return Single.fromCallable {
                return@fromCallable KvLite.putLong(key, l)
            }.subscribeOn(scheduler)
        }

        /**
         * 获取一个key的long值
         *
         * @return 不存的时候也会返回 [defVal]
         */
        fun getLong(key: String, defVal: Long = 0L): Single<Long> {
            return Single.fromCallable {
                return@fromCallable KvLite.getLong(key, defVal)
            }.subscribeOn(scheduler)
        }


        fun delete(key: String): Single<Boolean> {
            return Single.fromCallable {
                return@fromCallable KvLite.delete(key)
            }.subscribeOn(scheduler)
        }

    }

    /**
     * 异步调用
     *
     * coroutine封装
     */
    class CoroutineDelegate internal constructor() {

        internal val dispatch = Dispatchers.IO  // newSingleThreadContext("kv")
        private val job = Job()
        private val scopeContext = dispatch + job
        private val scope = CoroutineScope(scopeContext)

        /**
         * 如果不存在，则为null
         */
        suspend fun getKvRaw(key: String): KvEntity? {
            return withContext(scopeContext) {
                KvLite.getKvRaw(key)
            }
        }

        suspend fun getKv(key: String): String {
            return withContext(scopeContext) {
                KvLite.getKv(key)
            }
        }

        suspend fun putKv(key: String, text: String): KvEntity {
            return withContext(scopeContext) {
                KvLite.putKv(key, text)
            }
        }

        suspend fun putObj(key: String, obj: Any): KvEntity {
            return withContext(scopeContext) {
                KvLite.putObj(key, obj)
            }
        }

        /**
         * 单独设置[key]的int值
         * 会盖掉其它值
         */
        suspend fun putInt(key: String, i: Int): KvEntity {
            return withContext(scopeContext) {
                KvLite.putInt(key, i)
            }
        }

        /**
         * 获取一个key的int值
         *
         * @return 不存的时候也会返回 [defVal]
         */
        suspend fun getInt(key: String, defVal: Int = 0): Int {
            return withContext(scopeContext) {
                KvLite.getInt(key, defVal)
            }
        }

        /**
         * 单独设置[key]的bool值
         * 会盖掉其它值
         */
        suspend fun putBool(key: String, b: Boolean): KvEntity {
            return withContext(scopeContext) {
                KvLite.putBool(key, b)
            }
        }

        /**
         * 获取一个key的bool值
         *
         * @return 不存的时候也会返回 [defVal]
         */
        suspend fun getBool(key: String, defVal: Boolean = false): Boolean {
            return withContext(scopeContext) {
                KvLite.getBool(key, defVal)
            }
        }

        /**
         * 单独设置[key]的long值
         * 会盖掉其它值
         */
        suspend fun putLong(key: String, l: Long): KvEntity {
            return withContext(scopeContext) {
                KvLite.putLong(key, l)
            }
        }

        /**
         * 获取一个key的long值
         *
         * @return 不存的时候也会返回 [defVal]
         */
        suspend fun getLong(key: String, defVal: Long = 0L): Long {
            return withContext(scopeContext) {
                KvLite.getLong(key, defVal)
            }
        }


        suspend fun delete(key: String): Boolean {
            return withContext(scopeContext) {
                KvLite.delete(key)
            }
        }

    }


}