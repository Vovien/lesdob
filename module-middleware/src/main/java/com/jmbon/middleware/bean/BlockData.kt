package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.io.Serializable

@Keep
@Parcelize
data class BlockData(
    @SerializedName("image")
    var image: ArrayList<String> = arrayListOf(),
    @SerializedName("text")
    var text: ArrayList<Text> = arrayListOf(),
    @SerializedName("video")
    var video: ArrayList<String> = arrayListOf()
) : Parcelable,Serializable {
    @Keep
    @Parcelize
    data class Text(
        @SerializedName("context")
        var context: String = "",
        @SerializedName("positions")
        var positions: ArrayList<Position> = arrayListOf()
    ) : Parcelable,Serializable {
        @Keep
        @Parcelize
        data class Position(
            @SerializedName("endPos")
            var endPos: Int = 0,
            @SerializedName("startPos")
            var startPos: Int = 0
        ) : Parcelable,Serializable
    }
}