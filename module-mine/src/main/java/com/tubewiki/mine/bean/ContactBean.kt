package com.tubewiki.mine.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ContactBean(
    @SerializedName("contact_us")
    var contact: ArrayList<Contact> = arrayListOf<Contact>(),
    @SerializedName("customer_service_mobile")
    val customerServiceMobile: String = "",
) : Parcelable {
    @Keep
    @Parcelize
    data class Contact(
        @SerializedName("content")
        var content: ArrayList<String> = arrayListOf<String>(),
        @SerializedName("title")
        var title: String = ""
    ) : Parcelable
}