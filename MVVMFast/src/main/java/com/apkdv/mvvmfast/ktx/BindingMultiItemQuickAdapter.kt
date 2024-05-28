package com.apkdv.mvvmfast.ktx

import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BindingMultiItemQuickAdapter<T : MultiItemEntity, VB : ViewBinding>(data: MutableList<T>? = null) :
    BaseMultiItemQuickAdapter<T, BindingMultiItemQuickAdapter.BaseBindingHolder>(data) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
        BaseBindingHolder(inflateBindingWithGeneric<VB>(parent))

    open class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
        constructor(itemView: View) : this(ViewBinding { itemView })

        @Suppress("UNCHECKED_CAST")
        fun <VB : ViewBinding> getViewBinding() = binding as VB
    }
}