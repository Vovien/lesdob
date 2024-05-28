package com.tubewiki.home.bean


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class RecommendData(
    @SerializedName("data")
    var data: ArrayList<TubeArticleDetail> = arrayListOf(),
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("msg")
    var message: String = "",
) : Parcelable

