package com.dn.vi.app.base.app

import com.dn.vi.app.cm.log.VLog
import io.reactivex.rxjava3.exceptions.OnErrorNotImplementedException
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.functions.Consumer

/**
 * 默认的rx error 处理
 * Created by holmes on 2020/6/30.
 **/
open class DefaultRxErrorHandler : Consumer<Throwable> {

    override fun accept(t: Throwable?) {
        when (t) {
            is UndeliverableException -> {
                VLog.printErrStackTrace(t.cause, "[No Crash] caught undeliver")
            }
            is OnErrorNotImplementedException -> {
                val stack = t.cause ?: t
                VLog.printErrStackTrace(stack, "[No Crash] rx error default handle.")
            }
            else -> {
                VLog.printErrStackTrace(t, "[No Crash] caught error in rx")
            }
        }
    }
}