package com.tubewiki.mine.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class MineItemType(
    override var itemType: Int,
) : Parcelable, MultiItemEntity