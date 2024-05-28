package com.jmbon.middleware.utils

import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.IdentityInfoBean
import com.jmbon.middleware.bean.ItemTypeEnum
import com.jmbon.middleware.config.Constant

/******************************************************************************
 * Description: Banner通用的跳转逻辑
 *
 * Author: jhg
 *
 * Date: 2023/3/29
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object BannerHelper {

    /**
     * Banner的跳转逻辑
     * @param bannerInfo: 当前所点击的Banner信息
     */
    fun onClick(bannerInfo: CommonBanner) {
        when (bannerInfo.item_type) {
            ItemTypeEnum.ITEM_TYPE_COLUMN.value -> {
                ArouterUtils.toArticleColumnDetailActivity(bannerInfo.item_id)
            }

            ItemTypeEnum.ITEM_TYPE_ARTICLE.value -> {
                ArouterUtils.toArticleDetailsActivity(bannerInfo.item_id)
            }

            ItemTypeEnum.ITEM_TYPE_VIDEO.value -> {
                ARouter.getInstance().build("/video/details")
                    .withInt(TagConstant.VIDEO_ID, bannerInfo.item_id)
                    .navigation()
            }

            ItemTypeEnum.ITEM_TYPE_QUESTION.value -> {
                ARouter.getInstance().build("/question/activity/ask_detail")
                    .withInt(TagConstant.QUESTION_ID, bannerInfo.item_id)
                    .navigation()
            }

            ItemTypeEnum.ITEM_AI_TYPE_HELP.value -> {
                ARouter.getInstance().build("/help/detail/activity")
                    .withInt("id", bannerInfo.item_id)
                    .navigation()
            }

            ItemTypeEnum.ITEM_TYPE_HELP.value -> {
                ARouter.getInstance().build("/circle/ask/details")
                    .withInt(TagConstant.QUESTION_ID, bannerInfo.item_id)
                    .navigation()
            }

            ItemTypeEnum.ITEM_TYPE_GROUP_CHAT.value -> {
                ARouter.getInstance().build("/chat/group/info")
                    .withString(TagConstant.GROUP_NUMBER, bannerInfo.identity)
                    .navigation()
            }

            ItemTypeEnum.ITEM_TYPE_SEARCH_SUBJECT.value -> {
                ARouter.getInstance().build("/middleware/search/activity")
                    .withString(TagConstant.SEARCH_CONTENT, "/search/result/fragment")
                    .withBoolean(TagConstant.DIRECT_SEARCH, true)
                    .withString(TagConstant.SEARCH_KEY, bannerInfo.identity)
                    .navigation()
            }

            ItemTypeEnum.ITEM_TYPE_WEB.value -> {
                ARouter.getInstance().build("/webview/activity")
                    .withString(
                        "url",
                        bannerInfo.url
                    )
                    .withString("title", bannerInfo.origin_title)
                    .navigation()
            }

            ItemTypeEnum.ITEM_TYPE_CUSTOMIZED_SOLUTION_POPUP.value -> {
                toWxMiniProgram(ActivityUtils.getTopActivity(), 3)
            }

            ItemTypeEnum.ITEM_TYPE_TOAST.value -> {
                if (bannerInfo.identity.isNotEmpty()) {
                    bannerInfo.identity.showToast()
                }
            }

            ItemTypeEnum.ITEM_TYPE_GET_SCHEME.value -> {
                toWxMiniProgram(ActivityUtils.getTopActivity(), 2)
            }

            ItemTypeEnum.ITEM_TYPE_WECHAT_PAGE.value -> {
                try {
                    if (bannerInfo.identity.contains("-|-")) {
                        val arr = bannerInfo.identity.split("-|-")
                        toWxMiniProgram(
                            ActivityUtils.getTopActivity(),
                            type = arr[0],
                            groupName = arr[1]
                        )
                    } else {
                        val identityInfo =
                            GsonUtils.fromJson(bannerInfo.identity, IdentityInfoBean::class.java)
                                ?: throw NullPointerException("identityInfo is null")
                        toWxMiniProgram(
                            ActivityUtils.getTopActivity(),
                            type = identityInfo.pageType,
                            groupName = identityInfo.groupName,
                            pregnantStatus = if (identityInfo.needPregnancyType == 1) {
                                Constant.user?.user?.pregnantStatus ?: 0
                            } else {
                                0
                            }
                        )
                    }
                } catch (_: Exception) {
                    toWxMiniProgram(
                        ActivityUtils.getTopActivity(),
                        type = bannerInfo.identity
                    )
                }
            }

            else -> {
                "数据异常,请稍后重试~".showToast()
            }
        }
    }
}