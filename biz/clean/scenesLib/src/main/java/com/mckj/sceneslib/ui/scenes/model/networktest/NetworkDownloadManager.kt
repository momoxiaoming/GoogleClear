package com.mckj.sceneslib.ui.scenes.model.networktest

import android.os.FileUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.dn.baselib.http.HttpUtil
import com.mckj.sceneslib.data.http.HttpApi
import com.dn.baselib.http.ProgressListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Describe:
 *
 * Created By yangb on 2021/3/12
 */
class NetworkDownloadManager(private val lifecycleOwner: LifecycleOwner) : LifecycleObserver {

    companion object {
        private const val TAG = "NetworkManager"
    }

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * 是否下载
     */
    private var isDownloadLoop: AtomicBoolean = AtomicBoolean(false)

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    /**
     * 循环下载文件
     */
    fun downloadFileLoop(
        url: String,
        listener: ProgressListener,
    ) {
        isDownloadLoop.set(true)
        var parentDownloadSize = 0L
        var parentTotalSize = 0L
        val childListener = object : ProgressListener {
            override fun update(updateSize: Long, downloadSize: Long, totalSize: Long) {
                parentDownloadSize += updateSize
                if(updateSize == downloadSize){
                    //第一次下载
                    parentTotalSize += totalSize
                }
                listener.update(updateSize, parentDownloadSize, parentTotalSize)
            }

            override fun onSuccess(boolean: Boolean) {
                listener.onSuccess(boolean)
            }

            override fun onFailed(throwable: Throwable) {
                listener.onFailed(throwable)
            }
        }
        _downloadFileLoop(url, childListener)
    }

    private fun _downloadFileLoop(
        url: String,
        childListener: ProgressListener,
    ) {
        downloadFile(url, childListener, object : Observer<ResponseBody> {
            override fun onSubscribe(d: Disposable) {
                mCompositeDisposable.add(d)
            }

            override fun onNext(t: ResponseBody) {
            }

            override fun onError(e: Throwable) {
                childListener.onFailed(e)
            }

            override fun onComplete() {
                if (isDownloadLoop.get()) {
                    _downloadFileLoop(url, childListener)
                } else {
                    childListener.onSuccess(true)
                }
            }
        })
    }

    /**
     * 下载文件
     */
    fun downloadFile(
        url: String,
        listener: ProgressListener,
        observer: Observer<ResponseBody>,
    ) {
        HttpUtil.getProcessApi(HttpApi::class.java, listener)
            .downloadFile(url)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    fun close() {
        lifecycleOwner.lifecycle.removeObserver(this)
        isDownloadLoop.set(false)
        mCompositeDisposable.dispose()

    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        close()
    }

}