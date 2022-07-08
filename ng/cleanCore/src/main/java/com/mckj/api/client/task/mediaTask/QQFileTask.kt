package com.mckj.api.client.task.mediaTask

import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.dn.vi.app.base.app.AppMod
import com.mckj.api.client.JunkConstants
import com.mckj.api.client.task.base.BaseTask
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkDescription
import com.mckj.api.entity.JunkInfo
import com.mckj.api.entity.MediaEntity
import com.mckj.api.util.FileUtils
import com.mckj.api.util.RFileUtils
import io.reactivex.rxjava3.functions.Consumer
import java.io.File
import java.lang.Exception

/**
 * @author leix
 * @version 1
 * @createTime 2021/10/28 17:39
 * @desc
 */
class QQFileTask : BaseTask() {
    override fun getName(): String {
        return "QQ文件缓存"
    }

    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        val documentJunk = AppJunk(
            type = JunkConstants.AppCacheType.DOCUMENT_CACHE,
            appName = "QQ",
            packageName = "com.tencent.mobileqq",
            icon = null,
            junkDescription = JunkDescription(title = "QQ文档缓存", description = "QQ文档缓存")
        )
        try {
            val documents = mutableListOf<MediaEntity>()
            if (RFileUtils.isAbove30()) {
                documents.addAll(getDocumentsOnQ())
            } else {
                getDocuments()?.let {
                    documents.addAll(it)
                }
            }
            val list = mutableListOf<JunkInfo>()
            var totalSize = 0L
            documents?.forEach {
                list.add(
                    JunkInfo(
                        junkType = JunkConstants.JunkType.OTHER,
                        fileType = JunkConstants.JunkFileType.DOCUMENT,
                        name = it.name,
                        description = it.desc,
                        createTime = it.lastModify,
                        junkSize = it.length,
                        path = it.path
                    )
                )
                totalSize += it.length
            }
            documentJunk.junkSize = totalSize
            documentJunk.junks = list
        } catch (e: Exception) {
            return false
        }
        consumer.accept(documentJunk)
        return true
    }

    override fun clean(appjunk: AppJunk): Boolean {
        return true
    }


    private fun getDocuments(): List<MediaEntity>? {
        val documents = mutableListOf<MediaEntity>()
        val root = FileUtils.getSDPath()
        val downLoadDir = "$root/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv"
        val downLoadFile = File(downLoadDir)
        if (!downLoadFile.exists()) return null
        downLoadFile.listFiles()?.forEach {
            val suffix: String = it.name.substring(it.name.lastIndexOf(".") + 1)
            documents.add(
                MediaEntity(
                    it.lastModified(),
                    "$downLoadDir/${it.name}",
                    it.length(),
                    desc = suffix,
                    name = it.name
                )
            )
        }
        return documents
    }

    private fun getDocumentsOnQ(): List<MediaEntity> {
        val list = mutableListOf<MediaEntity>()
        val uri = RFileUtils.getUriTree()
        DocumentFile.fromTreeUri(AppMod.app, uri)?.uri?.let {
            DocumentFile.fromTreeUri(AppMod.app, it)?.apply {
                listFiles().forEach { documentFile ->
                    if (documentFile.isDirectory) {
                        val documentId = DocumentsContract.getDocumentId(documentFile.uri)
                        if (documentId == "${getRootPath()}/com.tencent.mobileqq") {
                            listQQ(documentFile.uri) { entity ->
                                list.add(entity)
                            }
                        }
                    }
                }
            }
        }
        return list
    }

    private fun listQQ(uri: Uri, consume: Consumer<MediaEntity>) {
        DocumentFile.fromTreeUri(AppMod.app, uri)?.apply {
            listFiles().forEach {
                if (it.isDirectory) {
                    val documentId = DocumentsContract.getDocumentId(it.uri)
                    if (documentId == "${getRootPath()}/com.tencent.mobileqq/Tencent/QQfile_recv") {
                        DocumentFile.fromTreeUri(AppMod.app, it.uri)?.apply {
                            listFiles().forEach { child ->
                                if (child.isFile) {
                                    val suffix: String =
                                        child.name?.substring(it.name!!.lastIndexOf(".") + 1) ?: ""
                                    consume.accept(
                                        MediaEntity(
                                            child.lastModified(),
                                            "",
                                            child.length(),
                                            desc = suffix,
                                            name = child.name!!,
                                            uri = child.uri
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        listQQ(it.uri, consume)
                    }
                }
            }
        }
    }

    override fun stop() {

    }
    private fun getRootPath(): String {
        return "primary:Android/data"
    }
}