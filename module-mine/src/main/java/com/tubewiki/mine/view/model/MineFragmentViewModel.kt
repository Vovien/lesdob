package com.tubewiki.mine.view.model

import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.api.API
import com.jmbon.middleware.bean.*
import com.jmbon.middleware.config.Constant
import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.base.MineViewModel
import com.tubewiki.mine.bean.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MineFragmentViewModel : MineViewModel() {

    private var page = 1

    /**
     * 获取公共引导banner
     * @date 2023/6/25 10:02
     */
    fun getGuideBanner() = launchWithFlow({
        val result = API.getGuideBanner()
        result.data
    }, handleError = false)

    fun getUserPost(type: String, refresh: Boolean) {
        if (refresh)
            page = 1
        launchOnlyResult({
            MineApi.getUserPost(page, type)
        }, {
            page++
        }, {
            it.message.showToast()
        })
    }

    fun getUserCollection(type: String, refresh: Boolean) {
        if (refresh)
            page = 1
        launchOnlyResult({
            MineApi.getUserCollection(page, type)
        }, {
            page++
        }, {
            it.message.showToast()
        })
    }


    val pregnantStatus = SingleLiveEvent<String>()

    /**
     * 状态：0：未设置，1：备孕中，2：已怀孕，3：有宝宝
     */
    fun setPregnantStatus(typeStr: String) {
        var type = 0
        launchOnlyResult({

            type = when (typeStr) {
                "备孕中" -> 1
                "已怀孕" -> 2
                "有宝宝" -> 3
                "试管婴儿" -> 4
                else -> 0
            }
            MineApi.setPregnantStatus(type)
        }, {
            pregnantStatus.postValue(typeStr)
        }, {
            it.message.showToast()
        }, handleError = false)
    }

    val mineAnswerResult = SingleLiveEvent<ResultThreeData<ArrayList<Question>, Boolean, Boolean>>()


    var answerPage = 1
    fun getMineAnswer(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                answerPage = 1
            }
            MineApi.getMineAnswer(answerPage)
        }, {
            if (it.answers.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                answerPage++
                mineAnswerResult.postValue(
                    ResultThreeData(
                        it.answers,
                        isRefresh,
                        it.answers.size < Constant.PAGE_SIZE
                    )
                )
            }

        })
    }


    val mineDraftResult =
        SingleLiveEvent<ResultThreeData<ArrayList<MineDraftData.Data>, Boolean, Boolean>>()

    var draftPage = 1
    fun getMineDraft(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                draftPage = 1
            }
            MineApi.getDraft(draftPage)
        }, {
            if (it.datas.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                mineDraftResult.postValue(
                    ResultThreeData(
                        it.datas,
                        isRefresh,
                        it.pageCount < draftPage
                    )
                )
                draftPage++
            }
        })
    }

    val mineQuestionResult =
        SingleLiveEvent<ResultThreeData<ArrayList<Question>, Boolean, Boolean>>()

    var questionPage = 1
    fun getMineQuestion(isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                questionPage = 1
            }
            MineApi.getMineQuestion(questionPage)
        }, {
            if (it.questions.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                questionPage++
                mineQuestionResult.postValue(
                    ResultThreeData(
                        it.questions,
                        isRefresh,
                        it.pageCount < questionPage
                    )
                )
            }

        })
    }


    val mineAnswerCollectionResult =
        SingleLiveEvent<ResultThreeData<MineAnswerBean, Boolean, Boolean>>()

    var collectionAnswerPage = 1
    fun getMineAnswerCollection(isRefresh: Boolean, keyWord: String = "") {
        launchOnlyResult({
            if (isRefresh) {
                collectionAnswerPage = 1
            }
            MineApi.getMineAnswerCollection(collectionAnswerPage, keyword = keyWord)
        }, {
            if (it.datas.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
                mineAnswerCollectionResult.postValue(
                    ResultThreeData(
                        MineAnswerBean(),
                        isRefresh,
                        false
                    )
                )
            } else {
                defLayout.showContent.call()
                collectionAnswerPage++
                mineAnswerCollectionResult.postValue(
                    ResultThreeData(
                        it,
                        isRefresh,
                        it.pageCount < collectionAnswerPage
                    )
                )
            }

        })
    }

 val mineVideoCollectionResult =
        SingleLiveEvent<ResultThreeData<CollectionVideoData, Boolean, Boolean>>()

    var collectionVideoPage = 1
    fun getMineVideoCollection(isRefresh: Boolean, keyWord: String = "") {
        launchOnlyResult({
            if (isRefresh) {
                collectionVideoPage = 1
            }
            MineApi.getMineVideoCollection(collectionVideoPage, keyword = keyWord)
        }, {

            if (it.datas.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
                mineVideoCollectionResult.postValue(
                    ResultThreeData(
                        CollectionVideoData(),
                        isRefresh,
                        collectionVideoPage == it.pageCount
                    )
                )
            } else {
                defLayout.showContent.call()
                mineVideoCollectionResult.postValue(
                    ResultThreeData(
                        it,
                        isRefresh,
                        collectionVideoPage == it.pageCount
                    )
                )
            }
            collectionVideoPage++
        })
    }


    val mineArticleCollectionResult =
        SingleLiveEvent<ResultThreeData<CollectionArticleData, Boolean, Boolean>>()

    var collectionArticlePage = 1

    fun getMineArticleCollection(isRefresh: Boolean, keyWord: String = "") {
        launchOnlyResult({
            if (isRefresh) {
                collectionArticlePage = 1
            }
            MineApi.getMineArticleCollection(collectionArticlePage, keyword = keyWord)
        }, {
            if (it.datas.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
                mineArticleCollectionResult.postValue(
                    ResultThreeData(
                        CollectionArticleData(),
                        isRefresh,
                        false
                    )
                )
            } else {
                defLayout.showContent.call()
                collectionArticlePage++
                mineArticleCollectionResult.postValue(
                    ResultThreeData(
                        it,
                        isRefresh,
                        it.pageCount < collectionArticlePage
                    )
                )
            }

        })
    }

    val mineHospitalCollectionResult =
        SingleLiveEvent<ResultThreeData<HospitalBean, Boolean, Boolean>>()

    var collectionHospitalPage = 1
    fun getMineHospitalCollection(isRefresh: Boolean, keyWord: String = "") {
        launchOnlyResult({
            if (isRefresh) {
                collectionHospitalPage = 1
            }
            MineApi.getMineHospitalCollection(collectionHospitalPage, keyword = keyWord)
        }, {
            if (it.datas.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
                mineHospitalCollectionResult.postValue(
                    ResultThreeData(
                        HospitalBean(),
                        isRefresh,
                        false
                    )
                )
            } else {
                defLayout.showContent.call()
                collectionHospitalPage++
                mineHospitalCollectionResult.postValue(
                    ResultThreeData(
                        it,
                        isRefresh,
                        it.pageCount < collectionHospitalPage
                    )
                )
            }

        })
    }


    val otherUserAnswerResult =
        SingleLiveEvent<ResultThreeData<ArrayList<Question>, Boolean, Boolean>>()

    var otherUserAnswerPage = 1

    /**
     * 获取他人主页的回答
     */
    fun getOtherUserAnswer(uid: Int, isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                otherUserAnswerPage = 1
            }
            MineApi.getOtherUserAnswer(uid, otherUserAnswerPage)
        }, {
            if (it.answers.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                otherUserAnswerPage++
                otherUserAnswerResult.postValue(
                    ResultThreeData(
                        it.answers,
                        isRefresh,
                        it.pageCount < otherUserAnswerPage
                    )
                )
            }

        })
    }


    val otherUserQuestionResult =
        SingleLiveEvent<ResultThreeData<ArrayList<Question>, Boolean, Boolean>>()

    var otherUserQuestionPage = 1

    /**
     * 获取其他个人主页的问题
     */
    fun getOtherUserQuestion(uid: Int, isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                otherUserQuestionPage = 1
            }
            MineApi.getOtherUserQuestion(uid, otherUserQuestionPage)
        }, {
            if (it.questions.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                otherUserQuestionPage++
                otherUserQuestionResult.postValue(
                    ResultThreeData(
                        it.questions,
                        isRefresh,
                        it.pageCount < otherUserQuestionPage
                    )
                )
            }

        })
    }


    val otherUserArticleResult =
        SingleLiveEvent<ResultThreeData<ArrayList<CollectionArticleData.Data>, Boolean, Boolean>>()

    var otherUserArticlePage = 1

    /**
     * 获取其他个人主页的文章
     */
    fun getOtherUserArticle(uid: Int, isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                otherUserArticlePage = 1
            }
            MineApi.getOtherUserArticle(uid, otherUserArticlePage)
        }, {
            if (it.articles.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
            } else {
                defLayout.showContent.call()
                otherUserArticleResult.postValue(
                    ResultThreeData(
                        it.articles,
                        isRefresh,
                        it.pageCount < otherUserArticlePage
                    )
                )
            }
            otherUserArticlePage++
        })
    }


    val otherUserVideoResult =
        SingleLiveEvent<ResultThreeData<MutableList<VideoDetail.VideoData>, Boolean, Boolean>>()

    var otherUserVideoPage = 1

    /**
     * 获取其他个人主页的视频
     */
    fun getOtherUserVideo(uid: Int, isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                otherUserVideoPage = 1
            }
            MineApi.getOtherUserVideo(uid, otherUserVideoPage)
        }, {
//            if (it.videos.isNullOrEmpty() && isRefresh) {
//                defLayout.showEmpty.call()
//            } else {
//                defLayout.showContent.call()
//                otherUserVideoResult.postValue(
//                    ResultThreeData(
//                        it.videos,
//                        isRefresh,
//                        it.videos.size < Constant.PAGE_SIZE
//                    )
//                )
//            }
            otherUserVideoPage++
        })
    }


    val fansUserResult =
        SingleLiveEvent<ResultThreeData<ArrayList<User>, Boolean, Boolean>>()
    var fansUserPage = 1

    /**
     * 获取粉丝列表
     */
    fun getUserFans(uid: Int, isToken: Boolean, isRefresh: Boolean) {
        launchOnlyResult({
            if (isRefresh) {
                fansUserPage = 1
            }
            MineApi.getUserFans(uid, if (isToken) "yes" else "no", fansUserPage)
        }, {
            if (it.fans.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
                fansUserResult.postValue(
                    ResultThreeData(
                        null,
                        false,
                        true
                    )
                )
            } else {
                defLayout.showContent.call()
                fansUserResult.postValue(
                    ResultThreeData(
                        it.fans,
                        isRefresh,
                        it.pageCount < fansUserPage
                    )
                )
            }
            fansUserPage++
        })
    }


    /**
     * 获取问题关注列表
     */
    fun getQuestionFans(questionId: Int, isRefresh: Boolean, totalNum: (num: Int) -> Unit) {
        launchOnlyResult({
            if (isRefresh) {
                fansUserPage = 1
            }
            MineApi.getQuestionFans(questionId, fansUserPage)
        }, {
            if (it.users.isNullOrEmpty() && isRefresh) {
                defLayout.showEmpty.call()
                fansUserResult.postValue(
                    ResultThreeData(
                        null,
                        false,
                        true
                    )
                )
            } else {
                defLayout.showContent.call()
                totalNum(it.total)
                fansUserResult.postValue(
                    ResultThreeData(
                        it.users,
                        isRefresh,
                        it.pageCount < fansUserPage
                    )
                )
            }
            fansUserPage++
        })
    }


    fun getBrowsesCircle() = launchWithFlow({
        MineApi.getBrowsesCircle()
    }, handleError = false)

    fun userToolsList() = launchWithFlow({
        MineApi.userToolsList()
    }, handleError = false)

    fun getUserDetailV2() = launchWithFlow({
        ResultThreeData(
            MineApi.getUserDetailV2Async(viewScope()).await(),
            if (Constant.isLogin) MineApi.circleMessageAsync(viewScope()).await()
            else CircleUnReadMessage(),
            if (Constant.isLogin)
                MineApi.getPregnantStatus(viewScope()).await()
            else PregantStatusBean()
        )
    }, handleError = false).map {
        val info = Constant.userInfo
        info.isSwitchTube = it.data3.isSwitchTube
        info.pregnantStatus = it.data3.myPregantStatus
        Constant.updateInfo(info)

        ResultThreeData(
            it.data1,
            it.data2.all,
            it.data2
        )
    }

    fun getSystemMsg() = launchWithFlow({
        MineApi.circleMessageAsync(viewScope()).await()
    }).map {
        ResultTwoData(
            it.all,
            it
        )
    }

    fun getGroupDetail(
        circleId: String = "0",
        number: String,
        type: String,
    ) = launchWithFlow({
        MineApi.getGroupDetail(circleId, number, type)
    }, handleError = false)


    fun getUserCircleDetail() {
        viewScope().launch {
            launchWithFlow({
                MineApi.circleUserDetail(Constant.getUserId())
            }, handleError = false).netCatch {
                message.showToast()
            }.next {
                Constant.circleUser = this
            }
        }
    }
}