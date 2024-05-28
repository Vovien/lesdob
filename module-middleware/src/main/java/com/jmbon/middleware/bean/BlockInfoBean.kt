package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class BlockInfoBean(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable {
    @Keep
    @Parcelize
    data class Data(
        @SerializedName("blockContents")
        var blockContents: ArrayList<String> = arrayListOf(),
        @SerializedName("blockImages")
        var blockImages: ArrayList<String> = arrayListOf(),
        @SerializedName("blockTitles")
        var blockTitles: ArrayList<String> = arrayListOf(),
        @SerializedName("triggerReportTotal")
        var triggerReportTotal: Int = 5 ,//限制次数
          @SerializedName("traceId")
        var traceId: String = ""
    ) : Parcelable
}