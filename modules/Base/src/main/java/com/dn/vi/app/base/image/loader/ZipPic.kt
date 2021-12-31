package com.dn.vi.app.base.image.loader

/**
 * zip 资源包里的图片
 *
 * use [from] get an instance
 *
 * Created by holmes on 20-1-5.
 */
class ZipPic private constructor(val filepath: String, val filename: String) {

    companion object {

        @JvmStatic
        fun from(filepath: String, filename: String) = ZipPic(filepath, filename)

    }

    override fun equals(other: Any?): Boolean {
        val oo = other as? ZipPic ?: return false
        return filepath == oo.filepath && filename == oo.filename
    }

    override fun hashCode(): Int {
        return filepath.hashCode() * 31 + filename.hashCode()
    }

    override fun toString(): String {
        return "zp$filepath::$filename" //"ZipPic(filepath='$filepath', filename='$filename')"
    }


}