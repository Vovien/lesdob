package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 创作者列表bean
 */
@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class CreatorUserData(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0,
    @SerializedName("users")
    var users: ArrayList<User> = arrayListOf()
) : Parcelable