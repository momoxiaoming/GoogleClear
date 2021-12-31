package com.mckj.module.wifi.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.mckj.module.wifi.R
import com.mckj.module.wifi.entity.FloatEntity
import com.mckj.module.wifi.manager.FloatDataManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import org.jetbrains.anko.textColorResource
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Describe:悬浮倒计时
 *
 * Created By yangb on 2021/1/25
 */
class FloatCountdownView(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {

    companion object {
        const val TAG = "FloatCountdownView"
    }

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet? = null) : this(
        context,
        attributeSet,
        0
    )

    private var mIndex = 0
    private var mType = FloatEntity.TYPE_GOLD
    private var mDefaultText: String = "领取奖励"
    private var mCountdownDisposable: Disposable? = null

    init {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.FloatCountdownView)
        mDefaultText =
            typedArray.getString(R.styleable.FloatCountdownView_floatDefaultText) ?: mDefaultText
        typedArray.recycle()
    }

    fun setData(index: Int, type: Int) {
        mIndex = index
        mType = type
    }

    fun setDefaultText(defaultText: String) {
        mDefaultText = defaultText
    }

    fun isCountdownFinish(): Boolean {
        return mCountdownDisposable == null
    }

    /**
     * 开始倒计时
     *
     * @param countdown 倒计时秒
     */
    fun start(countdown: Long) {
        if (countdown >= FloatDataManager.MAX_COUNT_DOWN) {
            setCountdownText(0)
            return
        }
        if (countdown <= 0) {
            setCountdownText(countdown)
            return
        }
        setCountdownText(countdown)
        textColorResource = R.color.red
        mCountdownDisposable = Flowable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setCountdownText(countdown - it)
            }
    }

    private fun setCountdownText(countdown: Long) {
        text = if (countdown <= 0) {
            close()
            textColorResource = R.color.white
            mDefaultText
        } else {
            val minutes = countdown / 60
            val seconds = countdown % 60
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }

    fun close() {
        mCountdownDisposable?.dispose()
        mCountdownDisposable = null
    }

}