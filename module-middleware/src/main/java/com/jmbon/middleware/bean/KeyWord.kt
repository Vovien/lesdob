package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class KeyWordList(
    @SerializedName("keywords")
    val keywords: ArrayList<KeyWord> = ArrayList()
) : Parcelable {
    @Keep
    @Parcelize
    data class KeyWord(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("keyword")
        val keyword: String = "",
        @SerializedName("keyword_highlight")
        var keywordHighlight: ArrayList<String> = ArrayList()
    ) : Parcelable
}