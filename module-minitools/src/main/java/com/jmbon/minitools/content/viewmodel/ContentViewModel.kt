package com.jmbon.minitools.content.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.minitools.content.api.ContentApi
import com.jmbon.minitools.content.bean.HelpGroupBean
import com.jmbon.minitools.content.bean.KnowledgeInfoBean
import kotlinx.coroutines.launch

class ContentViewModel : CommonViewModel() {

    /**
     * 好孕互助群
     */
    private val _helpGroupInfoLD = MutableLiveData<HelpGroupBean?>()
    val helpGroupInfoLD: LiveData<HelpGroupBean?> = _helpGroupInfoLD

    /**
     * 好孕互助群
     */
    private val _knowledgeInfoLD = MutableLiveData<KnowledgeInfoBean?>()
    val knowledgeInfoLD: LiveData<KnowledgeInfoBean?> = _knowledgeInfoLD

    /**
     * 获取指定类型的互助群列表
     * @param type: 互助群的类型
     */
    fun getHelperGroupList(type: Int) {
        viewModelScope.launch {
            launchWithFlow({ ContentApi.getHelperGroupList(type) }, {
                _helpGroupInfoLD.postValue(null)
            }).next {
                _helpGroupInfoLD.postValue(this)
            }
        }
    }

    /**
     * 获取知识内容
     */
    fun getKnowledgeInfo() {
        viewModelScope.launch {
            launchWithFlow({ ContentApi.getKnowledgeInfo() }, {
                _knowledgeInfoLD.postValue(null)
            }).next {
                _knowledgeInfoLD.postValue(this)
            }
        }
    }
}