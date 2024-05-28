package com.tubewiki.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.api.API
import com.jmbon.middleware.common.CommonViewModel
import com.tubewiki.home.api.HomeApi
import com.tubewiki.home.bean.GroupItem
import com.tubewiki.home.bean.HomeHeaderBean
import com.tubewiki.home.bean.QuestionItemRecordBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author : leimg
 * time   : 2022/8/1
 * desc   :
 * version: 1.0
 */
class MainFragmentViewModel : CommonViewModel() {

    val firstShowFlow = MutableStateFlow(true)

    /**
     * 首页头部信息
     */
    private val _homeHeaderInfoLD = MutableLiveData<HomeHeaderBean?>()
    val homeHeaderBean: LiveData<HomeHeaderBean?> = _homeHeaderInfoLD

    private val _groupItemListFlow = MutableStateFlow<List<GroupItem>>(listOf())
    val groupItemListFlow = _groupItemListFlow

    private val _questionCategoryListLD = MutableLiveData<List<QuestionItemRecordBean>?>()
    val questionCategoryListLD: LiveData<List<QuestionItemRecordBean>?> = _questionCategoryListLD

    /**
     * 获取头部信息
     */
    fun getHeaderInfo() {
        viewModelScope.launch {
            launchWithFlow({
                HomeApi.getHomeHeaderInfo()
            }, {
                _homeHeaderInfoLD.postValue(null)
            }).next {
                _homeHeaderInfoLD.postValue(this)
            }
        }
    }

    /**
     * 获取分类问题
     * @param categoryId: 分类id
     */
    fun getQuestionCategory(categoryId: Int) {
        viewModelScope.launch {
            launchWithFlow({ HomeApi.getQuestionCategory(categoryId) }, {
                _questionCategoryListLD.postValue(null)
            }).next {
                _questionCategoryListLD.postValue(question_list)
            }
        }
    }

    /**
     * 获取公共引导banner
     * @date 2023/6/25 10:02
     */
    fun getGuideBanner() = launchWithFlow({
        val result = API.getGuideBanner()
        result.data
    }, handleError = false)

    fun getHelperGroupType(type:Int) {
        launchOnlyResult({
            HomeApi.getHelperGroupTypes(type)
        }, {
            _groupItemListFlow.value = it.data
        }, {
            _groupItemListFlow.value = listOf()
        }, handleError = true)
    }

    fun getCommonQuestionAdv() = launchWithFlow({
        HomeApi.getCommonQuestionAdv()
    }, handleError = false)

    /**
     * 获取首页数据
     */
    fun index() = launchWithFlow({
        HomeApi.index()
    }, handleError = false)

    /**
     * 文章专栏详情页接口
     */
    fun topicList(topicId: Int, page: Int) = launchWithFlow({
        val list = HomeApi.topicList(topicId, page)
        return@launchWithFlow ResultTwoData(list, if (page == 1) HomeApi.topicHeaderData(topicId) else null)
    }, handleError = false)

    /**
     * 全部专栏接口
     */
    fun topicAllList(page: Int) = launchWithFlow({
        HomeApi.topicAllList(page)
    }, handleError = false)

    /**
     * 常见问题列表接口
     */
    fun problemList() = launchWithFlow({
        HomeApi.problemList()
    }, handleError = false)

    /**
     * 试管流程一级
     */
    fun tubeProcessList(firstId: Int) = launchWithFlow({
        HomeApi.tubeProcessList(firstId)
    }, handleError = false)

    /**
     * 试管流程二级
     */
    fun tubeSecondProcessList(firstId: Int) = launchWithFlow({
        HomeApi.tubeSecondProcessList(firstId)
    }, handleError = false)
}