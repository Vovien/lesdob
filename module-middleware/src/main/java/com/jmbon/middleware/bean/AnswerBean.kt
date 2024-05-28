package com.jmbon.middleware.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.utils.NullToQuestion
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Keep
data class AnswerBean(
    var webHeight: Int = 0,
    @SerializedName("give_count")
    var giveCount: Int = 0,
    @SerializedName("item_id")
    var itemId: Int = 0,
    @SerializedName("owner")
    var owner: User = User(),
    @SerializedName("receiver")
    var receiver: String = "",
    @SerializedName("reply_id")
    var replyId: Int = 0,
    @SerializedName("risk_type")
    var riskType: Int = 0,//风险类型：1争议  2谣言  3广告  4 摘自
    @SerializedName("risk_description")
    var riskDescription: String = "",
    @SerializedName("risk_from_title")
    var riskFromTitle: String = "",
    @SerializedName("risk_from_url")
    var riskFromUrl: String = "",
    @SerializedName("agree_label")
    var agreeLabel: String = "", //id、id2等人赞同了该回答
    @SerializedName("second_answer_id")
    var second_answer_id: Int = 0,

    @SerializedName("third_answers")
    var third_answers: ArrayList<AnswerBean> = arrayListOf(),

    @SerializedName("top_answer_id")
    var top_answer_id: Int = 0,
    @SerializedName("is_del")
    var isDel: Int = 0, //问题和回答 0:正常,审核通过 1:删除  2:审核中 (机审中和后台审核中都是这个) 3:审核不通过(机审不通过或者后台审核不通过) 4:审核不通过,但是可以恢复
    //视频审核状态
    @SerializedName("audit_status")
    var auditStatus: Int = 1, //0 未审核 ，1 审核完成 （通过或者不通过）

    @SerializedName("category_title")
    var category_title: String? = "",
    @SerializedName("answer_content_text")
    var answer_content_text: String = "",
    @SerializedName("type")
    var type: Int = 0, //content_type:2  时， 对应类型【1：圈子，2：小工具，3：自定义链接 4:文章，5：问答】
    @SerializedName("content_type")
    var contentType: Int = 0, //内容类型【1：回答，2：广告】
    @SerializedName("originality")
    var originality: String = "",//广告简介
    @SerializedName("tool_id")
    var toolId: String = "",//广告-小工具id
    @SerializedName("number")
    var number: String = "",//广告-圈子number
    @SerializedName("url")
    var url: String = "",//广告-网页url
    @SerializedName("title")
    var title: String = "",//广告-网页url
    @SerializedName("is_join")
    var isJoin: Boolean = false,//广告-圈子是否加入
    @SerializedName("collect_count")
    var collection_count: Int = 0,
    @SerializedName("views")
    var views: Int = 0,
    @SerializedName("watch_count")
    var watchCount: Int = 0,

    @SerializedName("is_comment")
    var isComment: Boolean = false,
    @SerializedName("is_focus")
    var isFocus: Boolean = false,
    @SerializedName("is_mutual_focus")
    var isMutualFocus: Boolean = false,
    @SerializedName("resources")
    var resources: ArrayList<ResourceBean> = arrayListOf(),
    @SerializedName("reward_users")
    var rewardUsers: ArrayList<User> = arrayListOf(),

    @SerializedName("answer_content_html")
    var answerContentHtml: String = "",

    @SerializedName("origin_answer_content")
    var originAnswerContent: String = "",

    @SerializedName("images")
    var images: ArrayList<String> = arrayListOf(),
    @SerializedName("videos")
    var videos: ArrayList<String> = arrayListOf(),

    @SerializedName("add_time")
    var addTime: Long = 0,
    @SerializedName("draft_id")
    var draftId: Int = 0,
    @SerializedName("answer_content")
    var answerContent: String = "",
    @SerializedName("answer_id")
    var answerId: Int = 0,
    @SerializedName("published_uid")
    var publishedUid: Int = 0,
    @SerializedName("comment_count")
    var commentCount: Int = 0,
    @SerializedName("is_collect")
    var isCollect: Boolean = false,
    @SerializedName("is_given")
    var isGiven: Boolean = false,
    @SerializedName("rewards")
    var rewards: ArrayList<User> = arrayListOf(),
    @SerializedName("sub_answers")
    var subAnswers: ArrayList<AnswerBean> = arrayListOf(),
    @SerializedName("sub_answer")
    var subAnswer: SubAnswer = SubAnswer(),
    @SerializedName("uid")
    var uid: Int = 0,
    @SerializedName("update_answer")
    var updateAnswer: String = "",
    @SerializedName("update_type")
    var updateType: Int = 0, //1 在审核中
    @SerializedName("updated_Time")
    var updatedTime: String = "",
    @SerializedName("updated_at")
    var updatedAt: Long = 0L,
    @SerializedName("user")
    var user: User = User(),
    @SerializedName("circle_id")
    var circleId: Int = 0,
    @SerializedName("circle")
    var circle: Circle = Circle(),
    @SerializedName("question")
    var question: Question? = null,

    @SerializedName("block_message")
    var blockMessage: String = "",

    @SerializedName("block_data")
    var blockData: BlockData? = null,
    @SerializedName("check_log_id")
    var check_log_id: Int = 0,
    @SerializedName("is_edit")
    var is_edit: Int = 0,

    //是否在审核中
    @SerializedName("is_finish_check")
    var isFinishCheck: Int = 0, //1 审核完成
    @SerializedName("is_checked")
    var isChecked: Int = 1, //0 未审核 1 审核通过 2 审核失败

    @SerializedName("block_message_status")
    var blockMessageStatus: Int = 0, // block_message_status  ,默认为0 , 1     展示:内容审核中 ; 2展示提示 : 内容部分涉嫌违规

    var nextAnswerId: Int = 0,
    var hashCodeId: Int = 0,
) : Parcelable, Serializable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class SubAnswer(
        @SerializedName("give_count")
        var giveCount: Int = 0,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("owner")
        var owner: User = User(),
        @SerializedName("receiver")
        var receiver: String = "",
        @SerializedName("reply_id")
        var replyId: Int = 0,
        @SerializedName("second_answer_id")
        var second_answer_id: Int = 0,
        @SerializedName("third_answers")
        var third_answers: ArrayList<AnswerBean> = arrayListOf(),
        @SerializedName("top_answer_id")
        var top_answer_id: Int = 0,
        @SerializedName("type")
        var type: Int = 0, //content_type:2  时， 对应类型【1：圈子，2：小工具，3：自定义链接】

        @SerializedName("category_title")
        var category_title: String? = "",
        @SerializedName("answer_content_text")
        var answer_content_text: String = "",

        @SerializedName("collect_count")
        var collection_count: Int = 0,
        @SerializedName("is_comment")
        var isComment: Boolean = false,
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("is_mutual_focus")
        var isMutualFocus: Boolean = false,
        @SerializedName("resources")
        var resources: ArrayList<ResourceBean> = arrayListOf(),
        @SerializedName("reward_users")
        var rewardUsers: ArrayList<User> = arrayListOf(),
        @SerializedName("answer_content_html")
        var answerContentHtml: String = "",
        @SerializedName("origin_answer_content")
        var originAnswerContent: String = "",
        @SerializedName("images")
        var images: ArrayList<String> = arrayListOf(),
        @SerializedName("videos")
        var videos: ArrayList<String> = arrayListOf(),
        @SerializedName("add_time")
        var addTime: Long = 0,
        @SerializedName("answer_content")
        var answerContent: String = "",
        @SerializedName("canReset")
        var canReset: Boolean = false,
    ) : Parcelable, Serializable
}