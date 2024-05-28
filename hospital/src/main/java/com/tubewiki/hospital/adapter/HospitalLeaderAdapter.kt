package com.tubewiki.hospital.adapter

import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.hospital.R
import com.tubewiki.hospital.bean.HospitalBean
import com.tubewiki.hospital.bean.HospitalDetailBean
import com.tubewiki.hospital.databinding.ItemHospitalImageLayoutBinding
import com.tubewiki.hospital.databinding.ItemHospitalLeaderLayoutBinding
import com.tubewiki.hospital.databinding.ItemHospitalTechLayoutBinding
import com.tubewiki.hospital.databinding.ItemRecommendHospitalLayoutBinding

/**
 * 医院详情图片adapter
 */
class HospitalLeaderAdapter :
    BindingQuickAdapter<HospitalDetailBean.Hospital.Leader, ItemHospitalLeaderLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: HospitalDetailBean.Hospital.Leader) {
        holder.getViewBinding<ItemHospitalLeaderLayoutBinding>().apply {

            tvTitle.text = item.name
            ivCover.loadRadius(item.avatarFile, 8f.dp())
            tvOffice.text = item.doctorDescript
            if (item.isLeader > 0) {
                ivTag.visible()
            } else {
                ivTag.gone()
            }

        }
    }
}