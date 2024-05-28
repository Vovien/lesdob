package com.jmbon.middleware.extention

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @Description：Context的扩展方法
 * @Author： jhg
 * @Time： 2022/3/17
 * @Version 1.0.0
 */
/**
 * 通过Context获取ViewModel
 */
fun <T: ViewModel> Context?.getViewModel(classX: Class<T>): T? {
    return if (this is FragmentActivity) {
        ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(classX)
    } else {
        try {
            classX.newInstance()
        } catch (e: Exception) {
            null
        }
    }
}


fun Any?.getLifecycleOwner(): LifecycleOwner? {
    if (this is FragmentActivity) {
        return this
    } else if (this is Fragment) {
        return this
    }

    return null
}
/**
 * 通过LifecycleOwner获取ViewModel
 */
fun <T: ViewModel> LifecycleOwner?.getViewModel(classX: Class<T>): T? {
    return when (this) {
        is FragmentActivity -> {
            ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(classX)
        }
        is Fragment -> {
            ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(classX)
        }
        else -> {
            try {
                classX.newInstance()
            } catch (e: Exception) {
                null
            }
        }
    }
}

/**
 * 根据View获取所在容器的ViewModel
 * @param classX 需要获取的ViewModel实例
 */
fun <T: ViewModel> View.getViewModel(classX: Class<T>): T? {
    return findViewTreeLifecycleOwner().getViewModel(classX)
}

fun Context?.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.getActivity()
        else -> null
    }
}