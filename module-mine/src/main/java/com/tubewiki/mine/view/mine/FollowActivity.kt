package com.tubewiki.mine.view.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.base.ViewModelActivity

import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.ViewPagerAdapter
import com.tubewiki.mine.databinding.ActivityFollowBinding


/**
 * 我的关注
 */
@Route(path = "/mine/follow")
class FollowActivity :
    AppBaseActivity<ActivityFollowBinding>() {


    private val fragmentList by lazy {
        arrayListOf(
            ARouter.getInstance().build("/mine/follow/follow_questions").navigation() as Fragment,
            ARouter.getInstance().build("/mine/follow/follow_topic").navigation() as Fragment,
            ARouter.getInstance().build("/mine/follow/follow_column").navigation() as Fragment,
            ARouter.getInstance().build("/mine/follow/follow_doctor").navigation() as Fragment,
            ARouter.getInstance().build("/mine/follow/follow_user").navigation() as Fragment,
        )
    }


    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.my_follow))

        binding.viewpager.adapter = ViewPagerAdapter(fragmentList, supportFragmentManager)
        val list = arrayListOf("问题", "话题", "专栏", "医生", "用户")

        binding.stlTitle.setTitle(list.toTypedArray())
        binding.stlTitle.setSnapOnTabClick(true)
        binding.stlTitle.textBold = 1
        binding.stlTitle.setViewPager(binding.viewpager)
        binding.viewpager.offscreenPageLimit = fragmentList.size
    }

    override fun initData() {
    }

    override fun getData() {

    }


}