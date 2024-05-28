package com.tubewiki.home.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class HomeBean(
    @SerializedName("cate_list")
    var cateList: MutableList<Cate> = mutableListOf(),
    @SerializedName("logo")
    var logo: String = "",
    @SerializedName("logo_right")
    var logoRight: String = "",
    @SerializedName("sentence")
    var sentence: String = "",
    val app_title: String = "",
    val bank_title: String = "",
    val bank_list: List<Bank>? = listOf(),
    val topic_list: List<TopicCategoryBean>? = listOf(),
) : Parcelable {
    @Keep
    @Parcelize
    data class Cate(
        @SerializedName("icon")
        var icon: String = "",
        @SerializedName("cate_id")
        var id: Int = 0,
        @SerializedName("name")
        var title: String = ""
    ) : Parcelable
}