package com.jmbon.middleware.provider

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BaseViewHolderWithBinding
import com.apkdv.mvvmfast.ktx.getViewBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.arouter.MiniProgramService
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.kotlinbus.UI
import com.jmbon.middleware.utils.WebViewPool
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.reflect.ParameterizedType

abstract class BindingBaseItemProvider<EV, T, VB : ViewBinding> : BaseItemProvider<T>() {
    val miniProgram by lazy {
        ARouter.getInstance().build("/miniprogram/start/service").navigation() as MiniProgramService
    }

    //快捷显示数据
    var isQuickShowAnswer = false

    //是否正在滑动
    var isScrolling = false

    var webPool: WebViewPool? = null

    // 返回 item 布局 layout
    override val layoutId: Int
        get() = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return onCreateDefViewHolder(parent, viewType)
    }


    private fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolderWithBinding(inflateBindingWithGeneric<VB>(parent))

//    open class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
//        constructor(itemView: View) : this(ViewBinding { itemView })
//
//        @Suppress("UNCHECKED_CAST")
//        fun <VB : ViewBinding> getViewBinding() = binding as VB
//    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.getViewBinding<VB>().apply {
            // Log.e("TAG att register", "${holder.adapterPosition}")
            KotlinBus.register(
                this@BindingBaseItemProvider.hashCode().toString() + "-" + holder.adapterPosition,
                UI,
                getGenericClass()
            ) {
                getAdapter()?.let { adapter ->
                    if (holder.adapterPosition < adapter.data.size && holder.adapterPosition >= 0) {
                        setEventData(it, adapter.data[holder.adapterPosition], this)
                    }
                }

            }

            registerEventBus(
                this@BindingBaseItemProvider.hashCode()
                    .toString() + "-registerEventBus-" + holder.adapterPosition,
                UI, holder.adapterPosition
            )
        }
    }

    abstract fun setEventData(it: EV, item: T, viewBinding: VB)


    //兼容注册多个EventData
    open fun registerEventBus(name: String, eventDispatcher: CoroutineDispatcher, pos: Int) {

    }

    abstract fun onViewRecycled(holder: BaseViewHolder)


    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        KotlinBus.unregister(this.hashCode().toString() + "-" + holder.adapterPosition)
        super.onViewDetachedFromWindow(holder)
    }

    private fun getGenericClass(): Class<EV> {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments[0] as Class<EV>
    }
}