package com.jmbon.middleware.bury.event

/******************************************************************************
 * Description: 点击埋点事件
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
enum class ClickEventEnum(val value: String) {

    /**
     * 点击接好孕经验信息流广告
     */
    EVENT_CLICK_GOOD_PREGNANCY_EXPERIENCE_AD("EventGoodPregnancyExperienceInfoFlowAd"),

    /**
     * 点击接好孕问答信息流广告
     */
    EVENT_CLICK_GOOD_PREGNANCY_QA_AD("EventGoodPregnancyAnswerInfoFlowAd"),

    /**
     * 点击回答详情页创意广告
     */
    EVENT_CLICK_QA_DETAIL_AD("EventAnswerDetailCaseAd"),

    /**
     * 点击回答详情页右侧悬浮广告
     */
    EVENT_CLICK_QA_DETAIL_FLOATING_AD("EventAnswerDetailRightFixAd"),

    /**
     * 点击文章详情页创意广告
     */
    EVENT_CLICK_ARTICLE_DETAIL_AD("EventArticleDetailCaseAd"),

    /**
     * 点击文章详情页右侧悬浮广告
     */
    EVENT_CLICK_ARTICLE_DETAIL_FLOATING_AD("EventArticleDetailRightFixAd"),

    /**
     * 点击经验详情页创意广告
     */
    EVENT_CLICK_EXPERIENCE_DETAIL_AD("EventExperienceDetailCaseAd"),

    /**
     * 点击经验详情页右侧悬浮广告
     */
    EVENT_CLICK_EXPERIENCE_DETAIL_FLOATING_AD("EventExperienceDetailRightFixAd"),

    /**
     * 点击视频广告详情页广告入口
     */
    EVENT_CLICK_VIDEO_AD_DETAIL_AD("EventVideoAdDetailCommonAd"),

    /**
     * 点击AI咨询对话页底部顾问
     */
    EVENT_CLICK_AI_ADVISORY_BOTTOM_COUNSELOR("EventAIConsultDetailBottomConsultant"),

    /**
     * 点击医院专题详情页医院电话
     */
    EVENT_CLICK_HOSPITAL_SUBJECT_HOSPITAL_PHONE("EventHospitalTopicDetailHospitalPhone"),

    /**
     * 点击医院专题详情页底部微信
     */
    EVENT_CLICK_HOSPITAL_SUBJECT_BOTTOM_WECHAT("EventHospitalTopicDetailBottomWeChat"),

    /**
     * 点击医院专题详情页案例广告
     */
    EVENT_CLICK_HOSPITAL_SUBJECT_CASE_AD("EventHospitalTopicDetailCaseAd"),

    /**
     * 点击海外专题详情页就医咨询
     */
    EVENT_CLICK_OVERSEAS_SUBJECT_CONSULT("EventOverseasTopicDetailConsult"),

    /**
     * 点击海外专题详情页案例
     */
    EVENT_CLICK_OVERSEAS_SUBJECT_CASE("EventOverseasTopicDetailCaseAd"),

    /**
     * 点击海外专题详情页生育特殊需求
     */
    EVENT_CLICK_OVERSEAS_SUBJECT_FERTILITY_REQUIREMENT("EventOverseasTopicDetailFertilityNeeds"),

    /**
     * 点击海外专题详情页底部微信
     */
    EVENT_CLICK_OVERSEAS_SUBJECT_BOTTOM_WECHAT("EventOverseasTopicDetailBottomWeChat"),

    /**
     * 点击海外专题详情页底部电话
     */
    EVENT_CLICK_OVERSEAS_SUBJECT_BOTTOM_PHONE("EventOverseasTopicDetailBottomPhone"),

    /**
     * 击定制方案中间页创意广告
     */
    EVENT_CLICK_CUSTOM_MIDDLE_AD("EventPlanMiddleCaseAd"),

    /**
     * 点击定制方案结果页底部微信
     */
    EVENT_CLICK_CUSTOM_RESULT_BOTTOM_WECHAT("EventPlanResultBottomConsultant"),

    /**
     * 点击医生中间页底部微信
     */
    EVENT_CLICK_DOCTOR_MIDDLE_BOTTOM_WECHAT("EventDoctorMiddleBottomWeChat"),

    /**
     * 点击海外就医中间页推荐案例
     */
    EVENT_CLICK_OVERSEAS_MEDICAL_MIDDLE_CASE("EventOverseasMedicalMiddleCaseAd"),

    /**
     * 点击海外就医中间页底部微信
     */
    EVENT_CLICK_OVERSEAS_MEDICAL_MIDDLE_BOTTOM_WECHAT("EventOverseasMedicalMiddleBottomWeChat"),

    /**
     * 点击海外就医中间页底部电话
     */
    EVENT_CLICK_OVERSEAS_MEDICAL_MIDDLE_BOTTOM_PHONE("EventOverseasMedicalMiddleBottomPhone"),

    /**
     * 点击群聊引导中间页添加顾问
     */
    EVENT_CLICK_GROUP_GUIDE_ADD_COUNSELOR("EventGroupGuideMiddleConsultant"),

    /**
     * 点击群聊引导详情页底部微信
     */
    EVENT_CLICK_GROUP_CHAT_DETAIL_BOTTOM_WECHAT("EventGroupGuideDetailBottomWeChat"),

    /**
     * 点击生育力自测结果页添加顾问
     */
    EVENT_CLICK_FERTILITY_RESULT_ADD_COUNSELOR("EventFertilitySelfTestResultConsultant"),

    /**
     * 点击试管自测结果页添加顾问
     */
    EVENT_CLICK_TEST_TUBE_RESULT_ADD_COUNSELOR("EventTestTubeSelfTestResultConsultant"),

    /**
     * 触发全局搜索页搜索事件
     */
    EVENT_CLICK_GLOBAL_SEARCH("EventGlobalSearchQuery"),

    /**
     * 点击评论区广告
     */
    EVENT_CLICK_COMMON_AD("EventCommentAd"),

    /**
     * 分享事件
     */
    EVENT_CLICK_SHARE("EventShare"),

    /**
     * 点击首页通用轮播banner
     */
    EVENT_CLICK_COMMON_BANNER("EventHomeCommonCarouselBanner"),

    /**
     * 点击消息页窄条图片
     */
    EVENT_CLICK_MSG_BANNER("EventMessageNarrowBanner"),

    /**
     * 点击我的页窄条图片
     */
    EVENT_CLICK_MINE_BANNER("EventMyNarrowBanner"),

    /**
     * 点击问题详情页通用轮播banner
     */
    EVENT_CLICK_QUESTION_BANNER("EventQuestionDetailCommonCarouselBanner"),

    /**
     * 点击问题详情页右侧悬浮广告
     */
    EVENT_CLICK_QUESTION_RIGHT_FLOATING_AD("EventQuestionDetailRightFixAd"),

    /**
     * 点击文章详情页内容中广告
     */
    EVENT_CLICK_ARTICLE_CONTENT_AD("EventArticleDetailContentAd"),

    /**
     * 点击文章详情页窄条图片
     */
    EVENT_CLICK_ARTICLE_BANNER("EventArticleDetailNarrowBanner"),

    /**
     * 点击经验详情页窄条图片
     */
    EVENT_CLICK_EXPERIENCE_BANNER("EventExperienceDetailNarrowBanner"),

    /**
     * 点击经验详情页底部定制方案
     */
    EVENT_CLICK_EXPERIENCE_BOTTOM_CUSTOM_PLAN("EventExperienceDetailBottomPlan"),

    /**
     * 点击经验详情页内容中广告
     */
    EVENT_CLICK_EXPERIENCE_CONTENT_AD("EventExperienceDetailContentAd"),

    /**
     * 点击接好孕通用轮播banner
     */
    EVENT_CLICK_GOOD_PREGNANCY_BANNER("EventGoodPregnancyCommonCarouselBanner"),

    /**
     * 点击海外就医中间页通用轮播banner
     */
    EVENT_CLICK__OVERSEAS_MEDICAL_BANNER("EventOverseasMedicalMiddleCommonCarouselBanner"),

    /**
     * 点击助孕专题详情页底部定制方案
     */
    EVENT_CLICK_HELP_PREGNANT_SUBJECT_BOTTOM_CUSTOM_PLAN("EventPregnancyTopicDetailBottomPlan"),

    /**
     * 点击助孕专题详情页头部广告组
     */
    EVENT_CLICK_HELP_PREGNANT_SUBJECT_HEADER_AD("EventPregnancyTopicDetailGroupAd"),

    /**
     * 点击生育问题标题
     */
    EVENT_CLICK_FERTILITY_PROBLEM_TITLE("EventFertilityProblemName"),

    /**
     * 点击生育问题导航
     */
    EVENT_CLICK_FERTILITY_PROBLEM_NAV("EventFertilityProblemNav"),

    /**
     * 点击强提醒弹框
     */
    EVENT_CLICK_REMIND_DIALOG("EventStrongReminderPopup"),

    /**
     * 点击医院专题详情页底部免费预约
     */
    EVENT_CLICK_HOSPITAL_SUBJECT_BOTTOM_APPOINTMENT("EventHospitalTopicDetailBottomFreeAppointment"),

    /**
     * 点击医生问诊结果页底部确定
     */
    EVENT_CLICK_DOCTOR_CONSULT_BOTTOM_CONFIRM("EventDoctorConsultationResultBottomConfirm"),

    /**
     * 点击医生中间页底部免费预约
     */
    EVENT_CLICK_DOCTOR_MIDDLE_BOTTOM_FREE_APPOINTMENT("EventDoctorMiddleBottomFreeAppointment"),

    /**
     * 点击群聊详情页底部悬浮工具栏
     */
    EVENT_CLICK_GROUP_CHAT_DETAIL_BOTTOM_FLOATING_TOOL("EventGroupChatDetailBottomFixTool"),

    /**
     * 点击群聊导航页右侧加号加入
     */
    EVENT_CLICK_GROUP_CHAT_NAV_RIGHT_JOIN("EventGroupChatNavRightJoin"),

    /**
     * 点击群聊封面页底部立即加入
     */
    EVENT_CLICK_GROUP_CHAT_COVER_BOTTOM_JOIN("EventGroupChatCoverBottomJoin"),

    /**
     * 点击试管自测结果页底部微信
     */
    EVENT_CLICK_TUBE_TEST_RESULT_BOTTOM_WECHAT("EventTestTubeSelfTestResultBottomWeChat"),

    /**
     * 点击生育力自测结果页底部微信
     */
    EVENT_CLICK_FERTILITY_TEST_RESULT_BOTTOM_WECHAT("EventFertilitySelfTestResultBottomWeChat"),

    /**
     * 点击产检解读结果页底部微信
     */
    EVENT_CLICK_REPORT_RESULT_BOTTOM_WECHAT("EventObstetricInspectionResultBottomWeChat"),

    /**
     * 点击注册转化页底部自定义按钮
     */
    EVENT_CLICK_REGISTER_TRANSFORM_BOTTOM_BUTTON("EventRegistrationConversionBottomCustomButton"),

    /**
     * 点击助孕专题详情页底部微信群聊
     */
    EVENT_CLICK_PREGNANCY_SUBJECT_BOTTOM_WECHAT("EventPregnancyTopicDetailBottomWeChatGroup"),

    /**
     * 点击海外专题详情页底部微信群聊
     */
    EVENT_CLICK_OVERSEAS_SUBJECT_BOTTOM_WECHAT2("EventOverseasTopicDetailBottomWeChatGroup"),

    /**
     * 点击医院专题详情页底部微信群聊
     */
    EVENT_CLICK_HOSPITAL_SUBJECT_BOTTOM_WECHAT2("EventHospitalTopicDetailBottomWeChatGroup"),

    /**
     * 点击常驻发布悬浮弹窗微信群
     */
    EVENT_CLICK_FAB_ADD_WECHAT("EventPermanentPublishFixPopupWeChatGroup"),

    /**
     * 点击常驻发布悬浮弹窗提问发帖
     */
    EVENT_CLICK_FAB_POST_QUESTION("EventPermanentPublishFixPopupQuestionPost"),

    /**
     * 点击常驻发布悬浮弹窗AI咨询
     */
    EVENT_CLICK_FAB_CREATE_AI_ADVISORY("EventPermanentPublishFixPopupAIConsult"),

    /**
     * 点击海外就医中间页底部微信群聊
     */
    EVENT_CLICK_OVERSEAS_MIDDLE_BOTTOM_WECHAT("EventOverseasMedicalMiddleBottomWeChatGroup"),


    /**
     * 点击定制方案中间页好孕方案
     */
    EVENT_PLAN_MIDDLE_GOOD_PREGNANCY_PLAN("EventPlanMiddleGoodPregnancyPlan"),

    /**
     * 点击群聊引导中间页生育群聊
     */
    EVENT_GROUP_GUIDE_MIDDLE_FERTILITY_GROUP("EventGroupGuideMiddleFertilityGroup"),

    /**
     * 点击好孕群聊详情页搜索框
     */
    ENTER_GOOD_PREGNANCY_GROUP_DETAIL_SEARCH_BOX("EnterGoodPregnancyGroupDetailSearchBox"),

    /**
     * 点击好孕群聊详情页群聊
     */
    ENTER_GOOD_PREGNANCY_GROUP_DETAIL_GROUP("EnterGoodPregnancyGroupDetailGroup"),

    /**
     * 点击常驻发布悬浮按钮
     */
    EVENT_PERMANENT_PUBLISH_FIX_BUTTON("EventPermanentPublishFixButton"),

    /**
     * 点击首页助孕工具
     */
    EVENT_HOME_FERTILITY_TOOLS("EventHomeFertilityTools"),

    /**
     * 点击首页获取方案
     */
    EVENT_HOME_GET_PLAN("EventHomeGetPlan"),

    /**
     * 点击首页互助群聊
     */
    EVENT_HOME_MUTUAL_AID_GROUP("EventHomeMutualAidGroup"),

    /**
     * 点击首页互助群聊查看更多
     */
    EVENT_HOME_MUTUAL_AID_GROUP_SEE_MORE("EventHomeMutualAidGroupSeeMore"),

    /**
     * 点击首页金刚区
     */
    EVENT_HOME_QUICK_ACCESS_AREA("EventHomeQuickAccessArea"),

    /**
     * 点击首页试管自测卡片
     */
    EVENT_HOME_TEST_TUBE_SELF_TEST_CARD("EventHomeTestTubeSelfTestCard"),

    /**
     * 点击首页报告解读卡片
     */
    EVENT_HOME_REPORT_INTERPRETATION_CARD("EventHomeReportInterpretationCard"),

    /**
     * 点击首页生育力自测卡片
     */
    EVENT_HOME_FERTILITY_SELF_TEST_CARD("EventHomeFertilitySelfTestCard"),

    /**
     * 点击首页导航栏
     */
    EVENT_HOME_NAV_BAR("EventHomeNavBar"),

    /**
     * 点击首页搜索框
     */
    EVENT_HOME_SEARCH_BOX("EventHomeSearchBox"),

}