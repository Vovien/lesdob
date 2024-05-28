package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.Question
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class FollowQuestionData(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("questioins")
    var questioins: ArrayList<Question> = arrayListOf(),
    @SerializedName("questions")
    var questions: ArrayList<Question> = arrayListOf(),
    @SerializedName("recommends")
    var recommends: ArrayList<Question> = arrayListOf(),
    @SerializedName("total")
    var total: Int = 0
    ) : Parcelable