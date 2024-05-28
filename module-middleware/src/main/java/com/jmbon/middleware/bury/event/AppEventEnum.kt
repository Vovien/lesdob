package com.jmbon.middleware.bury.event

/******************************************************************************
 * Description: App埋点事件
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
enum class AppEventEnum(val value: String) {
    /**
     * 启动App
     */
    EVENT_START_APP("EventStartApp"),

    /**
     * App切到后台
     */
    EVENT_BACK_APP("EventBackApp"),

    /**
     * 热力图记录
     */
    EVENT_HOT_IMAGE("EventHeatMap"),

    /**
     * 分享app回流事件
     */
    EVENT_OPEN_APP_BY_SHARE_URL("EventShareAppBackflow"),
}