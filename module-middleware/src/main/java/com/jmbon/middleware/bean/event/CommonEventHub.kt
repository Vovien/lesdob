package com.jmbon.middleware.bean.event

/******************************************************************************
 * Description: EventBus的事件仓库
 *
 * Author: jhg
 *
 * Date: 2023/3/27
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object CommonEventHub {

    /**
     * 推荐页面回到顶部
     */
    class ToTopEvent

    /**
     * 更新搜索页面主题事件
     * @param isLight: 是否为亮色主题
     * @param fromCancel: 是否是因为取消操作
     * @param clearHeaderBg: 是否需要情况Header背景
     */
    class UpdateSearchThemeEvent(
        val isLight: Boolean,
        val fromCancel: Boolean = false,
        val clearHeaderBg: Boolean = false
    )

    /**
     * 恢复首页Tab的样式事件
     */
    class ResetHomeTabStyle(val isRecommendPage: Boolean)


    /**
     * 修改当前位置
     */
    class ChangeCityEvent(
        val countryName: String = "",
        val cityName: String = "",
        val needClosePage: Boolean = true,
        val cityId: Int = 0,
        val byClick: Boolean = true
    )

    /**
     * 切换搜索页面的Tab
     */
    class SwitchTabEvent(val tabIndex: Int)

    /**
     * 收到人工回复消息
     */
    class ReceiveReplyEvent(val replyInfo: String?)

    /**
     * 切换RadioButton
     */
    class TabEvent(val index: Int)

    /**
     * 切换HomePage
     */
    class HomePageEvent(val index: Int)

    /**
     * 切换接好运Tab
     */
    class PregnantPageEvent(val index: Int)


    /**
     * 点击视频
     */
    class VideoPageEvent(val index: Int)

    class PageEvent(val index: Int)
}