package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.view.View

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util
 * @data  2022/1/21 15:42
 */
class MultipleAdapterHelper<T>( val adapter: IAdapterHelper<T>) {

    val selectList = arrayListOf<T>()


    fun checkSelectMax():Boolean=selectList.size<adapter.getSelectMaxCount()

    inline fun changeViewState(item:T, doContains:()->Unit, doRemoved:()->Unit){
        if (selectList.contains(item)) {
            doContains()
        } else {
            doRemoved()
        }
    }

    inline fun notifySelectView(view:View, item:T, position: Int, crossinline notify:(Int)->Unit){
        view.setOnClickListener {
            if (selectList.contains(item)) {
                selectList.remove(item)
            } else {
                if (checkSelectMax()) {
                    selectList.add(item)
                }
            }
            adapter.notifyAdapter(position)
            notify(position)
        }
    }

   inline fun notifySelectView(view:View, item:T,crossinline notify:(Int)->Unit){
        view.setOnClickListener {
            if (selectList.contains(item)) {
                selectList.remove(item)
            } else {
                if (checkSelectMax()) {
                    selectList.add(item)
                }
            }

            val position = adapter.getSrcList().indexOf(item)
            notify(position)
            adapter.notifyAdapter(position)
        }
    }

    fun addDefaultSelectData(list: MutableList<T>){
        selectList.addAll(list)
    }


    fun selectAll(){
        val srcList = adapter.getSrcList()
        selectList.removeAll(srcList.toSet())
        selectList.addAll(srcList)
        adapter.notifyAdapter()
    }

    fun removeSelectAll(){
        selectList.clear()
        adapter.notifyAdapter()
    }

    fun contrarySelectAll() {
        val srcList = adapter.getSrcList()
        val newList = arrayListOf<T>()
        newList.addAll(selectList)
        selectList.clear()
        selectList.addAll(srcList)
        selectList.removeAll(newList.toSet())
        adapter.notifyAdapter()
    }

    fun removePartSelect(list:List<T>){
        selectList.removeAll(list.toSet())
    }


    interface IAdapterHelper<T> {
        /**
         * ?????????????????????
         */
        fun getSelectMaxCount() = Int.MAX_VALUE
        /**
         * ????????????????????????
         * @param list MutableList<T>
         */
        fun addDefaultSrcList(list:MutableList<T>){}

        /**
         * ??????recycleView?????????
         * @return List<T>
         */
        fun getSrcList(): List<T>

        /**
         * ??????recycleView
         */
        fun notifyAdapter()

        /**
         * ????????????recycleView
         * @param position Int
         */
        fun notifyAdapter(position: Int)

        /**
         * ???????????????????????????
         * @return List<T>
         */
        fun getSelectList(): List<T>

        /**
         * ??????
         */
        fun selectAll(){}

        /**
         * ????????????
         */
        fun removeSelectAll(){}

        /**
         * ??????
         */
        fun contrarySelectAll(){}

        fun removePartSelect(list:List<T>)
    }

}