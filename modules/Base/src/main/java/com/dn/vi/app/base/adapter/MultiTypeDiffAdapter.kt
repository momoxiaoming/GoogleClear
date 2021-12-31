package com.dn.vi.app.base.adapter

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import androidx.recyclerview.widget.DiffUtil
import com.drakeet.multitype.MultiTypeAdapter
import com.drakeet.multitype.MutableTypes
import com.drakeet.multitype.Types

/**
 *  面向 LiveData的 MultiTypeAdapter
 *
 *  参考 [ListAdapter]
 * Created by holmes on 2020/5/21.
 **/
class MultiTypeDiffAdapter(
    initialCapacity: Int = 0,
    types: Types = MutableTypes(initialCapacity),
    diffCallback: DiffUtil.ItemCallback<Any> = ObjEqDiffCallback()
) : MultiTypeAdapter(emptyList(), initialCapacity, types) {

    override var items: List<Any>
        get() = mDiffer?.currentList ?: emptyList()
        set(value) {
            submitList(value)
        }

    var mDiffer: AsyncListDiffer<Any>? = null
    private val mListener: ListListener<Any?> =
        ListListener<Any?> { previousList, currentList ->
            this@MultiTypeDiffAdapter.onCurrentListChanged(
                previousList,
                currentList
            )
        }

    init {
        mDiffer = AsyncListDiffer<Any>(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder<Any>(diffCallback).build()
        )
        mDiffer!!.addListListener(mListener)
        submitList(items)
    }

    /**
     * 是否是个空的
     * @return
     */
    val isEmpty: Boolean
        get() = itemCount == 0

    /**
     * Submits a new list to be diffed, and displayed.
     *
     *
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param list The new list to be displayed.
     */
    fun submitList(list: List<Any?>?) {
        mDiffer!!.submitList(list)
    }

    /**
     * Set the new list to be displayed.
     *
     *
     * If a List is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     *
     * The commit callback can be used to know when the List is committed, but note that it
     * may not be executed. If List B is submitted immediately after List A, and is
     * committed directly, the callback associated with List A will not be run.
     *
     * @param list The new list to be displayed.
     * @param commitCallback Optional runnable that is executed when the List is committed, if
     * it is committed.
     */
    fun submitList(
        list: List<Any?>?,
        commitCallback: Runnable?
    ) {
        mDiffer!!.submitList(list, commitCallback)
    }

    protected fun getItem(position: Int): Any? {
        return mDiffer!!.currentList[position]
    }

    override fun getItemCount(): Int {
        return mDiffer!!.currentList.size
    }

    /**
     * Get the current List - any diffing to present this list has already been computed and
     * dispatched via the ListUpdateCallback.
     *
     *
     * If a `null` List, or no List has been submitted, an empty list will be returned.
     *
     *
     * The returned list may not be mutated - mutations to content must be done through
     * [.submitList].
     *
     * @return The list currently being displayed.
     *
     * @see .onCurrentListChanged
     */
    fun getCurrentList(): List<Any?> {
        return mDiffer!!.getCurrentList()
    }

    /**
     * Called when the current List is updated.
     *
     *
     * If a `null` List is passed to [.submitList], or no List has been
     * submitted, the current List is represented as an empty List.
     *
     * @param previousList List that was displayed previously.
     * @param currentList new List being displayed, will be empty if `null` was passed to
     * [.submitList].
     *
     * @see .getCurrentList
     */
    fun onCurrentListChanged(
        previousList: List<Any?>,
        currentList: List<Any?>
    ) {
    }

}

