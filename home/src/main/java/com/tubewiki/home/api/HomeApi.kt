package com.tubewiki.home.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.VersionInfo
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpDefault
import com.tubewiki.home.bean.ArticleRelatedBean
import com.tubewiki.home.bean.CityListInfoBean
import com.tubewiki.home.bean.CommonQuestionAdvBean
import com.tubewiki.home.bean.CommonQuestionBean
import com.tubewiki.home.bean.FeedbackResult
import com.tubewiki.home.bean.HelperGroupItemBean
import com.tubewiki.home.bean.HomeBean
import com.tubewiki.home.bean.HomeHeaderBean
import com.tubewiki.home.bean.QuestionCategoryBean
import com.tubewiki.home.bean.RecommendData
import com.tubewiki.home.bean.SubjectArticleBean
import com.tubewiki.home.bean.TopicDetail
import com.tubewiki.home.bean.TubeStepBean
import com.tubewiki.home.bean.TubeStepBeans
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import rxhttp.async
import rxhttp.wrapper.param.toResponse

@Keep
object HomeApi {

    /**
     * 首页数据聚合
     */
    suspend fun index(): HomeBean {
        return Http.post("index")
            .toResponse<HomeBean>().await()
    }

    /**
     * 首页推荐知识
     */
    suspend fun knowledgeReset(): RecommendData {
        return Http.post("recommend/knowledge/reset")
            .toResponse<RecommendData>().await()
    }

    suspend fun topicHeaderData(
        topicId: Int,
    ): TopicDetail {
        return HttpDefault.post("topic/detail")
            .add("topic_id", topicId)
            .toResponse<TopicDetail>().await()
    }

    suspend fun topicList(
        topicId: Int,
        page: Int,
        pageSize: Int = 16
    ): SubjectArticleBean {
        return HttpDefault.post("topic/articles")
            .add("topic_id", topicId)
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<SubjectArticleBean>().await()
    }

    /**
     * 全部专栏接口
     */
    suspend fun topicAllList(page: Int, pageSize: Int = Constant.PAGE_SIZE): RecommendData {
        return Http.post("topic/list")
            .add("page", page)
            .add("pageSize", pageSize)
            .toResponse<RecommendData>().await()
    }

    /**
     * 获取文章详情
     */
    suspend fun getArticleDetail(id: Int, scope: CoroutineScope): Deferred<TubeArticleDetail> {
        return HttpDefault.post("article/detail")
            .add("article_id", id)
            .toResponse<TubeArticleDetail>().async(scope)
    }

    /**
     * 收藏/取消收藏文章
     * @param id: 文章id
     * @param isCollect: 是否是收藏操作, true表示收藏, false表示取消收藏
     */
    suspend fun collectArticle(id: Int, isCollect: Boolean): Boolean {
        return HttpDefault.post("article/collect")
            .add("article_id", id)
            .add("operate_type", if (isCollect) "add" else "del")
            .toResponse<Boolean>()
            .await()
    }

    /**
     * 获取文章索引
     */
    suspend fun getArticleRelated(id: Int, scope: CoroutineScope): Deferred<ArticleRelatedBean> {
        return Http.post("article/index/bind/list")
            .add("articleId", id)
            .toResponse<ArticleRelatedBean>().async(scope)
    }

    /**
     * 文章反馈
     */
    suspend fun feedback(itemId: Int, type: Int): EmptyData {
        return Http.post("content/helpful/feedback")
            .add("id", itemId)
            .add("unhelpfulType", type)
            .toResponse<EmptyData>().await()
    }

    /**
     * 文章反馈有无帮助 help
     * itemType 类型：1文章
     * isHelpful 有无帮助1有；2无
     */
    suspend fun feedbackHelp(
        itemId: Int,
        itemTitle: String,
        isHelpful: Int,
        itemType: Int = 1
    ): FeedbackResult {
        return Http.post("${BuildConfig.BASE_URL}app/content/helpful/add")
            .add("itemId", itemId)
            .add("itemTitle", itemTitle)
            .add("itemType", itemType)
            .add("isHelpful", isHelpful)
            .toResponse<FeedbackResult>().await()
    }

    /**
     * 常见问题列表
     */
    suspend fun problemList(): CommonQuestionBean {
        return Http.post("problem/list")
            .toResponse<CommonQuestionBean>().await()
    }

    /**
     * 试管流程一级
     */
    suspend fun tubeProcessList(firstId: Int): TubeStepBeans {
        return Http.post("tube/process/list")
            .add("firstId", firstId)
            .toResponse<TubeStepBeans>().await()
    }

    /**
     * 试管流程二级
     */
    suspend fun tubeSecondProcessList(firstId: Int): TubeStepBean {
        return Http.post("tube/process/second/list")
            .add("firstId", firstId)
            .toResponse<TubeStepBean>().await()
    }


    /**
     * checkVersion/guide/submit
     */
    suspend fun checkVersion(): VersionInfo {
        return Http.post("s4/conf/current_version")
            .toResponse<VersionInfo>().await()
    }


    /**
     * 获取城市列表
     */
    suspend fun getCityList(): CityListInfoBean {
        return Http.post("common/city/get_list")
            .toResponse<CityListInfoBean>().await()
    }


    /**
     * 常见问题广告
     * @date 2023/8/7 9:04
     * @param
     */
    suspend fun getCommonQuestionAdv(): CommonQuestionAdvBean {
        return Http.post("problem/adv/get_list")
            .toResponse<CommonQuestionAdvBean>().await()
    }


    /**
     * 好孕互助群列表
     * @date 2023/9/11 14:08
     */
    suspend fun getHelperGroupTypes(type: Int): HelperGroupItemBean {
        return Http.post("api/helper/group/list")
            .add("type", type)
            .toResponse<HelperGroupItemBean>().await()
    }

    /**
     * 获取会话记录列表
     */
    suspend fun getQuestionCategory(categoryId: Int): QuestionCategoryBean {
        return Http.post("ai_question_list")
            .add("cate_id", categoryId)
            .toResponse<QuestionCategoryBean>().await()
    }

    /**
     * 获取首页头部配置信息
     */
    suspend fun getHomeHeaderInfo(): HomeHeaderBean {
        return Http.post("index")
            .toResponse<HomeHeaderBean>().await()
    }
}