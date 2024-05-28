package com.apkdv.mvvmfast.arouter

import com.alibaba.android.arouter.facade.template.IProvider

interface IUserActionService : IProvider {
    fun userIsLogin():Boolean

    fun cleanLoginUser()
}