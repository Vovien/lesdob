package com.tubewiki.android.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpDefault
import com.jmbon.middleware.config.network.HttpV2
import com.jmbon.middleware.config.network.HttpV3
import com.tubewiki.android.bean.CircleConfigBean
import com.tubewiki.android.bean.RecommendCircle
import rxhttp.wrapper.param.toResponse
@Keep
object MainApi {

    /**
     * https://api.jmbom.com/api/v1/app/v2/report/oaid
     * 同步设备激活接口
     */
    suspend fun syncDevice(
        oaid: String,
        ip: String,
        sysVersion: String,
        deviceModel: String
    ): EmptyData {
        return HttpV2.post("report/oaid")
            .add("ip", ip)
            .add("sys_version", sysVersion)
            .add("device_model", deviceModel)
            .toResponse<EmptyData>().await()
    }

    /**
     * 点击角标
     */
    suspend fun clickMark(): EmptyData {
        return HttpV3.post("index/click_mark").toResponse<EmptyData>().await()
    }

    /**
     * /api/v1/app/v3/index/set_use_tool_status
     * 绑定小工具
     */
    suspend fun setUseToolStatus(toolId: String): EmptyData {
        return HttpV3.post("index/set_use_tool_status")
            .add("tool_id", toolId)
            .toResponse<EmptyData>().await()
    }

    /**
     * 获取圈子配置信息
     */
    suspend fun getCircleConfig(): CircleConfigBean {
        return HttpDefault.post("tab/join_group")
            .toResponse<CircleConfigBean>().await()
    }

    suspend fun getRecommendCircle(
        page: Int,
        pageSize: Int = Constant.PAGE_SIZE
    ): RecommendCircle {
        return Http.post("circle/recommend_circle_v2")
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<RecommendCircle>().await()
    }
}