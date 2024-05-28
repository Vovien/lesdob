package com.jmbon.minitools.recommend.fragment

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ScreenUtils
import com.jmbon.middleware.adapter.CommonFragmentAdapter
import com.jmbon.middleware.extention.bindPager
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.R
import com.jmbon.minitools.databinding.FragmentChooseBudgetLayoutBinding
import com.jmbon.minitools.recommend.viewmodel.ChooseViewModel
import com.qmuiteam.qmui.kotlin.onClick
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/******************************************************************************
 * Description: 选择预算
 *
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class ChooseBudgetFragment :
    ViewModelFragment<ChooseViewModel, FragmentChooseBudgetLayoutBinding>() {

    override fun initView(view: View) {
        initIndicator()
    }

    override fun initViewModel() {
        super.initViewModel()
        // 请求预算信息
        viewModel.getBudgetInfo()
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        if (viewModel.budgetInfoLD.value == null) {
            "预算信息请求失败".showToast()
            return
        }

        viewModel.budgetInfoLD.value?.let {
            binding.vpContent.adapter = CommonFragmentAdapter(
                activity as FragmentActivity,
                mutableListOf(
                    ChildBudgetFragment.newInstance(0, it.tube_desc, it.tube_list),
                    ChildBudgetFragment.newInstance(
                        1,
                        it.insemination_desc,
                        it.insemination_list
                    ),
                )
            )
        } ?: kotlin.run {
            "预算信息请求失败".showToast()
        }
    }

    private fun initIndicator() {
        val titleList = mutableListOf("试管婴儿", "人工授精")
        binding.vpContent.isUserInputEnabled = false
        binding.vpContent.offscreenPageLimit = titleList.size
        binding.indicator.setBackground(
            backgroundColor = R.color.color_F1F1F1.toColorInt(),
            radius = 12.dp
        )
        binding.indicator.bindPager(binding.vpContent, object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return titleList.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                return ColorTransitionPagerTitleView(context).apply {
                    minimumWidth = (ScreenUtils.getAppScreenWidth() - 46.dp) / titleList.size
                    minimumHeight = 44.dp
                    normalColor = R.color.color_7F7F7F.toColorInt()
                    selectedColor = R.color.color_262626.toColorInt()
                    text = titleList[index]
                    onClick {
                        binding.vpContent.currentItem = index
                    }
                }
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                return WrapPagerIndicator(context).apply {
                    horizontalPadding = 58.dp
                    verticalPadding = 12.dp
                    fillColor = Color.WHITE
                    roundRadius = 11.dp.toFloat()
                }
            }
        })
    }
}