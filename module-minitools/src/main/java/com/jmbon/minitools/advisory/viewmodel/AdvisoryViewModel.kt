package com.jmbon.minitools.advisory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.minitools.advisory.api.AdvisoryApi
import com.jmbon.minitools.advisory.bean.AdvisoryConversationHistoryBean
import com.jmbon.minitools.advisory.bean.AdvisoryFinishReasonEnum
import com.jmbon.minitools.advisory.bean.AdvisoryInfoBean
import com.jmbon.minitools.advisory.bean.AdvisoryItemForm
import com.jmbon.minitools.advisory.bean.ItemReasonBean
import kotlinx.coroutines.launch

/******************************************************************************
 * Description: 咨询相关的ViewModel
 *
 * Author: jhg
 *
 * Date: 2023/5/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class AdvisoryViewModel : CommonViewModel() {

    /**
     * 咨询的目标对象
     */
    var target: String = ""

    /**
     * 是否正在等待回复
     */
    var waitReplying = false

    /**
     * 咨询id
     */
    var advisoryId: Int = 0

    /**
     * 当前会话id
     */
    var currentConversationId: Int = 0

    /**
     * 是否是查看模式
     */
    var isViewMode: Boolean = false

    /**
     * 确认咨询信息
     */
    val confirmAdvisoryInfoLD = MutableLiveData<Boolean>()

    /**
     * 咨询表单列表
     */
    private val _advisoryFormListLD = MutableLiveData<List<AdvisoryItemForm>?>()
    val advisoryFormListLD: LiveData<List<AdvisoryItemForm>?> = _advisoryFormListLD

    /**
     * 咨询放弃原因
     */
    private val _advisoryAbandonReasonLD = MutableLiveData<List<ItemReasonBean>?>()
    val advisoryAbandonReasonLD: LiveData<List<ItemReasonBean>?> = _advisoryAbandonReasonLD

    /**
     * 提交咨询放弃原因
     */
    private val _submitAbandonReasonResultLD = MutableLiveData<Boolean>()
    val submitAbandonReasonResultLD: LiveData<Boolean> = _submitAbandonReasonResultLD

    /**
     * 提交咨询
     */
    private val _createAdvisoryResultLD = MutableLiveData<AdvisoryInfoBean?>()
    val createAdvisoryResultLD: LiveData<AdvisoryInfoBean?> = _createAdvisoryResultLD

    /**
     * 咨询回复
     */
    private val _advisoryReplyLD = MutableLiveData<List<AdvisoryItemForm>?>()
    val advisoryReplyLD: LiveData<List<AdvisoryItemForm>?> = _advisoryReplyLD

    /**
     * 更新咨询状态
     */
    private val _updateAdvisoryStatusLD = MutableLiveData<Int>()
    val updateAdvisoryStatusLD: LiveData<Int> = _updateAdvisoryStatusLD

    /**
     * 会话中断
     */
    private val _conversationTerminateLD = MutableLiveData<Boolean>()
    val conversationTerminateLD: LiveData<Boolean> = _conversationTerminateLD

    /**
     * 咨询会话历史记录
     */
    private val _advisoryConversationHistoryLD = MutableLiveData<AdvisoryConversationHistoryBean?>()
    val advisoryConversationHistoryLD: LiveData<AdvisoryConversationHistoryBean?> =
        _advisoryConversationHistoryLD

    /**
     * 请求咨询表单列表
     */
    fun getAdvisoryFormList() {
        viewModelScope.launch {
            launchWithFlow(
                { AdvisoryApi.getAdvisoryFormList() },
                {
                    _advisoryFormListLD.postValue(null)
                }
            ).next {
                _advisoryFormListLD.postValue(list)
            }
        }
    }

    /**
     * 获取咨询放弃原因
     */
    fun getAdvisoryAbandonReason() {
        viewModelScope.launch {
            launchWithFlow(
                { AdvisoryApi.getAdvisoryAbandonReason() },
                {
                    _advisoryAbandonReasonLD.postValue(null)
                }
            ).next {
                _advisoryAbandonReasonLD.postValue(reason_list)
            }
        }
    }

    /**
     * 提交咨询放弃原因
     */
    fun submitAdvisoryAbandonReason(reasonIds: List<Int>) {
        viewModelScope.launch {
            launchWithFlow(
                { AdvisoryApi.submitAdvisoryAbandonReason(reasonIds.joinToString(",")) },
                {
                    it.message?.showToast()
                },
                isShowDialog = true
            ).next {
                _submitAbandonReasonResultLD.postValue(true)
            }
        }
    }

    /**
     * 创建咨询信息
     */
    fun createAdvisory(advisoryInfo: AdvisoryInfoBean?) {
        if (advisoryInfo == null) {
            return
        }
        viewModelScope.launch {
            launchWithFlow(
                {
                    AdvisoryApi.createAdvisory(
                        advisoryInfo.is_self,
                        advisoryInfo.relationship_me,
                        advisoryInfo.age,
                        advisoryInfo.gender,
                        advisoryInfo.is_birth_history,
                        advisoryInfo.report_img?.joinToString(",") ?: "",
                        advisoryInfo.title
                    )
                },
                {
                    it.message?.showToast()
                },
                isShowDialog = true
            ).next {
                advisoryId = consult?.id ?: 0
                _createAdvisoryResultLD.postValue(consult)
            }
        }
    }

    /**
     * 获取咨询回复
     * @param content: 咨询的内容
     */
    fun getAdvisoryReply(content: String, questionId: Int = 0) {
        waitReplying = true
        viewModelScope.launch {
            launchWithFlow(
                { AdvisoryApi.getAdvisoryReply(content, questionId = questionId, dialogId = currentConversationId) },
                {
                    waitReplying = false
                    _advisoryReplyLD.postValue(
                        listOf(
                            AdvisoryItemForm(
                                content = "网络异常, 请重试",
                            )
                        )
                    )
                },
            ).next {
                waitReplying = false
                currentConversationId = dialog_id
                _advisoryReplyLD.postValue(this.list)
            }
        }
    }

    /**
     * 设置咨询状态
     * @param status: 咨询状态, 3=已解决, 4=未解决
     * @param byClick: 是否为主动点击未解决, 0=不是, 1=是
     * @param reasonType: 结束原因: 0=正常, 1=时间结束, 2=回复次数达上限, 3=AI异常结束
     */
    fun updateAdvisoryStatus(
        status: Int,
        byClick: Int = 0,
        reasonType: Int = AdvisoryFinishReasonEnum.FINISH_REASON_NORMAL.value
    ) {
        viewModelScope.launch {
            launchWithFlow(
                {
                    AdvisoryApi.updateAdvisoryStatus(
                        advisoryId,
                        status,
                        byClick,
                        reasonType
                    )
                },
                {
                    it.message?.showToast()
                },
            ).next {
                if (byClick != 0) {
                    _updateAdvisoryStatusLD.postValue(status)
                }
                if (reasonType == AdvisoryFinishReasonEnum.FINISH_REASON_TIME_OVER.value) {
                    _conversationTerminateLD.postValue(true)
                }
            }
        }
    }

    /**
     * 获取咨询会话记录
     */
    fun getAdvisoryConversationHistory() {
        viewModelScope.launch {
            launchWithFlow(
                {
                    AdvisoryApi.getAdvisoryConversationHistory(advisoryId)
                },
                {
                    it.message?.showToast()
                    _advisoryConversationHistoryLD.postValue(null)
                },
            ).next {
                _advisoryConversationHistoryLD.postValue(this)
            }
        }
    }

    /**
     * 获取咨询会话记录
     */
    fun getDefaultConversationHistory(questionId: Int) {
        viewModelScope.launch {
            launchWithFlow(
                {
                    AdvisoryApi.getDefaultConversationHistory(questionId)
                },
                {
                    it.message?.showToast()
                    _advisoryConversationHistoryLD.postValue(null)
                },
            ).next {
                _advisoryConversationHistoryLD.postValue(this)
            }
        }
    }
}