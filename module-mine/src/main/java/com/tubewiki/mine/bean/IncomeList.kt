package com.tubewiki.mine.bean


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncomeList(
    @SerializedName("incomes")
    var incomes: ArrayList<Income> = arrayListOf(),
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("page_count")
    var pageCount: Int = 0
) : Parcelable {
    @Parcelize
    data class Income(
        @SerializedName("add_time")
        var addTime: Int = 0,
        @SerializedName("cate_type")
        var cateType: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("item_id")
        var itemId: Int = 0,
        @SerializedName("money")
        var money: Float = 0f,
        @SerializedName("passive_uid")
        var passiveUid: Int = 0,
        @SerializedName("passive_user")
        var passiveUser: User = User(),
        @SerializedName("pay_type")
        var payType: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type_title")
        var titleType: String = "",
        @SerializedName("type")
        var type: String = "",
        @SerializedName("uid")
        var uid: Int = 0,
        @SerializedName("user")
        var user: User = User(),
        @SerializedName("content")
        var content: IncomeContent = IncomeContent(),
    ) : Parcelable
}

@Parcelize
data class IncomeContent(
    @SerializedName("content")
    var content: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("order_number")
    var orderNumber: String = "",
    @SerializedName("title")
    var title: String = ""
) : Parcelable