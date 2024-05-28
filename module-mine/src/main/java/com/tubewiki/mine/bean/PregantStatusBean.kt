package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class PregantStatusBean(
    @SerializedName("is_switch_tube")
    var isSwitchTube: Int = 0,//'是否选中试管婴儿，0：未选中，1：已选中'
    @SerializedName("my_pregant_status")
    var myPregantStatus: Int = 0,
    @SerializedName("pregant_status")
    var pregantStatus: List<PregantStatus> = listOf()
) : Parcelable {
    @Keep
    @Parcelize
    data class PregantStatus(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("title")
        var title: String = ""
    ) : Parcelable
}