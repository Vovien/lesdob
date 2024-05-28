package com.tubewiki.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.tubewiki.home.api.SchemeApi
import com.tubewiki.home.bean.SchemeConfigBean
import com.tubewiki.home.bean.SchemeDetailBean
import kotlinx.coroutines.launch

/******************************************************************************
 * Description: 获取方案相关的ViewModel
 *
 * Author: jhg
 *
 * Date: 2023/6/1
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class GetSchemeViewModel: BaseViewModel() {

    /**
     * 方案配置信息
     */
    private val _schemeConfigLD = MutableLiveData<SchemeConfigBean?>()
    val schemeConfigLD: LiveData<SchemeConfigBean?> = _schemeConfigLD

    /**
     * 获取方案配置信息
     * @param type: 配置类型: 1=案例, 2=方案
     */
    fun getSchemeConfig(type: Int) {
        viewModelScope.launch {
            launchWithFlow({
                SchemeApi.getSchemeConfig(type)
            }, {
                _schemeConfigLD.postValue(null)
            }).next {
                _schemeConfigLD.postValue(this)
            }
        }
    }
}