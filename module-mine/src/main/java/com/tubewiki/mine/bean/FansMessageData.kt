package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize
@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class FansMessageData(
    @SerializedName("fans")
    var fans: ArrayList<User> = arrayListOf(),
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : Parcelable

