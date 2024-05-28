package com.tubewiki.mine.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class WithdrawalDetails(
    @SerializedName("withdrawal")
    var withdrawal: WithdrawalList.Withdrawal = WithdrawalList.Withdrawal()
) : Parcelable