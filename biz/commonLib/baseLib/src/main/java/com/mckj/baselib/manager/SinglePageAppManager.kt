package com.mckj.baselib.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dn.vi.app.base.app.kt.arouter
import com.drakeet.purewriter.ObscureLifecycleEventObserver
import com.drakeet.purewriter.addObserver
import com.mckj.baselib.R

/**
 * 单页app管理
 *
 * @author mmxm
 * @date 2021/6/9 17:51
 */


class SinglePageAppManager(val activity: FragmentActivity, val containerViewId: Int) :
    ObscureLifecycleEventObserver {

    companion object {
        private const val TAG = "SinglePageAppManager"
        private const val INTENT_ACTION = "intent_single_action"
        private const val INTENT_KEY_PATH = "INTENT_KEY_PATH"

        fun start(activity: FragmentActivity, path: String) {
            LocalBroadcastManager.getInstance(activity).sendBroadcast(Intent().let {
                it.action = INTENT_ACTION
                it.putExtra(INTENT_KEY_PATH, path)
            })
        }

        fun replace(activity: FragmentActivity, path: String,fragment: Fragment) {
            start(activity, path)
            finish(activity, fragment)
        }

        fun finish(activity: FragmentActivity, fragment: Fragment) {
            activity.supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    private val intentFilter by lazy {
        val intentFilter = IntentFilter()
        intentFilter.addAction(INTENT_ACTION)
        intentFilter
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val path = intent?.getStringExtra(INTENT_KEY_PATH) ?: ""
            val fragment = arouter().build(path).navigation() as? Fragment
            if (fragment != null) {
                activity.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_in_anim,R.anim.fragment_in_anim).add(containerViewId, fragment)
                    .addToBackStack(TAG)
                    .commitAllowingStateLoss()
            } else {
                Log.e(TAG, "找不到 fragment path->$path")
            }
        }
    }

    fun bindLifecycle(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    //@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun register() {
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, intentFilter)
    }


    //@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregister() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if(event == Lifecycle.Event.ON_RESUME) {
            register()
        } else if(event == Lifecycle.Event.ON_PAUSE) {
            unregister()
        }
    }
}