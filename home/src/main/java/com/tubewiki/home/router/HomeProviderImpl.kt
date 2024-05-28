package com.tubewiki.home.router

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.jmbon.middleware.arouter.RouterHub
import com.jmbon.middleware.arouter.service.IHomeProvider
import com.tubewiki.home.dialog.GetSchemeDialog

/******************************************************************************
 * Description: Home组件对外开发的接口实现
 *
 * Author: jhg
 *
 * Date: 2023/3/16
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = RouterHub.HOME_PROVIDER)
class HomeProviderImpl : IHomeProvider {

    override fun init(context: Context?) {

    }

    override fun toChooseCity(isGlobalConfig: Boolean) {
        HomeRouter.toChooseCity(isGlobalConfig)
    }

    override fun showGetSchemeDialog(context: Context, type: Int) {
        GetSchemeDialog(context, type).showDialog()
    }

    override fun toSchemeDetail() {
        HomeRouter.toGetSchemeDetail()
    }

}