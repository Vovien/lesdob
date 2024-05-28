package com.apkdv.mvvmfast.base.dialog

import androidx.fragment.app.FragmentActivity
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
 * @author : wangzhen
 * time   : 2021/3/26
 * desc   : 改造BottomPopupView  使用 viewbinding
 * version: 1.0
 */
abstract class BaseViewModelBottomDialog<VM : BaseViewModel, VB : ViewBinding>(
    val activity: FragmentActivity
) :
    BaseBottomDialog<VB>(activity),
    ViewModelStoreOwner {

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
        viewModel.defUI.showDialog.observe(activity) {
            if (activity is AppBaseActivity<*>)
                activity.showLoading()
        }
        viewModel.defUI.dismissDialog.observe(activity) {
            if (activity is AppBaseActivity<*>)
                activity.dismissLoading()
        }
        viewModel.defUI.toastEvent.observe(activity) {
            it.showToast()
        }

    }

}