package com.mckj.module.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.mckj.api.util.FileUtils
import com.dn.vi.app.base.app.AppMod
import com.mckj.baselib.view.ToastUtil
import java.io.File

/**
 * @author xx
 * @version 1
 * @createTime 2021/9/16 14:30
 * @desc
 */
object OpenFileUtil {
    fun findFile(filePath: String): Intent? {
        val androidIndex = filePath.indexOf("Android")
        var dirPath = filePath.substring(androidIndex, filePath.length)
        dirPath = dirPath.replace("/", "%2f")
        val uri =
            Uri.parse("content://com.android.externalstorage.documents/document/primary:$dirPath")
        val file = File(filePath)
        if (!file.exists()) return null
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
        intent.type = "*/*"
        return intent
    }

    fun openFile(filePath: String): Intent? {
        val file = File(filePath)
        if (!file.exists()) return null
//        return getAllIntent(filePath)
        val suffix: String = filePath.substring(filePath.lastIndexOf(".") + 1)
        return when {
            FileUtils.isWord(suffix) -> getWordFileIntent(filePath)
            FileUtils.isPdf(suffix) -> getPdfFileIntent(filePath)
            FileUtils.isExcel(suffix) -> getExcelFileIntent(filePath)
            FileUtils.isPPT(suffix) -> getPptFileIntent(filePath)
            FileUtils.isTxt(suffix) -> getTextFileIntent(filePath, false)
            else -> getAllIntent(filePath)
        }
    }


    // Android获取一个用于打开APK文件的intent
    private fun getAllIntent(param: String?): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "application/*")
        return intent
    }

    // Android获取一个用于打开APK文件的intent
    private fun getApkFileIntent(param: String?): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        return intent
    }

    // Android获取一个用于打开VIDEO文件的intent
    private fun getVideoFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "video/*")
        return intent
    }

    // Android获取一个用于打开AUDIO文件的intent
    private fun getAudioFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "audio/*")
        return intent
    }

    // Android获取一个用于打开Html文件的intent
    private fun getHtmlFileIntent(param: String?): Intent {
        val uri: Uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider")
            .scheme("content").encodedPath(param).build()
        val intent = Intent("android.intent.action.VIEW")
        intent.setDataAndType(uri, "text/html")
        return intent
    }

    // Android获取一个用于打开图片文件的intent
    private fun getImageFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "image/*")
        return intent
    }

    // Android获取一个用于打开PPT文件的intent
    private fun getPptFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        return intent
    }

    // Android获取一个用于打开Excel文件的intent
    private fun getExcelFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        return intent
    }

    // Android获取一个用于打开Word文件的intent
    private fun getWordFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "application/msword")
        return intent
    }

    // Android获取一个用于打开CHM文件的intent
    private fun getChmFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "application/x-chm")
        return intent
    }

    // Android获取一个用于打开文本文件的intent
    private fun getTextFileIntent(param: String?, paramBoolean: Boolean): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        if (paramBoolean) {
            val uri1: Uri = getUri(File(param))
            intent.setDataAndType(uri1, "text/plain")
        } else {
            val uri2: Uri = getUri(File(param))
            intent.setDataAndType(uri2, "text/plain")
        }
        return intent
    }

    // Android获取一个用于打开PDF文件的intent
    private fun getPdfFileIntent(param: String?): Intent {
        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        val uri: Uri = getUri(File(param))
        intent.setDataAndType(uri, "application/pdf")
        return intent
    }

    private fun getUri(file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            FileProvider.getUriForFile(
                AppMod.app, "com.mckj.moduleclean.fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }

    fun openFile(filePath: String, c: Context) {
        val extension: String = getExtension(filePath)
        val webkitMimeType =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1))
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri = getUri(File(filePath))
        val type: String = webkitMimeType ?: ""
        if (webkitMimeType.isNullOrEmpty()) {
            ToastUtil.postShow("暂不支持打开该文件")
            return
        }
        if ("*/*" == type) {
            intent.data = data
            intent.putExtra("org.openintents.extra.FROM_OI_FILEMANAGER", true)
        } else {
            intent.setDataAndType(data, type)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        try {
            val activities = c.packageManager.queryIntentActivities(intent, 0)
            if (activities.isEmpty() || activities.size == 1 && c.applicationInfo.packageName == activities[0].activityInfo.packageName) {
                ToastUtil.postShow("暂不支持打开该文件")
                return
            } else {
                c.startActivity(intent)
            }
        } catch (e: ActivityNotFoundException) {
            ToastUtil.postShow("暂不支持打开该文件")
        } catch (e: SecurityException) {
            ToastUtil.postShow("暂不支持打开该文件")
        } catch (e: RuntimeException) {
            ToastUtil.postShow("暂不支持打开该文件")
        }
    }

    private fun getExtension(uri: String): String {
        val dot = uri.lastIndexOf(".")
        return if (dot >= 0) {
            uri.substring(dot)
        } else {
            // No extension.
            ""
        }
    }

    fun shareFile(context: Context, filePath: String) {
        val file = File(filePath)
        if (!file.exists()) return
        val share = Intent(Intent.ACTION_SEND)
        share.putExtra(
            Intent.EXTRA_STREAM,
            getUri(file)
        )
        share.type = "*/*" //此处可发送多种文件
        context.startActivity(Intent.createChooser(share, "分享"))
    }

    fun shareFile(context: Context,uri: Uri){
        val share = Intent(Intent.ACTION_SEND)
        share.putExtra(
            Intent.EXTRA_STREAM,
            uri
        )
        share.type = "*/*" //此处可发送多种文件
        context.startActivity(Intent.createChooser(share, "分享"))
    }

    fun shareFile(context: Activity, fileList: List<String>) {
        val shareList = ArrayList<Uri>()
        fileList.forEach {
            val file = File(it)
            if (file.exists()) {
                shareList.add(getUri(file))
            }
        }
        val share = Intent(Intent.ACTION_SEND_MULTIPLE)
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, shareList)
        share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        share.type = "*/*" //此处可发送多种文件
        context.startActivity(Intent.createChooser(share, "分享"))
    }
}