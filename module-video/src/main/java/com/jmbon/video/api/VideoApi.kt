package com.jmbon.video.api

import com.jmbon.middleware.bean.VideoAdvBean
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpDefault
import com.jmbon.video.bean.VideoListDetail
import rxhttp.wrapper.param.toResponse

object VideoApi {


    suspend fun getVideoList(): VideoListDetail {
        return HttpDefault.post("api/video/list")
            .toResponse<VideoListDetail>().await()
    }

    suspend fun getVideoAdvList(): VideoAdvBean {
        return Http.post("api/ad/float")
            .toResponse<VideoAdvBean>().await()
    }
}