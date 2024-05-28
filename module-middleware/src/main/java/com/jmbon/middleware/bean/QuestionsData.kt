package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class QuestionsData(
    @SerializedName("categorys")
    var categorys: ArrayList<Category> = arrayListOf(),
    @SerializedName("index")
    var index: Int = 0,
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("data", alternate = ["questions"])
    var questions: ArrayList<Question> = arrayListOf(),
    @SerializedName("total")
    var total: Int = 0
) : Parcelable