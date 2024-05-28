package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class ReportTypeData(
    @SerializedName("reports")
    var reports: ArrayList<Report> = arrayListOf()
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Keep
    data class Report(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("reason")
            var reason: String = ""
        ) : Parcelable
    }
