package com.mckj.module.cleanup.ui.point

import android.Manifest
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.view.ToastUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.databinding.CleanupFragmentPointBinding
import com.mckj.module.cleanup.ui.viewbinder.PointMenuViewBinder
import com.mckj.sceneslib.entity.OptItem
import com.mckj.sceneslib.helper.addVerticalDividerLine
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.kotlin.subscribeBy


/**
 * PointFragment
 *
 * @author mmxm
 * @date 2021/3/3 16:40
 */
@Route(path = ARouterPath.Cleanup.FRAGMENT_POINT)
class PointFragment : DataBindingFragment<CleanupFragmentPointBinding, PointViewModel>() {



    private val menuAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(OptItem::class.java, PointMenuViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<OptItem> {
                override fun onItemClick(view: View, position: Int, t: OptItem) {
                    if(view.id==R.id.fixit_btn){
                        menuClickListener(t)
                    }
                }
            }
        })
        adapter
    }

    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_point
    }

    override fun getViewModel(): PointViewModel {
        return ViewModelProvider(this).get(PointViewModel::class.java)
    }
    override fun initData() {
        mModel.menuList.observe(this){
            mBinding.contentRv.adapter=menuAdapter
            menuAdapter.items=it
            menuAdapter.notifyDataSetChanged()
            mBinding.topContentIv.text="${it.size}"
            if(it.size==0){
                mBinding.topTipIv.text="?????????????????????"
            }

        }

        mModel.getMenuList()
    }
    override fun initView() {
        activity?.title = "??????"
        mBinding.contentRv.layoutManager = LinearLayoutManager(context)
        mBinding.contentRv.addVerticalDividerLine{
            DividerDrawable(1).also { d ->
                d.color = 0xffC8C8C8.toInt()
            }
        }
    }

    fun menuClickListener(it: OptItem) {
        when (it.type) {
            OptItem.AUTO_CLEAN -> {
                //????????????
            }
            OptItem.DESK_TOOL -> {
                //??????????????????
            }
            OptItem.OPEN_NOT -> {
                //????????????
            }
            OptItem.OPEN_FLOAT -> {
                //???????????????
            }
            OptItem.OPEN_AUTHORITY -> {
                //??????????????????
                reqPermissions()
            }
        }
    }

    fun reqPermissions(){
       val permissions= arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        activity?.also {
            val rp = RxPermissions(it)
            rp.request(*permissions)
                .subscribeBy (onError = {
                }){b->
                    if(b){
                        mModel.getMenuList()
                    }else{
                        ToastUtil.postShow("?????????????????????,???????????????-???????????????????????????")
                    }
                }
        }

    }

}
