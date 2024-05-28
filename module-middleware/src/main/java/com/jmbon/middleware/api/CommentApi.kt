package com.jmbon.middleware.api

import androidx.annotation.Keep
import com.jmbon.middleware.comment.bean.CommentList
import com.jmbon.middleware.comment.bean.GivenCount
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpV2
import com.jmbon.middleware.config.network.HttpV4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import rxhttp.async
import rxhttp.wrapper.param.toResponse

/**
 * @author : leimg
 * time   : 2021/5/6
 * desc   :
 * version: 1.0
 */
@Keep
object CommentApi {


    /**
     * 获取评论的子列表
     * /api/v1/answer/sub_list
     */
    suspend fun answerSubList(
        item_id: Int // 文章 问题 ID
        , answerId: Int = 0 // 一级评论对应的ID
        , type: String // article或question两个
        , sort: String = "default" // default或time两种排序
        , page: Int = 1, second_answer_id: Int = 0 // 请求三级评论的时候的二级评论 ID
        , page_size: Int = Constant.PAGE_SIZE
    ): CommentList {
        return Http.post("answer/sub_answer_list")
            .add("item_id", item_id)
            .add("answer_id", answerId)
            .add("type", type)
            .add("sort_type", sort)
            .add("page", page)
            .add("second_answer_id", second_answer_id)
            .add("page_size", page_size)
            .toResponse<CommentList>().await()
    }

    /**
     * desc:回答问题  评论接口
     *@param token            string		登录token
     *@param item_id            string		问题ID   问题ID
     *@param type      string	    article或question两种 默认 article  question
     *@param top_answer_id            int		对应的第一级评论ID，如果是评论问题或文章，则该参数必须传0  回答的id
     *@param second_answer_id           int	        对应的第二级评论ID,如果本身评论的是一级评论，则该参数传0  0
     *@param answer_id           int		    评论的回答ID,如果评论的是问题或文章，则该参数必须传0  评论谁传谁
     *@param content      String       评论的内容
     *@param draft_answer_id      integer       评论草稿箱ID,没有传0或不传
     */
    suspend fun answerComment(
        item_id: Int,
        content: String,
        draftAnswerId: Int = 0,
        type: String = "question",
        answer_id: Int = 0,
        clientId: String = ""
    ): CommentList {
        return HttpV4.post("answer/comment")
            .add("item_id", item_id)
            .add("content", content)
            .add("draft_answer_id", draftAnswerId)
            .add("type", type)
            .add("answer_id", answer_id)
            .add("client_id", clientId)
            .toResponse<CommentList>().await()
    }

    /**
     * 问题回答点赞或者收藏接口
     * /api/v1/answer/give_or_collect
     */
    suspend fun answerGivenCollect(answer_id: Int, type: String, operate_type: String): GivenCount {
        return Http.post("answer/give_or_collect")
            .add("answer_id", answer_id)
            .add("type", type)
            .add("operate_type", operate_type)
            .toResponse<GivenCount>().await()
    }


    /**
     * 文章点赞或者收藏接口
     * /api/v1/article/given_or_collect
     * 操作类型,allow或ban，即开启或取消，只能传这两个参数
     * 点赞或者收藏，given或collect，只能传这两个参数
     */
    suspend fun articleGivenCollect(
        article_id: Int,
        type: String,
        operate_type: String
    ): GivenCount {
        return Http.post("article/given_or_collect")
            .add("article_id", article_id)
            .add("type", type)
            .add("operate_type", operate_type)
            .toResponse<GivenCount>().await()
    }

    /**
     * desc:获取回答的评论的子评论
     * answer_id 父级评论id
     */
    suspend fun getAnswerCommentsChildV2(
        answerId: Int,
        page: Int = 1,
        type: Int = 2,
        page_size: Int = Constant.PAGE_SIZE
    ): CommentList {
        return HttpV2.post("/question/answer/comments/child_v2")
            .add("answer_id", answerId)
            .add("page", page)
            .add("type", type)
            .add("page_size", page_size)
            .toResponse<CommentList>().await()
    }

    /**
     * 获取评论内容 V2
     * question_id 问题或文章id
     * sort  default或time
     */
    suspend fun getAnswerCommentsV2(
        questionId: Int,
        answerId: Int,
        sort: String = Constant.DEFAULT,
        page: Int = 1,
        clickAnswerId: Int = 0,
        page_size: Int = Constant.PAGE_SIZE
    ): CommentList {
        return HttpV2.post("question/answer/comments_v2")
            .add("item_id", questionId)
            .add("answer_id", answerId)
            .add("sort", sort)
            .add("page", page)
            .add("blink_answer_id", clickAnswerId)
            .add("page_size", page_size)
            .toResponse<CommentList>().await()
    }


    suspend fun getAnswerCommentsV2Async(
        questionId: Int,
        answerId: Int,
        scope: CoroutineScope,
        sort: String = Constant.DEFAULT,
        page: Int = 1,
        page_size: Int = Constant.PAGE_SIZE
    ): Deferred<CommentList> {
        return Http.post("app/v2/question/answer/comments_v2")
            .add("item_id", questionId)
            .add("answer_id", answerId)
            .add("sort", sort)
            .add("page", page)
            .add("page_size", page_size)
            .toResponse<CommentList>().async(scope)
    }

    // 对应内容类型【1：文章，2：回答, 3:视频】
    suspend fun getAnswerCommentsV4Async(
        type: Int = 2,
        questionId: Int,
        answerId: Int,
        scope: CoroutineScope,
        sort: String = Constant.DEFAULT,
        page: Int = 1,
        clickAnswerId: Int = 0,
        page_size: Int = Constant.PAGE_SIZE
    ): Deferred<CommentList> {
        return HttpV4.post("answer/get_comments")
            .add("item_id", questionId)
            .add("type", type)
            .add("answer_id", answerId)
            .add("sort", sort)
            .add("blink_answer_id", clickAnswerId)
            .add("page", page)
            .add("page_size", page_size)
            .toResponse<CommentList>().async(scope)
    }

}