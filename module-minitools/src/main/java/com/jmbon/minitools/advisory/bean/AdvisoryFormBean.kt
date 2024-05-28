package com.jmbon.minitools.advisory.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.chad.library.adapter.base.entity.MultiItemEntity
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 咨询表单数据
 *
 * Author: jhg
 *
 * Date: 2023/5/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class AdvisoryFormBean(
    val dialog_id: Int = 0,
    val list: List<AdvisoryItemForm>? = listOf()
) : Parcelable

@Keep
@Parcelize
data class AdvisoryItemForm(
    var content: String = "",
    val avatar: String = "",
    val role: Int = 2,
    var isFakeData: Boolean = false,
) : Parcelable, MultiItemEntity {

    override val itemType: Int
        get() = if (role == 2) {
            ITEM_TYPE_ROBOT
        } else {
            ITEM_TYPE_USER
        }

    companion object {

        /**
         * 真是用户
         */
        const val ITEM_TYPE_USER = 1

        /**
         * 生育小助手
         */
        const val ITEM_TYPE_ROBOT = 2
    }
}

enum class AdvisoryFormTypeEnum(val value: String) {

    /**
     * 是否是本人
     */
    ADVISORY_FORM_TYPE_IS_OWNER("is_self"),

    /**
     * 您和Ta的关系
     */
    ADVISORY_FORM_TYPE_RELATIONSHIP("relationship_me"),

    /**
     * 性别
     */
    ADVISORY_FORM_TYPE_GENDER("gender"),
    /**
     * 年龄
     */
    ADVISORY_FORM_TYPE_AGE("age"),

    /**
     * 是否有生育史
     */
    ADVISORY_FORM_TYPE_HAS_FERTILITY_HISTORY("is_birth_history"),

    /**
     * 上传报告
     */
    ADVISORY_FORM_TYPE_UPLOAD_REPORT("report_img"),

    /**
     * ChatGPT无法正常工作, 供业务层使用
     */
    ADVISORY_FORM_GPT_INVALID("gap_invalid")
}