package com.tubewiki.mine.view.edit

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.config.Constant
import com.tubewiki.mine.databinding.ActivityEditShowGenderLayoutBinding
import com.tubewiki.mine.view.model.SettingViewModel

/**
 * 修改性别
 */
@Route(path = "/mine/edit/gender")
class EditPersonGenderActivity :
    ViewModelActivity<SettingViewModel, ActivityEditShowGenderLayoutBinding>() {

    var isUserCheck = true
    var showSex = 0
    override fun initView(savedInstanceState: Bundle?) {
        val info = Constant.userInfo
        val sex = if (info.sex == 1) "男" else "女"
        setTitleName("性别（$sex）")
        binding.stByDynamic.switchIsChecked = info.sexShow == 0
        // 0：不显示，1：显示
        binding.stByDynamic.setSwitchCheckedChangeListener { buttonView, isChecked ->
            if (!isUserCheck) {
                isUserCheck = true
                return@setSwitchCheckedChangeListener
            }
            showSex = if (isChecked) 0 else 1

            var infoMap = hashMapOf<String,String>()
            infoMap["isShowSex"] = "$showSex"
            viewModel.uploadInfo(infoMap)
        }
    }

    override fun initData() {

    }

    override fun getData() {
        viewModel.uploadInfoStatus.observe(this) {
            if (it) {
                val info = Constant.userInfo
                info.sexShow = showSex
                Constant.updateInfo(info)
            } else {
                isUserCheck = false
                binding.stByDynamic.switchIsChecked = !binding.stByDynamic.switchIsChecked
            }
        }
    }
}