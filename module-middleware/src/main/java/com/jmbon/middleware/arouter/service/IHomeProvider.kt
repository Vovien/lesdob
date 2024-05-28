package com.jmbon.middleware.arouter.service

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.facade.template.IProvider
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.jmbon.middleware.bean.QuestionMultiData

/******************************************************************************
 * Description: 首页模块的 Provider
 *
 * Author: jhg
 *
 * Date: 2023/3/16
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
interface IHomeProvider: IProvider {

    /**
     * 跳转到选择城市页面
     * @param isGlobalConfig: 是否需要全局生效
     */
    fun toChooseCity(isGlobalConfig: Boolean = true)

    /**
     * 显示获取方案弹框
     */
    fun showGetSchemeDialog(context: Context, type: Int)

    /**
     * 跳转到方案详情
     */
    fun toSchemeDetail()

}