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
 * 官方消息中心
 */
@Route(path = "/mine/message/jm_home")
class JmHomeMessageActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityJmHomeLayoutBinding>() {

    private val fragmentList by lazy {
        arrayListOf(
            ARouter.getInstance().build("/mine/fragment/message/official")
                .withString(TagConstant.TYPE, "all").navigation() as Fragment,
            ARouter.getInstance().build("/mine/fragment/message/official")
                .withString(TagConstant.TYPE, "reward").navigation() as Fragment,
            ARouter.getInstance().build("/mine/fragment/message/official")
                .withString(TagConstant.TYPE, "examine").navigation() as Fragment,
            ARouter.getInstance().build("/mine/fragment/message/official")
                .withString(TagConstant.TYPE, "violation").navigation() as Fragment,
            ARouter.getInstance().build("/mine/fragment/message/official")
                .withString(TagConstant.TYPE, "report").navigation() as Fragment,
        )
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.message_jmhome))

        initTabWithViewPage()

    }

    private fun initTabWithViewPage() {

        binding.viewPager.adapter = ViewPagerAdapter(fragmentList, supportFragmentManager)

        binding.commonTabLayout.setTitle(resources.getStringArray(R.array.jm_message_item))
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