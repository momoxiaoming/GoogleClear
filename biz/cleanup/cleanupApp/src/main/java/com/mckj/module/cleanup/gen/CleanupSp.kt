package com.mckj.module.cleanup.gen

import com.tencent.mmkv.MMKV

/**
 * Cleanup
 * Created by [Spgen-go] on 10:17:45 2021-09-24.
 */
class CleanupSp {

    companion object {
        val instance: CleanupSp by lazy { CleanupSp() }
    }

    private val mmkv: MMKV = MMKV.defaultMMKV()

    private constructor()

    // === Total 6 ===

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
     * 主界面特色菜单配置
     */
    var charaMenuConfig: String
        get() {
            return mmkv.decodeString("chara:menuConfig", "")
        }
        set(value) {
            mmkv.encode("chara:menuConfig", value)
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

    /**
     * 自动扫描时间
     */
    var cleanupHomeAutoScanTime: Long
        get() {
            return mmkv.decodeLong("cleanup:home:autoScanTime", 0L)
        }
        set(value) {
            mmkv.encode("cleanup:home:autoScanTime", value)
        }

    /**
     * 主界面引导框弹出时间
     */
    var cleanupHomeGuidePopTime: Long
        get() {
            return mmkv.decodeLong("cleanup:home:guidePopTime", 0L)
        }
        set(value) {
            mmkv.encode("cleanup:home:guidePopTime", value)
        }

    /**
     * 主界面引导清理是否第一次弹
     */
    var isCleanupHomeGuideFirstPop: Boolean
        get() {
            return mmkv.decodeBool("cleanup:home:guideCleanPopTime", true)
        }
        set(value) {
            mmkv.encode("cleanup:home:guideCleanPopTime", value)
        }

    /**
     * 主界面第一次自动清理
     */
    var firstCleanGuide: Boolean
        get() {
            return mmkv.decodeBool("cleanup:home:first_clean_guide", true)
        }
        set(value) {
            mmkv.encode("cleanup:home:first_clean_guide", value)
        }

    /**
     * 主页推荐跳过点击时间戳
     */
    var skipTimestamp: Long
        get() {
            return mmkv.decodeLong("cleanup:home:recommend_skip", 0L)
        }
        set(value) {
            mmkv.encode("cleanup:home:recommend_skip", value)
        }
}
