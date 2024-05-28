package com.jmbon.minitools.advisory.bean

import android.os.Parcelable
import android.provider.MediaStore
import androidx.annotation.Keep
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 创建咨询后的结果
 *
 * Author: jhg
 *
 * Date: 2023/5/8
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class SubmitAdvisoryResultBean(
    val consult: AdvisoryInfoBean? = AdvisoryInfoBean()
) : Parcelable

@Keep
@Parcelize
data class AdvisoryInfoBean(
    val id: Int = 0,

    @Bindable
    var title: String = "",

    /**
     * 咨询是否为本人: 0=否, 1=是
     */
    @Bindable
    var is_self: Int = 0,

    /**
     * 和本人的关系: 1=伴侣, 2=朋友
     */
    var relationship_me: Int = 0,

    /**咨询者年龄
     *
     */
    var age: Int = 0,

    /**
     * 咨询者性别: 1=男, 2=女
     */
    var gender: Int = 0,

    /**
     * 是否有生育史: 0=否, 1=是
     */
    var is_birth_history: Int = -1,

    /**
     * 上传的报告
     */
    @Bindable
    var report_img: List<String>? = listOf(),

    /**
     * 咨询状态: 1=沟通中, 2=待回复, 3=已解决, 4=未解决
     */
    var consult_status: Int = 0,

    /**
     * 结束类型: 0=正常, 1=时间结束, 2=回复次数达上限, 3=AI异常结束
     */
    var end_type: Int = 0,

    /**
     * 咨询信息是否可修改
     */
    var canModify: Boolean = true,

    // 补充内容
    var supplementContent: String? = null,

    /**
     * 咨询者的UserId
     */
    var uid: Int = 0,
) : Parcelable, BaseObservable() {

    val consultant: String
        get() = "${if (is_self == 0) if (relationship_me == 1) "伴侣" else "朋友" else "本人"} " +
                "${if (gender == 1) "男" else "女"} " +
                "${age}岁 ${if (gender == 2) if (is_birth_history == 1) "有生育史" else "无生育史" else ""}"

    val isHideReportImg: Boolean
        get() = report_img.isNullOrEmpty()

    val firstReportImg: String?
        get() = report_img?.getOrNull(0)

    val isHideReportSize: Boolean
        get() = report_img.isNullOrEmpty() || report_img?.size == 1

    val reportImgSize: String
        get() = "${report_img?.size ?: 0}张"

    val callHelp: String
        get() = if (supplementContent.isNullOrEmpty()) "无补充，向姐妹发起求助" else "确定，发起求助"

    @Bindable
    var isSelfStr: String = ""
        get() = if (is_self == 1) "是" else " 否"

    @Bindable
    var relationShipStrWithDefault: String = ""
        get() = if (is_self == 1) "" else when (relationship_me) {
            1 -> "伴侣"
            2 -> "朋友"
            else -> "选择"
        }

    @Bindable
    var relationShipStr: String = ""
        get() = if (is_self == 1) "" else when (relationship_me) {
            1 -> "伴侣"
            2 -> "朋友"
            else -> ""
        }

    @Bindable
    var ageTitleStr: String = ""
        get() = "您${relationShipStr}的年龄"

    @Bindable
    var ageStr: String = ""
        get() = "${age}岁"

    @Bindable
    var genderTitleStr: String = ""
        get() = "您${relationShipStr}的性别"

    @Bindable
    var genderStr: String = ""
        get() = when (gender) {
            1 -> "男"
            2 -> "女"
            else -> ""
        }

    @Bindable
    var fertilityHistoryTitleStr: String = ""
        get() = "您${relationShipStr}是否有过生育史"

    @Bindable
    var fertilityHistoryStr: String = ""
        get() = when (is_birth_history) {
            0 -> "没有"
            1 -> "有"
            else -> "选择"
        }

    @Bindable
    var uploadReportText: String = ""
        get() = if (report_img.isNullOrEmpty()) "上传报告" else "修改报告"

    @Bindable
    var question: String = ""
        get() = title

    fun getFinishReason(): String {
        return when (end_type) {
            AdvisoryFinishReasonEnum.FINISH_REASON_TIME_OVER.value -> "AI智能生育小助手咨询时间已结束，会话终止"
            AdvisoryFinishReasonEnum.FINISH_REASON_COUNT_OVER.value -> "AI智能生育小助手回复次数已达到上限，会话终止"
            AdvisoryFinishReasonEnum.FINISH_REASON_EXCEPTION.value -> "AI繁忙中，会话终止"
            else -> ""
        }
    }
}

/**
 * 咨询状态
 */
enum class AdvisoryStatusEnum(val value: Int) {
    /**
     * 沟通中
     */
    ADVISORY_STATUS_CHATTING(1),

    /**
     * 待回复
     */
    ADVISORY_STATUS_WAIT_REPLY(2),

    /**
     * 已解决
     */
    ADVISORY_STATUS_RESOLVED(3),

    /**
     * 未解决
     */
    ADVISORY_STATUS_UN_RESOLVE(4),
}

/**
 * 咨询结束原因
 */
enum class AdvisoryFinishReasonEnum(val value: Int) {
    /**
     * 正常
     */
    FINISH_REASON_NORMAL(0),

    /**
     * 时间结束
     */
    FINISH_REASON_TIME_OVER(1),

    /**
     * 回复次数达上限
     */
    FINISH_REASON_COUNT_OVER(2),

    /**
     * AI异常结束
     */
    FINISH_REASON_EXCEPTION(3),
}