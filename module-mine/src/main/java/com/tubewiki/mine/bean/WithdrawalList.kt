package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
@Parcelize
data class WithdrawalList(
    @SerializedName("page")
    var page: String = "",
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("total")
    var total: Int = 0,
    @SerializedName("withdrawals")
    var withdrawals: MutableList<Withdrawal> = mutableListOf()
) : Parcelable {
    @Parcelize
    @Keep
    data class Withdrawal(
        @SerializedName("amount")
        var amount: Float = 0f,
        @SerializedName("create_time")
        var createTime: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("number")
        var number: String = "",
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("uid")
        var uid: Int = 0
    ) : Parcelable
}