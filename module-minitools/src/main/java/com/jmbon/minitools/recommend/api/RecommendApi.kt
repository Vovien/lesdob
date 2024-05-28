package com.jmbon.minitools.recommend.api

import com.apkdv.mvvmfast.utils.GsonUtil
import com.jmbon.middleware.config.network.Http
import com.jmbon.minitools.recommend.bean.BudgetInfoBean
import com.jmbon.minitools.recommend.bean.ChooseFormBean
import com.jmbon.minitools.recommend.bean.GuideInfoBean
import com.jmbon.minitools.recommend.bean.RecommendResultBean
import rxhttp.wrapper.param.toResponse

/******************************************************************************
 * Description: 推荐流程相关的API
 *
 * Author: jhg
 *
 * Date: 2023/6/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object RecommendApi {

    /**
     * 获取选择表单
     */
    suspend fun getChooseForm(): ChooseFormBean {
        return Http.post("user/guide/get")
            .toResponse<ChooseFormBean>().await()
    }

    /**
     * 获取预算信息
     */
    suspend fun getBudgetInfo(): BudgetInfoBean {
        return Http.post("user/budget/get")
            .toResponse<BudgetInfoBean>().await()
    }

    /**
     * 获取选择表单
     */
    suspend fun getRecommendResult(params: Map<String, Any?>?): RecommendResultBean {
        return Http.post("s4/user/guide/submit")
            .add("param", GsonUtil.gson().toJson(params))
            .toResponse<RecommendResultBean>().await()
    }

    /**
     * 获取选择表单
     * @param id: 引导页面id
     */
    suspend fun getGuideInfo(id: Int): GuideInfoBean {
        return Http.post("app/v13/user/get_guide_page")
            .add("id", id)
            .toResponse<GuideInfoBean>().await()
    }
}