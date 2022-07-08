package com.mckj.sceneslib.manager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mckj.sceneslib.R
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.kt.transportData
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.manager.scenes.ScenesManager

@Route(path = ARouterPath.Receiver.FRAGMENT_EMPTY)
class EmptyJumpFragment : Fragment() {

    private val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

    // 通过name来映射URL中的不同参数
    @Autowired(name = "scenes_type")
    @JvmField
    var scenesType = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty_jump, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val check = requireContext().checkSelfPermission(permission)
            if (check == PackageManager.PERMISSION_GRANTED) {
                jumpPage(scenesType)
            } else {
                requestPer()
            }
        }
    }

    private fun requestPer() {
        requestPermissions(arrayOf(permission),123)
    }

    private fun jumpPage(scenesType: Int) {
        if (scenesType != -1) {
            val jumpPage = ScenesManager.getInstance().jumpPage(requireContext(), scenesType,"out")
            requireActivity().finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode!=123){
            return
        }

        var result = true
        for (res in grantResults){
            if (res != PackageManager.PERMISSION_GRANTED){
                result = false
            }
        }
        if (result){
            jumpPage(scenesType)
        }else{
            requireActivity().finish()
        }
    }
}