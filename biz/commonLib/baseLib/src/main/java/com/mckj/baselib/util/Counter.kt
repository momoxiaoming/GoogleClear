package com.mckj.baselib.util


import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.drakeet.purewriter.ObscureLifecycleEventObserver
import com.drakeet.purewriter.addObserver
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log


/**
 * 计时器
 * @property start Long 开始
 * @property end Long  结束时数值, -1 代表永不结束
 * @property step Long 步长,每次间隔
 * @property period Long 计时间隔 毫秒,默认延迟1000毫秒
 * @property initDelay Long 第一次计时的延时时间,默认毫秒
 * @property scope CoroutineScope 作用域
 * @constructor
 * @author mmxm
 * @date 2021/4/14 12:07
 */
class Counter(
    lifecycleOwner: LifecycleOwner,
    private val stopEvent:Lifecycle.Event = Lifecycle.Event.ON_DESTROY,  //这里停止的周期根据实际去把控
    private val start: Long,
    private val end: Long,
    private val step: Long = 1,
    private val period: Long = 1000,
    private val initDelay: Long = 0,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) :
    Serializable, ICounter {

    private val listReceiver = LinkedList<Counter.(Long) -> Unit>()
    private val listFinish = LinkedList<Counter.(Long) -> Unit>()
    private var state = CounterStatus.STATE_IDLE
    private var count = start
    private var countTime: Long = 0
    private var delay = 0L
    private var finish=false
    @ObsoleteCoroutinesApi
    private lateinit var ticker: ReceiveChannel<Unit>

    init {

        //lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
          //  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            //    if (event == stopEvent) {
              //      stop()
                //}
            //}
        //})

        lifecycleOwner.lifecycle.addObserver(object : ObscureLifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == stopEvent) {
                    stop()
                }
            }
        })
    }


    @ObsoleteCoroutinesApi
    private fun launch(delay: Long = initDelay) {
        finish=false
        scope.launch {
            ticker=ticker(period, delay, mode = TickerMode.FIXED_DELAY)
            for (unit in ticker) {
                noticeSubscribe()
                if((end in count until start)|| end in (start + 1)..count){
                    scope.cancel()
                    finish=true
                    noticeSubscribeFinish()
                }
                if (start > end) {
                    count-=step
                } else {
                    count+=step
                }
                countTime = System.currentTimeMillis()
            }
        }
    }

    private fun noticeSubscribe() {
        listReceiver.forEach {
            it(count)
        }
    }

    private fun noticeSubscribeFinish() {
        listFinish.forEach {
            it(count)
        }
    }

    override fun subscribe(block: Counter.(Long) -> Unit): Counter {
        listReceiver.add(block)
        return this
    }

    override fun subscribeFinish(block: Counter.(Long) -> Unit): Counter {
        listFinish.add(block)
        return this
    }

    override fun isEnd(): Boolean {
        return finish
    }

    override fun start() {
        when (state) {
            CounterStatus.STATE_IDLE, CounterStatus.STATE_PAUSE -> {
                state = CounterStatus.STATE_ACTIVE
                launch()
            }
            else -> {

            }
        }
    }

    override fun resume() {
        when (state) {
            CounterStatus.STATE_IDLE, CounterStatus.STATE_PAUSE -> {
                state = CounterStatus.STATE_ACTIVE
                launch(delay)
            }
            else -> {

            }
        }
    }

    override fun pause() {
        if (state == CounterStatus.STATE_ACTIVE) {
            state = CounterStatus.STATE_PAUSE
            delay = System.currentTimeMillis() - countTime
            scope.cancel()
        }
    }

    override fun reset() {
        if (state == CounterStatus.STATE_IDLE) return
        count = start
        scope.cancel()
        delay = initDelay
        if (state == CounterStatus.STATE_ACTIVE) launch()
    }

    override fun stop() {
        if (state == CounterStatus.STATE_IDLE) return
        state = CounterStatus.STATE_IDLE
        scope.cancel()
        count = start
    }

    fun getEnd():Long{
        return end
    }
}

interface ICounter {

    /**
     * 订阅计数器,计数器改变以及完成时都会粗发此回调
     */
    fun subscribe(block: Counter.(Long) -> Unit): Counter

    /**
     * 订阅计数器,计数器完成时会触发此回调
     *
     */
    fun subscribeFinish(block: Counter.(Long) -> Unit): Counter

    /**
     * 倒计时是否已结束
     */
    fun isEnd():Boolean

    /**
     * 启动
     */
    fun start()

    /**
     * 继续
     */
    fun resume()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 重置
     */
    fun reset()

    /**
     * 停止
     */
    fun stop()
}

/**
 * 计时器的状态
 */
enum class CounterStatus {
    STATE_ACTIVE, STATE_IDLE, STATE_PAUSE
}