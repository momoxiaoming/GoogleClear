package com.mckj.gen

import com.tencent.mmkv.MMKV

/**
 * Gallery
 * Created by [Spgen-go] on 18:45:10 2021-12-31.
 */
class GallerySp {

    companion object {
        val instance: GallerySp by lazy { GallerySp() }
    }

    private val mmkv: MMKV = MMKV.defaultMMKV()

    private constructor()

	// === Total 1 ===
	
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
