package com.tubewiki.mine.bean


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import android.os.Parcelable
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class FansData(
    @SerializedName("fans")
    var fans: ArrayList<User> = arrayListOf(),
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0,
    @SerializedName("users")
    var users: ArrayList<User> = arrayListOf(),
) : Parcelable

