package com.tubewiki.hospital.adapter

import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.hospital.R
import com.tubewiki.hospital.bean.HospitalBean
import com.tubewiki.hospital.databinding.ItemLocalHospitalLayoutBinding
import com.tubewiki.hospital.databinding.ItemRecommendHospitalLayoutBinding

/**
 * 首页推荐知识adapter
 */
class LocalHospitalAdapter :
    BindingQuickAdapter<HospitalBean, ItemLocalHospitalLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: HospitalBean) {
        holder.getViewBinding<ItemLocalHospitalLayoutBinding>().apply {

            ivCover.loadRadius(item.logo, 8f.dp())

            tvTitle.text = item.hospitalName
            tvLocation.text = "${item.province}${item.city}${item.address}"
            tvLevel.text = item.levelName
            tvSimpleName.text = item.hospitalShortName
            tvDoctorNum.text = "${item.doctorCount}位专家"
            tvLength.text = item.distance

        }
    }
}