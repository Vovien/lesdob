package com.apkdv.mvvmfast.view.state

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.apkdv.mvvmfast.R

/**
 * @ProjectName: MultiStatePage
 * @Author: 赵岩
 * @Email: 17635289240@163.com
 * @Description: TODO
 * @CreateDate: 2020/9/19 12:30
 */
@Keep
data class MultiStateConfig(
    val errorMsg: String = "出了点问题",
    @DrawableRes
    val errorIcon: Int = R.drawable.img_no_network,
    val emptyMsg: String = "这里什么都没有",
    val emptyMsg2: String = "暂无内容",
    @DrawableRes
    val emptyIcon: Int = R.mipmap.icon_follow_empty,
    val loadingMsg: String = "",
    var alphaDuration: Long = 300,
//    val defaultState: MultiState? = null
) {

    class Builder {
        private var errorMsg: String = "出了点问题"

        @DrawableRes
        private var errorIcon: Int = R.drawable.img_no_network
        private var emptyMsg: String = "这里什么都没有"

        @DrawableRes
        private var emptyIcon: Int = R.mipmap.icon_follow_empty
        private var loadingMsg: String = ""
        private var alphaDuration: Long = 500

        fun errorMsg(msg: String): Builder {
            this.errorMsg = msg
            return this
        }

        fun errorIcon(@DrawableRes icon: Int): Builder {
            this.errorIcon = icon
            return this
        }

        fun emptyMsg(msg: String): Builder {
            this.emptyMsg = msg
            return this
        }

        fun emptyIcon(@DrawableRes icon: Int): Builder {
            this.emptyIcon = icon
            return this
        }

        fun loadingMsg(msg: String): Builder {
            this.loadingMsg = msg
            return this
        }

        fun alphaDuration(duration: Long): Builder {
            this.alphaDuration = duration
            return this
        }

        fun build() = MultiStateConfig(
            errorMsg = errorMsg,
            errorIcon = errorIcon,
            emptyMsg = emptyMsg,
            emptyIcon = emptyIcon,
            loadingMsg = loadingMsg,
            alphaDuration = alphaDuration,
        )
    }
}