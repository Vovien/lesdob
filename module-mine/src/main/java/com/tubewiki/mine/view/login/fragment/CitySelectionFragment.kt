package com.tubewiki.mine.view.login.fragment

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.bean.CityList
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.databinding.FragmentCitySelectionBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


const val REQUEST_CODE_CITY_SELECTION = 1001
class CitySelectionFragment : AppBaseFragment<FragmentCitySelectionBinding>() {

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun selectCity(event: CityList.ChinaCity) {
        binding.sbSelectCity.text = event.title
    }

    override fun initView(view: View) {
        binding.sbSelectCity.setOnSingleClickListener({
            ARouter.getInstance().build("/middleware/search/activity")
                .withString(TagConstant.SEARCH_CONTENT, "/mine/city/select")
                .withBoolean(TagConstant.DIRECT_SEARCH, false)
                .navigation()
        })
    }
}
