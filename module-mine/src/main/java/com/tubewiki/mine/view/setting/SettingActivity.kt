package com.tubewiki.mine.view.setting

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.ktx.gone
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivitySettingLayoutBinding

@Route(path = "/mine/setting/activity")
class SettingActivity : AppBaseActivity<ActivitySettingLayoutBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.app_setting))
        binding.msgNotificationLayout.gone()
        binding.loginSafeLayout.setOnClickListener {
            ARouter.getInstance().build("/mine/setting/login_safe").navigation()
        }
        binding.msgNotificationLayout.setOnClickListener {
            ARouter.getInstance().build("/mine/setting/notification").navigation()
        }
        binding.aboutApp.setOnClickListener {
            ARouter.getInstance().build("/mine/about/activity").navigation()
        }
    }

    override fun enableBack(): Boolean {
        return true
    }

    override fun initData() {
    }

    override fun getData() {
    }
}