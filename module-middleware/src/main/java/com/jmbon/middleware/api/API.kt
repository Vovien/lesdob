package com.jmbon.middleware.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.blankj.utilcode.util.DeviceUtils
import com.jmbon.middleware.bean.CommonAdv
import com.jmbon.middleware.bean.CommonBannerBean
import com.jmbon.middleware.bean.GeneralConfBean
import com.jmbon.middleware.bean.LocalAddressInfoBean
import com.jmbon.middleware.bean.MinProgramConfig
import com.jmbon.middleware.bean.PopupImageBean
import com.jmbon.middleware.bean.StsEntity
import com.jmbon.middleware.bean.VodEntity
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.*
import rxhttp.wrapper.param.toResponse
@Keep
object API {
    //uploads/answer/20210413（日期不够10需要补0）/文件名
    // 图片限制5M，超过压缩
    // 获取oss的信息，为oss传输准备
    suspend fun getSts(): StsEntity {
        return HttpCommon.post("v1/oss_sts")
            .toResponse<StsEntity>().await()
    }

    // vod 上传参数
    suspend fun getVod(): VodEntity {
        return HttpV4.post("common/get_video_sts")
            .toResponse<VodEntity>().await()
    }



    /**
     * /api/v1/user/focus
     * 关注、取消关注用户
     * 关注用户的ID
     * focus或unfocus,即关注或取消
     */
    suspend fun focusUser(uid: Int, type: String): EmptyData {
        return Http.post("user/focus")
            .add("focus_user_id", uid)
            .add("type", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * 登录成功后 绑定设备device_id
     * /api/v1/app/v2/user/binding_device
     */
    suspend fun bindDevice(deviceId:String = Constant.getDeviceId()): EmptyData {
        return Http.post("user/bind/device")
            .add("device_id", deviceId)
            .add("device_type", "android")
            .toResponse<EmptyData>().await()
    }

    /**
     * app启动激活
     */
    suspend fun appActive(channel:String): EmptyData {
        return Http.post("app/active")
            .add("channel", channel)
            .toResponse<EmptyData>().await()
    }

    /**
     * 平台纠错
     * /api/v1/app/v3/user/error_correction
     * @param type 类型【1：内容有错误，2：部分内容缺失】
     * @param images 对应图片，多个用英文逗号隔开
     * @param description 描述信息
     * @param email 邮箱
     * @param contentType 内容类型【1：文章，2：医生，3：医院】
     */
    suspend fun errorCorrection(
        type: Int,
        images: String,
        description: String,
        email: String,
        contentType: Int,
        itemId: Int,
    ): EmptyData {
        return HttpCommon.post("v1/mistake_add")
            .add("type", type)
            .add("images", images)
            .add("description", description)
            .add("email", email)
            .add("content_type", contentType)
            .add("item_id", itemId)
            .toResponse<EmptyData>().await()
    }

    /**
     * 根据IP地址获取地址信息
     * @param ip: IP地址
     */
    suspend fun getAddressInfoByIP() =
        Http.post("common/ip/get_address")
            .toResponse<LocalAddressInfoBean>()
            .await()

    /**
     * 获取公共引导banner【消息中心与个人中心banner】
     * @date 2023/6/25 9:58
     */
    suspend fun getGuideBanner(): CommonAdv {
        return Http.post("common/banner/get_guide")
            .toResponse<CommonAdv>().await()
    }

    /**
     * 获取公共的Banner
     * @date 2023/7/28 18:03
     */
    suspend fun getCommonBanner(): CommonBannerBean {
        return Http.post("common/banner/get_list")
            .toResponse<CommonBannerBean>().await()
    }

    /**
     * 通用配置项
     */
    suspend fun generalConf(): GeneralConfBean {
        return Http.post("common/config").toResponse<GeneralConfBean>().await()
    }

    suspend fun getPopupImage(): PopupImageBean {
        return HttpCommon.post("v1/pop_ad")
            .toResponse<PopupImageBean>().await()
    }

    /**
     * 上报OAID
     */
    suspend fun uploadOAID(oaid: String?) {
        return HttpCommon.post("v1/oaid_report")
            .add("device_model", DeviceUtils.getModel())
            .add("sys_version", DeviceUtils.getSDKVersionName())
            .toResponse<Unit>().await()
    }

    /**
     * 激活App
     */
    suspend fun activeApp(oaid: String?) {
        return HttpCommon.post("/v2/app_install")
            .add("unique_device_id", oaid)
            .toResponse<Unit>().await()
    }

    /**
     * 获取小程序配置信息
     */
    suspend fun getMinProgramConfig(): MinProgramConfig {
        return HttpCommon.post("v1/we_app_config")
            .toResponse<MinProgramConfig>().await()
    }
}


