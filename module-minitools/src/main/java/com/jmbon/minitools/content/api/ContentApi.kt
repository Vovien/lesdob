package com.jmbon.minitools.content.api

import com.jmbon.middleware.config.network.HttpDefault
import com.jmbon.minitools.content.bean.HelpGroupBean
import com.jmbon.minitools.content.bean.KnowledgeInfoBean
import rxhttp.wrapper.param.toResponse

/******************************************************************************
 * Description:
 *
 * Author: jhg
 *
 * Date: 2023/10/23
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object ContentApi {

    /**
     * 获取指定类型的互助群列表
     * @param type: 互助群的类型
     */
    suspend fun getHelperGroupList(type: Int): HelpGroupBean {
        return HttpDefault.post("selected/circle/list")
            .add("group_type", type)
            .toResponse<HelpGroupBean>().await()
    }

    /**
     * 获取知识信息
     */
    suspend fun getKnowledgeInfo(): KnowledgeInfoBean {
        return HttpDefault.post("index/tab/knowledge")
            .toResponse<KnowledgeInfoBean>().await()
    }
}