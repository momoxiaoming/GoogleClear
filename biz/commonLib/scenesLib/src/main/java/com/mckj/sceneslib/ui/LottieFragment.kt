package com.mckj.sceneslib.ui

import androidx.databinding.ViewDataBinding
import com.dn.vi.app.base.helper.StepRunner
import com.org.openlib.help.Consumer
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.gen.St
import kotlin.coroutines.resume

/**
 * Describe:lottie动画Fragment
 *
 * Created By yangb on 2021/3/10
 */
abstract class LottieFragment<T : ViewDataBinding, VM : AbstractViewModel> :
    DataBindingFragment<T, VM>() {

    override fun initData() {
        StepRunner.runner(scope) {
            breakAction = Runnable {
                preFinish(false)
            }
            finishedAction = Runnable {
                preFinish(true)
            }
            contStep { cont ->
                startAnim {
                    cont.resume(it)
                }
            }
            contStep { cont ->
                runningAnim {
                    cont.resume(it)
                }
            }
            contStep { cont ->
                endAnim {
                    cont.resume(it)
                }
            }
        }
    }

    /**
     * 开始动画
     */
    protected abstract fun startAnim(consumer: Consumer<Boolean>)

    /**
     * 执行动画
     */
    protected abstract fun runningAnim(consumer: Consumer<Boolean>)

    /**
     * 结束动画
     */
    protected abstract fun endAnim(consumer: Consumer<Boolean>)

    /**
     * 界面结束之前调用
     */
    protected abstract fun preFinish(result: Boolean)

}