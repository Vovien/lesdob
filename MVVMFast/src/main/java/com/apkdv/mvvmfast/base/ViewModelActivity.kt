package com.apkdv.mvvmfast.base

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.showToast
import java.lang.reflect.ParameterizedType

/**
 *   @auther : lengyue
 *   time   : 2019/11/01
 */
abstract class ViewModelActivity<VM : BaseViewModel, VB : ViewBinding> : AppBaseActivity<VB>() {


    override fun initViewModel() {
        createViewModel()
        lifecycle.addObserver(viewModel)
        registerUIChange(viewModel)
    }

    protected lateinit var viewModel: VM

    /**
     * 通过反射的方式自动创建 ViewModel
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    protected fun createViewModel() {
        //返回表示此 Class所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
        // 使用反射技术得到T的真实类型
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            //返回表示此类型实际类型参数的 Type对象的数组。
            // 获取第一个类型参数的真实类型
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(tClass) as VM
        }
    }

    protected fun registerUIChange(viewModel: BaseViewModel) {
        registerDefUIChange(viewModel)
        registerLayoutChange(viewModel)
    }

    /**
     * 注册 UI 事件
     */
    protected fun registerDefUIChange(viewModel: BaseViewModel) {
        viewModel.defUI.showDialog.observe(this, {
            showLoading()
        })
        viewModel.defUI.dismissDialog.observe(this, {
            dismissLoading()
        })
        viewModel.defUI.toastEvent.observe(this, {
            it.showToast()
        })
        viewModel.defUI.msgEvent.observe(this, {
            handleEvent(it)
        })
    }


    /**
     * 注册 UI 事件
     */
    protected fun registerLayoutChange(viewModel: BaseViewModel) {
        viewModel.defLayout.showContent.observe(this, {
            showContentState()
        })
        viewModel.defLayout.showEmpty.observe(this, {
            showEmptyState()
        })
        viewModel.defLayout.showError.observe(this, {
            showErrorState()
        })
        viewModel.defLayout.showLoading.observe(this, {
            showLoadingState()
        })
        viewModel.defLayout.showNoNet.observe(this, {
            showNoNetState()
        })
        viewModel.defLayout.showErrorMsg.observe(this, {
            showErrorState(it)
        })
    }

    override fun initData() {

    }

    override fun getData() {

    }
}