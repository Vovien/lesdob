package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class RewardMessageData(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("rewards")
    var rewards: ArrayList<Reward> = arrayListOf(),
    @SerializedName("total")
    var total: Int = 0
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Reward(
        @SerializedName("add_time")
        var addTime: Int = 0,
        @SerializedName("cate_type")
        var cateType: Int = 0,
        @SerializedName("data")
        var `data`: Data = Data(),
        @SerializedName("description")
        var description: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("is_read")
        var isRead: Boolean = false,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("money")
        var money: String = "",
        @SerializedName("passive_uid")
        var passiveUid: Int = 0,
        @SerializedName("type")
        var type: String = "",
        @SerializedName("user")
        var user: User = User(),
        @SerializedName("uid")
        var uid: Int = 0
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        @Keep
        data class Data(
            @SerializedName("answer_content")
            var answerContent: String = "",
            @SerializedName("answer_id")
            var answerId: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("question_content")
            var questionContent: String = "",
            @SerializedName("question_id")
            var questionId: Int = 0,
            @SerializedName("title")
            var title: String = "",
            @SerializedName("user")
            var user: User = User(),
        ) : Parcelable
    }
}