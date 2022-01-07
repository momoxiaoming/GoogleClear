package com.mckj.module.cleanup.gen

import com.tencent.mmkv.MMKV

/**
 * Cleanup
 * Created by [Spgen-go] on 09:40:52 2021-07-29.
 */
class CleanupSp {

    companion object {
        val instance: CleanupSp by lazy { CleanupSp() }
    }

    private val mmkv: MMKV = MMKV.defaultMMKV()

    private constructor()

	// === Total 5 ===
	
	/**
	 * 主界面主菜单配置
	 */
    var cleanupHomeMenuConfig: String
        get() {
            return mmkv.decodeString("cleanup:home:menuConfig", "")
        }
        set(value) {
            mmkv.encode("cleanup:home:menuConfig", value)
		}
	
	/**
	 * 主界面业务菜单配置
	 */
    var cleanupBusinessMenuConfig: String
        get() {
            return mmkv.decodeString("cleanup:business:menuConfig", "")
        }
        set(value) {
            mmkv.encode("cleanup:business:menuConfig", value)
		}
	
	/**
	 * 主界面跳转菜单配置
	 */
    var cleanupJumpMenuConfig: String
        get() {
            return mmkv.decodeString("cleanup:jump:menuConfig", "")
        }
        set(value) {
            mmkv.encode("cleanup:jump:menuConfig", value)
		}
	
	/**
	 * 扫描时间
	 */
    var cleanupHomeScanTime: Long
        get() {
            return mmkv.decodeLong("cleanup:home:scanTime", 0L)
        }
        set(value) {
            mmkv.encode("cleanup:home:scanTime", value)
		}
	
	/**
	 * 图库引导进度
	 */
    var galleryGuideProgress: Int
        get() {
            return mmkv.decodeInt("galleryGuideProgress", 0)
        }
        set(value) {
            mmkv.encode("galleryGuideProgress", value)
		}
	

	
}
