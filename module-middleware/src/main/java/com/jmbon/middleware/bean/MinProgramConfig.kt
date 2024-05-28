package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 小程序配置信息
 *
 * Author: jhg
 *
 * Date: 2023/10/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class MinProgramConfig(
    val path: String = "",
    val wechat_mini_program_id: String = ""
) : Parcelable