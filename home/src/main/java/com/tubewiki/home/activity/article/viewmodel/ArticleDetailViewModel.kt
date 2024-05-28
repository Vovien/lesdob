package com.tubewiki.home.activity.article.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.common.CommonViewModel
import com.tubewiki.home.api.HomeApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel : CommonViewModel() {

    val firstShowFlow = MutableStateFlow(true)

    fun getDetails(articleId: Int) = launchWithFlow({
        val details = HomeApi.getArticleDetail(articleId, viewScope())
        //val articleRelated = HomeApi.getArticleRelated(articleId, viewScope())
        return@launchWithFlow ResultTwoData(details.await(), null)
    })

    /**
     * 收藏状态
     */
    private val _collectStatus = MutableLiveData<Boolean>()
    val collectStatus: LiveData<Boolean> = _collectStatus

    /**
     * 收藏/取消收藏文章
     * @param id: 文章id
     * @param isCollect: 是否是收藏操作, true表示收藏, false表示取消收藏
     */
    fun collectArticle(id: Int, isCollect: Boolean) {
        viewModelScope.launch {
            launchWithFlow({
                HomeApi.collectArticle(id, isCollect)
            }, {
                it.message.showToast()
            }).next {
                _collectStatus.postValue(isCollect)
            }
        }
    }

    /**
     * 反馈
     */
    fun feedback(itemId: Int, type: Int) = launchWithFlow({
        HomeApi.feedback(itemId, type)
    }, handleError = false)
   /**
     * 反馈有无帮助
     */
    fun feedbackHelp(itemId:Int,itemTitle: String,isHelpful: Int) = launchWithFlow({
        HomeApi.feedbackHelp(itemId, itemTitle,isHelpful,1)
    }, handleError = false)

}