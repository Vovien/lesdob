package com.jmbon.middleware.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.ShareCircleBean
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.HttpV2
import rxhttp.wrapper.param.toResponse

/**
 * @author : leimg
 * time   : 2021/9/14
 * desc   :
 * version: 1.0
 */
@Keep
object ShareApi {
    /**
     * 获取分享圈子列表
     * /api/v1/app/v2/circle/share/list
     */
    suspend fun getCircleShareList(
        circleId: String,
        page: Int,
    ): ShareCircleBean {
        return HttpV2.post("circle/share/list")
            .add("circle_id", circleId)
            .add("page", page)
            .add("page_size", Constant.PAGE_SIZE)
            .toResponse<ShareCircleBean>().await()
    }

    /**
     * 圈子内容分享
     * /api/v1/app/v2/circle/share
     * 分享类型【1：圈子，2：文章，3：求助，4：外部问题】
     * answer_id:分享问题传回答id,没有不传
     */
    suspend fun circleShare(
        circle_id: Int,
        type: Int,
        item_id: Int,
        answer_id: Int = 0,
    ): EmptyData {
        return HttpV2.post("circle/share")
            .add("circle_id", circle_id)
            .add("type", type)
            .add("item_id", item_id)
            .add("answer_id", answer_id)
            .toResponse<EmptyData>().await()
    }
}