package com.mckj.sceneslib.util

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.bean.GuideScenesBean
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.viewbinder.LandingGuideViewNormalBinder
import com.mckj.sceneslib.ui.viewbinder.LandingGuideViewSpecialBinder
import com.org.proxy.EvAgent

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.util
 * @data  2022/4/19 10:32
 */
class ScenesAdapterProvider {

    companion object{
        fun crateAdapter(fragment: Fragment,mModel:ScenesViewModel,special:(LandingGuideViewSpecialBinder)->Unit,normal:(LandingGuideViewNormalBinder)->Unit):MultiTypeAdapter{
                val adapter = MultiTypeAdapter()
            val scenesName = mModel.getScenesData().name
            adapter.register(GuideScenesBean::class).to(LandingGuideViewSpecialBinder(fragment).also {
                    special(it)
                    it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<GuideScenesBean> {
                        override fun onItemClick(view: View, position: Int, t: GuideScenesBean) {
//                            St.stLevelGuideClick(mModel.getScenesData().key)
//                            stItemRecommend(position,mModel)
                            setItemSt(it.getSafetyState(),scenesName)
                            t.scenes.enterFlag = "落地页"
                            t.scenes.jumpPage(fragment.requireContext()) { accept ->
                                if (accept) mModel.isFinish.value = true
                            }
                        }
                    }
                }, LandingGuideViewNormalBinder().also {
                    normal(it)
                    it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<GuideScenesBean> {
                        override fun onItemClick(view: View, position: Int, t: GuideScenesBean) {
//                            St.stLevelGuideClick(mModel.getScenesData().key)
//                            stItemRecommend(position,mModel)
                            setItemSt(it.getSafetyState(),scenesName)
                            t.scenes.enterFlag = "落地页"
                            t.scenes.jumpPage(fragment.requireContext()) { accept ->
                                if (accept) mModel.isFinish.value = true
                            }
                        }
                    }
                }).withKotlinClassLinker { position, item ->
                    when(item.type){
                        GuideScenesBean.SPECIAL_TYPE->LandingGuideViewSpecialBinder::class
                        else->LandingGuideViewNormalBinder::class
                    }
                }

//                adapter.register(NativeAdEntity::class, NativeAdViewBinder(context) { adStatus ->
//                })
                return adapter
            }

        private fun setItemSt(state:Boolean,name:String){
            if (state) {
                St.stLevelRecommendClick(name)
            }else{
                St.stExitConfirmThreatenClick(name)
            }
        }


        //单个推荐菜单点击打点
        private fun stItemRecommend(position: Int,mModel: ScenesViewModel) {
            val scenes = mModel.getScenes()
            val scenesData = scenes.getData()
            val map = HashMap<String, String>()
            var landTypes = ""
            scenes.landRecommendTypes.forEach { it ->
                landTypes = "$landTypes${scenes.getNameByType(it)}+"
            }
            when (scenes.landContentType) {
                ScenesType.LandType.TYPE_NEWS -> map["what"] = "新闻"
                ScenesType.LandType.TYPE_RECOMMEND -> map["what"] = landTypes
            }
            map["from"] = scenesData.name
            map["type"] = scenes.getNameByType(scenes.landRecommendTypes[position])

            EvAgent.sendEventMap("landing_page_show", map)
        }

    }


}