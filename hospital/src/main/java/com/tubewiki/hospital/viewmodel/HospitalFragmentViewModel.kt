package com.tubewiki.hospital.viewmodel

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.api.API
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.location.DLocationTools
import com.jmbon.middleware.location.DLocationUtils
import com.jmbon.middleware.location.DLocationWhat
import com.jmbon.middleware.location.OnLocationChangeListener
import com.jmbon.middleware.utils.PermissionUtils
import com.tubewiki.home.api.HospitalApi

/**
 * @author : leimg
 * time   : 2022/7/27
 * desc   :
 * version: 1.0
 */
class HospitalFragmentViewModel : CommonViewModel() {

    /**
     * 获取公共引导banner
     * @date 2023/6/25 10:02
     */
    fun getGuideBanner() = launchWithFlow({
        val result = API.getGuideBanner()
        result.data
    }, handleError = false)

    /**
     * 编辑个人信息接口，地域参数
     * firstLocate 城市
     */
    fun uploadInfo(
        infoMap: HashMap<String, String>
    ) = launchWithFlow({
        HospitalApi.setUserInfo(
           infoMap
        )
    }, handleError = false)

    /**
     * 推荐医院
     * hasLocal 1定位，0未定位
     */
    fun hospitalRecommend(
        lng: Double,
        lat: Double,
        hasLocal: Int,
        cityId: Int,
        cityPinyin: String
    ) = launchWithFlow({
        HospitalApi.hospitalRecommend(
            lng,
            lat,
            hasLocal,
            cityId,
            cityPinyin
        )
    }, handleError = false)


    /**
     * 找医院
     * hasLocal 1定位，0未定位
     * type：1.综合排序，2.医院等级排序，3.入驻医生数排序,默认综合排序
     */
    suspend fun hospitalList(
        keyword: String,
        type: Int,
        levelIds: Int,
        lng: Double,
        lat: Double,
        hasLocal: Int,
        cityId: Int,
        cityPinyin: String,
        page: Int,

        ) = launchWithFlow({
        HospitalApi.hospitalList(
            keyword,
            type,
            levelIds,
            lng,
            lat,
            hasLocal,
            cityId,
            cityPinyin,
            page,
        )
    }, handleError = false)


    /**
     * 同院医生
     */
    suspend fun hospitalDoctorList(
        hospitalId: Int,
        page: Int,

        ) = launchWithFlow({
        HospitalApi.hospitalDoctorList(
            hospitalId,
            page
        )
    }, handleError = false)

    fun getLocation(
        context: Context,
        onCityChangeListener: (city: String, latitude: Double, longitude: Double) -> Unit,
        onDisEnabledGPS: () -> Unit = {}
    ) {

        val arrayList: java.util.ArrayList<String> = java.util.ArrayList()
        arrayList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        arrayList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        PermissionUtils.doNeedPermissionAction2(
            context, arrayList, {
                //开始定位
                getGpsLocation(context, onCityChangeListener, onDisEnabledGPS)
            }, {
                onDisEnabledGPS()
            }, "位置",
            "定位", false
        )
    }

    private fun getGpsLocation(
        context: Context,
        onCityChangeListener: (city: String, latitude: Double, longitude: Double) -> Unit,
        onDisEnabledGPS: () -> Unit = {}
    ) {
        DLocationUtils.init(context)
        val state = DLocationUtils.getInstance().register(object : OnLocationChangeListener {
            override fun getLastKnownLocation(location: Location?) {
                location?.let {
//                    val city = DLocationTools.getLocality(
//                        context,
//                        location.latitude,
//                        location.longitude
//                    )
//                    onCityChangeListener(city)
                }
            }

            override fun onLocationChanged(location: Location?) {
                location?.let {
                    val city = DLocationTools.getLocality(
                        context,
                        location.latitude,
                        location.longitude
                    )
                    onCityChangeListener(city, location.latitude, location.longitude)
                }

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

        })
        if (state == DLocationWhat.NO_PROVIDER || state == DLocationWhat.NO_LOCATIONMANAGER) {
            "请打开系统定位功能".showToast()
            onDisEnabledGPS()
        }
    }

}