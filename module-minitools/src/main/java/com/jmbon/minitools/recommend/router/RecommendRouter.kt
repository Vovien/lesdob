package com.jmbon.minitools.recommend.router

import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.utils.navigationWithFinish
import com.jmbon.minitools.recommend.bean.GuideButtonStyleItemBean
import com.jmbon.minitools.recommend.bean.ItemRecommendBean
import com.jmbon.minitools.recommend.bean.ItemTypeBean

/******************************************************************************
 * Description: 个性化推荐模块的路由
 *
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object RecommendRouter {

    /**
     * 选择首页
     */
    const val RECOMMEND_CHOOSE_HOME = "/recommend/chooseHome"

    /**
     * 选择容器页
     */
    const val RECOMMEND_CHOOSE_CONTAINER = "/recommend/chooseContainer"

    /**
     * 推荐结果
     */
    const val RECOMMEND_RESULT = "/recommend/result"

    /**
     * 我要试管引导页
     */
    const val MINI_TOOL_TUBE_GUIDE = "/miniTool/tubeGuide"

    /**
     * 跳转到选择首页
     */
    fun toChooseHome() {
        ARouter.getInstance().build(RECOMMEND_CHOOSE_HOME).navigation()
    }

    /**
     * 跳转到选择页面
     */
    fun toChooseContainer(formInfo: ItemTypeBean?) {
        ARouter.getInstance()
            .build(RECOMMEND_CHOOSE_CONTAINER)
            .withParcelable("formInfo", formInfo)
            .navigation()
    }

    /**
     * 跳转到推荐结果页
     */
    fun toRecommendResult(activity: Activity?, recommendList: ArrayList<ItemRecommendBean>) {
        ARouter.getInstance().build(RECOMMEND_RESULT)
            .withParcelableArrayList("recommendList", recommendList)
            .navigationWithFinish(activity)
    }

    /**
     * 跳转到我要试管引导页
     * @param guidePictureUrl: 引导图片
     * @param buttonStyleList: 按钮样式
     */
    fun toTubeGuide(guidePictureId: Int, guidePictureUrl: String? = "", buttonStyleList: ArrayList<GuideButtonStyleItemBean>? = null) {
        ARouter.getInstance().build(MINI_TOOL_TUBE_GUIDE)
            .withInt("guidePictureId", guidePictureId)
            .withString("guidePictureUrl", guidePictureUrl)
            .withParcelableArrayList("buttonStyleList", buttonStyleList)
            .navigation()
    }
}

enum class RequirementTypeEnum(val value: Int) {
    /**
     * 我要试管
     */
    REQUIREMENT_TYPE_TEST_TUBE(1),

    /**
     * 我要怀孕
     */
    REQUIREMENT_TYPE_PREGNANT(2),

    /**
     * 我要保胎
     */
    REQUIREMENT_TYPE_PROTECT_BABY(3),
}