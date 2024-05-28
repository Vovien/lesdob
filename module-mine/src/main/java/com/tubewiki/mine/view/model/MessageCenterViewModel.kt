package com.tubewiki.mine.view.model

import android.util.Log
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.bean.*
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.api.MessageApi
import com.tubewiki.mine.base.MessageViewModel
import com.tubewiki.mine.bean.*

class MessageCenterViewModel : MessageViewModel() {

    val userSignDataResult = SingleLiveEvent<String>()

    fun getUserSign() {
        launchOnlyResult({
            MessageApi.getUserSig()
        }, {
            userSignDataResult.postValue(it.userSig)
        }, {
            it.message.showToast()
        }, isShowDialog = false)
    }


    val rewardMessageDataResult =
        SingleLiveEvent<ResultThreeData<ArrayList<RewardMessageData.Reward>, Boolean, Boolean>>()
    private var rewardMessagePage = 1
    fun getRewardMessage(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                rewardMessagePage = 1
            }
            MessageApi.rewardMessage(rewardMessagePage)
        }, {
            if (it.rewards.isNotNullEmpty()) {
                defLayout.showContent.call()
                rewardMessageDataResult.postValue(
                    ResultThreeData(
                        it.rewards,
                        isRefresh,
                        rewardMessagePage >= it.pageCount
                    )
                )
                rewardMessagePage++
            } else {
                defLayout.showEmpty.call()
                rewardMessageDataResult.postValue(
                    ResultThreeData(
                        it.rewards,
                        isRefresh,
                        false
                    )
                )
            }
        }, {
            it.message.showToast()
            defLayout.showErrorMsg.call()
        }, isShowDialog = false)
    }


    val commentMessageDataResult =
        SingleLiveEvent<ResultThreeData<ArrayList<CommentMessageData.Comment>, Boolean, Boolean>>()
    private var commentMessagePage = 1
    fun getCommentMessage(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                commentMessagePage = 1
            }
            MessageApi.commentMessage(commentMessagePage)
        }, {
            if (it.comments.isNotNullEmpty()) {
                defLayout.showContent.call()
                commentMessageDataResult.postValue(
                    ResultThreeData(
                        it.comments,
                        isRefresh,
                        commentMessagePage >= it.pageCount
                    )
                )
                commentMessagePage++
            } else {
                defLayout.showEmpty.call()
                commentMessageDataResult.postValue(
                    ResultThreeData(
                        it.comments,
                        isRefresh,
                        false
                    )
                )
            }
        }, {
            it.message.showToast()
            defLayout.showErrorMsg.call()
        }, isShowDialog = false)
    }

    val answerMessageDataResult =
        SingleLiveEvent<ResultThreeData<ArrayList<AnswerMessageData.Answer>, Boolean, Boolean>>()
    private var answerMessagePage = 1
    fun getAnswerMessage(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                answerMessagePage = 1
            }
            MessageApi.answerMessage(answerMessagePage)
        }, {
            if (it.answers.isNotNullEmpty()) {
                defLayout.showContent.call()
                answerMessageDataResult.postValue(
                    ResultThreeData(
                        it.answers,
                        isRefresh,
                        answerMessagePage >= it.pageCount
                    )
                )
                answerMessagePage++
            } else {
                defLayout.showEmpty.call()
                answerMessageDataResult.postValue(
                    ResultThreeData(
                        it.answers,
                        isRefresh,
                        false
                    )
                )
            }
        }, {
            it.message.showToast()
            defLayout.showErrorMsg.call()
        }, isShowDialog = false)
    }


    val fansMessageDataResult =
        SingleLiveEvent<ResultThreeData<ArrayList<User>, Boolean, Boolean>>()
    private var fansMessagePage = 1
    fun getFansMessage(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                fansMessagePage = 1
            }
            MessageApi.fansMessage(fansMessagePage)
        }, {
            if (it.fans.isNotNullEmpty()) {
                defLayout.showContent.call()
                fansMessageDataResult.postValue(
                    ResultThreeData(
                        it.fans,
                        isRefresh,
                        it.fans.size < Constant.PAGE_SIZE
                    )
                )
                fansMessagePage++
            } else {
                defLayout.showEmpty.call()
                fansMessageDataResult.postValue(
                    ResultThreeData(
                        null,
                        false,
                        true
                    )
                )
            }
        }, {
            it.message.showToast()
            defLayout.showErrorMsg.call()
        }, isShowDialog = false)
    }


    val focusUserResult = SingleLiveEvent<ResultThreeData<Boolean, Boolean, Int>>()

    /**
     * 关注、取消关注用户
     * 关注用户的ID
     * focus或unfocus,即关注或取消
     */
    fun focusUser(
        focusUserId: Int,
        isFocus: Boolean,
        position: Int,
    ) {

        launchOnlyResult(
            {
                MessageApi.focusUser(focusUserId, if (isFocus) "focus" else "unfocus")
            },
            {
                //关注或者取消关注成功
                KotlinBus.post(FocusChangedEvent.focusUser(focusUserId, isFocus))
            },

            {
                if (it.code.toInt() == 802) {
                    //已经关注
                    KotlinBus.post(FocusChangedEvent.focusUser(focusUserId, true))
                } else {
                    it.message.showToast()
                    //关注或者取消关注失败
                    KotlinBus.post(FocusChangedEvent.focusUser(focusUserId, !isFocus))
                }
            }, isShowDialog = false, handleError = false
        )
    }


    val mutuallyMessageDataResult =
        SingleLiveEvent<ResultThreeData<ArrayList<MutuallyMessageData.Message>, Boolean, Boolean>>()
    private var mutuallyMessagePage = 1
    fun getMutuallyMessage(type: String, isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                mutuallyMessagePage = 1
            }
            MessageApi.interactionMessage(type, mutuallyMessagePage)
        }, {
            if (it.messages.isNotNullEmpty()) {
                defLayout.showContent.call()
                mutuallyMessageDataResult.postValue(
                    ResultThreeData(
                        it.messages,
                        isRefresh,
                        mutuallyMessagePage >= it.pageCount
                    )
                )
                mutuallyMessagePage++
            } else {
                defLayout.showEmpty.call()
            }
        }, {
            it.message.showToast()
            defLayout.showErrorMsg.call()
        }, isShowDialog = false)
    }


    val officialMessageDataResult =
        SingleLiveEvent<ResultThreeData<ArrayList<OfficialMessageData.Official>, Boolean, Boolean>>()
    private var officialMessagePage = 1
    fun getOfficialMessage(type: String, isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                officialMessagePage = 1
            }
            MessageApi.officialMessage(type, officialMessagePage)
        }, {
            if (it.officials.isNotNullEmpty() || !isRefresh) {
                defLayout.showContent.call()
                officialMessageDataResult.postValue(
                    ResultThreeData(
                        it.officials,
                        isRefresh,
                        officialMessagePage >= it.pageCount
                    )
                )
                officialMessagePage++
            } else {
                defLayout.showEmpty.call()
                officialMessageDataResult.postValue(
                    ResultThreeData(
                        it.officials,
                        isRefresh,
                        officialMessagePage >= it.pageCount
                    )
                )
            }
        }, {
            if (isRefresh) {
                defLayout.showErrorMsg.call()
            }
            it.message.showToast()
        }, isShowDialog = false)
    }

    fun getOfficialPhone(result: (phone: String) -> Unit) {
        launchOnlyResult({

            MessageApi.officialMobile()
        }, {
            result(it.mobile)
        }, {
            it.message.showToast()
        }, isShowDialog = false)
    }

    val articleDetails = SingleLiveEvent<ArticleDetails.Article>()

    fun getArticleDetails(id: Int) {
        launchOnlyResult({
            MessageApi.violationArticleDetails(id)
        }, {
            articleDetails.postValue(it.article)
        }, {
            it.message.showToast()
            articleDetails.postValue(null)
        }, {
            defLayout.showContent.call()
        })
    }

    /**
     * 获取违规问题回答详情
     */
    fun getViolationQADetail(
        id: Int,
        type: String = "question",
    ) = launchWithFlow({
        MessageApi.violationQuestionDetail(id, type)
    }, handleError = false)

    /**
     * 获取审核消息详情
     */
    fun getExamineDetail(
        id: Int
    ) = launchWithFlow({
        MessageApi.getExamineDetail(id)
    }, handleError = false)


    /**
     * 获取消息条数
     */
    fun getMessagePoint(
        result: (numbers: MessagePointBean, totalNum: Int) -> Unit
    ) {
        launchOnlyResult({
            //获取系统消息数量
            MessageApi.getMessagePoint()
        }, {
            var total =
                it.numbers.answer + it.numbers.comment + it.numbers.fans + it.numbers.reward + it.numbers.offical + it.numbers.interaction
            result(
                it, total
            )
        }, {
            it.message.showToast()
        }, isShowDialog = false, handleError = false)
    }
}