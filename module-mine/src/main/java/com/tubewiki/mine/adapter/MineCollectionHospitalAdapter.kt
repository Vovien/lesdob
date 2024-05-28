package com.tubewiki.mine.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.R
import com.jmbon.middleware.arouter.MiniProgramService
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.jmbon.middleware.utils.setKeyHighLight
import com.tubewiki.mine.bean.HospitalBean
import com.tubewiki.mine.databinding.ItemMineCollectionHospitalBinding

/**
 * @author : wangzhen
 * time   : 2021/3/24
 * desc   : 收藏医院adapter
 * version: 1.0
 */
class MineCollectionHospitalAdapter :
    BindingQuickAdapter<HospitalBean.Data, ItemMineCollectionHospitalBinding>() {

    private val miniProgram by lazy {
        ARouter.getInstance().build("/miniprogram/start/service").navigation() as MiniProgramService
    }

    var highlight = ""

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: HospitalBean.Data) {
        holder.getViewBinding<ItemMineCollectionHospitalBinding>().apply {

            ivCover.loadRadius(item.logo, 8f.dp(), R.drawable.picture_image_placeholder)
            tvTitle.text = item.hospitalName.setKeyHighLight(highlight)
            tvName.text = item.hospitalShortName
            tvDocNum.text = "${item.doctorCount}位专家"
            tvLocation.text = item.address
            textDistance.text = item.distance

            root.setOnClickListener {
                val param = Bundle()
                param.putString(
                    "page",
                    "pages/hospitaldetail/hospitaldetail?id=${item.id}"
                ) //设置路径
                miniProgram.startMiniProgram(BuildConfig.HOSPITAL_DOCTOR_ID, param)

            }
        }
    }
}