package com.tubewiki.home.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.jmbon.middleware.bean.TubeArticleDetail

@Keep
@Parcelize
data class TubeStepBeans(
    @SerializedName("firstList")
    var firstList: MutableList<Step> = mutableListOf(),
    @SerializedName("top")
    var top: MutableList<Step> = mutableListOf(),
    @SerializedName("secondList")
    var secondList: MutableList<Step> = mutableListOf(),
) : Parcelable {

    @Keep
    @Parcelize
    data class Step(
        @SerializedName("article")
        var article: TubeArticleDetail? = TubeArticleDetail(),
        @SerializedName("isFixed")
        var isFixed: Boolean = false,
        @SerializedName("subTitle")
        var subTitle: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("icon")
        var icon: String = "",
        @SerializedName("img")
        var img: String = "",
        @SerializedName("id")
        var id: Int = 0,
    ) : Parcelable

}