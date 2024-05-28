package com.apkdv.mvvmfast.ktx

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelProvider.Factory
import kotlin.reflect.KClass

@MainThread
inline fun <reified VM : ViewModel> Fragment.activityViewModels(
    noinline factoryProducer: (() -> Factory)? = null
): Lazy<VM> = createViewModelLazy(
    VM::class, { requireActivity().viewModelStore },
    factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
)

@MainThread
fun <VM : ViewModel> Fragment.createViewModelLazy(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(viewModelClass, storeProducer, factoryPromise)
}