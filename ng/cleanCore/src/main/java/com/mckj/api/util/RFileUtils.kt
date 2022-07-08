package com.mckj.api.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.base.app.AppMod

/**
 * @author leix
 * @version 1
 * @createTime 2021/12/16 9:34
 * @desc
 */
object RFileUtils {

    const val REQUEST_CODE = 200

    fun isAbove30(): Boolean {
        return Build.VERSION.SDK_INT >= 30
    }
    var requestBlock: ((cons: Boolean) -> Unit)? = null

    fun isGrantAndroidRAndroidDataAccessPermission(context: Context): Boolean {
        return if (!isAbove30()) {
            true
        } else try {
            for (uriPermission in context.contentResolver.persistedUriPermissions) {
                if (uriPermission.isReadPermission && uriPermission.uri.toString() == "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata") {
                    return true
                }
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun checkExStoragePermission(context: Context, from:String, block: (accept: Boolean) -> Unit) {
        if (isGrantAndroidRAndroidDataAccessPermission(context)) {
            block.invoke(true)
        } else {
            requestBlock = {
                block.invoke(it)
            }
            requestPermission(from)
        }
    }

    fun requestPermission(from:String) {
        ARouter.getInstance().build("/rp/main").withString("from", from).navigation()
    }

    fun changeToUri2(str: String): String? {
        val split =
            str.replace("/storage/emulated/0/Android/data".toRegex(), "").split("/").toTypedArray()
        val sb =
            StringBuilder("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata")
        for (str2 in split) {
            if (str2.isNotEmpty()) {
                sb.append("%2F")
                sb.append(str2)
            }
        }
        return sb.toString()
    }


    @SuppressLint("WrongConstant")
    fun startForRoot(activity: Activity) {
        val fromTreeUri: DocumentFile? = DocumentFile.fromTreeUri(
            activity,
            Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata")
        )
        val intent = Intent("android.intent.action.OPEN_DOCUMENT_TREE")
        intent.flags = 195
        intent.putExtra("android.provider.extra.INITIAL_URI", fromTreeUri?.uri)
        activity.startActivityForResult(intent, REQUEST_CODE)

    }

    fun canAccess(context: Context): Boolean {
        val sp: SharedPreferences =
            context.getSharedPreferences("DirPermission", Context.MODE_PRIVATE)
        val uriTree = sp.getString("uriTree", "")
        return !uriTree.isNullOrEmpty()
    }

    fun getUriTree(): Uri {
        val sp: SharedPreferences =
            AppMod.app.getSharedPreferences("DirPermission", Context.MODE_PRIVATE)
        val uriTree = sp.getString("uriTree", "")
        return Uri.parse(uriTree)
    }

    fun deleteFile(uri: Uri): Boolean {
        return try {
            DocumentsContract.deleteDocument(AppMod.app.contentResolver, uri)
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }

    @SuppressLint("WrongConstant")
    fun takePersistableUriPermission(context: Context?, intent: Intent?, accept: Boolean) {
        var data: Uri? = null
        requestBlock?.invoke(accept)
        if (intent != null && context != null && intent.data.also { data = it } != null) {
            data?.let {
                val sp: SharedPreferences =
                    context.getSharedPreferences("DirPermission", Context.MODE_PRIVATE)
                // 重启才会生效，所以可以清除uriTree
                val editor = sp.edit()
                editor.putString("uriTree", it.toString())
                editor.apply()
                context.contentResolver.takePersistableUriPermission(it, intent.flags and 3)
            }
        }
    }
}