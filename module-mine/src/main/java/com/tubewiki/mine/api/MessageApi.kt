package com.tubewiki.mine.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.MessagePointBean
import com.jmbon.middleware.bean.UserSignData
import com.jmbon.middleware.bean.ViolateArticleData
import com.jmbon.middleware.bean.ViolateQuestionData
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.*
import com.tubewiki.mine.bean.*
import rxhttp.wrapper.param.toResponse
@Keep
object MessageApi {
    /**
     * 获取腾讯IM签名
     **/
    suspend fun getUserSig(): UserSignData {
        return Http.post("app/im/get_user_sig")
            .toResponse<UserSignData>().await()
    }

    /**
     * APP获取打赏列表
     **/
    suspend fun rewardMessage(page: Int, pageSize: Int = Constant.PAGE_SIZE): RewardMessageData {
        return Http.post("app/message/reward")
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<RewardMessageData>().await()
    }

    /**
     * APP获取消息回答列表
     **/
    suspend fun answerMessage(page: Int, pageSize: Int = Constant.PAGE_SIZE): AnswerMessageData {
        return HttpV3.post("message/answer")
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<AnswerMessageData>().await()
    }

    /**
     * APP获取评论消息列表
     **/
    suspend fun commentMessage(page: Int, pageSize: Int = Constant.PAGE_SIZE): CommentMessageData {
        return HttpV4.post("message/comment")
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<CommentMessageData>().await()
    }

    /**
     * APP获取粉丝消息列表
     **/
    suspend fun fansMessage(page: Int, pageSize: Int = Constant.PAGE_SIZE): FansMessageData {
        return Http.post("app/message/fans")
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<FansMessageData>().await()
    }

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
     * APP互动消息
     * /api/v1/app/message/interaction
     * type  类型，全部：all,点赞：given,收藏：collect
     */
    suspend fun interactionMessage(
        type: String,
        page: Int,
        pageSize: Int = Constant.PAGE_SIZE
    ): MutuallyMessageData {
        return HttpV4.post("message/interaction")
            .add("type", type)
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<MutuallyMessageData>().await()
    }

    /**
     * APP官方消息
     * /api/v1/app/message/official
     * 类型：all:全部，reward:悬赏，examine:审核，violation:违规，report：举报
     */
    suspend fun officialMessage(
        type: String,
        page: Int,
        pageSize: Int = Constant.PAGE_SIZE
    ): OfficialMessageData {
        return HttpV5.post("message/official")
            .add("type", type)
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<OfficialMessageData>().await()
    }

    /**
     * 获取官方客服电话
     * /api/v1/app/message/official_mobile
     * 类型：all:全部，reward:悬赏，violation:违规
     */
    suspend fun officialMobile(): PhoneData {
        return Http.post("app/message/official_mobile")
            .toResponse<PhoneData>().await()
    }

    /**
     * desc:APP获取违规 详情内容
     *@param id    违规内容id
     * 违规类型answer:评论或回答，article：文章，question:问题，reward:悬赏问题  video:视频，  image_review图片审核消息
     */
    suspend fun violationQuestionDetail(id: Int, type: String = "question"): ViolateQuestionData {
        return HttpV2.post("message/violation/detail")
            .add("id", id)
            .add("type", type)
            .toResponse<ViolateQuestionData>().await()
    }

    /**
     * desc:APP获取违规 详情内容
     *@param id    违规内容id
     * 违规类型answer:评论或回答，article：文章，question:问题，reward:悬赏问题
     */
    suspend fun violationArticleDetails(id: Int, type: String = "article"): ViolateArticleData {
        return Http.post("app/message/violation/detail")
            .add("id", id)
            .add("type", type)
            .toResponse<ViolateArticleData>().await()
    }

    /**
     * desc:获取审核消息详情
     *@param id    examineid
     */
    suspend fun getExamineDetail(id: Int): ExamineDetailBean {
        return HttpV5.post("message/get_examine_detail")
            .add("id", id)
            .toResponse<ExamineDetailBean>().await()
    }

    /**
     * desc:获取消息数量
     */
    suspend fun getMessagePoint(): MessagePointBean {
        // return HttpV3.post("message/points")
        return HttpV4.post("message/get_red_point")
            .toResponse<MessagePointBean>().await()
    }

}