package com.tubewiki.home.fragment

import android.view.View
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.jmbon.middleware.extention.getViewModel
import com.tubewiki.home.adapter.OverseasCountryAdapter
import com.tubewiki.home.databinding.FragmentCityListLayoutBinding
import com.tubewiki.home.viewmodel.LocalViewModel

/******************************************************************************
 * Description: 海外国家列表
 *
 * Author: jhg
 *
 * Date: 2023/3/17
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class OverseasFragment :
    ViewModelFragment<LocalViewModel, FragmentCityListLayoutBinding>() {

    private val activityViewModel by lazy {
        context.getViewModel(LocalViewModel::class.java)
    }

    private val overseasCountryAdapter by lazy {
        OverseasCountryAdapter()
    }

    override fun initView(view: View) {
        binding.rvCity.adapter = overseasCountryAdapter
    }

    override fun initViewModel() {
        super.initViewModel()

        // 监听城市列表
        activityViewModel?.cityListLD?.observe(this) {
            overseasCountryAdapter.setList(it?.overseas)
        }
    }
}