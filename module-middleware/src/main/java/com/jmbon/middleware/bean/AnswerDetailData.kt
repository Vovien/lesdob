package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class AnswerDetailData(
    @SerializedName("answer")
    var answer: Answer? = Answer(),
    @SerializedName("question")
    var question: Question? = Question(),
    @SerializedName("relevant")
    var relevant: ArrayList<Question> = arrayListOf(),
    @SerializedName("others")
    var others: ArrayList<PregnantStatusDetailBean.Other> = arrayListOf(),
    @SerializedName("invitation_content")
    var invitationContent: InvitationContentBean = InvitationContentBean(),
    @SerializedName("recommend_content")
    var recommendContent: ArrayList<RecommendContentBean> = arrayListOf(),
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Answer(
        @SerializedName("current")
        var current: AnswerRecommendBean = AnswerRecommendBean(),
        @SerializedName("next")
        var next: AnswerRecommendBean? = AnswerRecommendBean(),
        @SerializedName("prev")
        var prev: AnswerRecommendBean = AnswerRecommendBean(),

    ) : Parcelable


}