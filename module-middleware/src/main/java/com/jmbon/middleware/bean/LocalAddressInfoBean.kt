package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


/******************************************************************************
 * Description: 当前位置的地址信息
 *
 * Author: jhg
 *
 * Date: 2023/7/5
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Keep
@Parcelize
data class LocalAddressInfoBean(
    val address: LocationInfoBean = LocationInfoBean()
) : Parcelable