package com.mckj.sceneslib.ui.junk

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.cm.utils.FileUtil
import com.dn.vi.app.cm.utils.FileUtils
import com.mckj.api.client.JunkConstants
import com.mckj.api.client.JunkExecutor
import com.mckj.api.client.base.JunkClient
import com.mckj.api.client.task.CleanCooperation
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.CacheJunk
import com.mckj.api.entity.JunkInfo
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.Log
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.launch
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.MenuJunkChild
import com.mckj.sceneslib.entity.MenuJunkParent
import com.mckj.sceneslib.event.CleanFinishEvent
import com.mckj.sceneslib.gen.St
import com.org.openlib.help.Consumer3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class JunkViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "JunkViewModel"
        const val SCAN_MIN_TIME = 2000
    }

    var isOut: String = "in"

    /**
     * 垃圾总大小
     */
    var mTotalSize = 0L

    /**
     * 垃圾大小
     */
    val mJunkSizeLiveData = MutableLiveData<Long>()

    val junkList: MutableList<AppJunk> = mutableListOf()

    val mScanPathLiveData = MutableLiveData<String>()

    /**
     * 累计垃圾大小
     * @param list List<IJunkEntity>?
     */
    private fun initJunkData() {
        junkList.clear()
        mJunkSizeLiveData.postValue(mTotalSize)
    }


    fun getScanTask() {
        var junkTotal1 = 0L
        var junkTotal2 = 0L
        var junkTotal3 = 0L


        val stepCount = 4
        var step = 0
        step++
        dispatchProgress(step * 100 / stepCount)
        initJunkData()
        launch {
            val start = System.currentTimeMillis()
            JunkClient.instance.silentScan(
                CleanCooperation.getCacheExecutor(),
                consumer = { status, appJunk, cacheJunk ->
                    viewModelScope.launch {
                        if (status == JunkConstants.ScanStatus.SCAN_IDLE) {
                            //扫描中
                            appJunk?.let {
                                junkList.add(it)
                                mTotalSize += it.junkSize
                                junkTotal1 += it.junkSize
                                mJunkSizeLiveData.postValue(mTotalSize)
                                it.junks?.forEach { junkInfo ->
                                    mScanPathLiveData.postValue(junkInfo.path)
                                }
                            }
                        } else if (status == JunkConstants.ScanStatus.COMPLETE) {
                            val timeTotal = System.currentTimeMillis() - start
                            computationAndSt("缓存扫描", timeTotal, junkTotal1)
                            //扫描结束
                            if (timeTotal < SCAN_MIN_TIME) {
                                delay(1000)
                            }
                            step++
                            sendProgress(step * 100 / stepCount)
                           VLog.i( "缓存清理完成:$timeTotal")
                        }
                    }
                }) {

            }
        }
        launch {
            withContext(Dispatchers.IO) {
                val start = System.currentTimeMillis()
                JunkClient.instance.silentScan(
                    CleanCooperation.getApkCleanExecutor(),
                    consumer = { status, appJunk, cacheJunk ->
                        viewModelScope.launch {
                            if (status == JunkConstants.ScanStatus.SCAN_IDLE) {
                                //扫描中
                                appJunk?.let {
                                    junkList.add(it)
                                    mTotalSize += it.junkSize
                                    junkTotal2 += it.junkSize
                                    mJunkSizeLiveData.postValue(mTotalSize)
                                    it.junks?.forEach { junkInfo ->
                                        mScanPathLiveData.postValue(junkInfo.path)
                                    }
                                }
                            } else if (status == JunkConstants.ScanStatus.COMPLETE) {
                                //扫描结束
                                val timeTotal = System.currentTimeMillis() - start
                                val f = timeTotal / 1000.0f
                                computationAndSt("APK扫描", timeTotal, junkTotal1)
                                //扫描结束
                                if (timeTotal < SCAN_MIN_TIME) {
                                    delay(1000)
                                }
                                step++
                                sendProgress(step * 100 / stepCount)
                                VLog.i( "apk清理完成:${timeTotal}")
                            }
                        }
                    }) {

                }
            }
        }
        launch {
            withContext(Dispatchers.IO) {
                val start = System.currentTimeMillis()
                JunkClient.instance.silentScan(
                    CleanCooperation.getResidualCleanExecutor(),
                    consumer = { status, appJunk, cacheJunk ->
                        viewModelScope.launch {
                            if (status == JunkConstants.ScanStatus.SCAN_IDLE) {
                                //扫描中
                                appJunk?.let {
                                    junkList.add(it)
                                    mTotalSize += it.junkSize
                                    junkTotal3 += it.junkSize

                                    mJunkSizeLiveData.postValue(mTotalSize)
                                    it.junks?.forEach { junkInfo ->
                                        mScanPathLiveData.postValue(junkInfo.path)
                                    }
                                }
                            } else if (status == JunkConstants.ScanStatus.COMPLETE) {
                                val timeTotal = System.currentTimeMillis() - start
                                computationAndSt("卸载残留扫描", timeTotal, junkTotal1)
                                //扫描结束
                                if (timeTotal < SCAN_MIN_TIME) {
                                    delay(1000)
                                }
                                step++
                                sendProgress(step * 100 / stepCount)
                                VLog.i( "残留清理完成:${timeTotal}")
                            }
                        }
                    }) {

                }
            }
        }
    }

    /**
     * 统计垃圾大小与扫描时间
     *
     * 上传埋点
     */
    private fun computationAndSt(name: String, timeTotal: Long, junkTotal1: Long) {
        val f = timeTotal / 1000.0f
        val fileSizeNumberText = FileUtil.getFileSizeNumberText(junkTotal1)
        val fileSizeUnitText = FileUtil.getFileSizeUnitText(junkTotal1)
        St.stScanJunkFlashShow(
            "$fileSizeNumberText$fileSizeUnitText",
            isOut,
            name,
            String.format("%.1fs", f)
        )
    }


    private val progressList = LinkedList<Int>()

    /**
     * 发送进度，进行列队时延
     * @param progress 当前进度
     */
    private fun sendProgress(progress: Int) {
        progressList.offer(progress)
        doDelay()
    }

    /**
     * 当前是否正在时延
     */
    private val working = AtomicBoolean(false)

    /**
     * 进行时延列队
     */
    private fun doDelay() {
        //如果当前正在时延，结束
        if (working.get()) {
            return
        }
        //检查列队，为空结束
        if (progressList.isEmpty()) {
            return
        }
        //执行时延
        launch {
            working.set(true)
            //延时
            delay(1000)
            //获取进度列队第一个
            val poll = progressList.poll()
            poll?.let {
                //分发进度
                dispatchProgress(it)
            }
            working.set(false)
            //自循环，检查列队
            doDelay()
        }
    }


    val mScanProgress = MutableLiveData<Int>(0)
    private fun dispatchProgress(progress: Int) {
        mScanProgress.postValue(progress)
    }


    /**
     * 列表详情
     */
    val mDetailLiveData = MutableLiveData<List<Any>>()

    /**
     * 选中大小
     */
    val mSelectSizeLiveData = MutableLiveData<Long>()

    private val mDetailList: MutableList<MenuJunkParent> by lazy { mutableListOf() }

    fun getDetailList() = mDetailList

    fun initRecycleData(list: MutableList<AppJunk>?) {
        mDetailList.clear()
        if (list.isNullOrEmpty()) {
            return
        }
        val allJunks = mutableListOf<JunkInfo>()//全部的缓存颗粒
        list.forEach {
            it.junks?.apply {
                allJunks.addAll(this)
            }
        }
        //缓存颗粒根据垃圾类型重新分类
        val typesMap = allJunks.groupBy {
            it.junkType
        }
        //重新组装数据
        for ((key, value) in typesMap) {
            val name = getParentName(key)//垃圾名称：@Linked  JunkConstants.JunkType
            val size = getSize(value)
            val parent = MenuJunkParent(
                name,
                size,
                isExpand = false,
                select = true
            )
            val childList = mutableListOf<MenuJunkChild>()
            //重新分组数据:将相同的包名的垃圾分组
            val groupMap = value.groupBy { junkInfo ->
                junkInfo.parent
            }
            for ((key, value) in groupMap) {
                buildAppJunk(key, list)?.let {
                    it.junks = value as MutableList<JunkInfo>
                    it.junkSize = getSize(value)
                    childList.add(
                        MenuJunkChild(
                            select = true,
                            iJunkEntity = it, parent
                        )
                    )
                }
            }
            parent.childList = childList
            mDetailList.add(parent)
        }
        resetList()
    }

    private fun buildAppJunk(packageName: String, list: List<AppJunk>?): AppJunk? {
        var appJunk: AppJunk?
        list?.let {
            for (bean in it) {
                if (packageName == bean.packageName) {
                    val temp = mutableListOf<JunkInfo>()
                    temp.addAll(bean.junks!!)
                    appJunk = AppJunk(
                        type = bean.type,
                        appName = bean.appName,
                        packageName = bean.packageName,
                        icon = bean.icon,
                        junkSize = bean.junkSize,
                        junkDescription = bean.junkDescription,
                        junks = temp
                    )
                    return appJunk
                }
            }
        }
        return null
    }


    private fun getParentName(junkType: Int): String {
        return when (junkType) {
            JunkConstants.JunkType.CACHE -> ResourceUtil.getString(R.string.scenes_app_cache)
            JunkConstants.JunkType.AD_CACHE -> ResourceUtil.getString(R.string.scenes_ad_spam)
            JunkConstants.JunkType.LOG -> ResourceUtil.getString(R.string.scenes_log_cache)
            JunkConstants.JunkType.DOWNLOAD -> ResourceUtil.getString(R.string.scenes_download_cache)
            JunkConstants.JunkType.APK -> ResourceUtil.getString(R.string.scenes_useless_package)
            JunkConstants.JunkType.EMPTY_DIR -> ResourceUtil.getString(R.string.scenes_empty_folder)
            else -> ResourceUtil.getString(R.string.scenes_other)
        }
    }

    private fun resetList() {
        val list = mutableListOf<Any>()
        var totalSize = 0L
        for (item in mDetailList) {
            list.add(item)
            val childList = item.childList ?: continue
            if (item.isExpand) {
                list.addAll(childList)
            }
            childList.forEach {
                if (it.select) {
                    totalSize += it.iJunkEntity.junkSize
                }
            }
        }
        mDetailLiveData.value = list
        mSelectSizeLiveData.value = totalSize
    }

    /**
     * 获取大小
     */
    fun getSize(list: List<JunkInfo>): Long {
        var size = 0L
        for (item in list) {
            size += item.junkSize
        }
        return size
    }

    fun select(item: MenuJunkParent) {
        item.select = !item.select
        item.childList?.forEach {
            it.select = item.select
        }
        resetList()
    }

    fun select(item: MenuJunkChild) {
        item.select = !item.select
        val parent = item.parent
        var select = true
        parent.childList?.let {
            for (child in it) {
                if (!child.select) {
                    select = false
                    break
                }
            }
        }
        parent.select = select
        resetList()
    }

    fun expand(item: MenuJunkParent) {
        item.isExpand = !item.isExpand
        resetList()
    }


    private fun expandAll() {
        for (item in mDetailList) {
            item.isExpand = false
        }
        resetList()
    }


    fun cleanJunk(consumer: Consumer3<Int, Int, JunkInfo?>) {
        //折叠所有垃圾列表
        expandAll()
        //不同垃圾执行器map，value为对应垃圾执行器选中的垃圾集合
        val junkTypeMap = HashMap<Int, MutableList<JunkInfo>>()
        //处理无垃圾的情况
        if (mDetailList.isEmpty()) {
            consumer.accept(0, 0, null)
            return
        }
        //处理有垃圾的情况
        for (parent in mDetailList) {
            val childList = parent.childList ?: continue
            for (child in childList) {
                if (!child.select) {
                    continue
                }
                val iJunkEntity = child.iJunkEntity
                //在没有垃圾执行器的情况下默认为缓存
                val junkType = iJunkEntity.type ?: JunkConstants.AppCacheType.APP_CACHE
                iJunkEntity.junks?.let {
                    var junkInfoList = junkTypeMap[junkType]
                    if (junkInfoList == null) {
                        junkInfoList = mutableListOf()
                    }
                    junkInfoList.addAll(it)
                    junkTypeMap[junkType] = junkInfoList
                }
            }
        }

        val cleanFinishCallback: (Int, JunkInfo?) -> Unit = { result, item ->
            consumer.accept(junkTypeMap.size, result, item)
        }
        val list = mutableListOf<Any>()
        val values = junkTypeMap.values
        for (item in values) {
            list.addAll(item)
        }
        val delay = calculateDelayBySize(list.size)
        //遍历有垃圾的执行器分类
        val iterator = junkTypeMap.iterator()
        launch {
            while (iterator.hasNext()) {
                val next = iterator.next()
                //对应垃圾的执行器的列表
                val value = next.value
                //清理对应垃圾
                try {
                    JunkClient.instance.cleanBySuspend(next.key, value, cleanFinishCallback, delay)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        EventBus.getDefault().post(CleanFinishEvent())
    }

    /**
     * 小于2000个垃圾时，总延时低于2s
     * @param junkSize 处理垃圾总个数
     * @return 单个垃圾的延时
     */
    private fun calculateDelayBySize(junkSize: Int): Long {
        val totalDelay = 2000L
        val delay = when (junkSize) {
            in 0..50 -> {
                (20..40).random().toLong()
            }
            in 51..2000 -> {
                totalDelay / junkSize
            }
            else -> {
                0
            }
        }
        return delay
    }

    /**
     * 获取选中列表
     */
    fun getSelectList(): List<AppJunk> {
        val list = mutableListOf<AppJunk>()
        for (item in mDetailList) {
            val childList = item.childList ?: continue
            childList.forEach {
                if (it.select) {
                    list.add(it.iJunkEntity)
                }
            }
        }
        return list
    }


    override fun onCleared() {
        super.onCleared()
        JunkClient.instance.clearExecutor(JunkConstants.Session.APP_CACHE)
        JunkClient.instance.clearExecutor(JunkConstants.Session.APK)
        JunkClient.instance.clearExecutor(JunkConstants.Session.UNINSTALL_RESIDUE)
    }
}