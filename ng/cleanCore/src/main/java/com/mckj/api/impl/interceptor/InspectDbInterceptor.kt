package com.mckj.api.impl.interceptor

import android.util.Log
import com.mckj.api.data.DealData
import com.mckj.api.http.response.VersionInfo
import com.mckj.api.util.DbUtil
import com.mckj.api.db.JunkDatabase
import com.mckj.api.manager.RetrofitManage
import com.mckj.api.util.FileUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.*

/**
 * @author xx
 * @version 1
 * @createTime 2021/8/7 11:22
 * @desc 检查db是否需要更新
 */
class InspectDbInterceptor : LInterceptor {

    companion object {
        const val TAG = "DbMonitor"
        private const val MAX_RETRY_TIME = 5//下载失败的最大重视次数
    }

    private var mRetryCount: Int = 0
    private lateinit var mDealData: DealData
    private lateinit var mChain: LInterceptor.RealChain
    override fun intercept(chain: LInterceptor.RealChain) {
        this.mDealData = chain.mDealData
        this.mChain = chain
        if (!mDealData.isFetchDataSuccess || mDealData.responseFromRemote == null) {
            proceed()
            return
        }
        mDealData.responseFromRemote?.let {
            inspectDb(it)
        }
    }

    /**
     * @return 本地db文件是否存在
     */
    private fun nativeDbExists(): Boolean {
        val dbPath = DbUtil.getDbPath()//db保存的本地地址
        val file = File(dbPath)
        if (!file.exists() || !file.isFile || !file.canRead()) {
            return false
        }
        return true
    }

    fun inspectDb(dbVersionInfo: VersionInfo) {
        if (!nativeDbExists()) {
            downloadDb()
            return
        }
        dbVersionInfo.apply {
            val remoteVersion = this.version
            val nativeDbVersion = JunkDatabase.getInstance().junkVersionDao()
                    .getJunkVersionByName("cleanDB")?.version_code
                    ?: 0
            Log.d(TAG , "nativeDbVersion：-->$nativeDbVersion----remoteVersion:-->$remoteVersion")
            if (nativeDbVersion < remoteVersion) {
                downloadDb()
            } else {
                Log.d(TAG , "should not update")
                proceed()
            }
        }
    }


    /**
     * 下载数据库
     */
    private fun downloadDb() {
        val url = mDealData.responseFromRemote?.url
        val downLoadPath = DbUtil.getDbDownLoadPath()
        if (url.isNullOrEmpty() || mRetryCount > MAX_RETRY_TIME) {
            Log.d(TAG , "downloadDb error:retry too much: $mRetryCount")
            proceed()
            return
        }
        val request: Request = Request.Builder()
            .url(url)
            .build()
        RetrofitManage.instance.getClient()
            ?.newCall(request)
            ?.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    retry(downLoadPath)
                }

                override fun onResponse(call: Call, response: Response) {
                    var inputStream: InputStream? = null
                    val buf = ByteArray(2048)
                    var len: Int
                    var fos: FileOutputStream? = null
                    try {
                        inputStream = response.body?.byteStream()
                        val file = File(downLoadPath)
                        fos = FileOutputStream(file)
                        var sum: Long = 0
                        while (inputStream!!.read(buf).also { len = it } != -1) {
                            fos.write(buf, 0, len)
                            sum += len.toLong()
                        }
                        fos.flush()
                    } catch (e: Exception) {
                        retry(downLoadPath)
                        e.printStackTrace()
                    } finally {
                        Log.d(TAG ,  "downLoadSuccess:$url")
                        mRetryCount++
                        inputStream?.close()
                        fos?.close()
                        verifyFile()
                    }
                }
            })
    }

    /**
     * 校验文件
     */
    private fun verifyFile() {
        val downLoadPath = DbUtil.getDbDownLoadPath()
        val md5 = FileUtils.getFileMD5(downLoadPath)
        val remoteMd5 = mDealData.responseFromRemote?.MD5
        if (remoteMd5.isNullOrEmpty() || md5 != remoteMd5) {
            Log.d(TAG, "文件校验失败:服务器md5:${remoteMd5}------本地文件md5:$md5")
            retry(downLoadPath)
            return
        }
        FileUtils.copyFile(downLoadPath, DbUtil.getDbPath())
        clearDownLoadFile(downLoadPath)//删除下载的临时文件
        proceed()
    }

    private fun retry(path: String) {
        mRetryCount++
        clearDownLoadFile(path)
        downloadDb()
    }

    private fun clearDownLoadFile(path: String) {
        FileUtils.delete(path)
    }

    private fun proceed() {
        mDealData.prepareLoadDb = true
        mChain.proceed(mDealData)
    }
}