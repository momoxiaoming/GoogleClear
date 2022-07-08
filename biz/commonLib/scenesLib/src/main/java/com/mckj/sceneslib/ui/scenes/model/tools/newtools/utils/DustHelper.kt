package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.Delegates

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util
 * @data  2022/2/25 09:30
 */
class DustHelper(context: Context) : DefaultLifecycleObserver {


    companion object {
        private const val TAG="DustHelper"
        private const val totalIndex = 100
        private const val totalTime = 20 * 1000L
        private const val intervalTime = totalTime/totalIndex
        const val MODE_NONE = -1
        const val MODE_START = 0
        const val MODE_STOP = 1
        const val MODE_RESUME = 2
        const val MODE_RESET = 3
    }

    private val mVibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private var start={}
    private var ing: (Int) -> Unit = {}
    private var end = {}
    private var reset = {}
    private var stop = {}

    private var mCountDownTimer: CountDownTimer? = null
    private var mTotalTime = totalTime
    private var mRemainTime = 0L
    private var mCurrentProgress = 0
    private var mVibrateState = MODE_NONE
    private var mStopProgress=0
    private var progressIndex = 0

    private var mCurrentMode by Delegates.observable(MODE_NONE) { _, _, newModel ->
        mVibrateState = newModel
        when (newModel) {
            MODE_START -> {
                complicatedVibrate()
            }
            MODE_STOP -> {
                stopVibrate()
                mStopProgress=mCurrentProgress
                stop()
            }
            MODE_RESUME -> {
                mTotalTime = mRemainTime
                progressIndex = 0
                complicatedVibrate()
            }
            MODE_RESET -> {
                resetConfig()
            }
        }
    }

    private fun resetConfig(isEnd :Boolean=false) {
        mVibrateState = MODE_RESET
        mTotalTime = totalTime
        mRemainTime = 0L
        mCurrentProgress = 0
        progressIndex = 0
        mStopProgress=0
        stopVibrate()
        if (!isEnd) {
            reset()
        }
    }


    private fun startTimer() {
        if (mCountDownTimer == null) {
            mCountDownTimer = object : CountDownTimer(mTotalTime, intervalTime) {
                override fun onTick(millisUntilFinished: Long) {
                    progressIndex++
                    mRemainTime = millisUntilFinished
                    val ingValue=((progressIndex / totalIndex.toFloat()) * 100).toInt()

                    val progress = if (mCurrentMode == MODE_RESUME) {
                        val p=mStopProgress+ingValue
                        if (p> totalIndex) totalIndex else p
                    } else {
                        ingValue
                    }
                    mCurrentProgress =progress
                    Log.d(TAG, "startTimer--------$mCurrentMode-------$progress")
                    ing(progress)
                 }
                override fun onFinish() {
                    end()
                    resetConfig(true)
                }
            }.start()
        }
    }

    /**
     *
     * @param timings 交替开关时间的模式，从关闭开始。值为0将导致忽略时序 / 幅度序列。
     * @param amplitude 振动的强度。它必须是1到255之间的值，或DEFAULT_AMPLITUDE。
     *  @param repeat 索引到重复的模式，如果你不想重复，则为-1。
     */
    private fun complicatedVibrate(
        timings: LongArray = longArrayOf(0,1000),
        amplitude: IntArray = intArrayOf(255,255),
        repeat: Int = 0
    ) {
        stopVibrate()
        startTimer()
        start()
        startVibrate(timings,amplitude,repeat)
    }

    /**
     * 停止震动
     */
    private fun stopVibrate() {
        mVibrator.cancel()
        mCountDownTimer?.cancel()
        mCountDownTimer = null

    }

    private fun startVibrate(timings: LongArray = longArrayOf(0,100),
                             amplitude: IntArray = intArrayOf(255,255),
                             repeat: Int = 0){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val createWaveform = VibrationEffect.createWaveform(timings, amplitude, repeat)
            mVibrator.vibrate(createWaveform)
        } else {
            val pattern = longArrayOf(0, 1000)
            mVibrator.vibrate(pattern, repeat)
        }
    }


    fun doVibrateWork() {
        when (mVibrateState) {
            MODE_RESET, MODE_NONE -> {
                mCurrentMode = MODE_START
            }
            MODE_START, MODE_RESUME -> {
                mCurrentMode = MODE_STOP
            }
            MODE_STOP -> {
                mCurrentMode = MODE_RESUME
            }
        }
    }

    fun changeMode() {
        startVibrate(repeat = -1)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        resetConfig()
    }


    fun setOnVibrateListener(
        start:()->Unit,
        ing: (Int) -> Unit = {},
        end: () -> Unit = {},
        stop: () -> Unit = {},
        reset: () -> Unit = {}
    ) {
        this.start=start
        this.ing = ing
        this.end = end
        this.stop = stop
        this.reset = reset
    }

}