package com.tubewiki.mine.adapter


import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.bean.User
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.kotlinbus.FocusQuickAdapter
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setKeyHighLight
import com.jmbon.middleware.utils.setUserNickColorByHighLight
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ItemFansUserLayoutBinding


/**
 * time   : 2021/5/24
 * desc   : 粉丝列表适配器
 * version: 1.0
 */
class FansAdapter : FocusQuickAdapter<User, ItemFansUserLayoutBinding>() {

    var type = 0

    init {
        addChildClickViewIds(R.id.sb_focus_on)
    }

    override fun convert(holder: BaseBindingHolder, item: User) {

        holder.getViewBinding<ItemFansUserLayoutBinding>().apply {
            tvUserName.setUserNickColorByHighLight(
                item.userName.setKeyHighLight(item.highlight),
                item.userName,
                R.color.color_262626,
                item.isCancel
            )

            civHead.loadCircle(item.avatarFile)
            tvDesc.text = item.description
            if (item.description.isNullOrEmpty()) tvDesc.gone() else tvDesc.visible()

            setBtnType(item)
        }
    }

    private fun ItemFansUserLayoutBinding.setBtnType(item: User) {

        sbFocusOn.isSelected = item.isFocus //|| item.isMutualFocus
        if (item.isFocus && item.isMutualFocus) {
            sbFocusOn.text = "相互关注"
        } else if (item.isFocus) {
            sbFocusOn.text = "取消关注"
        } else {
            if (type != 0) {
                sbFocusOn.text = "关注"
            } else {
                sbFocusOn.text = "回关"
            }
        }

    }


    override fun setEventData(
        it: FocusChangedEvent,
        item: User,
        viewBinding: ItemFansUserLayoutBinding
    ) {
        if (it.id == item.uid) {
            LogUtils.i(it.toString())
            item.isFocus = it.isFocus
            if (type == 0) {
                item.isMutualFocus = it.isFocus
            } else {
                //保证问题关注列表取消关注后再关注依然有相互关注的状态
                //  item.isMutualFocus = false
            }
            viewBinding.setBtnType(item)
        }
    }

}