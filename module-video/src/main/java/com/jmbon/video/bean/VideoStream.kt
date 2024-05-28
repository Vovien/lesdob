package com.jmbon.video.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.jmbon.middleware.bean.User

@Keep
@Parcelize
data class VideoStream(
    @SerializedName("results") var results: ArrayList<VideoStreamResult> = arrayListOf()
) : Parcelable {
    @Keep
    @Parcelize
    data class VideoStreamResult(
        @SerializedName("adv") var adv: Adv = Adv(),
        @SerializedName("circle") var circle: ArrayList<Circle> = arrayListOf(),
        @SerializedName("flow_vido") var flowVido: FlowVido = FlowVido(),
        @SerializedName("recommend_label") var recommendLabel: ArrayList<RecommendLabel> = arrayListOf(),
        @SerializedName("type") var type: String = "",
        var imageHeight: Int = 0
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Adv(
            @SerializedName("add_time") var addTime: Int = 0,
            @SerializedName("circle_id") var circleId: String ="" ,
            @SerializedName("content_type") var contentType: Int = 0,
            @SerializedName("covers") var covers: String = "",
            @SerializedName("create_time") var createTime: Int = 0,
            @SerializedName("icon") var icon: String = "",
            @SerializedName("id") var id: Int = 0,
            @SerializedName("is_del") var isDel: Int = 0,
            @SerializedName("is_join") var isJoin: Boolean = false,
            @SerializedName("item_id") var itemId: Int = 0,
            @SerializedName("name") var name: String = "",
            @SerializedName("number") var number: String = "",
            @SerializedName("originality") var originality: String = "",
            @SerializedName("resource_type") var resourceType: Int = 0,
            @SerializedName("resources") var resources: List<Resource> = listOf(),
            @SerializedName("title") var title: String = "",
            @SerializedName("tool_id") var toolId: String = "",
            @SerializedName("label") var label: String = "",

            @SerializedName("tool_type") var toolType: Int = 0,
            @SerializedName("type") var type: Int = 0,
            @SerializedName("update_time") var updateTime: Int = 0,
            @SerializedName("url") var url: String = "",
            @SerializedName("user") var user: User = User(),
            @SerializedName("views") var views: Int = 0,
            @SerializedName("item_type")
            var itemType: String = "",
            @SerializedName("identity")
            var identity: Int = 0
        ) : Parcelable {
            @Keep
            @Parcelize
            data class Resource(
                @SerializedName("src") var src: String = "",
                @SerializedName("type") var type: String = "",
                @SerializedName("height") var height: Float = 0f,
                @SerializedName("width") var width: Float = 0f,
            ) : Parcelable
        }

        @Keep
        @Parcelize
        data class Circle(
            @SerializedName("covers") var covers: String = "",
            @SerializedName("description") var description: String = "",
            @SerializedName("group_id") var groupId: Int = 0,
            @SerializedName("icon") var icon: String = "",
            @SerializedName("id") var id: Int = 0,
            @SerializedName("is_join") var isJoin: Boolean = false,
            @SerializedName("member_count") var memberCount: Int = 0,
            @SerializedName("name") var name: String = "",
            @SerializedName("number") var number: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class FlowVido(
            @SerializedName("comment_count") var commentCount: Int = 0,
            @SerializedName("cover") var cover: String = "",
            @SerializedName("create_time") var createTime: Int = 0,
            @SerializedName("description") var description: String = "",
            @SerializedName("duration") var duration: Int = 0,
            @SerializedName("give_count") var giveCount: Int = 0,
            @SerializedName("height") var height: Float = 0f,
            @SerializedName("id") var id: Int = 0,
            @SerializedName("play_count") var playCount: Int = 0,
            @SerializedName("style") var style: Int = 0,
            @SerializedName("title") var title: String = "",
            @SerializedName("uid") var uid: Int = 0,
            @SerializedName("url") var url: String = "",
            @SerializedName("user") var user: User = User(),
            @SerializedName("watch_count") var watchCount: Int = 0,
            @SerializedName("fake_play_count") var fakePlayCount: Int = 0,
            @SerializedName("width") var width: Float = 0f
        ) : Parcelable

        @Keep
        @Parcelize
        data class RecommendLabel(
            @SerializedName("long_label") var longLabel: String = "",
            @SerializedName("label") var label:  String = "",
            @SerializedName("id") var id: Int = 0
        ) : Parcelable
    }
}