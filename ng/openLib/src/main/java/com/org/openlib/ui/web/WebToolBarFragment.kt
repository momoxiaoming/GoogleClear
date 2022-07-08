package com.org.openlib.ui.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.DatabindingFragment
import com.dn.vi.app.base.app.kt.peek
import com.dn.vi.app.base.helper.DataTransport
import com.org.openlib.databinding.PiLayoutWebToolbarBinding

/**
 * WebToolBarFragment
 * web页上部toolbar
 * Created by Vito on 2020/7/8.
 **/
@Route(path = "/pipe/sense/webToolbar")
class WebToolBarFragment : DatabindingFragment<PiLayoutWebToolbarBinding>() {

    @JvmField
    var title : String = ""

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PiLayoutWebToolbarBinding {
        return PiLayoutWebToolbarBinding.inflate(inflater, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val req = DataTransport.getInstance().peek<WebHelper.Request>(WebHelper.EXTRA_WEB_REQ2)
        if (req == null) {
            return
        }
        req.inject(this)

        binding.titleBar.title = title
        binding.titleBar.setNavigationOnClickListener {
            activity?.finish()
        }
    }

}