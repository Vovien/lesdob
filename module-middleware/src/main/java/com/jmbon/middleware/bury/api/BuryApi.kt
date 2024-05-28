package com.jmbon.middleware.bury.api

import com.jmbon.middleware.config.Constant
import rxhttp.wrapper.param.toResponse

/******************************************************************************
 * Description: 埋点相关的接口
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
internal object BuryApi {

    /**
     * 埋点
     */
    suspend fun buryPoint(json: String) {
        return BuryHttp.post("point")
            .add("uid", Constant.getRealUserId())
            .add("json_list", json)
            .toResponse<Unit>().await()
    }
}