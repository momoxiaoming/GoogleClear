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
         * 可选元素最大值
         */
        fun getSelectMaxCount() = Int.MAX_VALUE
        /**
         * 添加默认选中数据
         * @param list MutableList<T>
         */
        fun addDefaultSrcList(list:MutableList<T>){}

        /**
         * 获取recycleView数据源
         * @return List<T>
         */
        fun getSrcList(): List<T>

        /**
         * 刷新recycleView
         */
        fun notifyAdapter()

        /**
         * 部分刷新recycleView
         * @param position Int
         */
        fun notifyAdapter(position: Int)

        /**
         * 已经选择的元素列表
         * @return List<T>
         */
        fun getSelectList(): List<T>

        /**
         * 全选
         */
        fun selectAll(){}

        /**
         * 取消全选
         */
        fun removeSelectAll(){}

        /**
         * 反选
         */
        fun contrarySelectAll(){}

        fun removePartSelect(list:List<T>)
    }

}