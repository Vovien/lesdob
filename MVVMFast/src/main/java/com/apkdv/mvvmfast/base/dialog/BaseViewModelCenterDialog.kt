package com.apkdv.mvvmfast.base.dialog

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.showToast
import java.lang.reflect.ParameterizedType

/**
 * time   : 2021/4/29
 * desc   : 改造CenterPopupView  使用 viewbinding
 * version: 1.0
 */
abstract class BaseViewModelCenterDialog<VM : BaseViewModel, VB : ViewBinding>(context: Context) :
    BaseCenterDialog<VB>(context), ViewModelStoreOwner {

    private val mViewModelStore by lazy { ViewModelStore() }

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

    override fun onCreate() {
        super.onCreate()
        createViewModel()
        registerDefUIChange(viewModel)
    }

    override fun getViewModelStore(): ViewModelStore {
        return mViewModelStore
    }


    /**
     * 注册 UI 事件
     */
    protected fun registerDefUIChange(viewModel: BaseViewModel) {
        if (context is LifecycleOwner) {
            viewModel.defUI.showDialog.observe(context as LifecycleOwner) {
                if (context is AppBaseActivity<*>)
                    (context as AppBaseActivity<*>).showLoading()
            }
            viewModel.defUI.dismissDialog.observe(context as LifecycleOwner) {
                if (context is AppBaseActivity<*>)
                    (context as AppBaseActivity<*>).dismissLoading()
            }
            viewModel.defUI.toastEvent.observe(context as LifecycleOwner) {
                it.showToast()
            }

        }

        }

    }

