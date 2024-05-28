package com.jmbon.middleware.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.kotlinbus.UI
import com.jmbon.middleware.utils.ViewBindingDelegate
import com.jmbon.middleware.utils.ViewBindingViewHolder
import java.lang.reflect.ParameterizedType


abstract class EventViewBindingDelegate<EV, T, VB : ViewBinding> : ViewBindingDelegate<T, VB>() {


    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
    ): ViewBindingViewHolder<VB> {
        return ViewBindingViewHolder(inflateBindingWithGeneric(parent))
    }

    override fun onViewAttachedToWindow(holder: ViewBindingViewHolder<VB>) {
        super.onViewAttachedToWindow(holder)
        holder.apply {
            // Log.e("TAG att register", "${holder.adapterPosition}")
            KotlinBus.register(
                this@EventViewBindingDelegate.hashCode()
                    .toString() + "-" + holder.bindingAdapterPosition,
                UI,
                getGenericClass()
            ) {
                if (holder.bindingAdapterPosition < adapter.itemCount && holder.bindingAdapterPosition >= 0) {
                    setEventData(it, adapter.items[holder.bindingAdapterPosition], holder.binding)
                }
            }
        }
    }

    abstract fun setEventData(it: EV, item: Any, viewBinding: VB)


    override fun onViewDetachedFromWindow(holder: ViewBindingViewHolder<VB>) {
        KotlinBus.unregister(this.hashCode().toString() + "-" + holder.bindingAdapterPosition)
        super.onViewDetachedFromWindow(holder)
    }


    private fun getGenericClass(): Class<EV> {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments[0] as Class<EV>
    }
}