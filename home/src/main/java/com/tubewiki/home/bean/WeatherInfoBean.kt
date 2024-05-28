package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/******************************************************************************
 * Description: 天气信息
 *
 * Author: jhg
 *
 * Date: 2023/3/10
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class WeatherInfoBean(
    val weather: Weather? = Weather()
) : Parcelable

@Keep
@Parcelize
data class Weather(
    /**
     * 温度
     */
    val temperature: String = "",
    /**
     * 天气描述
     */
    val weather: String = "",
    /**
     * 天气图标
     */
    val weather_pic: String = "",
    /**
     * 城市名称
     */
    val city: String = "",
    /**
     * 温度区域
     */
    val temperature_range:String? = "",
) : Parcelable