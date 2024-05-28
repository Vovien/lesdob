package com.tubewiki.mine.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class IncomeDetails(
    @SerializedName("income")
    var income: IncomeList.Income = IncomeList.Income()
) : Parcelable