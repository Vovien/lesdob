package com.jmbon.pay.bean

import androidx.annotation.Keep
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.jmbon.pay.ext.WxInfo

@Keep
data class WxLoginEvent(var either: ResultTwoData<WxInfo, Throwable>)