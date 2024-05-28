package com.tubewiki.mine.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.common.CommonViewModel
import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.bean.ItemRecordBean
import kotlinx.coroutines.launch

open class MineViewModel: CommonViewModel() {
//    val apiService by lazy { MVVMRequest.retrofit().create(MineAPI::class.java) }

    private val _conversationRecordListLD = MutableLiveData<List<ItemRecordBean>?>()
    val conversationRecordListLD: LiveData<List<ItemRecordBean>?> = _conversationRecordListLD

    /**
     * 获取会话记录列表
     */
    fun getConversationRecordList() {
        viewModelScope.launch {
            launchWithFlow({ MineApi.getConversationRecordList() }, {
                _conversationRecordListLD.postValue(null)
            }).next {
                _conversationRecordListLD.postValue(dialog_list)
            }
        }
    }
}