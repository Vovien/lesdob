package com.jmbon.middleware.config

/**
 * @author : leimg
 * time   : 2021/4/19
 * desc   : MMKV sp 本地存储常量key
 * version: 1.0
 */
object MMKVKey {

//    //当前用户怀孕状态 拼接 uid
//    val USER_PREGNANT_STATUS_KEY = "USER_PREGNANT_STATUS_" + Constant.getUserId()
//
//    //当前用户是否是第一次打开悬赏窗口 拼接 uid
//    val USER_REWARD_RULE_STATUS_KEY = "USER_REWARD_RULE_STATUS_"
//
//    //当前用户是否是第一次打开采纳窗口 拼接 uid
//    val USER_ACCEPT_RULE_STATUS_KEY = "USER_ACCEPT_RULE_STATUS_"
//
//
//    //当前用户是否是采纳不再提示 拼接 uid
//    val USER_ACCEPT_RESULT_STATUS_KEY = "USER_ACCEPT_RESULT_STATUS"// + Constant.userInfo?.uid ?: ""
//
//
//    //本地提问草稿key
//    val DRAFT_QUESTION_KEY = "ASK_QUESTION_"
//
//    //收藏按钮是否被点击过
//    val ANSWER_COLLECTED_KEY = "ANSWER_COLLECTED_"

    val DEVICE_ID_KEY = "device_id_key"
    val UUID_ID_KEY = "uuid_id_key"


    //当前用户是否是第一次禁言用户  拼接 uid
    val USER_LIMIT_SEND_MESSAGE_STATUS_KEY =
        "USER_LIMIT_SEND_MESSAGE_STATUS_"
//
//    val HOT_PUSH = "hot_push"
//
//    //同步设备激活
//    val ACTIVATE_DEVICE = "activate_device"

    // 第一次进入 APP
    val FIRST_ENTER = "FIRST_ENTER"

    //同步设备激活
    val ACTIVATE_DEVICE = "activate_device"


    //页面隐私提示TAG
    //快捷登录页面
//    val PRIVATE_QUICK_LOGIN_PAGE = "private_quick_login_page"

    //登录页面
//    val PRIVATE_LOGIN_PAGE = "private_login_page"

    //一键授权登录页面
    val PRIVATE_ONE_KEY_LOGIN_PAGE = "private_one_key_login_page"

    //密码登录登录页面
    val PRIVATE_PASSWORD_LOGIN_PAGE = "private_password_login_page"


    //分享功能
    //视频分享提示
    val SHARE_VIDEO_TIPS = "share_video_tips"

    //回答分享提示
    val SHARE_ANSWER_TIPS = "share_answer_tips"

    //文章分享提示
    val SHARE_ARTICLE_TIPS = "share_article_tips"

    //小程序分享提示
    val SHARE_TOOL_TIPS = "share_tools_tips"

    //小工具提示
    val CIRCLE_TOOLS_TIPS = "circle_tools_tips"


    //第一封信阅读
    val FIRST_LETTR_READ = "first_letter_read"
    val FIRST_LETTR_INTO = "first_letter_into"

    //第一封信阅读
    val BACKGROUND_IMAGE = "background_image"


    //选择定位地址
    val APP_LOCATION = "app_location"

    //修改推荐反馈提交
    val RECOMMEND_FEEDBACK = "recommend_feedback"
    val LAST_LOCATION = "latest_location"

    // 方案配置信息
    val SCHEME_CONFIG = "scheme_config"

    // 通用配置
    val COMMON_CONFIG = "common_config"

    // 是否实时上传埋点数据
    val REALTIME_UPLOAD = "realtime_upload"

    // 每次上传指定条数的埋点数据
    val BURY_UPLOAD_COUNT = "bury_upload_count"

    // 是否为上传指定条数的埋点数据
    val UPLOAD_SPECIFIC_COUNT = "upload_specific_count"

    val OAID = "oaid"

    /**
     * OAID是否上报成功
     */
    val OAID_UPLOAD_SUCCESS = "oaid_upload_success"
}