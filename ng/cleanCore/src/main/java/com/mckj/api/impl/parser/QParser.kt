package com.mckj.api.impl.parser

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.api.client.JunkConstants
import com.mckj.api.db.entity.JunkDbEntity
import com.mckj.api.entity.AppJunk
import com.org.openlib.help.Consumer2
import com.mckj.api.entity.JunkInfo
import com.mckj.api.manager.junk.JunkHelper
import com.mckj.api.entity.DocumentBean
import com.mckj.api.util.RFileUtils
import io.reactivex.rxjava3.functions.Consumer

/**
 * @author xx
 * @version 1
 * @createTime 2021/12/22 9:33
 * @desc
 */
class QParser : AbsParser() {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun parseJunk(
        map: Map<String, MutableList<JunkDbEntity>>,
        consumer: Consumer<AppJunk>,
        type: Int
    ) {
        if (mStopFlag.get()) {
            mStopFlag.set(false)
            return
        }
        mOptEnable.set(true)
        val startTime = System.currentTimeMillis()
        for ((key, value) in map) {
            if (!mOptEnable.get()) {
                return
            }
            val details = mutableListOf<JunkInfo>()
            var name = ""
            var totalSize = 0L
            var scanTime = System.currentTimeMillis()
            scanAndroidData(AppMod.app, value, key) { pkg, bean ->
                details.add(bean)
                totalSize += bean.junkSize
            }
            val icon = AppUtil.getAppIcon(AppMod.app, key)
            val junks = mutableListOf<JunkInfo>()
            junks.addAll(details.distinctBy {
                it.uri
            })
//            Log.d(
//                TAG,
//                "结束扫描：$key----耗时：${System.currentTimeMillis() - scanTime}----扫描的文件大小：$totalSize"
//            )
            consumer.accept(
                AppJunk(
                    type,
                    appName = name,
                    packageName = key,
                    icon = icon,
                    junkSize = totalSize,
                    junks = junks
                )
            )
        }
    }

    private fun scanAndroidData(
        context: Context,
        list: MutableList<JunkDbEntity>,
        pkg: String,
        consume: Consumer2<String, JunkInfo>
    ) {
        if (!RFileUtils.canAccess(context)) {
            return
        }
        try {
            val uri = RFileUtils.getUriTree()
            val root = DocumentFile.fromTreeUri(context, uri)
            root?.uri?.let {
                listFiles(context, it, pkg, list, consume)
            }
        } catch (e: Exception) {

        }
    }

    private fun listFiles(
        context: Context,
        uri: Uri,
        pkg: String,
        list: MutableList<JunkDbEntity>,
        consume: Consumer2<String, JunkInfo>
    ) {
        DocumentFile.fromTreeUri(context, uri)?.apply {
            listFiles().forEach {
                if (!mOptEnable.get()) {
                    return
                }
                if (it.isDirectory) {
                    val documentId = DocumentsContract.getDocumentId(it.uri)
                    if (isPackage(documentId, pkg)) {
                        listPkg(context, it.uri, pkg, list, consume)
                    }
                }
            }
        }
    }

    private fun listPkg(
        context: Context,
        uri: Uri,
        pkg: String,
        list: MutableList<JunkDbEntity>,
        consume: Consumer2<String, JunkInfo>
    ) {
        try {
            DocumentFile.fromTreeUri(context, uri)?.apply {
                listFiles().forEach {
                    if (!mOptEnable.get()) {
                        return
                    }
                    val documentId = DocumentsContract.getDocumentId(it.uri)
                    find(documentId, list)?.let { junkDbEntity ->
                        if (documentId == changeRPath(JunkHelper.getUsePath(junkDbEntity))) {

//                            if (it.name == "cache") {
//                                Log.d(TAG, "匹配到cache:${junkDbEntity.filePath}--直接计算大小")
//                                buildCache(context, documentId, uri, pkg, junkDbEntity, consume)
//                            } else {
                                buildScanResult(
                                    context,
                                    documentId,
                                    it.uri,
                                    pkg,
                                    junkDbEntity,
                                    consume
                                )
//                            }

                        } else {
                            listPkg(context, it.uri, pkg, list, consume)
                        }
                    }
                }
            }
        } catch (e: Exception) {

        }
    }

    private fun buildCache(
        context: Context,
        documentId: String,
        uri: Uri,
        pkg: String,
        junkDbEntity: JunkDbEntity,
        consume: Consumer2<String, JunkInfo>
    ) {
        var total = 0L
        var start = System.currentTimeMillis()
        recursionCache(context, uri) {
            total += it
        }
        val junkInfo = getJunkDetailEntity(
            junkDbEntity.junkType ?: 0,
            junkDbEntity.fileType ?: 0,
            pkg,
            junkDbEntity.desc,
            "cache",
            JunkHelper.getUsePath(junkDbEntity) ?: "",
            total,
            uri
        )
        consume.accept(pkg, junkInfo)
    }

