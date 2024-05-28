package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 公共banner
 * @author MilkCoder
 * @date 2023/7/28
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class CommonBannerBean(
    @SerializedName("banner_list")
    val banners: List<CommonBanner> = listOf()
) : Parcelable
