package com.tubewiki.mine.bean

import androidx.annotation.Keep
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.tubewiki.mine.R

@Keep
data class MessageCenter<T>(
    var title: String = "",
    var desc: String = "",
    var type: Int = 2,
    var avatar: String = "",
    var avatarId: Int = R.drawable.default_man_activies_icon,
    var data: T? = null
) : MultiItemEntity {


    override val itemType: Int
        get() = type
}
