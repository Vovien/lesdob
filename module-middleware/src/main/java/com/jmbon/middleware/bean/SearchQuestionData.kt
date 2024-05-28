package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class SearchQuestionData(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("questions")
    var questions: ArrayList<Question> = arrayListOf(),
    @SerializedName("total")
    var total: Int = 0
) : Parcelable
