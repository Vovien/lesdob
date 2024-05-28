package com.jmbon.video.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.VideoDetail
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class VideoListDetail(
    @SerializedName("data") var data: ArrayList<VideoDetail.VideoData> = arrayListOf(),
) : Parcelable