package com.mckj.sceneslib.util

import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.util
 * @data  2022/4/19 11:47
 */
class CountDownTimer(val lifecycle: LifecycleOwner):DefaultLifecycleObserver {

    companion object{
        private const val INTERVAL_TIME = 1000L
        private const val TOTAL_TIME = 3*1000L
    }
    private var ing:(Int)->Unit={}
    private var end:()->Unit={}
    private var mTimer:CountDownTimer?=null
    private var mCurrentTime = TOTAL_TIME
    private var isStart = false

    init {
        lifecycle.lifecycle.addObserver(this)
    }

    private fun crateTimer(){
        if(mCurrentTime<=0) return
        mTimer= object : CountDownTimer(mCurrentTime, INTERVAL_TIME) {
            override fun onTick(millisUntilFinished: Long) {
                isStart=true
                mCurrentTime=millisUntilFinished
                ing((millisUntilFinished/INTERVAL_TIME).toInt())

            }
            override fun onFinish() {
                mCurrentTime=0
                isStart=false
                end()

            }
        }.start()
    }



    fun setTimerAction(ing:(Int)->Unit,end:()->Unit){
        this.ing = ing
        this.end = end
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (!isStart) {
            crateTimer()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        mTimer?.cancel()
        isStart=false
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        mTimer?.cancel()
    }

    fun stopTimer(){
        mTimer?.cancel()
    }

}