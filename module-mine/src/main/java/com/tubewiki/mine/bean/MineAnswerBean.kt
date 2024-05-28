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
data class MineAnswerBean(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0,
    @SerializedName("datas")
    var datas: ArrayList<Question> = arrayListOf(),
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable
