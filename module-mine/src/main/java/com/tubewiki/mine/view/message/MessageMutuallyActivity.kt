package com.tubewiki.mine.view.message

import android.os.Bundle
import androidx.fragment.app.Fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.ViewPagerAdapter

import com.tubewiki.mine.databinding.ActivityJmHomeLayoutBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel



/**
 * 互动消息
 */
@Route(path = "/mine/message/mutually")
class MessageMutuallyActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityJmHomeLayoutBinding>() {

    private val fragmentList by lazy {
        arrayListOf(
            ARouter.getInstance().build("/mine/fragment/message/mutually")
                .withString(TagConstant.TYPE, "all").navigation() as Fragment,
            ARouter.getInstance().build("/mine/fragment/message/mutually")
                .withString(TagConstant.TYPE, "given").navigation() as Fragment,
            ARouter.getInstance().build("/mine/fragment/message/mutually")
                .withString(TagConstant.TYPE, "collect").navigation() as Fragment,
        )
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.message_mutually))

        initTabWithViewPage()

    }

    private fun initTabWithViewPage() {

        binding.viewPager.adapter = ViewPagerAdapter(fragmentList, supportFragmentManager)

        binding.commonTabLayout.setTitle(resources.getStringArray(R.array.mutually_item))
        binding.commonTabLayout.setViewPager(binding.viewPager)
        //设置默认选中问答
        binding.commonTabLayout.currentTab = 0
        binding.viewPager.currentItem = 0
        binding.viewPager.offscreenPageLimit = fragmentList.size
    }

    override fun initData() {
    }

    override fun getData() {
    }


}