package com.mckj.module.wifi.gen

import com.tencent.mmkv.MMKV

/**
 * Wifi
 * Created by [Spgen-go] on 17:36:12 2021-06-05.
 */
class WifiSp {

    companion object {
        val instance: WifiSp by lazy { WifiSp() }
    }

    private val mmkv: MMKV = MMKV.defaultMMKV()

    private constructor()

	// === Total 5 ===
	
	/**
	 * 扫描时间
	 */
    var homeScanTime: Long
        get() {
            return mmkv.decodeLong("home:scanTime", 0L)
        }
        set(value) {
            mmkv.encode("home:scanTime", value)
		}
	
	/**
	 * 第一次申请权限
	 */
    var homeFirstRequestPermission: Boolean
        get() {
            return mmkv.decodeBool("home:firstRequestPermission", false)
        }
        set(value) {
            mmkv.encode("home:firstRequestPermission", value)
		}
	
	/**
	 * 主界面主菜单配置
	 */
    var homeMenuConfig: String
        get() {
            return mmkv.decodeString("home:menuConfig", "")
        }
        set(value) {
            mmkv.encode("home:menuConfig", value)
		}
	
	/**
	 * 主界面业务菜单配置
	 */
    var businessMenuConfig: String
        get() {
            return mmkv.decodeString("business:menuConfig", "")
        }
        set(value) {
            mmkv.encode("business:menuConfig", value)
		}
	
	/**
	 * 主界面跳转菜单配置
	 */
    var jumpMenuConfig: String
        get() {
            return mmkv.decodeString("jump:menuConfig", "")
        }
        set(value) {
            mmkv.encode("jump:menuConfig", value)
		}
	

	
}
