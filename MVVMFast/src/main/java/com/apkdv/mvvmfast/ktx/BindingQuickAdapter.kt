package com.apkdv.mvvmfast.ktx

import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BindingQuickAdapter<T, VB : ViewBinding> :
    BaseQuickAdapter<T, BindingQuickAdapter.BaseBindingHolder>(layoutResId = -1) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
        BaseBindingHolder(inflateBindingWithGeneric<VB>(parent))

    open class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
        constructor(itemView: View) : this(ViewBinding { itemView })
        @Suppress("UNCHECKED_CAST")
        fun <VB : ViewBinding> getViewBinding() = binding as VB
    }

    private var last = 0L
    private var lastChildHashCode = 0
    var chidTimeLag = 500
    override fun setOnItemChildClick(v: View, position: Int) {
        val current = System.currentTimeMillis()
        if (lastChildHashCode != v.hashCode()) {
            super.setOnItemChildClick(v, position)
            last = System.currentTimeMillis()
            lastChildHashCode = v.hashCode()
        } else if (current - last > chidTimeLag) {
            super.setOnItemChildClick(v, position)
            last = System.currentTimeMillis()
            lastChildHashCode = v.hashCode()
        }
    }

    private var itemLast = 0L
    private var lastItemHashCode = 0
    override fun setOnItemClick(v: View, position: Int) {
        val current = System.currentTimeMillis()
        if (lastItemHashCode != v.hashCode()) {
            super.setOnItemClick(v, position)
        } else if (current - itemLast > 500) {
            super.setOnItemClick(v, position)
        }
        itemLast = System.currentTimeMillis()
        lastItemHashCode = v.hashCode()
    }
}