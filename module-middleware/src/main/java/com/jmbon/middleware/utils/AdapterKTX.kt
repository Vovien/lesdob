package com.jmbon.middleware.utils

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.adapter.BaseMultiAdapter
import com.apkdv.mvvmfast.ktx.BaseViewHolderWithBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.apkdv.mvvmfast.ktx.resetLayoutManager
import com.apkdv.mvvmfast.utils.divider.RecyclerViewDivider
import com.blankj.utilcode.util.SizeUtils
import com.drakeet.multitype.ItemViewDelegate
import com.drakeet.multitype.MultiTypeAdapter

abstract class ViewBindingDelegate<T, VB : ViewBinding> :
    ItemViewDelegate<T, ViewBindingViewHolder<VB>>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup
    ): ViewBindingViewHolder<VB> {
        return ViewBindingViewHolder(inflateBindingWithGeneric(parent))
    }
}

abstract class AnyCallback(val oldItems: List<Any> = arrayListOf(), val newItems: List<Any> = arrayListOf()) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return areItemsTheSame(oldItem, newItem)

    }

    abstract fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return areContentsTheSame(oldItem, newItem)
    }

    abstract fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean
}

// 带动画的差分更新
fun MultiTypeAdapter.submitList(callback: AnyCallback) {
    val result = DiffUtil.calculateDiff(callback)
    items = callback.newItems
    result.dispatchUpdatesTo(this)
}

// 添加数据
fun MultiTypeAdapter.addData(@NonNull newData: List<Any>) {
    this.items += newData
    notifyItemRangeInserted(this.items.size - newData.size, newData.size)
}
// 添加数据
fun MultiTypeAdapter.addData(@NonNull newData: Any) {
    this.items += newData
    notifyItemRangeInserted(this.items.size - 1, 1)
}
fun MultiTypeAdapter.clean() {
    this.items.toMutableList().clear()
    notifyDataSetChanged()
}


fun MultiTypeAdapter.setNewInstance(list: List<Any>?) {
    if (list === this.items) {
        return
    }
    this.items = list ?: arrayListOf()
    notifyDataSetChanged()
}

fun MultiTypeAdapter.removeAt(@IntRange(from = 0) position: Int) {
    if (position >= this.items.size) {
        return
    }
    val oldList = this.items.toMutableList()
    oldList.removeAt(position)

    this.items = oldList
    notifyItemRemoved(position)
    notifyItemRangeChanged(position, this.items.size - position)
}


// 旧方法更新
fun MultiTypeAdapter.updateItems(items: List<Any>) {
    this.items = items
    this.notifyDataSetChanged()
}

class ViewBindingViewHolder<VB : ViewBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root)


/**
 * 通过[V] [H] [GV2] [GH3] [SV2] [SV3] 方式, 设置 [LayoutManager]
 * */
fun RecyclerView.initAdapter(
    match: String = "V", dividerHeight: Float = 0f, dividerColor: Int = Color.WHITE,
    left: Float = 0f, right: Float = 0f, top: Float = 0f, bottom: Float = 0f,
    vertical: Boolean = true, showEnd: Boolean = false
): BaseMultiAdapter {
    val adapter = BaseMultiAdapter()
    resetLayoutManager(match)
    this.adapter = adapter
    //禁止多指点击
    this.isMotionEventSplittingEnabled = false
    if (dividerHeight > 0f) {
        val divider = RecyclerViewDivider.builder(this.context)
            .leftMargin(SizeUtils.dp2px(left))
            .rightMargin(SizeUtils.dp2px(right))
            .topMargin(SizeUtils.dp2px(top))
            .bottomMargin(SizeUtils.dp2px(bottom))
            .color(dividerColor, SizeUtils.dp2px(dividerHeight))
        if (!showEnd)
            divider.hideLastDecoration()
        if (vertical)
            divider.horizontal()
        this.addItemDecoration(
            divider.build()
        )
    }
    return adapter
}