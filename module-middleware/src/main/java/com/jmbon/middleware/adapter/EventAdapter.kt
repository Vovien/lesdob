package com.jmbon.middleware.adapter

import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.kotlinbus.UI
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.reflect.ParameterizedType


abstract class EventAdapter<EV, T, VB : ViewBinding> : BindingQuickAdapter<T, VB>() {

    override fun onViewAttachedToWindow(holder: BaseBindingHolder) {
        super.onViewAttachedToWindow(holder)
        holder.getViewBinding<VB>().apply {
            // Log.e("TAG att register", "${holder.adapterPosition}")
            KotlinBus.register(
                this@EventAdapter.hashCode().toString() + "-" + holder.adapterPosition,
                UI,
                getGenericClass()
            ) {
                if (holder.adapterPosition < data.size && holder.adapterPosition >= 0) {
                    setEventData(it, data[holder.adapterPosition], this)
                }
            }

            registerEventBus(
                this@EventAdapter.hashCode()
                    .toString() + "-registerEventBus-" + holder.adapterPosition,
                UI, holder.adapterPosition
            )

        }
    }

    abstract fun setEventData(it: EV, item: T, viewBinding: VB)

    //兼容注册多个EventData
    open fun registerEventBus(name: String, eventDispatcher: CoroutineDispatcher, pos: Int) {

    }

    override fun onViewDetachedFromWindow(holder: BaseBindingHolder) {
        KotlinBus.unregister(this.hashCode().toString() + "-" + holder.adapterPosition)
        super.onViewDetachedFromWindow(holder)
    }


    private fun getGenericClass(): Class<EV> {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments[0] as Class<EV>
    }
}