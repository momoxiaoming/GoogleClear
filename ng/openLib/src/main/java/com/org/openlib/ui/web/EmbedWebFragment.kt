package com.org.openlib.ui.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.DatabindingFragment
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.app.ViFragment
import com.dn.vi.app.base.app.kt.arouter
import com.org.openlib.databinding.PiLayoutWebEmbedBinding

/**
 * 带title样式的，内嵌入webView.
 *
 * 可以不用缓存实例
 *
 * Created by holmes on 2020/7/30.
 **/
@Route(path = "/pipe/sense/web/embed")
class EmbedWebFragment : DatabindingFragment<PiLayoutWebEmbedBinding>() {

//    @Autowired(name = "title", required = false)
    @JvmField
    var title: String = ""

//    @Autowired(name = "webUrl")
    @JvmField
    var webUrl: String = ""

//    @Autowired(name = "extJs", required = false)
    @JvmField
    var extJs: String = ""

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PiLayoutWebEmbedBinding {
        return PiLayoutWebEmbedBinding.inflate(inflater, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        arouter().inject(this)

        title = arguments?.getString("title","").toString()
        webUrl = arguments?.getString("webUrl","").toString()
        extJs = arguments?.getString("extJs","").toString()

        binding.titleBar.title = this.title
        binding.titleBar.setNavigationOnClickListener {
            activityAs<ViActivity> { goBack() }
        }

        val fragmentWeb = arouter().build("/pipe/sense/web")
            .withString("webUrl", webUrl)
            .withString("extJs", extJs)
            .navigation() as ViFragment

        childFragmentManager.commit {
            add(binding.container.id, fragmentWeb, "web")
        }
    }

}