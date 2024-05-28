package com.tubewiki.mine.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.CircleUnReadMessage
import com.jmbon.middleware.bean.CircleUser
import com.jmbon.middleware.bean.CityList
import com.jmbon.middleware.bean.CollectionVideoData
import com.jmbon.middleware.bean.GroupDetail
import com.jmbon.middleware.bean.PrivateMessageData
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.bean.VersionInfo
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpDefault
import com.jmbon.middleware.config.network.HttpUser
import com.jmbon.middleware.config.network.HttpV2
import com.jmbon.middleware.config.network.HttpV3
import com.jmbon.middleware.config.network.HttpV4
import com.jmbon.middleware.config.network.HttpV5
import com.tubewiki.mine.BuildConfig
import com.tubewiki.mine.bean.CircleList
import com.tubewiki.mine.bean.CollectionArticleData
import com.tubewiki.mine.bean.CollectionBean
import com.tubewiki.mine.bean.ContactBean
import com.tubewiki.mine.bean.ContentTortBean
import com.tubewiki.mine.bean.ConversationRecordBean
import com.tubewiki.mine.bean.EmailBean
import com.tubewiki.mine.bean.ExamineBean
import com.tubewiki.mine.bean.FansData
import com.tubewiki.mine.bean.FeedBackBean
import com.tubewiki.mine.bean.HospitalBean
import com.tubewiki.mine.bean.LogOffResultBean
import com.tubewiki.mine.bean.LogOffSign
import com.tubewiki.mine.bean.MineAnswerBean
import com.tubewiki.mine.bean.MineDraftData
import com.tubewiki.mine.bean.NickUpdateCount
import com.tubewiki.mine.bean.NotifySetting
import com.tubewiki.mine.bean.OneKeyMobile
import com.tubewiki.mine.bean.OtherUserAnswerData
import com.tubewiki.mine.bean.OtherUserArticleData
import com.tubewiki.mine.bean.OtherUserQuestionData
import com.tubewiki.mine.bean.PregantStatusBean
import com.tubewiki.mine.bean.UserDetailsV2
import com.tubewiki.mine.bean.UserEmailData
import com.tubewiki.mine.bean.UserInfoData
import com.tubewiki.mine.bean.VerificationCodeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import rxhttp.async
import rxhttp.wrapper.param.toResponse

@Keep
object MineApi {
    const val JMB_DEBUG_BASE_URL = "https://testapi6.jmbon.com/api/v1/"
    const val JMB_RELEASE_BASE_URL = "https://api.jmbon.com/api/v1/"

    /**
     * checkVersion
     */
    suspend fun checkVersion(): VersionInfo {
        return Http.post("s4/conf/current_version")
            .toResponse<VersionInfo>().await()
    }


    /**
     * APP获取手机号
     */
    suspend fun getMobile(token: String): OneKeyMobile {
        return Http.post("app/user/get_mobile")
            .add("access_token", token)
            .toResponse<OneKeyMobile>().await()
    }

    /**
     * 发送手机验证码
     * 1：注册，2：登录,3:找回密码
     */
    suspend fun quickLogin(token: String): UserData {
        return Http.post("user/m/quick/login")
            .add("accessToken", token)
            .toResponse<UserData>().await()
    }

    /**
     * 微信登录检测是否绑定手机号
     */
    suspend fun wxLoginCheck(openId: String, username: String, avatarFile: String): UserData {
        return HttpV5.post("user/wechat_login")
            .add("openid", openId)
            .add("username", username)
            .add("avatar_file", avatarFile)
            .toResponse<UserData>().await()
    }

