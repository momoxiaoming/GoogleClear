package com.mckj.sceneslib.ui.scenes.model.ap

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.app.DatabindingFragment
import com.mckj.sceneslib.databinding.ScenesLayoutHeaderApBinding

/**
 * Ap 头部
 * Created by holmes on 2021/4/23.
 **/
class WifiApLandingHeaderFragment : DatabindingFragment<ScenesLayoutHeaderApBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): WifiApLandingHeaderFragment {
            val args = Bundle()

            val fragment = WifiApLandingHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mApModel by lazy {
        ViewModelProvider(requireActivity()).get(WifiApViewModel::class.java)
    }

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ScenesLayoutHeaderApBinding {
        return ScenesLayoutHeaderApBinding.inflate(inflater, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.title.text = "热点已开启"
        binding.desc.text = "其他设备快来连接使用吧"
        val apInfo = mApModel.sharedAp.value?.data
        if (apInfo != null) {
            binding.apName.text = "热点名称: ${apInfo.ssid}"
            if (apInfo.passwd.isNullOrEmpty()) {
                binding.apPasswd.text = "热点密码: 点击查看WLAN热点"
                binding.apPasswd.setPaintFlags(binding.apPasswd.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                binding.apPasswd.setOnClickListener {
                    showTetherSetting(it.context)
                }
            } else {
                binding.apPasswd.text = "热点密码: ${apInfo.passwd}"
            }
        } else {
            binding.apName.text = "热点名称: --"
            binding.apPasswd.text = "热点密码: --"
        }
        binding.displayApInfo = !(apInfo?.passwd.isNullOrEmpty())
    }

    private fun showTetherSetting(context: Context) {
        // === 一些热点设置的跳转 ===

        val tetherSettings = object : SettingDirect {
            override fun startActivity(context: Context): Boolean {
                val tetherSettings = Intent()
                tetherSettings.setClassName(
                    "com.android.settings",
                    "com.android.settings.TetherSettings"
                )
                try {
                    startActivity(tetherSettings)
                    return true
                } catch (e: Exception) {
                }
                return false
            }
        }

        val internalTetherSettings = object : SettingDirect {
            override fun startActivity(context: Context): Boolean {
                val tetherSettings = Intent()
                tetherSettings.setClassName(
                    "com.android.settings",
                    "com.android.settings.Settings\$TetherSettingsActivity"
                )
                try {
                    startActivity(tetherSettings)
                    return true
                } catch (e: Exception) {
                }
                return false
            }
        }

        val wireSetting = object : SettingDirect {
            override fun startActivity(context: Context): Boolean {
                try {
                    startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    return true
                } catch (e: Exception) {
                }
                return false
            }
        }

        // === $ ===

        // 跳转选择顺序
        val directs = listOf(
            internalTetherSettings,
            tetherSettings,
            wireSetting
        )

        for (direct in directs) {
            if (direct.startActivity(context)) {
                break
            }
        }
    }

    private interface SettingDirect {
        fun startActivity(context: Context): Boolean
    }

}