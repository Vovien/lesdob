package com.jmbon.middleware.comment.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.api.CommentApi
import com.jmbon.middleware.comment.bean.CommentList
import com.jmbon.middleware.config.Constant

class CommentViewModel : BaseViewModel() {
    private var page = 1
    lateinit var comment: CommentList
    val commentList = SingleLiveEvent<ResultThreeData<CommentList, Boolean, Boolean>>()
    val keyboardHeight = SingleLiveEvent<Int>()

    /**
     * 文章或问题一级评论分页接口
     */
    fun getComments(
        itemId: Int,
        type:Int,
        sort: String = "default",
        refresh: Boolean = true,
        clickAnswerId: Int = 0
    ) {
        if (refresh)
            page = 1
        launchOnlyResult({
            CommentApi.getAnswerCommentsV4Async(type,
                itemId,
                0,
                viewScope(),
                sort,
                page,
                clickAnswerId).await()

        }, {
            val isFinish = it.answers.size < Constant.PAGE_SIZE
            comment = if (page == 1) {
                it
            } else {
                it.answers.addAll(0, comment.answers)
                it
            }
            commentList.postValue(ResultThreeData(comment, isFinish, refresh))
            page++
        }, {
            commentList.postValue(ResultThreeData(CommentList(), true, refresh))
            defLayout.showError.call()
            it.message.showToast()
        }, isShowDialog = false)
    }

    /**
     * 评论
     */
    fun answerComment(
        itemId: Int,
        content: String,
        type: String,
        answerId: Int,
        clientId: String
    ) = launchWithFlow({
        CommentApi.answerComment(
            itemId,
            content,
            0,
            type,
            answerId,
            clientId
        )
    }, handleError = false)


    val givenCount = SingleLiveEvent<ResultThreeData<ResultTwoData<Int, Boolean>, Int, Int>>()
    private fun answerGivenCollect(
        answerId: Int,
        type: String,
        status: String,
        topPosition: Int,
        position: Int
    ) {
        launchOnlyResult(
            {
                CommentApi.answerGivenCollect(answerId, type, status)
            }, {
                givenCount.postValue(
                    ResultThreeData(
                        ResultTwoData(it.count, status == "allow"),
                        topPosition,
                        position
                    )
                )
            }, {
                it.message.showToast()
            }, handleError = false
        )
    }

    fun answerGiven(answerId: Int, status: Boolean, topPosition: Int, position: Int) {
        answerGivenCollect(answerId, "given", if (status) "allow" else "ban", topPosition, position)
    }


    val answerGivenCollectionResult = SingleLiveEvent<ResultTwoData<Boolean, Boolean>>()


    fun answerGivenCollect2(
        answerId: Int, type: Boolean, status: Boolean
    ) = launchWithFlow({
        CommentApi.answerGivenCollect(
            answerId,
            if (type) "given" else "collect",
            if (status) "allow" else "ban"
        )
    }, handleError = false)


    /**
     * tips:如果问答详情的fragment滑动多个在最后后再回到上面就会不执行launchOnlyResult中的代码，推荐采用上面的flow方式解决
     * type 类型 点赞 true
     * status 问答是否点赞收藏
     */
    fun answerGivenCollect(answerId: Int, type: Boolean, status: Boolean) {
        launchOnlyResult(
            {
                //要求及时显示动画，所以不等结果
                answerGivenCollectionResult.postValue(ResultTwoData(type, status))
                CommentApi.answerGivenCollect(
                    answerId,
                    if (type) "given" else "collect",
                    if (status) "allow" else "ban"
                )
            }, {
                //answerGivenCollectionResult.postValue(ResultTwoData(type, status))
            }, {
                if (it.code.toInt() != 811) {
                    it.message.showToast()
                }
            }, handleError = true
        )
    }


//    val articleCount = SingleLiveEvent<ResultTwoData<Boolean, Boolean>>()

    /**
     * 文章点赞收藏
     * type 类型 点赞 true
     * status 是否点赞
     */
    fun articleGivenCollect2(
        articleId: Int, type: Boolean, status: Boolean
    ) = launchWithFlow({
        CommentApi.articleGivenCollect(
            articleId,
            if (type) "given" else "collect",
            if (status) "allow" else "ban"
        )
    }, handleError = false)


    /**
     * type 类型 点赞 true
     * status 是否点赞
     */
    fun articleGivenCollect(articleId: Int, type: Boolean, status: Boolean) {
        launchOnlyResult(
            {
                CommentApi.articleGivenCollect(
                    articleId,
                    if (type) "given" else "collect",
                    if (status) "allow" else "ban"
                )
            }, {
//                articleCount.postValue(ResultTwoData(type, status))
            }, {
                if (it.code.toInt() != 811) {
                    it.message.showToast()
                }
            }, handleError = false
        )
    }


    val commentResult =
        SingleLiveEvent<ResultThreeData<CommentList, Boolean, Boolean>>()

    var commentPage = 1

    /**
     * 获取评论
     */
    fun getAnswerTopComment(
        questionId: Int,
        answerId: Int,
        sort: String,
        isRefresh: Boolean,
        clickAnswerId: Int = 0
    ) {
        if (isRefresh) {
            commentPage = 1
        }
        launchOnlyResult({
            CommentApi.getAnswerCommentsV4Async(2,
                questionId,
                answerId,
                viewScope(),
                sort,
                commentPage,
                clickAnswerId).await()
        }, {
            val isFinish = it.answers.size < Constant.PAGE_SIZE
            comment = if (commentPage == 1) {
                it
            } else {
                it.answers.addAll(0, comment.answers)
                it
            }
            commentResult.postValue(ResultThreeData(comment, isFinish, isRefresh))
            commentPage++
        }, {
            commentResult.postValue(ResultThreeData(CommentList(), true, isRefresh))
            defLayout.showError.call()
            it.message.showToast()
        }, isShowDialog = false)
    }


    /**
     * 获取评论
     */
    fun getVideoTopComment(
        questionId: Int,
        sort: String,
        isRefresh: Boolean,
        clickAnswerId: Int = 0,
    ) {
        if (isRefresh) {
            commentPage = 1
        }
        launchOnlyResult({
            CommentApi.getAnswerCommentsV4Async(3,
                questionId,
                0,
                viewScope(),
                sort,
                commentPage,
                clickAnswerId).await()
        }, {
            val isFinish = it.answers.size < Constant.PAGE_SIZE
            comment = if (commentPage == 1) {
                it
            } else {
                it.answers.addAll(0, comment.answers)
                it
            }
            commentList.postValue(ResultThreeData(comment, isFinish, isRefresh))
            commentPage++
        }, {
            commentList.postValue(ResultThreeData(CommentList(), true, isRefresh))
            defLayout.showError.call()
            it.message.showToast()
        }, isShowDialog = false)
    }

    val childCommentResult =
        SingleLiveEvent<ResultThreeData<CommentList, Int, Int>>()


    /**
     * 获取子评论
     */
    fun getAnswerCommentsChild(answerId: Int, type: Int, page: Int, position: Int) {

        launchOnlyResult({
            val commentList = CommentApi.getAnswerCommentsChildV2(answerId, page, type = type)

            return@launchOnlyResult ResultThreeData(
                commentList,
                page, position
            )
        }, {
            childCommentResult.postValue(it)
        }, {
            childCommentResult.postValue(ResultThreeData(CommentList(), 1, 0))
            it.message.showToast()
        }, isShowDialog = false)

    }

}