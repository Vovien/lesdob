package com.tubewiki.mine.view.city

import com.apkdv.mvvmfast.base.BaseViewModel
import com.tubewiki.mine.api.MineApi
import com.jmbon.middleware.bean.CityList
import kotlinx.coroutines.flow.MutableStateFlow

class SelectCityViewModel : BaseViewModel() {

    // 搜索推荐数据
    val chinaCityList = MutableStateFlow<Pair<ArrayList<CityList.ChinaCity>,ArrayList<CityList.ChinaCity>>?>(
        Pair(arrayListOf(), arrayListOf())
    )
    //overseasCountryList
    val overseasCountryList = MutableStateFlow<ArrayList<CityList.ChinaCity>?>(arrayListOf())


    fun getCityList() = launchWithFlow({
        MineApi.getCityList()
    })
}