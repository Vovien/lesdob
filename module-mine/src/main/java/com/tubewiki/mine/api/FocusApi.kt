package com.tubewiki.mine.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.*
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpV2
import com.tubewiki.mine.bean.*
import rxhttp.wrapper.param.toResponse
@Keep
object FocusApi {
    /**
     * /api/v1/user/focus
     * 关注、取消关注用户
     * 关注用户的ID
     * focus或unfocus,即关注或取消
     */
    suspend fun focusUser(uid: Int, type: String): EmptyData {
        return Http.post("user/focus")
            .add("focus_user_id", uid)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * 获取我的关注列表
     * type 类型：用户：user,问题：question,话题：topic,专栏:column
     **/
    suspend fun getFollowQuestionData(
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE,
        type: String = "question"
    ): FollowQuestionData {
        return Http.post("app/user/focus")
            .add("page", page)
            .add("page_size", page_size)
            .add("type", type)
            .toResponse<FollowQuestionData>().await()
    }

    /**
     * 获取我的关注列表
     * type 类型：用户：user,问题：question,话题：topic,专栏:column
     **/
    suspend fun getFollowColumnData(
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE,
        type: String = "column"
    ): FollowColumnData {
        return Http.post("app/user/focus")
            .add("page", page)
            .add("page_size", page_size)
            .add("type", type)
            .toResponse<FollowColumnData>().await()
    }

    /**
     * 获取我的关注列表
     * type 类型：用户：user,问题：question,话题：topic,专栏:column
     **/
    suspend fun getFollowTopicData(
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE,
        type: String = "topic"
    ): FollowTopicData {
        return Http.post("app/user/focus")
            .add("page", page)
            .add("page_size", page_size)
            .add("type", type)
            .toResponse<FollowTopicData>().await()
    }

    /**
     * 获取我的关注列表
     * type 类型：用户：user,问题：question,话题：topic,专栏:column  医生：doctor
     **/
    suspend fun getFollowUserData(
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE,
        type: String = "user"
    ): FollowUserData {
        return Http.post("app/user/focus")
            .add("page", page)
            .add("page_size", page_size)
            .add("type", type)
            .toResponse<FollowUserData>().await()
    }

    /**
     * 获取我的关注列表
     * type 类型：用户：user,问题：question,话题：topic,专栏:column  医生：doctor
     **/
    suspend fun getFollowDoctorData(
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE,
        type: String = "doctor"
    ): FollowUserData {
        return Http.post("app/user/focus")
            .add("page", page)
            .add("page_size", page_size)
            .add("type", type)
            .toResponse<FollowUserData>().await()
    }

    /**
     * 关注专栏
     * /api/v1/column/focus
     * focus或unfocus
     */
    suspend fun columnFocus(category_id: Int, type: String): EmptyData {
        return Http.post("column/focus")
            .add("column_id", category_id)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * desc:关注问题或者取消关注问题
     *@param token              string		登录token
     *@param type            focus或unfocus,即关注或取消关注,默认为关注
     *@param question_id           int		问题ID
     */
    suspend fun focusQuestion(questionId: Int, type: String = "focus"): EmptyData {
        return Http.post("question/focus")
            .add("question_id", questionId)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * /api/v1/topic/focus
     * 话题关注接口
     * focus或unfocus,即关注或取消
     */
    suspend fun topicFocus(topicID: Int, type: String): EmptyData {
        return HttpV2.post("topic/focus")
            .add("topic_id", topicID)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

}