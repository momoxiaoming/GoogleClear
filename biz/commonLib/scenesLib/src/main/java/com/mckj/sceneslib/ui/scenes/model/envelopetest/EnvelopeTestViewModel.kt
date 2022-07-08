package com.mckj.sceneslib.ui.scenes.model.envelopetest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.launch
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.DelayTestTaskData
import com.mckj.sceneslib.entity.PingResult
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.util.NetUtil
import com.mckj.sceneslib.util.ShellUtil
import kotlinx.coroutines.*
import kotlin.math.roundToInt

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class EnvelopeTestViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "EnvelopeTestViewModel"
    }


    var adCached: Boolean = false

    //网络连接
    var isConnect: Boolean = false

    //动画旋转中
    var isRotate: Boolean = false

    //头部动画区域是否显示
    var isHeadShow: Boolean = true


    /**
     * 百分比
     */
    val progressNum = MutableLiveData<Int>(0)

    /**
     * 任务列表数据
     */
    val taskDataLiveData = MutableLiveData<List<DelayTestTaskData>>()


    fun initTestTask(): MutableList<DelayTestTaskData> {
        val taskList = mutableListOf<DelayTestTaskData>()

        val wx = DelayTestTaskData(
            R.drawable.scenes_ic_envelope_wx,
            "微信",
            "extshort.weixin.qq.com",
            -1
        )
        wx.status = DelayTestTaskData.STATE_LOADING

        val zfb = DelayTestTaskData(
            R.drawable.scenes_ic_envelope_zfb,
            "支付宝",
            "mgw.alipay.com",
            -1
        )
        zfb.status = DelayTestTaskData.STATE_LOADING


        val qq = DelayTestTaskData(
            R.drawable.scenes_ic_envelope_qq,
            "QQ",
            "mqq.tenpay.com",
            -1
        )
        qq.status = DelayTestTaskData.STATE_LOADING


        val dy = DelayTestTaskData(
            R.drawable.scenes_ic_envelope_dy,
            "抖音",
            "aweme.snssdk.com",
            -1
        )
        dy.status = DelayTestTaskData.STATE_LOADING


        val ks = DelayTestTaskData(
            R.drawable.scenes_ic_envelope_ks,
            "快手",
            "gifshow.com",
            -1
        )
        ks.status = DelayTestTaskData.STATE_LOADING

        taskList.add(wx)
        taskList.add(zfb)
        taskList.add(qq)
        taskList.add(dy)
        taskList.add(ks)

        taskDataLiveData.value = taskList
        return taskList
    }


    fun runTask(task: MutableList<DelayTestTaskData>) {
        taskStartTime = System.currentTimeMillis()
        runTaskNum = task.size
        task.mapIndexed { index, _ ->
            runTaskOfPosition(task, index)
        }

    }

    /**
     * 只运行任务列表中的指定索引任务
     */
    fun runTaskOfIndex(task: MutableList<DelayTestTaskData>, index: Int) {
        runTaskNum = 1
        taskStartTime = System.currentTimeMillis()
        runTaskOfPosition(task, index)
    }

    private var taskStartTime: Long = 0
    var taskCountTime: Long = 0
    private var runTaskNum = 0
    private fun runTaskOfPosition(task: MutableList<DelayTestTaskData>, position: Int) {
        val size = task.size
        progressNum.value = (100 - runTaskNum / size.toFloat() * 100).roundToInt()
        launch {
            /*更新状态为loading*/
            val delayTestTaskData = task[position]
            delayTestTaskData.status = DelayTestTaskData.STATE_LOADING
            delayTestTaskData.delay = -1
            taskDataLiveData.value = task

            /*ping*/
            val testNetDelay = testNetDelay(delayTestTaskData.host)
            val status = testNetDelay.status
            /*更新状态*/
            if (status == -1) {
                delayTestTaskData.delay = -1
                delayTestTaskData.status = DelayTestTaskData.STATE_ERROR
            } else {
                delayTestTaskData.status = DelayTestTaskData.STATE_FINISH
                delayTestTaskData.delay = testNetDelay.avg
            }

            /*更新进度*/
            runTaskNum--
            //如果任务运行完毕
            if (runTaskNum == 0) {
                //统计任务时间
                taskCountTime = System.currentTimeMillis() - taskStartTime

                val waitAdTime = 8*1000L
                //如果任务运行时间小于等待广告时间
                if (taskCountTime < waitAdTime) {
                    //等待广告结束
                    waitAdLoadEnd(waitAdTime - taskCountTime)
                }
            }

            progressNum.value = (100 - runTaskNum / size.toFloat() * 100).roundToInt()
            taskDataLiveData.value = task
        }
    }


    private suspend fun testNetDelay(host: String): PingResult {
        //ping6次，10s超时
        val cmd = "/system/bin/ping -c 6 -w 10 $host"
        var execCmd: ShellUtil.CommandResult?
        withContext(Dispatchers.IO) {
            execCmd = ShellUtil.execCmd(cmd)
        }
        val successMsg = execCmd?.successMsg

        return if (successMsg.isNullOrEmpty()) {
            NetUtil.parsPingResult(host, "")
        } else {
            NetUtil.parsPingResult(host, successMsg)
        }
    }


    fun getConnectInfo(): ConnectInfo? {
        return NetworkData.getInstance().connectInfoLiveData.value
    }

    /**
     * 插入等待任务，任务完毕时设置进度为100,在progressNum中回调
     * @see progressNum
     */
    fun waitTimeOut(timeout: Long = 5000) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(timeout)
            taskCountTime = System.currentTimeMillis() - taskStartTime
            progressNum.postValue(100)
        }
    }

    /**
     * 等待广告加载结束直到超时
     * @param timeout 超时时间
     * @return 广告加载完毕返回true，超时false
     */
    private suspend fun waitAdLoadEnd(timeout: Long = 5000): Boolean {
        return withTimeoutOrNull(timeout) {
            var result = false
            while (true) {
                if (adCached) {
                    result = true
                    break
                }
                delay(100)
            }
            result
        } ?: false
    }

}