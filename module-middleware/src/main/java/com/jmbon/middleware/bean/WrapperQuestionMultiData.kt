package com.jmbon.middleware.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class WrapperQuestionMultiData(
    @SerializedName("data")
    var questionMultiData: ArrayList<QuestionMultiData> = arrayListOf(),
) : Parcelable