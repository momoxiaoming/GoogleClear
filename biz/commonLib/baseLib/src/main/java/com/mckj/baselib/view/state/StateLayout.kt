package com.mckj.baselib.view.state

import android.annotation.SuppressLint
import android.content.Context
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.R


/**
 *  缺省图状态组件
 *
 * @author mmxm
 * @date 2021/6/28 15:47
 */
@SuppressLint("Recycle")


enum class StateStatus {
    DEFAULT,
    EMPTY,
    ERROR,
    LOADING
}


@SuppressLint("Recycle")
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleRes) {

    companion object {
        const val TAG = "StateLayout"
    }

    private var contentId: Int = -2

    /**
     * 存储所有缺省视图
     */
    private val contentViews = ArrayMap<Int, View>()

    /**
     * 当前状态
     */
    private var status: StateStatus = StateStatus.DEFAULT

    private var onErrorBlock: (View.(data: Any?) -> Unit)? = null
    private var onEmptyBlock: (View.(data: Any?) -> Unit)? = null
    private var onLoadingBlock: (View.(data: Any?) -> Unit)? = null
    private var onRefreshBlock: (StateLayout.(data: Any?) -> Unit)? = null

    /**
     * [showLoading]时是否回调[onRefreshBlock]
     */
    private var refresh: Boolean = true

    /**
     * 错误视图可重试id集合
     */
    var errorRetryIds: IntArray = intArrayOf()

    /**
     * 空视图可重试id集合
     */
    var emptyRetryIds: IntArray = intArrayOf()


    /**
     * 自动启用视图点击重试功能
     */
    private var autoRetryLoading: Boolean = true

    @LayoutRes
    var emptyLayout: Int = View.NO_ID
        set(value) {
            if (field != value) {
                removeView(field)
                field = value
            }
        }

    @LayoutRes
    var errorLayout: Int = View.NO_ID
        set(value) {
            if (field != value) {
                removeView(field)
                field = value
            }
        }

    @LayoutRes
    var loadingLayout: Int = View.NO_ID
        set(value) {
            if (field != value) {
                removeView(field)
                field = value
            }
        }


    init {
        context.obtainStyledAttributes(attrs, R.styleable.StateLayout).let {
            emptyLayout = it.getResourceId(R.styleable.StateLayout_empty_layout, View.NO_ID)
            errorLayout = it.getResourceId(R.styleable.StateLayout_error_layout, View.NO_ID)
            loadingLayout = it.getResourceId(R.styleable.StateLayout_loading_layout, View.NO_ID)
            it.recycle()
        }
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1 || childCount == 0) {
            throw UnsupportedOperationException("StateLayout只能包含一个子视图")
        }
        setContent(getChildAt(0))
    }

    /**
     * 设置刷新回调
     * @param block Function0<Unit>
     * @return StateLayout
     */
    fun onRefresh(block: StateLayout.(data: Any?) -> Unit): StateLayout {
        this.onRefreshBlock = block
        return this
    }

    /**
     * 空回调
     * @param block Function0<Unit>
     * @return StateLayout
     */
    fun onEmpty(block: View.(data: Any?) -> Unit): StateLayout {
        onEmptyBlock = block
        return this
    }

    /**
     * 加载中回调
     * @param block Function0<Unit>
     * @return StateLayout
     */
    fun onLoading(block: View.(data: Any?) -> Unit): StateLayout {
        onLoadingBlock = block
        return this
    }

    /**
     * 错误回调
     * @param block Function0<Unit>
     * @return StateLayout
     */
    fun onError(block: View.(data: Any?) -> Unit): StateLayout {
        onErrorBlock = block
        return this
    }

    fun showLoading(data: Any? = null, refresh: Boolean = true): StateLayout {
        if (status == StateStatus.LOADING) {
            return this
        }
        if (loadingLayout == NO_ID) {
            //使用全局参数
            loadingLayout = StateConfig.loadingLayout
        }
        this.refresh = refresh
        if (loadingLayout != NO_ID) {
            showStateView(loadingLayout, data)
        }
        return this
    }

    /**
     * 显示错误视图
     * @param any Any? 传入数据, 将通过[onError]回调返回
     * @param autoRetryLoading Boolean 是否自动启用视图点击重试功能
     */
    fun showError(any: Any?=null, autoRetryLoading: Boolean = true) {
        if(status==StateStatus.ERROR) return
        if (errorLayout == NO_ID) {
            //使用全局参数
            errorLayout = StateConfig.errorLayout
        }
        this.autoRetryLoading = autoRetryLoading
        if (errorLayout != NO_ID) {
            showStateView(errorLayout, any)
        }
    }

    /**
     *  显示空视图
     * @param data Any? 传入数据, 将通过[onEmpty]回调返回
     * @param autoRetryLoading Boolean 是否自动启用视图点击重试功能
     */
    fun showEmpty(data: Any?=null, autoRetryLoading: Boolean = true) {
        if(status==StateStatus.EMPTY) return
        if (emptyLayout == NO_ID) {
            //使用全局参数
            emptyLayout = StateConfig.emptyLayout
        }
        this.autoRetryLoading = autoRetryLoading
        if (emptyLayout != NO_ID) {
            showStateView(emptyLayout, data)
        }
    }

    fun showContent() {
        if(status==StateStatus.DEFAULT) return
        showStateView(contentId, null)
    }

   internal fun setContent(view: View){
        if (contentViews.isEmpty()) {
            contentViews[contentId] = view
        }
    }
    /**
     * 显示状态视图
     */
    private fun showStateView(@LayoutRes layoutId: Int, data: Any?) {
        runMain {
            val view = getView(layoutId)
            contentViews.values.forEach {
                if (it != view) {
                    it.gone()
                }
            }
            view.show()
            when (layoutId) {
                emptyLayout -> { //空
                    status = StateStatus.EMPTY
                    if (autoRetryLoading) {
                        if (emptyRetryIds.isEmpty()) emptyRetryIds = StateConfig.emptyRetryIds
                        emptyRetryIds.forEach {
                            findView<View>(it)?.tingleClick {
                                //重试
                                showLoading(data)
                            }
                        }
                    }
                    onEmptyBlock ?: StateConfig.onEmptyBlock?.invoke(view, data)
                }
                loadingLayout -> { //加载
                    status = StateStatus.LOADING
                    onLoadingBlock ?: StateConfig.onLoadingBlock?.invoke(view, data)
                    if(context.isNetConnected()){
                        if (refresh) {
                            onRefreshBlock?.invoke(this, data)
                        }
                    }else{
                        showError()
                    }
                }
                errorLayout -> { //错误
                    status = StateStatus.ERROR
                    if (autoRetryLoading) {
                        if (errorRetryIds.isEmpty()) errorRetryIds = StateConfig.errorRetryIds
                        errorRetryIds.forEach {
                            findView<View>(it)?.tingleClick {
                                //重试
                                showLoading(data)
                            }
                        }
                    }
                    onErrorBlock ?: StateConfig.onErrorBlock?.invoke(view, data)
                }
                else -> {
                    status = StateStatus.DEFAULT
                }
            }
        }
    }


    private fun getView(@LayoutRes layoutId: Int): View {
        contentViews[layoutId]?.let {
            return it
        }
        val stateLayout = context.inflate(layoutId, this, false)
        addView(stateLayout)
        contentViews[layoutId] = stateLayout
        return stateLayout
    }

    private fun removeView(@LayoutRes layoutId:Int){
        if(contentViews.containsKey(layoutId)){
            val view=contentViews.remove(layoutId)
            removeView(view)
        }
    }
}

