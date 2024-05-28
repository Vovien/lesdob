package com.jmbon.middleware.bury.event

/******************************************************************************
 * Description: 页面埋点事件
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
enum class PageEventEnum(val value: String) {

    /**
     * 进入首页
     */
    EVENT_PAGE_ENTER_HOME("EnterHomePage"),

    /**
     * 离开首页
     */
    EVENT_PAGE_LEAVE_HOME("LeaveHomePage"),

    /**
     * 进入接好孕页
     */
    EVENT_PAGE_ENTER_GOOD_PREGNANCY("EnterGoodPregnancyPage"),

    /**
     * 离开接好孕页
     */
    EVENT_PAGE_LEAVE_GOOD_PREGNANCY("LeaveGoodPregnancyPage"),

    /**
     * 进入LesDoB
     */
    EVENT_PAGE_ENTER_WIKI_HOME("EnterEncyclopediasPage"),

    /**
     * 离开LesDoB
     */
    EVENT_PAGE_LEAVE_WIKI_HOME("LeaveEncyclopediasPage"),

    /**
     * 进入回答详情页
     */
    EVENT_PAGE_ENTER_ANSWER_DETAIL("EnterAnswerDetailPage"),

    /**
     * 离开回答详情页
     */
    EVENT_PAGE_LEAVE_ANSWER_DETAIL("LeaveAnswerDetailPage"),

    /**
     * 进入文章详情页
     */
    EVENT_PAGE_ENTER_ARTICLE_DETAIL("EnterArticleDetailPage"),

    /**
     * 离开文章详情页
     */
    EVENT_PAGE_LEAVE_ARTICLE_DETAIL("LeaveArticleDetailPage"),

    /**
     * 进入经验详情页
     */
    EVENT_PAGE_ENTER_EXPERIENCE_DETAIL("EnterExperienceDetailPage"),

    /**
     * 离开经验详情页
     */
    EVENT_PAGE_LEAVE_EXPERIENCE_DETAIL("LeaveExperienceDetailPage"),

    /**
     * 进入视频广告详情页##
     */
    EVENT_PAGE_ENTER_VIDEO_AD_DETAIL("EnterVideoAdDetailPage"),

    /**
     * 离开视频广告详情页##
     */
    EVENT_PAGE_LEAVE_VIDEO_AD_DETAIL("LeaveVideoAdDetailPage"),

    /**
     * 进入AI咨询对话页##
     */
    EVENT_PAGE_ENTER_AI_ADVISORY("EnterAIConsultDetailPage"),

    /**
     * 离开AI咨询对话页##
     */
    EVENT_PAGE_LEAVE_AI_ADVISORY("LeaveAIConsultDetailPage"),

    /**
     * 进入助孕专题详情页##
     */
    EVENT_PAGE_ENTER_HELP_PREGNANT_SUBJECT("EnterPregnancyTopicDetailPage"),

    /**
     * 离开助孕专题详情页##
     */
    EVENT_PAGE_LEAVE_HELP_PREGNANT_SUBJECT("LeavePregnancyTopicDetailPage"),

    /**
     * 进入医院专题详情页##
     */
    EVENT_PAGE_ENTER_HOSPITAL_SUBJECT("EnterHospitalTopicDetailPage"),

    /**
     * 离开医院专题详情页##
     */
    EVENT_PAGE_LEAVE_HOSPITAL_SUBJECT("LeaveHospitalTopicDetailPage"),

    /**
     * 进入搜索专题详情页
     */
    EVENT_PAGE_ENTER_SEARCH_SUBJECT("EnterSearchTopicDetailPage"),

    /**
     * 离开搜索专题详情页
     */
    EVENT_PAGE_LEAVE_SEARCH_SUBJECT("LeaveSearchTopicDetailPage"),

    /**
     * 进入海外专题详情页##
     */
    EVENT_PAGE_ENTER_OVERSEAS_SUBJECT("EnterOverseasTopicDetailPage"),

    /**
     * 离开海外专题详情页##
     */
    EVENT_PAGE_LEAVE_OVERSEAS_SUBJECT("LeaveOverseasTopicDetailPage"),

    /**
     * 进入定制方案中间页##
     */
    EVENT_PAGE_ENTER_CUSTOM_MIDDLE("EnterPlanMiddlePage"),

    /**
     * 进入定制方案结果页##
     */
    EVENT_PAGE_ENTER_CUSTOM_RESULT("EnterPlanResultPage"),

    /**
     * 进入医生中间页##
     */
    EVENT_PAGE_ENTER_DOCTOR_MIDDLE("EnterDoctorMiddlePage"),

    /**
     * 进入海外就医中间页##
     */
    EVENT_PAGE_ENTER_OVERSEAS_MEDICAL_MIDDLE("EnterOverseasMedicalMiddlePage"),

    /**
     * 进入群聊引导中间页
     */
    EVENT_PAGE_ENTER_CHAT_GUIDE("EnterGroupGuideMiddlePage"),

    /**
     * 进入群聊引导详情页##
     */
    EVENT_PAGE_ENTER_GROUP_CHAT_GUIDE_DETAIL("EnterGroupGuideDetailPage"),

    /**
     * 进入生育力自测结果页
     */
    EVENT_PAGE_ENTER_FERTILITY_TEST("EnterFertilitySelfTestResultPage"),

    /**
     * 进入试管自测结果页
     */
    EVENT_PAGE_ENTER_TEST_TUBE_TEST("EnterTestTubeSelfTestResultPage"),

    /**
     * 离开全局搜索页
     */
    EVENT_PAGE_LEAVE_SEARCH("LeaveGlobalSearchPage"),

    /**
     * 统计搜索贡献度, 点击搜索结果页中的内容时统计, 为了简化操作, 从当前页面跳出时统计
     */
    EVENT_PAGE_SEARCH_DEVOTE("EventGlobalSearchDevote"),

    /**
     * 进入消息页
     */
    EVENT_PAGE_ENTER_MSG("EnterMessagePage"),

    /**
     * 进入消息页
     */
    EVENT_PAGE_LEAVE_MSG("LeaveMessagePage"),

    /**
     * 进入我的页面
     */
    EVENT_PAGE_ENTER_MINE("EnterMyPage"),

    /**
     * 离开我的页面
     */
    EVENT_PAGE_LEAVE_MINE("LeaveMyPage"),

    /**
     * 进入问题详情页##
     */
    EVENT_PAGE_ENTER_QUESTION_DETAIL("EnterQuestionDetailPage"),

    /**
     * 离开问题详情页##
     */
    EVENT_PAGE_LEAVE_QUESTION_DETAIL("LeaveQuestionDetailPage"),
}