package com.jmbon.middleware.config

import com.apkdv.mvvmfast.utils.getParcelable
import com.apkdv.mvvmfast.utils.getString
import com.apkdv.mvvmfast.utils.mmkv
import com.apkdv.mvvmfast.utils.saveParcelable
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.CircleUser
import com.jmbon.middleware.bean.FollowUnReadBean
import com.jmbon.middleware.bean.GeneralConfBean
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.bean.WalletSettingData
import com.jmbon.middleware.config.MMKVKey.DEVICE_ID_KEY
import com.jmbon.middleware.config.MMKVKey.UUID_ID_KEY
import com.jmbon.middleware.utils.Color
import com.jmbon.middleware.utils.isNotNullEmpty
import com.lzf.easyfloat.EasyFloat
import java.util.UUID


object Constant {

    const val APP_TYPE = 5
    const val APP_NAME = "les_dob"

    // 进入专题的次数
    var enterArticleColumnDetailCount = 0

    // 进入文章详情的次数
    var enterArticleDetailCount = 0

    var isInfoDetails = false

    val REGEX_EMAIL =
        "^([0-9A-Za-z\\-_\\.]+)@([0-9a-z]+\\.[a-z]{2,6}(\\.[a-z]{2,6})?)\$" //和后端统一邮件验证格式

    val SHI_GUAN_ID = "2021102900000003"

    //腾讯id
    val TENCENT_ID = 1400530835


    // 腾讯IM前缀
    val TIM_PREFIX = if (BuildConfig.DEBUG) "test_user" else "user"

    // 分页数据数量
    val PAGE_SIZE = 12

    //问答需求默认返回40个一组
    val PAGE_QA_SIZE = 40

    // 聊天信息分页数量
    val MESSAGE_SIZE = 20

    // 聊天信息字数限制
    val MESSAGE_CHAR_LENGTH = 550 //600

    // 分页排序方式-默认
    val DEFAULT = "default"

    // 分页排序方式-时间
    val TIME = "time"

    // 分页数据数量
    val MZ_CHANNEL = "meizu"

    // 创建话题最大字符数
    val TOPIC_MAX_NUM = 12


    // 类型
    val TYPE_QUESTION = "question"
    val TYPE_QUESTION_MAIN = "question_main"

    // 针对文章、视频的评论

    // 文章 、问题的评论的普通展示界面
    val TYPE_ARTICLE_MAIN = "article_main"
    val TYPE_ARTICLE = "article"

    // 视频
    val TYPE_VIDEO_MAIN = "video_main"
    val TYPE_VIDEO = "video"

    // KEY
    val USER_INFO = "USER_INFO"
    val USER_ARTICLE = "USER_INFO"

    // 用户登录状态
    val LOGIN_STATUS = "LOGIN_STATUS"
    var PHONE_NUMBER_AUTH = false

    //首页问答列表视频是否静音播放
    var QA_MUTE = true

    //首页回答详情视频是否静音播放
    var ANSWER_MUTE = true

    // 同意隐私协议
    val AGREE_TO_PRIVACY_AGREEMENT = "agree_to_privacy_agreement"

    //是否是第一次进入隐私协议页面
    val FIRST_IN_PRIVACY_AGREEMENT = "first_in_privacy_agreement"


    // 第一次进入文章
    val FIRST_TIME_INTO_ARTICLE = "for_first_time_into_article"


    // 目前处于的groupId,用于判断当前是否处于指定的群聊界面
    var CURRENT_GROUP_ID = 0

    // 是否展示试管婴儿切换弹出,要求在圈子界面不弹出，所以做个判断标识
    var SHOW_TUBE_DIALOG = false

    var USER_SIGN = ""
    var DEVICE_ID = ""
    var imInitSuccess = false
    var isLogin = false
    var currGroupRole = 0
    var walletSetting: WalletSettingData.Data = WalletSettingData.Data()
    var user: UserData = UserData()
    var userInfo: UserData.User = UserData.User()
    var followUnReadBean: FollowUnReadBean = FollowUnReadBean()
    var circleUser: CircleUser? = null
    var trafficTips = false

    var isActiveChannel = false
    var activeChannel: String? = ""


    var unHelpLabels = mutableListOf<GeneralConfBean.Tag>()

    var isCheckPrivacy = false //是否勾选隐私协议

    private const val TAG = "Constant"

    init {
        loaderUser()
        getUUID()
    }

    private fun loaderUser() {
        USER_INFO.getParcelable<UserData>(null)?.let {
            user = it
            if (user.token.isNotNullEmpty() && it.user != null) {
                isLogin = true
                userInfo = it.user
            }
        }
    }

    fun saveUser(user: UserData) {
        LogUtils.iTag("SAVE USER", user)
        isLogin = if (user.token.isNotNullEmpty() && user.user != null) {
            this.user = user
            user.saveParcelable(USER_INFO)
            userInfo = user.user
            true
        } else false
    }

    @Deprecated("虚假值, 弃用!!!, 请使用getRealUserId获取用户的UID")
    fun getUserId(): Int {
        return user?.user?.fakeUserId ?: 0
    }

    fun getRealUserId(): String {
        return user?.user?.uid ?: ""
    }

    /**
     * 运营管理
     */
    fun isAppManager(): Boolean {
//        return true
        return if (isLogin) circleUser?.user?.isCircleAdmin ?: 0 == 1 else false
    }


    /**
     * 圈子管理
     */
    fun isCircleManager(): Boolean {
//        return true
        return if (isLogin) circleUser?.isAdmin ?: false else false
    }

    /**
     * 群管理员
     */
    fun isGroupManager(): Boolean {
        return (currGroupRole == 2 || currGroupRole == 3)
    }


