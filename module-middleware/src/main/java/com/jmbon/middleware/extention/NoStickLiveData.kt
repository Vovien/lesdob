package com.jmbon.middleware.extention

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * 解决LiveData的数据倒灌问题
 */
class NoStickLiveData<T>(private var isStickEvent: Boolean = false) : MutableLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (isStickEvent) {
            super.observe(owner, observer)
        } else {
            super.observe(owner, CustomObserver(observer, value))
        }
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(observer)
    }

    /**
     * 自定义观察者
     */
    class CustomObserver<T>(var observer: Observer<in T>, var initValue: T?) : Observer<T> {

        override fun onChanged(t: T) {
            // 如果监听之前已有初始值则忽略
            if (initValue != null && initValue === t) {
                initValue = null
                return
            }
            observer.onChanged(t)
        }
    }
}