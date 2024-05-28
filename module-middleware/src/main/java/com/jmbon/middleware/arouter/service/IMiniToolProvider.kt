package com.jmbon.middleware.arouter.service

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/******************************************************************************
 * Description: 小工具模块Provider
 *
 * Author: jhg
 *
 * Date: 2023/4/24
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
interface IMiniToolProvider : IProvider {

    override fun init(context: Context?) {

    }

    /**
     * 跳转到选择首页
     */
    fun toChooseHome(activity: Activity? = null, withFinish: Boolean = false)

    /**
     * 跳转到咨询求助页面
     * @param advisoryId: 咨询id
     */
    fun toAdvisoryHelp(advisoryId: Int = 0, questionId: Int = 0, question: String = "")

    /**
     * 跳转到助孕圈子页面
     * @param title: 助孕圈子标题
     * @param type: 助孕圈子类型
     */
    fun toHelpGroup(title: String, type: Int)
}