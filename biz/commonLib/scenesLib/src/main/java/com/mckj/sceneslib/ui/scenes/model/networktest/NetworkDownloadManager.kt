package com.mckj.sceneslib.ui.scenes.model.networktest

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.drakeet.purewriter.ObscureLifecycleEventObserver
import com.drakeet.purewriter.addObserver
import com.drakeet.purewriter.removeObscureObserver
import com.mckj.sceneslib.data.http.HttpApi
import com.mckj.baselib.util.http.HttpUtil
import com.mckj.baselib.util.http.ProgressListener
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
class NetworkDownloadManager(private val lifecycleOwner: LifecycleOwner) :
    ObscureLifecycleEventObserver {

    companion object {
        private const val TAG = "NetworkManager"
    }

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * 是否下载
     */
    private var isDownloadLoop: AtomicBoolean = AtomicBoolean(false)

    private var progressListener: ProgressListener? = null

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
        this.progressListener = listener
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
                progressListener?.update(updateSize, parentDownloadSize, parentTotalSize)
            }

            override fun onSuccess(boolean: Boolean) {
                progressListener?.onSuccess(boolean)
            }

            override fun onFailed(throwable: Throwable) {
                progressListener?.onFailed(throwable)
            }

            override fun onClose() {

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
        mCompositeDisposable.dispose()
        isDownloadLoop.set(false)
        lifecycleOwner.lifecycle.removeObscureObserver(this)
        progressListener?.onClose()
        progressListener = null

    }

    //@OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        close()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if(event == Lifecycle.Event.ON_DESTROY) {
            onDestroy()
        }
    }

}