package com.mckj.sceneslib.ui.scenes.model.file

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.drakeet.multitype.MultiTypeAdapter
import com.dn.baselib.base.databinding.DataBindingFragment

import com.dn.baselib.base.AbstractViewBinder
import com.dn.baselib.util.SizeUtil
import com.dn.datalib.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentFileMenuBinding
import com.mckj.sceneslib.entity.FileMenuItem
import com.mckj.sceneslib.helper.addVerticalDividerLine
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.viewbinder.FileMenuViewBinder

/**
 * FileMenuFragment
 *
 * @author mmxm
 * @date 2021/3/5 10:43
 */
@Route(path = ARouterPath.Scenes.FRAGMENT_FILE_MENU)
class FileMenuFragment : DataBindingFragment<ScenesFragmentFileMenuBinding, FileViewModel>() {


    private val adapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(FileMenuItem::class.java, FileMenuViewBinder().apply {
            itemClickListener = object : AbstractViewBinder.OnItemClickListener<FileMenuItem> {
                override fun onItemClick(view: View, position: Int, t: FileMenuItem) {
                    itemClick(t)
                }
            }

        })

        adapter
    }


    override fun getLayoutId(): Int {
        return R.layout.scenes_fragment_file_menu
    }

    override fun getViewModel(): FileViewModel {
        return ViewModelProvider(this).get(FileViewModel::class.java)

    }

    override fun initData() {
    }

    override fun initView() {
        activity?.title = "文件管理"
        mBinding.fileRv.layoutManager = LinearLayoutManager(requireContext())
        mBinding.fileRv.adapter = adapter

        mBinding.fileRv.addVerticalDividerLine {
            DividerDrawable(1).let { d ->
                d.color = 0xffC8C8C8.toInt()
                d.setMargin(SizeUtil.dp2px(25f), 0, SizeUtil.dp2px(25f), 0)
                d
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        mModel.fileMenu.observe(this) {
            adapter.items = it
            adapter.notifyDataSetChanged()
        }


    }

    fun itemClick(t: FileMenuItem) {

        when (t.type) {
            FileMenuItem.PHOTO -> {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), ScenesType.TYPE_PHOTO_MANAGER)
            }
            FileMenuItem.VIDEO -> {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), ScenesType.TYPE_VIDEO_MANAGER)
            }
            FileMenuItem.APK -> {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), ScenesType.TYPE_APK_MANAGER)
            }
            FileMenuItem.AUDIO -> {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), ScenesType.TYPE_AUDIO_MANAGER)
            }
            FileMenuItem.ZIP -> {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), ScenesType.TYPE_ZIP_MANAGER)
            }
            FileMenuItem.BIG_FILE -> {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), ScenesType.TYPE_BIG_FILE_MANAGER)
            }
        }
    }

}
