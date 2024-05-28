package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 广告
 * @author MilkCoder
 * @date 2023/9/8
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class PopupImageBean(
    val pop_info: PopupAdvBean? = null
): Parcelable

@Keep
@Parcelize
data class PopupAdvBean(
    @SerializedName("img")
    var popupImg: String = "",
    @SerializedName("item_type")
    var itemType: String = "",
    @SerializedName("identity")
    var identity: String = ""
) : Parcelable

