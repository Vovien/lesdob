package com.tubewiki.mine.view.city

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.activityViewModels
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.view.state.initState
import com.blankj.utilcode.util.KeyboardUtils
import com.jmbon.middleware.search.SearchMessageViewModel
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.FragmentSelectCityBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Route(path = "/mine/city/select")
class SelectCityFragment : AppBaseFragment<FragmentSelectCityBinding>() {
    private val model: SearchMessageViewModel by activityViewModels()

    private val viewModel by lazy {
        activity?.viewModelStore?.let {
            ViewModelProvider(it, ViewModelFactory<Any, Any?>()).get(
                SelectCityViewModel::class.java
            )
        }
    }

    override fun beforeViewInit() {
        super.beforeViewInit()

    }

    override fun initView(view: View) {
        model.needShowSearchContentWhenInit.value = true
        model.showCancel.value = false
        model.enableEdited.value = false
        model.showBack.value = true
        model.editHint.value = "请输入城市名称"
        initPageState(binding.viewPager.initState { refreshDataWhenError() })
        context?.let { initTabWithViewPage(it) }

    }

    private fun initTabWithViewPage(context: Context) {
        binding.apply {
            val array = context.resources?.getStringArray(R.array.select_city)
            //类型【1：备孕中，2：怀孕中，3：有宝宝，4：试管婴儿】
            val fragmentList = arrayListOf(
                ARouter.getInstance().build("/mine/domestic/city/select").navigation() as Fragment,
                ARouter.getInstance().build("/mine/overseas/city/select").navigation() as Fragment
            )
            // 必要
            commonTabLayout.setViewPager(
                viewPager,
                array,
                childFragmentManager,
                fragmentList,
            )
            viewPager.addOnPageChangeListener(object :
                ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                }

                override fun onPageSelected(position: Int) {
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }

    }

    override fun getData() {
        model.onEditClick.onEach {
            val bundle = Bundle()
            bundle.putBoolean(TagConstant.PAGE_TYPE, true)
            ARouter.getInstance().build("/middleware/search/activity")
                .withString(TagConstant.SEARCH_CONTENT, "/mine/overseas/city/select")
                .withBundle(TagConstant.SEARCH_CONTENT_BUNDLE, bundle)
                .withBoolean(TagConstant.DIRECT_SEARCH, false)
                .navigation()
        }.launchIn(lifecycleScope)
        getCityList()
    }

    private fun getCityList() {
        started {
            viewModel?.getCityList()?.next {
                // 分发数据
                viewModel?.chinaCityList?.value = Pair(this.chinaCityList, this.hotCityList)
                viewModel?.overseasCountryList?.value = this.overseasCountryList
                showContentState()
            }
        }
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        getData()
    }

}