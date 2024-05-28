package com.tubewiki.mine.adapter

import android.util.Log
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.bean.User
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.kotlinbus.FocusQuickAdapter
import com.jmbon.middleware.utils.DateFormatUtil
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setUserNickColor
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ItemFansMessageLayoutBinding

class FansMessageAdapter :
    FocusQuickAdapter<User, ItemFansMessageLayoutBinding>() {
    var isAllRead = false

    init {
        addChildClickViewIds(R.id.sb_focus_on)
    }


    override fun convert(holder: BaseBindingHolder, item: User) {

        holder.getViewBinding<ItemFansMessageLayoutBinding>().apply {
            ivAvatar.loadCircle(item.avatarFile)
            tvDate.text = DateFormatUtil.getStringByFormat(
                item.focusTime * 1000L,
                DateFormatUtil.dateFormatYMD
            )

            tvUserName.text = item.userName
            tvUserName.setUserNickColor(R.color.color_262626, item.isCancel)
            viewPoint.visibility = if (!item.isRead && !isAllRead) View.VISIBLE else View.INVISIBLE

            setBtnType(item)

            root.setOnClickListener {
                ARouter.getInstance().build("/mine/message/personal_page")
                    .withInt(TagConstant.USER_CANCEL, item.isCancel)
                    .withInt(TagConstant.PARAMS, item.uid).navigation()
            }


        }
    }

    private fun ItemFansMessageLayoutBinding.setBtnType(item: User) {
        sbFocusOn.isSelected = item.isFocus
        sbFocusOn.text =
            if (item.isFocus) "相互关注" else "回关"
    }

    override fun setEventData(
        it: FocusChangedEvent,
        item: User,
        viewBinding: ItemFansMessageLayoutBinding
    ) {
        if (it.id == item.uid) {
            Log.e(this@FansMessageAdapter::class.java.simpleName, it.toString())
            item.isFocus = it.isFocus
            item.isMutualFocus = it.isFocus
            viewBinding.setBtnType(item)
        }
    }
}