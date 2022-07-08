package com.org.openlib.ui.web

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.app.ViFragment
import com.dn.vi.app.base.app.kt.arouter
import com.dn.vi.app.base.app.kt.getAs
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.base.helper.DataTransport
import com.org.openlib.R
import com.org.openlib.path.ARouterPath


/**
 * WebActivity
 * Created by Vito on 2020/7/8.
 */
@Route(path = ARouterPath.PAGE_WEB)
class WebActivity : ViActivity() {
    private val toolbarContainerPlaceHolderId: Int = R.id.toolbarContainer
    private val webContainerPlaceHolderId: Int = R.id.webContainer

    @JvmField
    var webUrl: String = ""

    @JvmField
    var title: String = ""

    @JvmField
    var extJs: String = ""

    /**
     * 如果为 "lock",则应用web嵌新闻
     */
    @JvmField
    var contentKey: String = "lock"

    /**
     * 是否开启deeplink功能
     */
    @JvmField
    var acceptDp: Boolean = false

    private val vm: WebViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(WebViewModel::class.java)
    }

    override fun initLayout() {
        setContentView(R.layout.pi__activity_toolbar_web)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val req = DataTransport.getInstance().getAs<WebHelper.Request>(WebHelper.EXTRA_WEB_REQ)
        if (req == null) {
            finish()
            return
        }
        req.inject(this)

        // 让下面两个fragment去用
        transportData {
            put(WebHelper.EXTRA_WEB_REQ2, req)
        }

        val fragmentToolBar = arouter().build("/pipe/sense/webToolbar")
            .navigation() as ViFragment

        val fragmentWeb = arouter().build("/pipe/sense/web")
            .navigation() as ViFragment

        replaceFragment(fragmentToolBar, toolbarContainerPlaceHolderId)
        replaceFragment(fragmentWeb, webContainerPlaceHolderId)
    }

    private fun replaceFragment(fragment: ViFragment, placeHolderId: Int) {
        supportFragmentManager.commit {
            replace(placeHolderId, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        vm.onActivityResult(requestCode, resultCode, data)
    }


}