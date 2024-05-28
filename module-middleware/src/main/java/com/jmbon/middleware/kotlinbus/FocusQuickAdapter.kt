package com.jmbon.middleware.kotlinbus

import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.event.FocusChangedEvent


abstract class FocusQuickAdapter<T, VB : ViewBinding>(var category: String = "") :
    BindingQuickAdapter<T, VB>() {

    override fun onViewAttachedToWindow(holder: BaseBindingHolder) {
        super.onViewAttachedToWindow(holder)
        holder.getViewBinding<VB>().apply {
            // Log.e("TAG att register", "${holder.adapterPosition}")
            KotlinBus.register(
                this@FocusQuickAdapter.hashCode().toString() + "-" + holder.adapterPosition,
                UI,
                FocusChangedEvent::class.java
            ) {
                if (holder.adapterPosition < data.size && holder.adapterPosition >= 0) {
                    setEventData(it, data[holder.adapterPosition], this)
                }
            }
        }
    }

    abstract fun setEventData(it: FocusChangedEvent, item: T, viewBinding: VB)


    override fun onViewDetachedFromWindow(holder: BaseBindingHolder) {
        KotlinBus.unregister(this.hashCode().toString() + "-" + holder.adapterPosition)
        super.onViewDetachedFromWindow(holder)
    }
}