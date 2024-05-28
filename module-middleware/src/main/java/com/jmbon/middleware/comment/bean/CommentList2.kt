package com.jmbon.middleware.comment.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class CommentList2(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var data: CommentList = CommentList(),
    @SerializedName("msg")
    var msg: String = ""
) : Parcelable
