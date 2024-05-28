package com.tubewiki.mine.view.coupon

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.ktx.resumed
import com.apkdv.mvvmfast.view.state.showEmptyState2
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityCouponBinding

/**
 * 折扣券页面
 */
@Route(path = "/coupon/main")
class CouponActivity : AppBaseActivity<ActivityCouponBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.mine_rebate))
        initPageState(binding.stateLayout)


        resumed {
            showEmptyState()
        }
    }


    override fun initData() {
    }

    override fun getData() {

    }

    override fun showEmptyState() {
        stateContainer?.showEmptyState2("暂无抵扣券可使用")
    }

}