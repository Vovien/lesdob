package com.tubewiki.home.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.jmbon.middleware.bean.TubeArticleDetail

@Keep
@Parcelize
data class TubeStepBean(
    @SerializedName("data")
    var data: MutableList<TubeStepBeans.Step> = mutableListOf(),
) : Parcelable