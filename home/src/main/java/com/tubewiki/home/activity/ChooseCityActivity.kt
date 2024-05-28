package com.tubewiki.home.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.blankj.utilcode.util.ResourceUtils
import com.tubewiki.home.router.HomeRouter
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.tubewiki.home.R
import com.tubewiki.home.databinding.ActivityChooseCityLayoutBinding
import com.tubewiki.home.fragment.CityListFragment
import com.tubewiki.home.fragment.OverseasFragment
import com.tubewiki.home.viewmodel.LocalViewModel

/******************************************************************************
 * Description: 选择城市列表
 *
 * Author: jhg
 *
 * Date: 2023/3/17
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = HomeRouter.HOME_CHOOSE_CITY)
class ChooseCityActivity :
    ViewModelActivity<LocalViewModel, ActivityChooseCityLayoutBinding>() {

    private val searchView by lazy {
        createSearchView()
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (searchView.parent == null) {
            titleBarView.addView(
                searchView,
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp).apply {
                    leftMargin = 60.dp
                    rightMargin = 20.dp
                    addRule(RelativeLayout.CENTER_VERTICAL)
                })
        }
        initTabLayout()
    }


    override fun initViewModel() {
        super.initViewModel()
        viewModel.getCityList()
    }

    private fun initTabLayout() {
        binding.tabLayout.apply {
            setViewPager(
                binding.vpContent,
                arrayOf("国内城市", "海外城市"),
                supportFragmentManager,
                arrayListOf<Fragment>(CityListFragment(), OverseasFragment()),
            )
        }
    }

    /**
     * 创建顶部的搜索布局
     */
    private fun createSearchView(): View {
        return FrameLayout(this).apply {
            setBackground(backgroundColor = R.color.colorF5F5F5.toColorInt(), radius = 12.dp)
            TextView(this@ChooseCityActivity).apply {
                minHeight = 50.dp
                gravity = Gravity.CENTER
                text = "请输入城市名称"
                textSize = 16f
                setTextColor(Color.parseColor("#7F7F7F"))
                compoundDrawablePadding = 2.dp
                ResourceUtils.getDrawable(R.drawable.icon_search_black).apply {
                    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                    setCompoundDrawables(this, null, null, null)
                }
                addView(this, FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                })
            }
            setOnClickListener {
                HomeRouter.toSearchCity()
            }
        }
    }
}