    /**
     * 文章/问题被锁定后，只有普通用户弹锁定弹窗，其余角色都不弹
     * 判断是否是特殊人员
     */
    fun isSpecialMember(): Boolean {
        return false
    }

    /**
     * 群主管理员
     */
    fun isGroupOwnerManager(): Boolean {
        return (currGroupRole == 3)
    }

    fun getIMUserId(): String {
        if (user.token.isNotNullEmpty() && user.user != null) {
            return TIM_PREFIX + user.user.uid
        }
        return ""
    }

    fun getPregnantStatus(typeStr: String): Int {
        return when (typeStr) {
            "备孕中" -> 1
            "已怀孕" -> 2
            "有宝宝" -> 3
            "试管婴儿", "试管助孕" -> 4
            else -> 1
        }
    }

    fun getDefaultPregnantStatus(): String {
        return when (userInfo.pregnantStatus) {
            1 -> "备孕中"
            2 -> "已怀孕"
            3 -> "有宝宝"
            4 -> "试管婴儿"
            else -> "备孕中"
        }
    }

    fun isSwitchTube(): Boolean {
        return false
    }

    fun getDefaultPregnantIntStatus(): Int {
        return if (isLogin) userInfo.pregnantStatus else 0
    }

    fun getDefaultPregnantColor(forTag: Boolean = false): Array<Int> {

        return when (userInfo.pregnantStatus) {
            1 -> arrayOf(R.color.ColorF296E6.Color)
            2 -> arrayOf(R.color.ColorFF9F9F.Color)
            3 -> arrayOf(R.color.ColorB6A9FE.Color)
            4 -> if (forTag) arrayOf(R.color.ColorA9C4FF.Color) else arrayOf(
                R.color.ColorA9C4FF.Color,
                R.color.ColorD197E4.Color,
                R.color.ColorFF9E9E.Color
            )

            else -> arrayOf(R.color.ColorF296E6.Color)
        }
    }

    fun getDefaultPregnantColor(forTag: Boolean = false, type: Int): Array<Int> {

        return when (type) {
            1 -> arrayOf(R.color.ColorF296E6.Color)
            2 -> arrayOf(R.color.ColorFF9F9F.Color)
            3 -> arrayOf(R.color.ColorB6A9FE.Color)
            4 -> if (forTag) arrayOf(R.color.ColorA9C4FF.Color) else arrayOf(
                R.color.ColorA9C4FF.Color,
                R.color.ColorD197E4.Color,
                R.color.ColorFF9E9E.Color
            )

            else -> arrayOf(R.color.ColorF296E6.Color)
        }
    }

    fun getDefaultPregnantColor2(type: Int): Array<Int> {
        return when (type) {
            1 ->
                arrayOf(
                    R.color.ColorF296E6.Color,
                    R.color.ColorF296E6.Color,
                    R.color.ColorF296E6.Color
                )

            2 -> arrayOf(
                R.color.ColorFF9F9F.Color,
                R.color.ColorFF9F9F.Color,
                R.color.ColorFF9F9F.Color
            )

            3 -> arrayOf(
                R.color.ColorB6A9FE.Color,
                R.color.ColorB6A9FE.Color,
                R.color.ColorB6A9FE.Color
            )

            4 -> arrayOf(
                R.color.ColorA9C4FF.Color,
                R.color.ColorD197E4.Color,
                R.color.ColorFF9E9E.Color
            )

            else -> arrayOf(
                R.color.ColorA9C4FF.Color,
                R.color.ColorD197E4.Color,
                R.color.ColorFF9E9E.Color
            )
        }
    }

    fun savePregnantStatus(type: Int) {
        userInfo.pregnantStatus = type
        updateInfo(userInfo)
    }

    fun updateInfo(info: UserData.User) {
        user.user = info
        saveUser(user)
    }

    /**
     * 更新用户头像
     * @date 2023/8/2 10:40
     */
    fun updateUserAvatar(avatar_file: String) {
        user.user.avatarFile = avatar_file
        saveUser(user)
    }


    fun cleanLoginInfo() {
        mmkv.remove(USER_INFO)
        user = UserData()
        userInfo = UserData.User()
        isLogin = false
        // 悬浮窗
        if (EasyFloat.isShow("Global")) {
            EasyFloat.dismiss("Global")
        }
    }


    fun getDeviceId(): String {
        return when {
            DEVICE_ID.isNotNullEmpty() -> {
                DEVICE_ID
            }

            DEVICE_ID_KEY.getString().isNotNullEmpty() -> {
                DEVICE_ID_KEY.getString() ?: getUUID()
            }

            else -> {
                getUUID()
            }
        }
    }

    private fun saveUUID(): String {
        val uniqueID = UUID.randomUUID().toString()
        uniqueID.saveToMMKV(UUID_ID_KEY)
        return uniqueID
    }

    private fun getUUID(): String {
        val uniqueID = UUID_ID_KEY.getString()
        return if (uniqueID.isNullOrEmpty())
            saveUUID()
        else uniqueID
    }

    fun setWalletSettingData(walletSettingData: WalletSettingData.Data) {
        this.walletSetting = walletSettingData
    }

    fun setFollowUnReadData(followUnReadBean: FollowUnReadBean) {
        this.followUnReadBean = followUnReadBean
    }

    /**
     * 添加进入内容详情次数,每三次清空
     * @date 2023/9/8 15:40
     */
    fun addArticleDetailCount() {
        if (enterArticleDetailCount == 2) {
            enterArticleDetailCount = 0
        } else {
            enterArticleDetailCount++
        }
    }

    fun addArticleColumnDetailCount() {
        if (enterArticleColumnDetailCount == 2) {
            enterArticleColumnDetailCount = 0
        } else {
            enterArticleColumnDetailCount++
        }
    }

}