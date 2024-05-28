package com.tubewiki.home.api

import com.jmbon.middleware.config.network.Http
import com.tubewiki.home.bean.SchemeConfigBean
import com.tubewiki.home.bean.SchemeDetailBean
import rxhttp.wrapper.param.toResponse

/******************************************************************************
 * Description: 方案相关的接口
 *
 * Author: jhg
 *
 * Date: 2023/6/14
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object SchemeApi {

    /**
     * 获取方案详情
     * @param type: 弹窗类型【1：获取方案，2：定制生育方案，3：预约医生，4：预约医院】
     */
    suspend fun getSchemeConfig(type: Int): SchemeConfigBean {
        return Http.post("common/popup/get")
            .add("type", type)
            .toResponse<SchemeConfigBean>().await()
    }
}