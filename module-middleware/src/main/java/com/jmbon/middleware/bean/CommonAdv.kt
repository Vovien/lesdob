package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 公共广告
 * @author MilkCoder
 * @date 2023/6/25
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class CommonAdv(
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("code")
    var code: Int = 0,
    val data: List<Adv> = listOf()
) : Parcelable {

    @Keep
    @Parcelize
    data class Adv(
        val cover: String = "",
        @SerializedName("item_type")
        val itemType: String = "",
        val identity: String = ""
    ) : Parcelable

}


