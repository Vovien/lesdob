package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Member

/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/9/22
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class CircleData(
    @SerializedName("circle_id") var circleId: Int = 0,
    @SerializedName("covers", alternate = ["cover"]) var covers: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("index_name") var indexName: String = "",
    @SerializedName("is_join") var isJoin: Int = 0,
    @SerializedName("member_count") var memberCount: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("name_highlight") var nameHighlight: ArrayList<String> = arrayListOf(),
    @SerializedName("member_list") var memberList: ArrayList<Member> = arrayListOf(),
    @SerializedName("number") var number: String = "",
    /**
     * 圈子类型: 1=真实圈子, 2=运营圈子
     */
    val circle_type: Int = 0,
    val item_type: String = "",
    val identity: String = "",
    /**
     * 副文案, 富文本形式
     */
    val assistant: String = "",
) : Parcelable {
    @Keep
    @Parcelize
    data class Member(
        @SerializedName("avatar_file") var avatarFile: String = ""
    ) : Parcelable
}