    private fun recursionCache(
        context: Context,
        uri: Uri,
        consume: Consumer<Long>
    ) {
        DocumentFile.fromTreeUri(context, uri)?.apply {
            val files = listFiles()
            if (files.isNullOrEmpty()) {
                return
            }
            files.forEach {
                if (it.isDirectory) {
                    val documentId = DocumentsContract.getDocumentId(it.uri)
                    recursionCache(context, it.uri, consume)
                } else {
                    consume.accept(it.length())
                }
            }
        }
    }

    private fun buildScanResult(
        context: Context,
        documentId: String,
        uri: Uri,
        pkg: String,
        junkDbEntity: JunkDbEntity,
        consume: Consumer2<String, JunkInfo>
    ) {
        val startTime = System.currentTimeMillis()
        DocumentFile.fromTreeUri(context, uri)?.let {
            recursionFile(context, documentId, uri, pkg, junkDbEntity, consume)
        }
    }

    private fun find(documentId: String, list: MutableList<JunkDbEntity>): JunkDbEntity? {
        list.forEach {
            val path = changeRPath(JunkHelper.getUsePath(it))
            if (path.contains(documentId)) {
                return it
            }
        }
        return null
    }

    private fun changeRPath(path: String?): String {
        return "primary:$path"
    }

    private fun isParent(documentId: String, list: MutableList<JunkDbEntity>): Boolean {
        list.forEach {
            val path = "primary:${JunkHelper.getUsePath(it)}"
            if (path.contains(documentId)) {
                return true
            }
        }
        return false
    }

    private fun recursionFile(
        context: Context,
        documentId: String,
        uri: Uri,
        pkg: String,
        junkDbEntity: JunkDbEntity,
        consume: Consumer2<String, JunkInfo>
    ) {
        if (!mOptEnable.get()) {
            return
        }
        DocumentFile.fromTreeUri(context, uri)?.apply {
            val files = listFiles()
            if (isDirectory && files.isNullOrEmpty()) {
                //empty file dir
                val junkInfo = getJunkDetailEntity(
                    JunkConstants.JunkFileType.EMPTY_DIR,
                    JunkConstants.JunkFileType.EMPTY_DIR,
                    pkg,
                    junkDbEntity.desc,
                    name,
                    JunkHelper.getUsePath(junkDbEntity) ?: "",
                    length(),
                    uri
                )
                consume.accept(pkg, junkInfo)
            }
            if (filter(files)) {
                return
            }
            files.forEach {
                val documentId = DocumentsContract.getDocumentId(it.uri)
                if (it.isFile) {
                    val junkInfo = getJunkDetailEntity(
                        junkDbEntity.junkType ?: 0,
                        junkDbEntity.fileType ?: 0,
                        pkg,
                        junkDbEntity.desc,
                        it.name,
                        JunkHelper.getUsePath(junkDbEntity) ?: "",
                        it.length(),
                        it.uri
                    )
                    consume.accept(pkg, junkInfo)
                } else {
                    recursionFile(context, documentId, it.uri, pkg, junkDbEntity, consume)
                }
            }
        }
    }

    private fun getLength(context: Context, uri: Uri, consume: Consumer<Long>) {
        DocumentFile.fromTreeUri(context, uri)?.apply {
            val files = listFiles()
            if (filter(files)) {
                return
            }
            files.forEach {
                if (it.isFile) {
                    consume.accept(it.length())
                } else {
                    getLength(context, it.uri, consume)
                }
            }
        }
    }

    private fun filter(fileList: Array<DocumentFile>): Boolean {
        var dirCount = 0
        for (bean in fileList) {
            if (bean.isDirectory) dirCount++
        }
        return dirCount > 20
    }


    /**
     * 扫描包名下的指定文件
     */

    private fun getRootPath(): String {
        return "primary:Android/data"
    }

    private fun isPackage(
        documentId: String,
        pkg: String
    ): Boolean {
        val key = "${getRootPath()}/$pkg"
        if (documentId == key) {
            return true
        }
        return false
    }

    private fun findDocumentFiles(
        list: MutableList<DocumentBean>,
        documentId: String
    ): MutableList<DocumentBean> {
        val documents = mutableListOf<DocumentBean>()
        for (bean in list) {
            if (bean.documentId!!.contains(documentId)) {
                documents.add(bean)
            }
        }
        return documents
    }
}