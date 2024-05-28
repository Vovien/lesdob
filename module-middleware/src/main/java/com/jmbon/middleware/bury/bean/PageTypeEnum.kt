package com.jmbon.middleware.bury.bean

/******************************************************************************
 * Description: 页面类型
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
enum class PageTypeEnum(val value: String) {

    /**
     * 首页
     */
    PAGE_TYPE_HOME("home"),

    /**
     * 接好孕-经验
     */
    PAGE_TYPE_PREGNANCY_EXPERIENCE("good_pregnancy_experience"),

    /**
     * 接好孕-问答
     */
    PAGE_TYPE_PREGNANCY_QA("good_pregnancy_answer"),

    /**
     * LesDoB页
     */
    PAGE_TYPE_WIKI_HOME("encyclopedias"),

    /**
     * 定制中间页
     */
    PAGE_TYPE_CUSTOM_MIDDLE("plan_middle"),

    /**
     * 群聊引导中间页
     */
    PAGE_TYPE_GROUP_CHAT_GUIDE_MIDDLE("group_guide_middle"),

    /**
     * 海外就医中间页
     */
    PAGE_TYPE_OVERSEAS_MEDICAL_MIDDLE("overseas_medical_middle"),
}