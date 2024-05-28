package com.jmbon.middleware.utils

/**
 * @author : leimg
 * time   : 2022/5/25
 * desc   :
 * version: 1.0
 */
object ShareRandomUtils {
    /**
     * 圈子分享
     */
    fun getShareCircleDesc(): String {
        return "${getShareFiveHDRandom()}位姐妹正在热议，赶紧加入群聊吧！"
    }

    /**
     * 提问分享
     */
    fun getShareQuestionDesc(): String {
        return "${getShareTwoHDRandom()}位姐妹正在围观该问题"
    }

    /**
     * 回答分享
     * name:用户名称
     */
    fun getShareAnswerDesc(name: String): String {
        return "${name}回答了该问题，快来看看吧！"
    }

    /**
     * 文章分享
     */
    fun getShareArticleDesc(): String {
        return "好文推荐，${getShareFiveHDRandom()}位姐妹已经收藏了！"
    }

    /**
     * 视频分享
     */
    fun getShareVideoDesc(): String {
        return "此视频已被${getShareFiveHDRandom()}位姐妹分享，快来看看吧！"
    }

    /**
     * 取500-1000的随机数
     */
    fun getShareFiveHDRandom(): Int {
        return (Math.random() * 500 + 500).toInt()
    }

    /**
     * 取200-1000的随机数
     */
    fun getShareTwoHDRandom(): Int {
        return (Math.random() * 800 + 200).toInt()
    }


}