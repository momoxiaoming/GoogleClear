package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tencent.mars.xlog.Log
import kotlin.math.roundToInt

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util
 * @data  2022/2/24 18:09
 */
class AudioHelper(val context: Context):DefaultLifecycleObserver{
    /**
    STREAM_VOICE_CALL 通话
    STREAM_SYSTEM 系统
    STREAM_RING 铃声
    STREAM_MUSIC 媒体音量
    STREAM_ALARM 闹钟
    STREAM_NOTIFICATION 通知
     */
    companion object{
        private const val  VOLUME_CHANGE_ACTION = "android.media.VOLUME_CHANGED_ACTION"
        private const val EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE"
    }
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    private val volumeReceiver by lazy { VolumeReceiver() }
   // private val mVolumeBroadCastReceiver by lazy { VolumeBroadCastReceiver() }

    private var volumeChange:(Int)->Unit = {}

    fun getMaxSystemVolume() = maxVolume.toFloat()

    fun getCurrentVolume() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)


    fun setLevelStreamVolume(level:Float=1f){
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume*level).roundToInt(),AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        android.util.Log.d("666", "setLevelStreamVolume: ----$maxVolume--------${maxVolume*level}---------${ (maxVolume*level).roundToInt()}")
    }


    fun setOnVolumeListener(action:(Int)->Unit){
        volumeChange=action
    }


    inner class VolumeReceiver : ContentObserver(Handler(Looper.getMainLooper())){
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            val voiceLevel = (getCurrentVolume() / getMaxSystemVolume()) * 100
            volumeChange(voiceLevel.roundToInt())
            android.util.Log.d("666", "onChange: -$selfChange----$maxVolume----------${getCurrentVolume()}-------$voiceLevel")
        }
    }


//    //定义一个想监听音量变化的广播接受者
//    inner class VolumeBroadCastReceiver : BroadcastReceiver(){
//        override fun onReceive(context: Context?, intent: Intent) {
//            if(intent.action == VOLUME_CHANGE_ACTION && intent.getIntExtra(EXTRA_VOLUME_STREAM_TYPE, -1) == AudioManager.STREAM_MUSIC){
//                val voiceLevel = (getCurrentVolume() / getMaxSystemVolume()) * 100
//                volumeChange(voiceLevel.roundToInt())
//                android.util.Log.d("666", "VolumeBroadCastReceiver: ----$maxVolume-----------${getCurrentVolume()}-------$voiceLevel")
//            }
//        }
//
//    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        context.contentResolver.registerContentObserver(android.provider.Settings.System.CONTENT_URI,true,volumeReceiver)
       // context.registerReceiver(mVolumeBroadCastReceiver, IntentFilter(VOLUME_CHANGE_ACTION))
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        context.contentResolver.unregisterContentObserver(volumeReceiver)
      //  context.unregisterReceiver(mVolumeBroadCastReceiver)
    }



}