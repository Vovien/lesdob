package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/******************************************************************************
 * Description: 定位相关的信息
 *
 * Author: jhg
 *
 * Date: 2023/3/10
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class LocationInfoBean(
    /**
     * 当前城市名称
     */
    @SerializedName("city")
    val cityName: String = "",
    /**
     * 城市简称
     */
    var citySimpleName: String = "",
    /**
     * 当前的城市Code
     */
    val cityCode: String = "",
    /**
     * 当前的省份名称
     */
    @SerializedName("province")
    val provinceName: String = "",
    /**
     * 当前的省份Code
     */
    val provinceCode: String = "",
    /**
     * 定位的详细地址
     */
    val detailAddress: String = "",
    /**
     * 当前位置的经度
     */
    var longitude: Double = 0.0,
    /**
     * 当前位置的维度
     */
    var latitude: Double = 0.0,
    /**
     * 国家名词
     */
    @SerializedName("country")
    val countryName: String = "",
    /**
     * 三级行政单位, 如区
     */
    @SerializedName("district")
    val district: String = "",
): Parcelable {
    companion object {
        fun getSimpleCityName(cityName: String?): String {
            if (cityName.isNullOrBlank()) {
                return ""
            }

            return cityName.replace("市", "")
                .replace("特别行政区", "")
        }
    }

}