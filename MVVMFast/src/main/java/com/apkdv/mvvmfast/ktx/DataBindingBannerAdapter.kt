package com.apkdv.mvvmfast.ktx

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.youth.banner.adapter.BannerAdapter

/**
 * DataBinding的Banner
 * @author MilkCoder
 * @date 2023/6/6
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
abstract class DataBindingBannerAdapter<T, DB : ViewDataBinding> :
    BannerAdapter<T, DataBindingBannerAdapter.AutomaticDataBindingHolder<DB>>(listOf()) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int) =
        AutomaticDataBindingHolder(inflateDataBindingWithGeneric<DB>(parent).apply {
            this.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })

    open class AutomaticDataBindingHolder<DB : ViewDataBinding>(binding: DB) :
        BaseDataBindingHolder<DB>(binding.root)
}