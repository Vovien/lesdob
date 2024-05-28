package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class VideoAlbum(
    @SerializedName("album") var album: Album = Album(),
    @SerializedName("videos") var videos: ArrayList<VideoAlbumItem> = arrayListOf(),
) : Parcelable {
    @Keep
    @Parcelize
    data class Album(
        @SerializedName("id") var id: Int = 0,
        @SerializedName("name") var name: String = "",
        @SerializedName("total") var total: Int = 0,
    ) : Parcelable

    @Keep
    @Parcelize
    data class VideoAlbumItem(
        @SerializedName("cover") var cover: String = "",
        @SerializedName("description") var description: String = "",
        @SerializedName("duration") var duration: Int = 0,
        @SerializedName("id") var id: Int = 0,
        @SerializedName("title") var title: String = "",
        @SerializedName("url") var url: String = "",
        @SerializedName("video_id") var videoId: Int = 0,
        @SerializedName("main_title") var mainTitle: String = "",
    ) : Parcelable
}