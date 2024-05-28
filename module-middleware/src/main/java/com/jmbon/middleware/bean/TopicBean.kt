package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * @author : leimg
 * time   : 2021/4/19
 * desc   :话题bean
 * version: 1.0
 *
 */
@Parcelize
@Keep
data class TopicBean(
    @SerializedName("topic_id")
    var topic_id: Int = 0,
    @SerializedName("topic_title")
    var topic_title: String = "",
    @SerializedName("topic_pic")
    var topic_pic: String = "",
    @SerializedName("topic_description")
    var topic_description: String = "",
    @SerializedName("topic_focus_count")
    var topicFocusCount: Int = 0,
    @SerializedName("focus_count")
    var focus_count: Int = 0,
    @SerializedName("discuss_count")
    var discuss_count: Int = 0,
    @SerializedName("question_id")
    var question_id: Int = 0,
    @SerializedName("question_content")
    var question_content: String = "",
    @SerializedName("question_detail")
    var question_detail: String = "",
    @SerializedName("published_uid")
    var published_uid: Int = 0,
    @SerializedName("images")
    var images: ArrayList<String> = arrayListOf(),
    @SerializedName("add_time")
    var add_time: Int = 0,
    @SerializedName("is_reply")
    var is_reply: Boolean = false,
    @SerializedName("has_reply")
    var has_reply: Boolean = false,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("item_id")
    var item_id: Int = 0,
    @SerializedName("is_read")
    var is_read: Int = 1,
    @SerializedName("topics")
    var topics: ArrayList<TopicBean> = arrayListOf(),
    @SerializedName("is_focus")
    var is_focus: Boolean = false,
    @SerializedName("question_count")
    var question_count: Int = 0,
    @SerializedName("is_reward")
    var isReward: Int = 0,
    @SerializedName("lock")
    var lock: Int =0,// 0  未锁定  1：已锁定
    @SerializedName("reward_money")
    var reward_money: String = ""
) : Serializable, Parcelable {
    fun covertToQuestion(): Question {
        var question = Question()
        question.add_time = add_time
        question.focusCount = focus_count
        question.questionId = question_id
        question.questionContent = question_content
        question.questionDetail = question_detail
        question.publishedUid = published_uid
        question.images = images
        question.is_reply = is_reply
        question.topics = topics
        question.is_focus = is_focus
        question.isReward = isReward
        question.lock = lock
        question.reward_money = reward_money

        return question
    }
}


