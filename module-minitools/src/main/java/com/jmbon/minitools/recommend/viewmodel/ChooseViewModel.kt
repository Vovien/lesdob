package com.jmbon.minitools.recommend.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.jmbon.middleware.api.API
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.config.Constant
import com.jmbon.minitools.recommend.api.RecommendApi
import com.jmbon.minitools.recommend.bean.BudgetInfoBean
import com.jmbon.minitools.recommend.bean.GuideInfoBean
import com.jmbon.minitools.recommend.bean.ItemRecommendBean
import com.jmbon.minitools.recommend.bean.ItemTypeBean
import com.jmbon.minitools.recommend.bean.RecommendResultBean
import kotlinx.coroutines.launch

/******************************************************************************
 * Description: 选择相关的ViewModel
 *
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class ChooseViewModel : CommonViewModel() {

    /**
     * 选择表单
     */
    private val _chooseFormLD = MutableLiveData<List<ItemTypeBean>?>()
    val chooseFormLD: LiveData<List<ItemTypeBean>?> = _chooseFormLD

    /**
     * 预算信息
     */
    private val _budgetInfoLD = MutableLiveData<BudgetInfoBean?>()
    val budgetInfoLD: LiveData<BudgetInfoBean?> = _budgetInfoLD

    /**
     * 推荐结果
     */
    private val _recommendResultLD = MutableLiveData<RecommendResultBean?>()
    val recommendResultLD: LiveData<RecommendResultBean?> = _recommendResultLD

    /**
     * 引导信息
     */
    private val _guideInfoLD = MutableLiveData<GuideInfoBean?>()
    val guideInfoLD: LiveData<GuideInfoBean?> = _guideInfoLD

    /**
     * 选择结果
     */
    val paramsMap: MutableMap<String, Any?> = mutableMapOf()

    /**
     * 试管预算
     */
    var tubeBudget = ""

    /**
     * 人工授精预算
     */
    var inseminationBudget = ""

    /**
     * 获取选择表单
     */
    fun getChooseForm() {
        viewModelScope.launch {
            launchWithFlow({
                RecommendApi.getChooseForm()
            }, {
                it.message.showToast()
            }, isShowDialog = true).next {
                _chooseFormLD.postValue(form_data)
            }
        }
    }

    /**
     * 获取预算信息
     */
    fun getBudgetInfo() {
        viewModelScope.launch {
            launchWithFlow({
                RecommendApi.getBudgetInfo()
            }, {
                _budgetInfoLD.postValue(null)
            }).next {
                _budgetInfoLD.postValue(this)
            }
        }
    }

    /**
     * 获取推荐结果
     */
    fun getRecommendResult() {
        viewModelScope.launch {
            launchWithFlow({
                if (!tubeBudget.isNullOrBlank() || !inseminationBudget.isNullOrBlank()) {
                    paramsMap["budget"] = "${tubeBudget},${inseminationBudget}"
                }
                RecommendApi.getRecommendResult(paramsMap)
            }, {
                it.message.showToast()
                _recommendResultLD.postValue(null)
            }, isShowDialog = true).next {
                Constant.updateUserAvatar(this.avatar_file)
                _recommendResultLD.postValue(this)
            }
        }
    }

    /**
     * 获取选择表单
     * @param id: 引导页面id
     */
    fun getGuideInfo(id: Int) {
        viewModelScope.launch {
            launchWithFlow({
                RecommendApi.getGuideInfo(id)
            }, {
                _guideInfoLD.postValue(null)
            }).next {
                _guideInfoLD.postValue(this)
            }
        }
    }
}