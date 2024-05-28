package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class GeneralConfBean(
    @SerializedName("app_introduce")
    var appIntroduce: String = "",
    @SerializedName("dialog_title")
    var dialogTitle: String = "",
    @SerializedName("robot_avatar")
    var robotAvatar: String = "",
    var popupImg: String = "",
    var itemType: String = "",
    var identity: String = "",
    /**
     * 是否开启审核, 0=否, 1=是
     */
    val examine_switch: Int = 0,
    /**
     * 圈子按钮是否隐藏 0=否, 1=是
     */
    val circle_wechat_is_hide: Int = 0,
) : Parcelable{
    @Keep
    @Parcelize
    data class Tag(
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("labelName")
        var labelName: String = "",

        ) : Parcelable
}