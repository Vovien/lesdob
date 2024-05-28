package com.tubewiki.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.common.CommonViewModel
import com.tubewiki.home.api.HomeApi
import com.tubewiki.home.bean.CityListInfoBean
import com.tubewiki.home.bean.ItemCityBean
import com.tubewiki.home.bean.WeatherInfoBean
import kotlinx.coroutines.launch


/******************************************************************************
 * Description: 地点页面关联的VM
 *
 * Author: jhg
 *
 * Date: 2023/3/8
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class LocalViewModel: CommonViewModel() {

    /**
     * 天气信息
     */
    private val _weatherInfoLD = MutableLiveData<WeatherInfoBean?>()
    val weatherInfoLD: LiveData<WeatherInfoBean?> = _weatherInfoLD

    /**
     * 城市列表
     */
    private val _cityListLD = MutableLiveData<CityListInfoBean?>()
    val cityListLD: LiveData<CityListInfoBean?> = _cityListLD

    /**
     * 获取城市列表
     */
    fun getCityList() {
        viewModelScope.launch {
            launchWithFlow({ HomeApi.getCityList() }, {
                _cityListLD.postValue(null)
            }).next {
                _cityListLD.postValue(this)
            }
        }
    }


    /**
     * 更新天气信息
     */
    fun updateWeatherInfo(weatherInfo: WeatherInfoBean?) {
        _weatherInfoLD.postValue(weatherInfo)
    }
}