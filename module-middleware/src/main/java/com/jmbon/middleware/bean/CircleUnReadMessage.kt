package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.math.max

@Keep
@Parcelize
data class CircleUnReadMessage(
    @SerializedName("help_count")
    var helpCount: Int = 0,
    @SerializedName("appeal_count")
    var appealCount: Int = 0,
    @SerializedName("system_count")
    var systemCount: SystemCount = SystemCount(),
    var systemAll: Int = 0
) : Parcelable {
    @Keep
    @Parcelize
    data class SystemCount(
        @SerializedName("block")
        var block: Int = 0,
        @SerializedName("forbidden")
        var forbidden: Int = 0,
        @SerializedName("violation")
        var violation: Int = 0
    ) : Parcelable
}

var CircleUnReadMessage?.system: Int
    get() = if (this == null)
        0
    else {
        if (systemAll == 0)
            systemAll = systemCount.block + systemCount.forbidden + systemCount.violation
        else systemAll
        systemAll
    }
    set(value) {
        this?.systemAll = max(value, 0)
    }


val CircleUnReadMessage?.all: Int
    get() = if (this == null) 0 else (system + help + appealCount)

var CircleUnReadMessage?.appeal: Int
    get() = this?.appealCount ?: 0
    set(value) {
        this?.appealCount = max(value, 0)
    }


var CircleUnReadMessage?.help: Int
    get() = this?.helpCount ?: 0
    set(value) {
        this?.helpCount = max(value, 0)
    }
