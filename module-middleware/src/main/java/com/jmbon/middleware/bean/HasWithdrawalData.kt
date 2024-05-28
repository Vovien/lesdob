package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class HasWithdrawalData(
    @SerializedName("has_withdrawal")
    val hasWithdrawal: Boolean = false
) : Parcelable
