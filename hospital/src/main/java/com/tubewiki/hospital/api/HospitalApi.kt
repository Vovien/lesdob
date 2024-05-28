package com.tubewiki.home.api


import androidx.annotation.Keep
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.tubewiki.hospital.bean.DoctorDetailBean
import com.tubewiki.hospital.bean.FindHospitalBean
import com.tubewiki.hospital.bean.HospitalDetailBean
import com.tubewiki.hospital.bean.RecommendHospitalBean

import rxhttp.wrapper.param.toResponse

@Keep
object HospitalApi {
    /**
     * 编辑资料
     * /api/v1/user/set_info
     *
     *性别：1男2女
     * 是否显示性别：0显示1不显示
     */
    suspend fun setUserInfo(infoMap: HashMap<String, String>): String {
        return Http.post("user/info/edit")
            .addAll(infoMap)
            .toResponse<String>().await()
    }

    /**
     * 推荐医院
     * hasLocal 1定位，0未定位
     */
    suspend fun hospitalRecommend(
        lng: Double,
        lat: Double,
        hasLocal: Int,
        cityId: Int,
        cityPinyin: String
    ): RecommendHospitalBean {
        return Http.post("hospital/recommend")
            .add("lng", lng)
            .add("lat", lat)
            .add("hasLocal", hasLocal)
            .add("cityId", cityId)
            .add("pinyin", cityPinyin)
            .toResponse<RecommendHospitalBean>().await()
    }

    /**
     * 找医院
     * hasLocal 1定位，0未定位
     * type：1.综合排序，2.医院等级排序，3.入驻医生数排序,默认综合排序
     * levelIds 1三甲 2三乙 3以上
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
        pageSize: Int = Constant.PAGE_SIZE,

        ): FindHospitalBean {
        return Http.post("hospital/list")
            .add("keyword", keyword)
            .add("type", type)
            .add("levelIds", levelIds)
            .add("lng", lng)
            .add("lat", lat)
            .add("hasLocal", hasLocal)
            .add("cityId", cityId)
            .add("pinyin", cityPinyin)
            .add("page", page)
            .add("pageSize", pageSize)
            .toResponse<FindHospitalBean>().await()
    }


    /**
     * 医院详情
     */
    suspend fun hospitalDetail(
        hospitalId: Int,
    ): HospitalDetailBean {
        return Http.post("hospital/detail")
            .add("hospitalId", hospitalId)
            .toResponse<HospitalDetailBean>().await()
    }

    /**
     * 医生详情
     */
    suspend fun doctorDetail(
        doctorId: Int,
    ): DoctorDetailBean {
        return Http.post("doctor/detail")
            .add("doctorId", doctorId)
            .toResponse<DoctorDetailBean>().await()
    }

    /**
     * 同院医生
     */
    suspend fun hospitalDoctorList(
        hospitalId: Int,
        page: Int,
    ): HospitalDetailBean {
        return Http.post("hospital/doctor/list")
            .add("hospitalId", hospitalId)
            .add("page", page)
            .add("pageSize", Constant.PAGE_SIZE)
            .toResponse<HospitalDetailBean>().await()
    }

}