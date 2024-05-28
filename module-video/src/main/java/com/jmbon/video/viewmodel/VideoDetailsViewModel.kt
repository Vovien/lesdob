package com.jmbon.video.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.jmbon.video.api.VideoApi
import kotlinx.coroutines.flow.map

class VideoDetailsViewModel : BaseViewModel() {

    /**
     * 获取视频列表
     * @date 2023/9/11 16:51
     */
    fun getAllVideoList() = launchWithFlow({
        VideoApi.getVideoList()
    }, handleError = true).map {
        it.data
    }
}