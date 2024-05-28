package com.jmbon.middleware.utils;


public class TagConstant {

    public static final String FROM_SEARCH = "from_search";
    public static final String ADV_ID = "adv_id";

    // 是否需要登录
    public static final String NEED_LOGIN_KEY = "need_login";
    // 是否需要弹出第三方免责声明
    public static final String NEED_EXEMPTION_DIALOG = "need_exemption_dialog";
    // 每日更新的更新时间
    public static final String UPDATED_DAILY_TIME = "updated_daily_time";

    //是否需要回到主页
    public static final String NEED_BACK_HOME = "isBackHome";
    //类型区分
    public static final String TYPE = "type";
    public static final String IS_CHECK = "is_check";
    // 是否系统消息
    public static final String IS_SYSTEM_NOTICE = "is_system_notice";
    // 页面的标题
    public static final String PAGE_TITLE = "page_title";

    //过滤功能
    public static final String FILTER_FUNCTION = "filter_function";

    // 我的界面类型
    public static final String PAGE_TYPE = "pageType";
    //来自我的个人界面
    public static final String FROM_MINE_PERSON_PAGE = "from_mine_person_page";

    //问题id
    public static final String QUESTION_ID = "question_id";
    //问题check id
    public static final String QUESTION_CHECK_ID = "check_id";

    //审核问题id
    public static final String EXAMINE_ID = "examine_id";

    //来源回答详情
    public static final String PAGE_FROM_ANSWER = "is_from_answer";
    //来源问题详情
    public static final String PAGE_FROM_QUESTION = "is_from_question";
    //话题分类id
    //话题、专栏分类id
    public static final String CATEGORY_ID = "category_id";
    // 分类名字
    public static final String CATEGORY_NAME = "category_name";

    //话题id
    public static final String TOPIC_ID = "topic_id";
    //文章id
    public static final String ARTICLE_ID = "article_id";
    //文章内容分
    public static final String ARTICLE_CONTENT = "article_content";

    //医生id
    public static final String DOCTOR_ID = "doctor_id";
    //医院id
    public static final String HOSPITAL_ID = "hospital_id";

    // 分类 ID
    public static final String COLUMN_ID = "column_id";
    // 搜索类型
    public static final String SEARCH_TYPE = "search_type";

    //问题
    public static final String QUESTION_DATA = "question_data";
    //答案
    public static final String ANSWER_DATA = "answer_data";
    //答案id
    public static final String ANSWER_ID = "answer_id";
    //POSITION
    public static final String POSITION = "POSITION";


    // 搜索专栏内的文章
    public static final int SEARCH_ARTICLE_IN_COLUMN = 1;
    // 搜专栏
    public static final int SEARCH_COLUMN = 2;
    // 搜专栏
    public static final int SEARCH_TOPIC = 4;
    // 搜作者
    public static final int SEARCH_CREATOR = 3;
    // 搜待回答问题
    public static final int SEARCH_WAIT_ANSWER = 6;
    // 搜索话题里的问题
    public static final int SEARCH_TOPIC_CONTENT = 5;

    // 搜问答
    public static final int SEARCH_ANSWER = 7;
    //搜文章
    public static final int SEARCH_ARTICLE = 8;
    //搜求助问答
    public static final int SEARCH_HELP_QUESTION = 9;

    //搜分类内的求助问答
    public static final int SEARCH_CATEGORY_HELP_QUESTION = 10;

    // 搜索 关键字
    public static final String SEARCH_KEY = "search_key";

    //局部刷新type
    public static final int UPDATE_ACCEPT = 100;
    public static final int UPDATE_FOCUS = 101;
    public static final int UPDATE_USER_FOCUS = 102;
    public static final int SCAN_RESULT_CODE = 0x00123;
    //
    public static final int UPDATE_PICTURE_RESULT = 0x00132;


    //是否展示评论dialog
    public static final String SHOW_COMMENT = "show_comment";
    public static final String NEED_TOP_ID = "need_top_id";

    //通用参数Tag
    public static final String PARAMS = "params";
    //通用参数Tag
    public static final String PARAMS2 = "params2";
    //通用参数Tag
    public static final String PARAMS3 = "params3";

    public static final String PARAMS4 = "params4";
    public static final String PARAMS5 = "params5";
    public static final String PARAMS6 = "params6";
    public static final String INDEX = "index";
    public static final String BTN_PADDING = "btn_padding";

    public static final String SEARCH_BOX = "search_box";
    // 全部金额
    public static final String FULL_AMOUNT = "full_amount";
    // group id
    public static final String GROUP_ID = "group_id";
    // circle id
    public static final String CIRCLE_ID = "circle_id";
    // group_number
    public static final String GROUP_NUMBER = "group_number";

    // 跳过版本
    public static final String APP_VERSION = "app_version";
    // 返回结果
    public static final String SCAN_RESULT = "scan_result";
    // 用户 UID
    public static final String USER_UID = "user_uid";
    public static final String USER_CANCEL = "user_cancel";
    public static final String CIRCLE_NICK = "circle_nick";

    // 用户 UID
    public static final String IS_DOCTOR = "is_doctor";

    public static final String SEARCH_CONTENT = "search_content";
    // 搜索页面，没有开始搜索时的状态
    public static final String SEARCH_INDEX = "search_index";

    public static final String SEARCH_INDEX_BUNDLE = "search_index_bundle";
    public static final String SEARCH_CONTENT_BUNDLE = "search_content_bundle";

    public static final String NOTIFY = "notify";
    //申诉的 iD
    public static final String ITEM_ID = "item_id";
    //是否需要返回键
    public static final String NEED_BACK = "need_back";
    public static final String NEED_REFRESH = "need_refresh";
    // 直接搜索
    public static final String DIRECT_SEARCH = "direct_search";

    // 隐藏输入方式
    public static final String CAN_SHOW_KEYBOARD = "can_show_keyboard";
    // 路径
    public static final String IMAGE_PATH = "image_path";
    // 今日热点
    public static final String TODAY_HOT = "today_hot";
    // 首页视频静音
    public static final String VOICE_MUTE = "voice_mute";
    // 回答视频静音
    public static final String ANSWER_VOICE_MUTE = "answer_voice_mute";

    //视频相关
    public static final String VIDEO_URL = "video_url";
    public static final String VIDEO_TITLE = "video_title";
    public static final String VIDEO_IMAGE = "video_image";
    public static final String VIDEO_DESCRIPTION = "video_description";
    public static final String VIDEO_ID = "video_id";
    public static final String VIDEO_TYPE = "video_type";
    public static final String VIDEO_COLLECTION_SEARCH_KEY = "video_collection_search_key";
    public static final String VIDEO_IS_ALBUM = "video_is_album";
    public static final String VIDEO_DATA = "video_data";
    public static final String VIDEO_ALIYUN_ID = "aliyun_video_id";

    public static final String VIDEO_FROM_HOME = "video_from_home";
    public static final String VIDEO_FROM_COLLECTION = "video_from_collection";
    public static final String VIDEO_FROM_HOME_POSITION = "video_from_home_position";

    public static final String VIDEO_CATEGORY_IMAGE = "video_category_image";

    public static final String FROM_JMB = "from_jmbon";

}
