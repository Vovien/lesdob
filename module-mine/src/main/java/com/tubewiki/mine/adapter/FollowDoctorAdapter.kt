package com.tubewiki.mine.adapter

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setUserNickColor
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.FollowUserData
import com.tubewiki.mine.databinding.ItemFollowDoctorLayoutBinding
import com.tubewiki.mine.databinding.ItemFollowUserLayoutBinding

/**
 * time   : 2021/5/24
 * desc   : 医生列表适配器
 * version: 1.0
 */
class FollowDoctorAdapter :
    BindingQuickAdapter<FollowUserData.User, ItemFollowDoctorLayoutBinding>() {

    init {
        addChildClickViewIds(R.id.sb_focus_on)
    }

    override fun convert(holder: BaseBindingHolder, item: FollowUserData.User) {
        holder.getViewBinding<ItemFollowDoctorLayoutBinding>().apply {
            tvUserName.text = item.name
            tvUserName.setUserNickColor(R.color.color_262626, item.isCancel)
            civHead.loadCircle(item.avatarFile)
            tvDesc.text = item.description
            if (item.description.isNullOrEmpty()) tvDesc.gone() else tvDesc.visible()

            setBtnType(item)

        }
    }

    private fun ItemFollowDoctorLayoutBinding.setBtnType(item: FollowUserData.User) {

        sbFocusOn.isSelected = item.isFocus
        if (item.isFocus) {
            sbFocusOn.text = "取消关注"
        } else {
            sbFocusOn.text = "关注"
        }
    }
}