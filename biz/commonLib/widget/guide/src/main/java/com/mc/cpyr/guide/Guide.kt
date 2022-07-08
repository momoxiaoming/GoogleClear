package com.mc.cpyr.guide

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.drakeet.purewriter.ObscureLifecycleEventObserver
import com.drakeet.purewriter.addObserver
import com.mc.cpyr.guide.config.BuildException
import com.mc.cpyr.guide.config.Component
import com.mc.cpyr.guide.config.Configuration
import com.mc.cpyr.guide.listener.ComponentClickListener
import com.mc.cpyr.guide.listener.MaskViewClickListener
import com.mc.cpyr.guide.listener.VisibilityChangedListener
import com.mc.cpyr.guide.utils.Utils
import com.mc.cpyr.guide.view.MaskView


/**
 * Guide
 *
 * @author mmxm
 * @date 2021/3/8 17:12
 */
class Guide(var mConfiguration: Configuration, lifecycleOwner: LifecycleOwner) :
    View.OnTouchListener, ObscureLifecycleEventObserver, View.OnKeyListener {

    private  var mComponents :MutableList<Component>?=null
    private var mMaskView: MaskView? = null
    // 根据locInwindow定位后，是否需要判断loc值非0
    private var mShouldCheckLocInWindow = true

    /**
     * 蒙版显示和消失监听
     */
    private var mOnVisibilityChangedListener: VisibilityChangedListener? = null

    /**
     * 遮罩上component view的点击事件
     */
    private var mComponentClickListener: ComponentClickListener? = null


    /**
     * 蒙版点击事件
     */
    private var guideViewClick: MaskViewClickListener? = null

    private var startY = -1f

    private  var activity:Activity?=null


    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }


    /**
     * 显示遮罩
     *
     * @param activity 目标Activity
     */
    fun show(activity: Activity) {
        show(activity, null)
    }



    /**
     * 显示遮罩
     *
     * @param activity 目标Activity
     * @param overlay  遮罩层view
     */
    fun show(activity: Activity, overlay: ViewGroup?) {
        this.activity=activity
        var overlay = overlay
        mMaskView = onCreateView(activity, overlay)
        if (overlay == null) {
            overlay = activity.window.decorView as ViewGroup
        }
        if (mMaskView!!.parent == null && mConfiguration.mTargetView != null) {
            overlay.addView(mMaskView)
            if (mConfiguration.mEnterAnimationId != -1) {
                val anim = AnimationUtils.loadAnimation(
                    activity,
                    mConfiguration.mEnterAnimationId
                )!!
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        if (mOnVisibilityChangedListener != null) {
                            mOnVisibilityChangedListener!!.onShown()
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                mMaskView!!.startAnimation(anim)
            } else {
                if (mOnVisibilityChangedListener != null) {
                    mOnVisibilityChangedListener!!.onShown()
                }
            }
        }
    }
    //@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun clear() {
        if (mMaskView == null) {
            return
        }
        val vp = mMaskView!!.parent as ViewGroup ?: return
        vp.removeView(mMaskView)
        onDestroy()
    }

    /**
     * 隐藏该遮罩并回收资源相关
     */
    fun dismiss() {
        if (mMaskView == null) {
            return
        }
        val vp = mMaskView!!.parent as ViewGroup ?: return
        if (mConfiguration.mExitAnimationId != -1) {
            // mMaskView may leak if context is null
            val context = mMaskView!!.context!!
            val anim = AnimationUtils.loadAnimation(context, mConfiguration.mExitAnimationId)!!
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    vp.removeView(mMaskView)
                    if (mOnVisibilityChangedListener != null) {
                        mOnVisibilityChangedListener!!.onDismiss()
                    }
                    onDestroy()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            mMaskView!!.startAnimation(anim)
        } else {
            vp.removeView(mMaskView)
            if (mOnVisibilityChangedListener != null) {
                mOnVisibilityChangedListener!!.onDismiss()
            }
            onDestroy()
        }
    }

    /**
     * 根据locInwindow定位后，是否需要判断loc值非0
     */
    fun setShouldCheckLocInWindow(set: Boolean) {
        mShouldCheckLocInWindow = set
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onCreateView(activity: Activity, overlay: ViewGroup?): MaskView? {
        val overlay = activity.window.decorView as ViewGroup
        val maskView = MaskView(activity)
        maskView.setFullingColor(activity.resources.getColor(mConfiguration.mFullingColorId))
        maskView.setFullingAlpha(mConfiguration.mAlpha)
        maskView.setHighTargetCorner(mConfiguration.mCorner)
        maskView.setPadding(mConfiguration.mPadding)
        maskView.paddingLeft = mConfiguration.mPaddingLeft
        maskView.paddingTop = mConfiguration.mPaddingTop
        maskView.paddingRight = mConfiguration.mPaddingRight
        maskView.paddingBottom = mConfiguration.mPaddingBottom
        maskView.setHighTargetGraphStyle(mConfiguration.mGraphStyle)
        maskView.setOverlayTarget(mConfiguration.mOverlayTarget)
        maskView.setOnKeyListener(this)

        // For removing the height of status bar we need the root content view's
        // location on screen
        var parentX = 0
        var parentY = 0
        val loc = IntArray(2)
        overlay.getLocationInWindow(loc)
        parentX = loc[0]
        parentY = loc[1]
        if (mConfiguration.mTargetView != null) {
            maskView.setTargetRect(
                Utils.getViewAbsRect(
                    mConfiguration.mTargetView!!, parentX,
                    parentY
                )
            )
        } else {
            // Gets the target view's abs rect
            val target = activity.findViewById<View>(mConfiguration.mTargetViewId)
            if (target != null) {
                maskView.setTargetRect(Utils.getViewAbsRect(target, parentX, parentY))
            }
        }
        if (mConfiguration.mOutsideTouchable) {
            maskView.isClickable = false
        } else {
            maskView.setOnTouchListener(this)
        }

        // Adds the components to the mask view.
        for (c in mComponents!!) {
            val view=Utils.componentToView(activity.layoutInflater, c)

            view?.setOnClickListener(componentsListener)
            maskView.addView(view)
        }
        return maskView
    }


    private val componentsListener=View.OnClickListener {
        mComponentClickListener?.let{ b ->
             if(b.onClick(it)){
                 dismiss()
             }
        }
    }

    //@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        if (mMaskView != null) {
            mMaskView!!.removeAllViews()
            mMaskView = null
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
        //拦截返回键
        Log.d("allen", "----keyCode---${keyCode}----event->${event}")
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            if (mConfiguration.mAutoDismiss) {
                dismiss()
                true
            } else {
                false
            }
        } else false
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            startY = motionEvent.y
        } else if (motionEvent.action == MotionEvent.ACTION_UP) {
            if (mConfiguration.mAutoDismiss) {
                dismiss()
            }
            maskViewClick(view, motionEvent)
        }
        return true
    }

    private fun maskViewClick(view: View, motionEvent: MotionEvent) {
        val mTouchStartX = motionEvent.x
        val mTouchStartY = motionEvent.y
        if (view is MaskView) {
            if (guideViewClick != null) {
                val bl = guideViewClick!!.onClick(view, view.isClickTarget(mTouchStartX, mTouchStartY))
                if(bl){
                    dismiss()
                }
            }
        }
    }


    /**
     * 蒙版构造器
     *
     * @author mmxm
     * @date 2021/3/8 16:59
     */
    class Builder {

        private var mComponents= mutableListOf<Component>()
        private var maskViewClickListener: MaskViewClickListener? = null
        private var mOnVisibilityChangedListener: VisibilityChangedListener? = null
        private var mConfiguration: Configuration = Configuration()
        private var mComponentClickListener: ComponentClickListener? = null


        /**
         * 构造器build后,禁止再次进行修改
         */
        private var mBuilt: Boolean = false

        /**
         * 设置蒙板透明度
         *
         * @param alpha [0-255] 0 表示完全透明，255表示不透明
         * @return GuideBuilder
         */
        fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int): Builder {
            var alpha = alpha
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            } else if (alpha < 0 || alpha > 255) {
                alpha = 0
            }
            mConfiguration.mAlpha = alpha
            return this
        }

        /**
         * 设置目标view
         */
        fun setTargetView(v: View): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            }
            mConfiguration.mTargetView = v
            return this
        }

        /**
         * 设置目标View的id
         *
         * @param id 目标View的id
         * @return GuideBuilder
         */
        fun setTargetViewId(@IdRes id: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            }
            mConfiguration.mTargetViewId = id
            return this
        }

        /**
         * 设置高亮区域的圆角大小
         *
         * @return GuideBuilder
         */
        fun setHighTargetCorner(corner: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            } else if (corner < 0) {
                mConfiguration.mCorner = 0
            }
            mConfiguration.mCorner = corner
            return this
        }

        /**
         * 设置高亮区域的图形样式
         *
         * @return GuideBuilder
         */
        fun setHighTargetGraphStyle(style: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            }
            mConfiguration.mGraphStyle = style
            return this
        }

        /**
         * 设置蒙板颜色的资源id
         *
         * @param id 资源id
         * @return GuideBuilder
         */
        fun setFullingColorId(@IdRes id: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            }
            mConfiguration.mFullingColorId = id
            return this
        }

        /**
         * 是否在点击的时候自动退出蒙板
         *
         * @param b true if needed
         * @return GuideBuilder
         */
        fun setAutoDismiss(b: Boolean): Builder {
            if (mBuilt) {
                throw BuildException("Already created, rebuild a new one.")
            }
            mConfiguration.mAutoDismiss = b
            return this
        }

        /**
         * 是否覆盖目标
         *
         * @param b true 遮罩将会覆盖整个屏幕
         * @return GuideBuilder
         */
        fun setOverlayTarget(b: Boolean): Builder {
            if (mBuilt) {
                throw BuildException("Already created, rebuild a new one.")
            }
            mConfiguration.mOverlayTarget = b
            return this
        }

        /**
         * 设置进入动画
         *
         * @param id 进入动画的id
         * @return GuideBuilder
         */
        fun setEnterAnimationId(@AnimatorRes id: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            }
            mConfiguration.mEnterAnimationId = id
            return this
        }

        /**
         * 设置退出动画
         *
         * @param id 退出动画的id
         * @return GuideBuilder
         */
        fun setExitAnimationId(@AnimatorRes id: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            }
            mConfiguration.mExitAnimationId = id
            return this
        }

        /**
         * 添加一个控件
         *
         * @param component 被添加的控件
         * @return GuideBuilder
         */
        fun addComponent(component: Component): Builder {
            if (mBuilt) {
                throw BuildException("Already created, rebuild a new one.")
            }
            mComponents.add(component)
            return this
        }

        /**
         * 设置遮罩可见状态变化时的监听回调
         */
        fun setOnVisibilityChangedListener(
            onVisibilityChangedListener: VisibilityChangedListener
        ): Builder {
            if (mBuilt) {
                throw BuildException("Already created, rebuild a new one.")
            }
            mOnVisibilityChangedListener = onVisibilityChangedListener
            return this
        }
        /**
         * 设置遮罩上Component view的点击点击事件
         */
        fun setonComponentClickListener(
            componentClickListener: ComponentClickListener
        ): Builder {
            if (mBuilt) {
                throw BuildException("Already created, rebuild a new one.")
            }
            mComponentClickListener = componentClickListener
            return this
        }

        /**
         * 设置遮罩系统是否可点击并处理点击事件
         *
         * @param touchable true 遮罩不可点击，处于不可点击状态 false 可点击，遮罩自己可以处理自身点击事件
         */
        fun setOutsideTouchable(touchable: Boolean): Builder {
            mConfiguration.mOutsideTouchable = touchable
            return this
        }

        /**
         * 设置高亮区域的padding
         *
         * @return GuideBuilder
         */
        fun setHighTargetPadding(padding: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            } else if (padding < 0) {
                mConfiguration.mPadding = 0
            }
            mConfiguration.mPadding = padding
            return this
        }

        /**
         * 设置高亮区域的左侧padding
         *
         * @return GuideBuilder
         */
        fun setHighTargetPaddingLeft(padding: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            } else if (padding < 0) {
                mConfiguration.mPaddingLeft = 0
            }
            mConfiguration.mPaddingLeft = padding
            return this
        }

        /**
         * 设置高亮区域的顶部padding
         *
         * @return GuideBuilder
         */
        fun setHighTargetPaddingTop(padding: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            } else if (padding < 0) {
                mConfiguration.mPaddingTop = 0
            }
            mConfiguration.mPaddingTop = padding
            return this
        }

        /**
         * 设置高亮区域的右侧padding
         *
         * @return GuideBuilder
         */
        fun setHighTargetPaddingRight(padding: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            } else if (padding < 0) {
                mConfiguration.mPaddingRight = 0
            }
            mConfiguration.mPaddingRight = padding
            return this
        }

        /**
         * 设置高亮区域的底部padding
         *
         * @return GuideBuilder
         */
        fun setHighTargetPaddingBottom(padding: Int): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            } else if (padding < 0) {
                mConfiguration.mPaddingBottom = 0
            }
            mConfiguration.mPaddingBottom = padding
            return this
        }

        /**
         * 设置蒙版点击事件
         *
         * @return GuideBuilder
         */
        fun setMaskViewClickListener(maskViewClickListener: MaskViewClickListener?): Builder {
            if (mBuilt) {
                throw BuildException("Already created. rebuild a new one.")
            }
            this.maskViewClickListener = maskViewClickListener
            return this
        }


        /**
         * 创建Guide，
         *
         * @return Guide
         */
        fun build(lifecycleOwner: LifecycleOwner): Guide {
            val guide = Guide(mConfiguration, lifecycleOwner)
            guide.mComponents=mComponents
            guide.mOnVisibilityChangedListener = mOnVisibilityChangedListener
            guide.guideViewClick = maskViewClickListener
            guide.mComponentClickListener=mComponentClickListener
            mBuilt = true
            return guide
        }

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if(event == Lifecycle.Event.ON_STOP) {
            clear()
        } else if(event == Lifecycle.Event.ON_DESTROY) {
            onDestroy()
        }
    }


}