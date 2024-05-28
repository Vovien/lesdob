package com.jmbon.minitools.advisory.arouter

import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.minitools.advisory.bean.AdvisoryInfoBean
import com.jmbon.minitools.advisory.bean.AdvisoryItemForm

/******************************************************************************
 * Description: 咨询求助相关的路由
 *
 * Author: jhg
 *
 * Date: 2023/5/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object AdvisoryRouter {

    /**
     * 咨询求助页面
     */
    const val ADVISORY_HELP = "/advisory/home"

    /**
     * 确认咨询信息页面
     */
    const val CONFIRM_ADVISORY_INFO = "/advisory/confirmAdvisoryInfo"

    /**
     * 生成求助引导页
     */
    const val GENERATE_HELP_GUIDE = "/advisory/generateHelpGuide"

    /**
     * 编辑咨询信息
     */
    const val EDIT_ADVISORY_INFO = "/advisory/editAdvisoryInfo"

    /**
     * 修改咨询内容
     */
    const val UPDATE_ADVISORY_CONTENT = "/advisory/updateAdvisoryContent"

    /**
     * 跳转到咨询求助页面
     * @param advisoryId: 咨询id
     */
    fun toAdvisoryHelp(advisoryId: Int = 0, helpId: Int, isViewMode: Boolean = false) {
        ARouter.getInstance().build(ADVISORY_HELP)
            .withInt("advisoryId", advisoryId)
            .withInt("helpId", helpId)
            .withBoolean("isViewMode", isViewMode)
            .navigation()
    }

    /**
     * 跳转到确认咨询信息页面
     */
    fun toConfirmAdvisoryInfo(advisoryInfo: ArrayList<AdvisoryItemForm>?) {
        ARouter.getInstance()
            .build(CONFIRM_ADVISORY_INFO)
            .withParcelableArrayList("advisoryInfo", advisoryInfo)
            .navigation()
    }

    /**
     * 跳转到咨询已解决页面
     */
    fun toGenerateHelpGuide(advisoryInfo: AdvisoryInfoBean?) {
        ARouter.getInstance()
            .build(GENERATE_HELP_GUIDE)
            .withParcelable("advisoryInfo", advisoryInfo)
            .navigation()
    }

    /**
     * 跳转到咨询已解决页面
     */
    fun toEditAdvisoryInfo(activity: Activity, advisoryInfo: AdvisoryInfoBean?) {
        ARouter.getInstance().build(EDIT_ADVISORY_INFO)
            .withParcelable("advisoryInfo", advisoryInfo)
            .navigation(activity, 0x100)
    }

    /**
     * 跳转到修改咨询内容页面
     */
    fun toUpdateAdvisoryContent(activity: Activity, advisoryContent: String?) {
        ARouter.getInstance().build(UPDATE_ADVISORY_CONTENT)
            .withString("advisoryContent", advisoryContent)
            .navigation(activity, 0x101)

    }
}