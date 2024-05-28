package com.apkdv.mvvmfast.ktx

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
 * @Description 绑定DataBinding的适配器
 * @Author MilkCoder
 * @Date 2023/5/6
 * @Version 6.1.0
 * @Copyright All copyrights reserved to ManTang.
 */
abstract class DataBindingQuickAdapter<T, DB : ViewDataBinding> :
    BaseQuickAdapter<T, DataBindingQuickAdapter.AutomaticDataBindingHolder<DB>>(layoutResId = -1) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
        AutomaticDataBindingHolder(inflateDataBindingWithGeneric<DB>(parent))

    open class AutomaticDataBindingHolder<DB : ViewDataBinding>(binding: DB) :
        BaseDataBindingHolder<DB>(binding.root)

    private var last = 0L
    private var lastChildHashCode = 0
    var childTimeLag = 500
    override fun setOnItemChildClick(v: View, position: Int) {
        val current = System.currentTimeMillis()
        if (lastChildHashCode != v.hashCode()) {
            super.setOnItemChildClick(v, position)
            last = System.currentTimeMillis()
            lastChildHashCode = v.hashCode()
        } else if (current - last > childTimeLag) {
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
        } else if (current - itemLast > childTimeLag) {
            super.setOnItemClick(v, position)
        }
        itemLast = System.currentTimeMillis()
        lastItemHashCode = v.hashCode()
    }
}