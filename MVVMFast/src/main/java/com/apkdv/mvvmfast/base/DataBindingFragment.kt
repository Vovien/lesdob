package com.apkdv.mvvmfast.base

import androidx.databinding.ViewDataBinding

abstract class DataBindingFragment<VM : BaseViewModel, DB : ViewDataBinding> : ViewModelFragment<VM, DB>()