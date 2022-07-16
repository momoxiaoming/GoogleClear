package com.mckj.api.entity

import android.net.Uri
import java.util.*

/**
 * @author xx
 * @version 1
 * @createTime 2021/12/21 17:34
 * @desc
 */
data class DocumentBean(
    var display: String? = null,

    var lastModify: Long = 0,

    var size: Long = 0,

    var documentId: String? = null,

    var mimeType: String? = null,

    var uri: Uri? = null,

    var parentDocumentIdS: String? = null,
    var emptyFile: Boolean = false
) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val (_, _, _, documentId1, _, _) = o as DocumentBean
        return documentId == documentId1
    }

    override fun hashCode(): Int {
        return Objects.hash(documentId)
    }

    fun isFile(): Boolean {
        return mimeType.equals("application/octet-stream")
    }
}