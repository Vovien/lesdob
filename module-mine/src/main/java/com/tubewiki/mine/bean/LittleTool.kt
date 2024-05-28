package com.tubewiki.mine.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LittleTool(
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("tool_type")
    var toolType: Int = 1,//默认是1 ，胎教音乐是2
    @SerializedName("tool_id")
    var toolId: String = ""
) : Parcelable