    /**
     * 发送手机验证码
     * 1：注册，2：登录,3:找回密码 ，3注销验证码 (注销还是3)
     */
    suspend fun sendVerificationCode(
        mobile: String,
        type: Int,
    ): EmptyData {
        return HttpUser.post("v1/user/m-code/send")
            .add("mobile", mobile)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * 验证验证码
     * /api/v1/mobile/verify
     */
    suspend fun verifyCaptcha(code: String, mobile: String, zip: String): EmptyData {
        return Http.post("mobile/verify")
            .add("code", code)
            .add("mobile", mobile)
            .add("mobile_code", zip)
            .toResponse<EmptyData>().await()
    }

    /**
     * 验证验证码
     * 类型：1:注册; 2:登录; 3:找回密码|修改手机号；默认3
     * /api/v1/app/v2/user/auth
     */
    suspend fun userCaptchaAuth(
        type: Int,
        mobile: String,
        requestMCode: String,
    ): UserData {
        return Http.post("user/m-code/check")
            .add("type", type)
            .add("mobile", mobile)
            .add("requestMCode", requestMCode)
            .toResponse<UserData>().await()
    }

    /**
     * 验证码验证
     * @date 2023/10/12 14:31
     */
    suspend fun mobileCodeCheck(
        mobile: String,
        requestMCode: String,
    ): EmptyData {
        return HttpUser.post("v1/user/m-code/check")
            .add("mobile", mobile)
            .add("request_mcode", requestMCode)
            .toResponse<EmptyData>().await()
    }

    suspend fun changePhoneValidate(mobile: String, code: String): EmptyData {
        return Http.post("app/user/validate_code")
            .add("mobile", mobile)
            .add("request_mcode", code)
            .toResponse<EmptyData>().await()
    }

    /**
     * APP验证修改密码验证码
     * /api/v1/app/user/validate_code
     */
    suspend fun userCaptchaValidate(mobile: String, code: String): VerificationCodeToken {
        return Http.post("app/user/validate_code")
            .add("mobile", mobile)
            .add("code", code)
            .toResponse<VerificationCodeToken>().await()
    }

    /**
     * 注销验证验证码
     * /api/v1/app/user/validate_code
     */
    suspend fun checkAccount(code: String): LogOffSign {
        return HttpV3.post("user/check_account")
            .add("code", code)
            .toResponse<LogOffSign>().await()
    }

    /**
     * 编辑资料
     * /api/v1/user/set_info
     *
     *性别：1男2女
     * 是否显示性别：0显示1不显示
     */
    suspend fun setUserInfo(infoMap: HashMap<String, String>): String {
        return Http.post("user/info/edit")
            .addAll(infoMap)
//            .add("nickname", nickname)
//            .add("age", age)
//            .add("avatar", avatar)
//            .add("description", description)
//            .add("sex", sex)
//            .add("isShowSex", isShowSex)
            .toResponse<String>().await()
    }

    /**
     * 设置性别和年龄
     * sex 性别：1男2女
     */
    suspend fun setAgeAndGender(sex: Int, age: String): EmptyData {
        return Http.post("user/info/edit")
            .add("age", age)
            .add("sex", sex)
            .toResponse<EmptyData>().await()
    }

    /**
     * 设置个人资料
     * /api/v1/user/set_info
     * 性别是否显示，0：不显示，1：显示
     */
    suspend fun setSex(sex: Int): EmptyData {
        return HttpV3.post("user/set_sex")
            .add("sex", sex)
            .toResponse<EmptyData>().await()
    }

    /**
     * /api/v1/app/v3/index/set_pregant_status
     * APP设置怀孕状态
     * 设备类型:android和ios
     * 状态：0：未设置，1：备孕中，2：已怀孕，3：有宝宝 ,4试管婴儿
     */
    suspend fun setPregnantStatus(status: Int, type: String = "android"): String {
        return HttpV3.post("index/set_pregant_status")
            .add("pregnant_status", status)
            .add("type", type)
            .toResponse<String>().await()
    }

    /**
     * /api/v1/app/v3/index/get_pregnant_status
     * APP获取怀孕状态
     */
    suspend fun getPregnantStatus(viewScope: CoroutineScope): Deferred<PregantStatusBean> {
        return HttpV3.post("index/get_pregnant_status")
            .toResponse<PregantStatusBean>().async(viewScope)
    }

    /**
     * 消息通知设置
     */
    suspend fun notifysSetting(): NotifySetting {
        return HttpV2.post("user/notifys")
            .toResponse<NotifySetting>().await()
    }

    /**
     * 消息通知设置
     */
    suspend fun setNotify(field: String, int: Int): EmptyData {
        return HttpV2.post("user/notify_setting")
            .add("field", field)
            .add("status", int)
            .toResponse<EmptyData>().await()
    }

    /**
     * 消息通知设置
     */
    suspend fun getMineCollection(page: Int, pageSize: Int, type: Int): EmptyData {
        return Http.post("user/collection_v2")
            .add("page", page)
            .add("page_size", pageSize)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * 发布内容分页
     * type ask或answer或article或ask_people
     */
    suspend fun getUserPost(page: Int, type: String, pageSize: Int = 15, sort: Int = 0): EmptyData {
        return Http.post("user/release_v2")
            .add("page", page)
            .add("type", type)
            .add("page_size", pageSize)
            .add("sort", sort)
            .toResponse<EmptyData>().await()
    }

    /**
     * /api/v1/user/collection_v2
     * 我的收藏
     * type answer或article或medicine或hospital
     */
    suspend fun getUserCollection(page: Int, type: String, pageSize: Int = 15): EmptyData {
        return Http.post("user/collection_v2")
            .add("page", page)
            .add("type", type)
            .add("page_size", pageSize)
            .toResponse<EmptyData>().await()
    }


    /**
     * 获取我的详情信息
     * /api/v1/app/v2/user/detail_v2
     * type answer或article或medicine或hospital
     */
    suspend fun getUserDetailV2Async(scope: CoroutineScope): Deferred<UserDetailsV2> {
        return HttpV2.post("user/detail_v2")
            .toResponse<UserDetailsV2>().async(scope)
    }


    /**
     * 获取 别人的详情信息
     * /api/v1/app/user/other_user_detail
     * type answer或article或medicine或hospital
     */
    suspend fun getOtherDetail(uid: Int): EmptyData {
        return HttpV2.post("other_user_detail")
            .add("uid", uid)
            .toResponse<EmptyData>().await()
    }

    /**
     * APP设置用户背景
     * app/user/set_background
     * type answer或article或medicine或hospital
     */
    suspend fun setBackground(background: String): String {
        return Http.post("app/user/set_background")
            .add("background", background)
            .toResponse<String>().await()
    }

    /**
     * 设置密码
     * /api/v1/user/set_password
     */
    suspend fun setPassword(password: String, validate_token: String, mobile: String): EmptyData {
        return Http.post("app/user/set_password")
            .add("password", password)
            .add("validate_token", validate_token)
            .add("mobile", mobile)
            .add("is_login", 0)
            .toResponse<EmptyData>().await()
    }

    /**
     * 修改手机号
     * app/user/set_mobile
     */
    suspend fun setMobile(requestMCode: String, mobile: String): EmptyData {
        return HttpUser.post("v1/user/m/update/mobile")
            .add("request_mcode", requestMCode)
            .add("mobile", mobile)
            .toResponse<EmptyData>().await()
    }

    /**
     * 修改邮箱与设置邮箱发送邮箱
     * /api/v1/email/send_verify_email
     */
    suspend fun sendVerifyEmail(validate_token: String, email: String): VerificationCodeToken {
        return Http.post("app/user/email/send")
            .add("validate_token", validate_token)
            .add("email", email)
            .toResponse<VerificationCodeToken>().await()
    }

    /**
     * 修改邮箱与设置邮箱发送邮箱
     * /api/v1/email/send_verify_email
     * /api/v1/app/user/email/set
     */
    suspend fun setEmail(email_token: String): UserEmailData {
        return Http.post("app/user/email/set")
            .add("email_token", email_token)
            .toResponse<UserEmailData>().await()
    }

    /**
     * 修改邮箱与设置邮箱发送邮箱
     * /api/v1/app/user/safe
     */
    suspend fun getUserSafe(): UserData.User {
        return HttpUser.post("v1/user/safe/info")
            .toResponse<UserData.User>().await()
    }

    /**
     * 退出登录
     * /api/v1/user/logout
     */
    suspend fun logout(): EmptyData {
        return HttpV2.post("user/logout")
            .add("device_id", Constant.getDeviceId())
            .add("device_type", "android")
            .toResponse<EmptyData>().await()
    }


    /**
     * 我的-草稿
     */
    suspend fun getDraft(
        page: Int = 1,
        type: String = "answer",
        sort: Int = 0,
        page_size: Int = Constant.PAGE_SIZE
    ): MineDraftData {
        return HttpV2.post("user/draft")
            .add("page", page)
            .add("type", type)
            .add("sort", sort)
            .add("page_size", page_size)
            .toResponse<MineDraftData>().await()
    }

    /**
     * 我的-提问
     * /api/v1/user/release_v2
     * ask或answer或article或ask_people
     * 0:倒序，1：正序
     *
     */
    suspend fun getMineQuestion(
        page: Int = 1,
        type: String = "ask",
        sort: Int = 0,
        page_size: Int = Constant.PAGE_SIZE
    ): OtherUserQuestionData {
        return HttpV2.post("user/ask")
            .add("page", page)
            .add("type", type)
            .add("sort", sort)
            .add("page_size", page_size)
            .toResponse<OtherUserQuestionData>().await()
    }

    /**
     * 我的-回答
     */
    suspend fun getMineAnswer(
        page: Int = 1,
        type: String = "answer",
        sort: Int = 0,
        page_size: Int = Constant.PAGE_SIZE
    ): OtherUserAnswerData {
        return HttpV2.post("user/answer")
            .add("page", page)
            .add("type", type)
            .add("sort", sort)
            .add("page_size", page_size)
            .toResponse<OtherUserAnswerData>().await()
    }

    /**
     * 我的-收藏-回答
     * type ： answer或article或medicine或hospital video
     */
    suspend fun getMineAnswerCollection(
        page: Int = 1,
        type: String = "answer",
        keyword: String = "",
        page_size: Int = Constant.PAGE_SIZE
    ): MineAnswerBean {
        return HttpV4.post("user/get_collect_list")
            .add("page", page)
            .add("type", type)
            .add("keyword", keyword)
            .add("page_size", page_size)
            .toResponse<MineAnswerBean>().await()
    }

    /**
     * 我的-收藏-文章
     * type ： answer或article或medicine或hospital
     */
    suspend fun getMineArticleCollection(
        page: Int = 1,
        type: String = "article",
        keyword: String = "",
        page_size: Int = Constant.PAGE_SIZE
    ): CollectionArticleData {
        return HttpV4.post("user/get_collect_list")
            .add("page", page)
            .add("type", type)
            .add("keyword", keyword)
            .add("page_size", page_size)
            .toResponse<CollectionArticleData>().await()
    }

    /**
     * 我的-收藏-视频
     * type ： answer或article或medicine或hospital video
     */
    suspend fun getMineVideoCollection(
        page: Int = 1,
        type: String = "video",
        keyword: String = "",
        page_size: Int = Constant.PAGE_SIZE
    ): CollectionVideoData {
        return HttpV4.post("user/get_collect_list")
            .add("page", page)
            .add("type", type)
            .add("keyword", keyword)
            .add("page_size", page_size)
            .toResponse<CollectionVideoData>().await()
    }

    /**
     * 我的-收藏-医院
     * type ： answer或article或medicine或hospital
     */
    suspend fun getMineHospitalCollection(
        page: Int = 1,
        type: String = "hospital",
        keyword: String = "",
        page_size: Int = Constant.PAGE_SIZE
    ): HospitalBean {
        return HttpV4.post("user/get_collect_list")
            .add("page", page)
            .add("type", type)
            .add("keyword", keyword)
            .add("page_size", page_size)
            .toResponse<HospitalBean>().await()
    }

    /**
     * 获取别人用户信息
     */
    suspend fun getUserInfo(uid: Int = 0, isToken: String = "no"): UserInfoData {
        return HttpV3.post("user/info")
            .add("uid", uid)
            .add("is_token", isToken)
            .toResponse<UserInfoData>().await()
    }

    /**
     * /api/v1/user/other_people
     * 获取别人主页信息 -回答
     **/
    suspend fun getOtherUserAnswer(
        uid: Int = 0,
        page: Int = 1,
        type: String = "answer",
        sort_type: String = "desc",
        page_size: Int = Constant.PAGE_SIZE
    ): OtherUserAnswerData {
        return Http.post("user/other_people")
            .add("uid", uid)
            .add("page", page)
            .add("type", type)
            .add("sort_type", sort_type)
            .add("page_size", page_size)
            .toResponse<OtherUserAnswerData>().await()
    }

    /**
     * /api/v1/user/other_people
     * 获取别人主页信息 -文章
     *
     */
    suspend fun getOtherUserArticle(
        uid: Int = 0,
        page: Int = 1,
        type: String = "article",
        sort_type: String = "desc",
        page_size: Int = Constant.PAGE_SIZE
    ): OtherUserArticleData {
        return Http.post("user/other_people")
            .add("uid", uid)
            .add("page", page)
            .add("type", type)
            .add("sort_type", sort_type)
            .add("page_size", page_size)
            .toResponse<OtherUserArticleData>().await()
    }

    /**
     * user/get_video_list
     * 获取个人主页信息 -视频
     *
     */
    suspend fun getOtherUserVideo(
        uid: Int = 0,
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE
    ): VideoDetail {
        return HttpV4.post("user/get_video_list")
            .add("uid", uid)
            .add("page", page)
            .add("page_size", page_size)
            .toResponse<VideoDetail>().await()
    }


    /**
     * /api/v1/user/other_people
     * 获取别人主页信息 - 提问
     *
     */
    suspend fun getOtherUserQuestion(
        uid: Int = 0,
        page: Int = 1,
        type: String = "question",
        sort_type: String = "desc",
        page_size: Int = Constant.PAGE_SIZE
    ): OtherUserQuestionData {
        return Http.post("user/other_people")
            .add("uid", uid)
            .add("page", page)
            .add("type", type)
            .add("sort_type", sort_type)
            .add("page_size", page_size)
            .toResponse<OtherUserQuestionData>().await()
    }

    /**
     * desc:举报问题
     * item_id 问题或回答id
     * type  举报类型1：问题，2：回答
     */
    suspend fun report(uid: Int): EmptyData {
        return Http.post("app/user/report")
            .add("uid", uid)
            .toResponse<EmptyData>().await()
    }

    /**
     *
     * 获取粉丝列表
     **/
    suspend fun getUserFans(
        uid: Int = 0,
        isToken: String = "yes",
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE
    ): FansData {
        return Http.post("user/fans")
            .add("uid", uid)
            .add("is_token", isToken)
            .add("page", page)
            .add("page_size", page_size)
            .toResponse<FansData>().await()
    }

    /**
     *
     * 获取问题关注的用户列表
     **/
    suspend fun getQuestionFans(
        questionId: Int = 0,
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE
    ): FansData {
        return Http.post("app/question/focus/users")
            .add("question_id", questionId)
            .add("page", page)
            .add("page_size", page_size)
            .toResponse<FansData>().await()
    }

    /**
     * 获取用户是否关闭收发私信,未关闭才能发私信
     **/
    suspend fun getUserPrivateMessagePermission(uid: Int = 0): PrivateMessageData {
        return Http.post("app/im/can_send")
            .add("uid", uid)
            .toResponse<PrivateMessageData>().await()
    }

    /**
     * 获取推荐圈子
     * /api/v1/app/v2/circle/browses
     **/
    suspend fun getBrowsesCircle(): CircleList {
        return HttpV2.post("circle/browses")
            .toResponse<CircleList>().await()
    }

    /**
     * 工具页面
     * /api/v1/app/v2/user/tools
     **/
    suspend fun userToolsList(
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE
    ): String {
        return HttpV2.post("user/tools")
            .add("page", page)
            .add("page_size", page_size)
            .toResponse<String>().await()
    }

    /**
     * 群详情信息
     * /api/v1/app/v2/circle/chat_detail
     *
     * type 类型【1：圈子id获取信息，2:腾讯群号获取信息】
     */
    suspend fun getGroupDetail(
        circleId: String,
        number: String,
        type: String
    ): GroupDetail {
        return HttpV2.post("circle/chat_detail")
            .add("circle_id", circleId)
            .add("number", number)
            .add("type", type)
            .toResponse<GroupDetail>().await()
    }


    /**
     * /api/v1/app/v2/circle/message/points
     * 圈子用户详情
     */
    suspend fun circleMessageAsync(viewScope: CoroutineScope): Deferred<CircleUnReadMessage> {
        return HttpV2.post("circle/message/points")
            .toResponse<CircleUnReadMessage>().async(viewScope)
    }

    /**
     * /api/v1/app/v2/circle/user/detail
     * 圈子用户信息
     */
    suspend fun circleUserDetail(uid: Int, circleId: Int = 0): CircleUser {
        return HttpV2.post("circle/user/detail")
            .add("uid", uid)
            .add("circle_id", circleId)
            .toResponse<CircleUser>().await()
    }

    /**
     * /api/v1/app/v2/user/get_nickname_update_count
     * 圈子用户详情
     */
    suspend fun getNickCount(): NickUpdateCount {
        return HttpV2.post("user/get_nickname_update_count")
            .toResponse<NickUpdateCount>().await()
    }

    /**
     * /api/v1/app/v2/user/set_qrcode_status
     * 设置二维码登陆状态
     * status 设置状态【2：设置为已扫码，3：设置为取消登陆，4：设置为已登陆】
     */
    suspend fun qrcodeLogin(qrcodeToken: String, status: Int): EmptyData {
        return HttpV2.post("user/set_qrcode_status")
            .add("qrcode_token", qrcodeToken)
            .add("status", status)
            .toResponse<EmptyData>().await()
    }

    /**
     * /api/v1/app/v2/common/contact_us
     * 联系我们
     */
    suspend fun contactUs(): ContactBean {
        return Http.post("common/contact_us")
            .toResponse<ContactBean>().await()
    }

    /**
     * 获取被侵权举报信息
     */
    suspend fun getContentTortInfo(id: Int): ContentTortBean {
        return HttpV3.post("user/get_content_tort_info")
            .add("id", id)
            .toResponse<ContentTortBean>().await()
    }

    /**
     * 获取被侵权举报信息
     * /api/v1/app/v3/user/content_tort_feedback
     * type 处理状态【1：已知晓该问题，问题已解决，2：没有复现该问题，3：设计如何，不会修复，4：已知晓，未来会修复】
     */
    suspend fun contentTortFeedback(type: Int, feedback: String, contentId: Int): EmptyData {
        return HttpV3.post("user/content_tort_feedback")
            .add("feedback", feedback)
            .add("type", type)
            .add("content_id", contentId)
            .toResponse<EmptyData>().await()
    }

    /**
     * 获取反馈信息
     * /api/v1/app/v3/user/get_content_tort_feedback
     */
    suspend fun getContentTortFeedback(id: Int): FeedBackBean {
        return HttpV3.post("user/get_content_tort_feedback")
            .add("id", id)
            .toResponse<FeedBackBean>().await()
    }

    /**
     * 获取反馈信息
     * /api/v1/app/v3/message/get_official_email
     */
    suspend fun getOfficialEmail(): EmailBean {
        return HttpV3.post("message/get_official_email")
            .toResponse<EmailBean>().await()
    }

    /**
     * 获取官方审核详情
     * /api/v1/app/v3/message/get_examine_detail
     * 对应类型【1：问题审核，2：回答审核】
     */
    suspend fun getExamineDetail(id: Int, type: Int): ExamineBean {
        return HttpV3.post("message/get_examine_detail")
            .add("type", type)
            .add("id", id)
            .toResponse<ExamineBean>().await()
    }

    /**
     * /api/v1/app/v3/feedback/save
     * type 对应内容类型【0：意见反馈，1：推荐内容反馈】
     */
    suspend fun feedback(
        content: String,
        itemType: Int = 0,
    ): EmptyData {
        return Http.post("user/feedback/add")
            .add("description", content)
            .add("type", itemType)
            .toResponse<EmptyData>().await()
    }

    /**
     * 注销账号
     * is_force是否仍然注销【0：注销， 1：强制仍然注销】
     */
    suspend fun cancelAccount(
        cancel_sign: String,
        is_force: Int,
        device_id: String
    ): LogOffResultBean {
        return HttpV3.post("user/cancel_account")
            .add("cancel_sign", cancel_sign)
            .add("is_force", is_force)
            .add("device_id", device_id)
            .toResponse<LogOffResultBean>().await()
    }

    /**
     * 微信绑定手机号并且登录
     */
    suspend fun wxBindMobileLogin(wxSign: String, mobile: String, code: String): UserData {
        return HttpV5.post("user/check_mobile")
            .add("wechat_sign", wxSign)
            .add("mobile", mobile)
            .add("code", code)
            .toResponse<UserData>().await()
    }

    /**
     * 登录安全：绑定微信
     */
    suspend fun bindWechat(openid: String, nickName: String): EmptyData {
        return HttpUser.post("v1/user/bind/wx")
            .add("openid", openid)
            .add("username", nickName)
            .toResponse<EmptyData>().await()
    }

    /**
     * 登录安全：解绑微信
     */
    suspend fun unBindWechat(): EmptyData {
        return HttpUser.post("v1/user/m/unbind/wx")
            .toResponse<EmptyData>().await()
    }

    /**
     * 获取城市
     */
    suspend fun getCityList(): CityList {
        return Http.post("city/list")
            .toResponse<CityList>().await()
    }

    /**
     * LesDoB
     * /api/v1/app/v2/user/auth
     */
    suspend fun JMBuserCaptchaAuth(
        type: Int,
        mobile: String,
        password: String,
        is_app: Int = 1,
    ): UserData {
        return Http.post("${if (BuildConfig.DEBUG) JMB_DEBUG_BASE_URL else JMB_RELEASE_BASE_URL}app/v2/user/auth")
            .add("type", type)
            .add("mobile", mobile)
            .add("is_app", is_app)
            .add("password", password)
            .toResponse<UserData>().await()
    }


    /**
     * LesDoB用户注销
     * test-tube-api.jmbon.com/app/v1/user/account/cancel
     */
    suspend fun userAccountCancel(
        mobile: String,
        requestMCode: String
    ): EmptyData {
        return HttpUser.post("v1/user/account/cancel")
            .add("mobile", mobile)
            .add("request_mcode", requestMCode)
            .toResponse<EmptyData>().await()
    }


    /**
     * 登录
     * type:1: 验证码登录;  3:微信登录
     * password:密码或验证码
     * 用微信登录，接口返回code=10010时，需走绑定手机号逻辑
     */
    suspend fun auth(
        mobile: String,
        type: String,
        password: String,
        openid: String,
        username: String,
        avatar: String
    ): UserData {
        return HttpUser.post("v1/user/auth")
            .add("mobile", mobile)
            .add("type", type)
            .add("password", password)
            .add("openid", openid)
            .add("username", username)
            .add("avatar", avatar)
            .toResponse<UserData>().await()
    }

    /**
     * 微信绑定手机号登录
     */
    suspend fun wxBind(
        mobile: String,
        password: String,
        openid: String,
        username: String,
        avatar: String
    ): UserData {
        return HttpUser.post("v1/user/m/bind")
            .add("mobile", mobile)
            .add("password", password)
            .add("openid", openid)
            .add("username", username)
            .add("avatar", avatar)
            .toResponse<UserData>().await()
    }


    /**
     * 获取会话记录列表
     */
    suspend fun getConversationRecordList(): ConversationRecordBean {
        return Http.post("ai_dialog_list")
            .toResponse<ConversationRecordBean>().await()
    }

    /**
     * 获取我的收藏
     */
    suspend fun getMineCollection(currentPage: Int, pageSize: Int = 20): CollectionBean {
        return HttpDefault.post("user/my_collect")
            .add("page", currentPage)
            .add("page_size", pageSize)
            .toResponse<CollectionBean>().await()
    }

}