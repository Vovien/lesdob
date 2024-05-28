package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 图文广告
 * @author MilkCoder
 * @date 2023/6/21
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class ImageAdv(
    @SerializedName("collect_num_false")
    val collectNumFalse: Int = 0,
    //封面图片
    val cover: String = "",
    @SerializedName("create_time")
    val createTime: Int = 0,
    @SerializedName("give_count")
    val giveCount: Int = 0,
    @SerializedName("given_num_false")
    val givenNumFalse: Int = 0,
    val id: Int = 0,
    val logo: String = "",
    val name: String = "",
    val title: String = "",
    val user: ImageUser = ImageUser(),
    @SerializedName("video_id")
    val videoId: Int = 0,
    @SerializedName("view_count")
    val viewCount: Int = 0,
    @SerializedName("view_num_false")
    val viewNumFalse: Int = 0,
    @SerializedName("buttom_word")
    val buttomWord: String = "",
    //广告词文字
    @SerializedName("adv_word")
    val advWord:String = "",
    //跳转类型【wetchat_popup:加V弹窗】
    @SerializedName("item_type")
    val itemType: String = "",
    val identity:Int = 0,
) : Parcelable {

    val giveStr:String
        get() = "$giveCount 赞"

    val browseStr:String
        get() = "$viewCount 浏览"

    @Keep
    @Parcelize
    data class ImageUser(
        @SerializedName("avatar_file")
        val avatarFile: String = "",
        val uid: Int = 0,
        @SerializedName("user_name")
        val userName: String = ""
    ) : Parcelable
}

