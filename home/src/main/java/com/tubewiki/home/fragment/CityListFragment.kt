package com.tubewiki.home.fragment

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.LocationInfoBean
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.decoration.GridSpaceItemDecoration
import com.jmbon.middleware.extention.getViewModel
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.realScrollToPosition
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.adapter.CityAdapter
import com.tubewiki.home.bean.ItemCityBean
import com.tubewiki.home.databinding.FragmentCityListLayoutBinding
import com.tubewiki.home.databinding.HeaderCityListLayoutBinding
import com.tubewiki.home.viewmodel.LocalViewModel
import org.greenrobot.eventbus.EventBus

/******************************************************************************
 * Description: 城市列表
 *
 * Author: jhg
 *
 * Date: 2023/3/17
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class CityListFragment :
    ViewModelFragment<LocalViewModel, FragmentCityListLayoutBinding>() {

    private val isGlobalConfig by lazy {
        activity?.intent?.getBooleanExtra("isGlobalConfig", true) ?: true
    }

    private val activityViewModel by lazy {
        context.getViewModel(LocalViewModel::class.java)
    }

    private val hotCityAdapter by lazy {
        object: BaseQuickAdapter<ItemCityBean, BaseViewHolder>(R.layout.item_hot_city_layout) {

            init {
                setOnItemClickListener { _, _, position ->
                    data[position].apply {
                        if (isGlobalConfig) {
                            CommonViewModel.updateLocation(LocationInfoBean(cityName = title, cityCode = id.toString(), countryName = "中国"))
                        } else {
                            EventBus.getDefault().post(CommonEventHub.ChangeCityEvent(cityName = title, cityId = id))
                        }
                    }
                    if (context is Activity) {
                        (context as Activity).finish()
                    }
                }
            }

            override fun convert(holder: BaseViewHolder, item: ItemCityBean) {
                holder.setText(R.id.tv_name, item.title)
            }
        }
    }

    private val headerView by lazy {
        HeaderCityListLayoutBinding.inflate(LayoutInflater.from(context)).apply {
            rvHotCity.addItemDecoration(GridSpaceItemDecoration(12.dp, false))
            rvHotCity.adapter = hotCityAdapter

            tvLocalCity.onClick {
                if (isGlobalConfig) {
                    CommonViewModel.updateLocation(viewModel.currentLocation.value)
                } else {
                    EventBus.getDefault().post(CommonEventHub.ChangeCityEvent(cityName = viewModel.currentLocation.value?.citySimpleName ?: ""))
                }
                if (context is Activity) {
                    (context as Activity).finish()
                }
            }
        }
    }

    private val cityAdapter by lazy {
        CityAdapter().apply {
            addHeaderView(headerView.root)
        }
    }

    override fun initView(view: View) {
        val layoutManager = binding.rvCity.layoutManager as? LinearLayoutManager
        binding.rvCity.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItemIndex = layoutManager?.findFirstVisibleItemPosition() ?: 0
                if (firstVisibleItemIndex > 1) {
                    binding.tvAlphabet.isVisible = true
                    binding.tvAlphabet.text = cityAdapter.data[firstVisibleItemIndex-1].initial
                } else {
                    binding.tvAlphabet.isVisible = false
                }
            }
        })

        binding.rvCity.adapter = cityAdapter
        binding.sideIndexBar.setOnIndexChangedListener { index, _ ->
            scrollToPosition(index)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        // 监听位置信息
        viewModel.currentLocation.observe(this) {
            if (!it?.cityName.isNullOrBlank()) {
                headerView.apply {
                    tvLocalCityTitle.isVisible = true
                    tvLocalCity.isVisible = true
                    tvLocalCity.text = it?.cityName
                }
            }
        }
        viewModel.startRealLocation()

        // 监听城市列表
        activityViewModel?.cityListLD?.observe(this) {
            val cityList = mutableListOf<ItemCityBean>()
            val alphaList = mutableListOf<String>()
            it?.citys?.forEach { item ->
                if (!alphaList.contains(item.initial)) {
                    alphaList.add(item.initial)
                    cityList.add(ItemCityBean(initial = item.initial))
                }
                cityList.add(item)
            }
            cityAdapter.setList(cityList)
            hotCityAdapter.setList(it?.hot_citys)
        }
    }

    private fun scrollToPosition(alphabet: String) {
        cityAdapter.data.find { it.initial == alphabet && !it.title.isNullOrBlank() }?.apply {
            binding.tvAlphabet.isVisible = true
            binding.tvAlphabet.text = alphabet
            binding.rvCity.realScrollToPosition(
                cityAdapter.data.indexOf(
                    this
                )
            )
        }
    }
}