package com.jmbon.video.util

import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.bean.VideoDetail


fun VideoDetail.VideoData.getAvailablePlayAddress(): String {
    if (this.playUrlMp4.isNotNullEmpty()) {
        return this.playUrlMp4
    }
    if (this.originUrl.isNotNullEmpty()) {
        return this.originUrl
    }
    if (this.playUrlU3u8.isNotNullEmpty()) {
        return this.playUrlU3u8
    }
    return ""
}