package com.tubewiki.hospital.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.tubewiki.home.api.HospitalApi

/**
 * @author : leimg
 * time   : 2022/7/27
 * desc   :
 * version: 1.0
 */
class HospitalDoctorViewModel : BaseViewModel() {
    /**
     * 医院详情
     * hasLocal 1定位，0未定位
     */
    fun hospitalDetail(
        hospitalId: Int,

        ) = launchWithFlow({
        HospitalApi.hospitalDetail(
            hospitalId
        )
    }, handleError = false)

    /**
     * 医院详情
     * hasLocal 1定位，0未定位
     */
    fun doctorDetail(
        doctorId: Int,

        ) = launchWithFlow({
        HospitalApi.doctorDetail(
            doctorId
        )
    }, handleError = false)
}