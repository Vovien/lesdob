package com.tubewiki.mine.view.model

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.bean.Question
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.tubewiki.mine.R
import com.tubewiki.mine.api.FocusApi
import com.tubewiki.mine.bean.FollowColumnData
import com.tubewiki.mine.bean.FollowTopicData
import com.tubewiki.mine.bean.FollowUserData

class FollowViewModel : BaseViewModel() {


    val followQuestionResult =
        SingleLiveEvent<ResultThreeData<ArrayList<Question>, Boolean, Boolean>>()
    var followPage = 1

    /**
     * 获取关注列表-问题
     */
    fun getFollowQuestionData(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                followPage = 1
            }
            FocusApi.getFollowQuestionData(followPage)
        }, {
            if (it.questions.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                followPage++
                followQuestionResult.postValue(
                    ResultThreeData(
                        it.questions,
                        isRefresh,
                        it.pageCount < followPage
                    )
                )
            }

        }, {
            defLayout.showError.call()
            it.message.showToast()
        }, isShowDialog = false, handleError = false)
    }

    val followColumnResult =
        SingleLiveEvent<ResultThreeData<ArrayList<FollowColumnData.Column>, Boolean, Boolean>>()

    /**
     * 获取关注列表-专栏
     */
    fun getFollowColumnData(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                followPage = 1
            }
            FocusApi.getFollowColumnData(followPage)
        }, {
            if (it.columns.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                followPage++
                followColumnResult.postValue(
                    ResultThreeData(
                        it.columns,
                        isRefresh,
                        it.pageCount < followPage
                    )
                )
            }

        }, {
            defLayout.showError.call()
            it.message.showToast()
        }, isShowDialog = false, handleError = false)
    }

    val followTopicResult =
        SingleLiveEvent<ResultThreeData<ArrayList<FollowTopicData.Topic>, Boolean, Boolean>>()

    /**
     * 获取关注列表-话题
     */
    fun getFollowTopicData(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                followPage = 1
            }
            FocusApi.getFollowTopicData(followPage)
        }, {
            if (it.topics.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                followPage++
                followTopicResult.postValue(
                    ResultThreeData(
                        it.topics,
                        isRefresh,
                        it.pageCount < followPage
                    )
                )
            }

        }, {
            defLayout.showError.call()
            it.message.showToast()
        }, isShowDialog = false, handleError = false)
    }

    val followUserResult =
        SingleLiveEvent<ResultThreeData<ArrayList<FollowUserData.User>, Boolean, Boolean>>()

    /**
     * 获取关注列表-话题
     */
    fun getFollowUserData(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                followPage = 1
            }
            FocusApi.getFollowUserData(followPage)
        }, {
            if (it.users.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                followPage++
                followUserResult.postValue(
                    ResultThreeData(
                        it.users,
                        isRefresh,
                        it.pageCount < followPage
                    )
                )
            }

        }, {
            defLayout.showError.call()
            it.message.showToast()
        }, isShowDialog = false, handleError = false)
    }


    val followDoctorResult =
        SingleLiveEvent<ResultThreeData<ArrayList<FollowUserData.User>, Boolean, Boolean>>()

    /**
     * 获取关注列表-医生
     */
    fun getFollowDoctorData(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                followPage = 1
            }
            FocusApi.getFollowDoctorData(followPage)
        }, {
            if (it.doctors.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                followPage++
                followDoctorResult.postValue(
                    ResultThreeData(
                        it.doctors,
                        isRefresh,
                        it.pageCount < followPage
                    )
                )
            }

        }, {
            defLayout.showError.call()
            it.message.showToast()
        }, isShowDialog = false, handleError = false)
    }

    /**
     * 关注、取消关注用户
     * 关注用户的ID
     * focus或unfocus,即关注或取消
     */
    fun focusUser(
        focusUserId: Int,
        isFocus: Boolean,
        position: Int
    ) {
        if (Constant.getUserId() == focusUserId) {
            R.string.not_focus_self.showToast()
            KotlinBus.post(FocusChangedEvent.focusUser(focusUserId, false))
            return
        }
        launchOnlyResult(
            {
                FocusApi.focusUser(focusUserId, if (isFocus) "focus" else "unfocus")
            },
            {
                //关注或者取消关注成功
                KotlinBus.post(FocusChangedEvent.focusUser(focusUserId, isFocus))
            },
            {
                //关注或者取消关注失败
                KotlinBus.post(FocusChangedEvent.focusUser(focusUserId, isFocus))
                it.message.showToast()
            }, handleError = false
        )
    }

    val focusColumnResult = SingleLiveEvent<ResultThreeData<Int, Boolean, Boolean>>()

    /**
     * 关注专栏
     */
    fun columnFocus(position: Int, columnId: Int, isFocus: Boolean) {
        launchOnlyResult({
            FocusApi.columnFocus(columnId, if (isFocus) "focus" else "unfocus")
        }, {
            KotlinBus.post(FocusChangedEvent.focusColumn(columnId, isFocus))
        }, {
            KotlinBus.post(FocusChangedEvent.focusColumn(columnId, !isFocus))
            it.message.showToast()
        }, handleError = false)
    }

    val focusQuestionResult = SingleLiveEvent<ResultThreeData<Int, Boolean, Boolean>>()

    /**
     * 关注问题
     */
    fun focusQuestion(position: Int, questionId: Int, isFocus: Boolean) {
        launchOnlyResult({
            FocusApi.focusQuestion(questionId, if (isFocus) "focus" else "unfocus")
        }, {
            KotlinBus.post(FocusChangedEvent.focusQuestion(questionId, isFocus))
        }, {
            it.message.showToast()
            KotlinBus.post(FocusChangedEvent.focusQuestion(questionId, !isFocus))
        }, handleError = false)
    }

    val focusTopicResult = SingleLiveEvent<ResultThreeData<Int, Boolean, Boolean>>()

    /**
     * 关注话题
     */
    fun topicFocus(topicID: Int, isFocus: Boolean, position: Int) {
        launchOnlyResult({
            FocusApi.topicFocus(topicID, if (isFocus) "focus" else "unfocus")
        }, {
            KotlinBus.post(FocusChangedEvent.focusTopic(topicID, isFocus))
        }, {
            KotlinBus.post(FocusChangedEvent.focusTopic(topicID, !isFocus))
            it.message.showToast()
        }, handleError = false)
    }

}