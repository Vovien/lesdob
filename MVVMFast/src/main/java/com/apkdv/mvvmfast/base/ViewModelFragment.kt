package com.apkdv.mvvmfast.base

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 *   time   : 2019/11/01
 */
abstract class ViewModelFragment<VM : BaseViewModel, VB : ViewBinding> : AppBaseFragment<VB>() {
    protected lateinit var viewModel: VM

    override fun initViewModel() {
        createViewModel()
        lifecycle.addObserver(viewModel)
        //注册 UI事件
        registerUIChange(viewModel)
    }
    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(tClass) as VM
        }
    }


    protected fun registerUIChange(viewModel: BaseViewModel) {
        registerDefUIChange(viewModel)
        registerLayoutChange(viewModel)
    }
}