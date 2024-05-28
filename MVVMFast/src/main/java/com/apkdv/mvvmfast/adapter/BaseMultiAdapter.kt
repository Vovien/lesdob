package com.apkdv.mvvmfast.adapter

import com.drakeet.multitype.MultiTypeAdapter

open class BaseMultiAdapter : MultiTypeAdapter() {
    val data by lazy { mutableListOf<Any>() }

    init {
        items = data
    }

    //    open var items: MutableList<Any> = mutableListOf()
    open fun setNewInstances(viewData: Collection<Any>) {
        data.clear()
        data.addAll(viewData)
        notifyDataSetChanged()
    }

    open fun addData(viewData: Any) {
        val oldSize = itemCount
        data.add(viewData)
        notifyItemInserted(oldSize)
    }

    open fun addData(viewData: Collection<Any>) {
        val oldSize = itemCount
        data.addAll(viewData)
        notifyItemRangeInserted(oldSize, itemCount)
    }

    open fun addData(viewData: Collection<Any>, index: Int = 0) {
        val oldSize = itemCount
        data.addAll(index, viewData)
        notifyItemRangeInserted(oldSize, itemCount)
    }

    open fun removeData(position: Int): Any? {
        var removedViewData: Any? = null
        if (position in 0 until itemCount) {
            removedViewData = data.removeAt(position)
            notifyItemRemoved(position)
        }
        return removedViewData
    }

    open fun removeData(viewData: Any): Any? {
        val position = data.indexOf(viewData)
        return removeData(position)
    }

    open fun clearData() {
        val oldSize = itemCount
        data.clear()
        notifyItemRangeRemoved(0, oldSize)
    }

    open fun getData(position: Int): Any? {
        return if (position in 0 until itemCount) {
            data[position]
        } else {
            null
        }
    }

    open fun getItemPosition(item: Any?): Int {
        return if (item != null && data.isNotEmpty()) data.indexOf(item) else -1
    }
}