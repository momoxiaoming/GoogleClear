package com.mc.cpyr.guide.config

/**
 * BuildException
 *
 * @author mmxm
 * @date 2021/3/8 17:07
 */
class BuildException(var mDetailMessage: String):RuntimeException() {

    private val serialVersionUID = 6208777692136933357L


    override fun getLocalizedMessage(): String? {
        return "Build GuideFragment failed: $mDetailMessage"
    }

}