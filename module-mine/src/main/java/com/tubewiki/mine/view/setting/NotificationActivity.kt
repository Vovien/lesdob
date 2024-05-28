package com.tubewiki.mine.view.setting

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.utils.getBoolean
import com.apkdv.mvvmfast.utils.mmkv
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.jmbon.middleware.config.Constant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivitySettingMsgNotificationLayoutBinding
import com.tubewiki.mine.view.model.SettingViewModel

/**
 * 消息通知设置
 */
@Route(path = "/mine/setting/notification")
class NotificationActivity :
    ViewModelActivity<SettingViewModel, ActivitySettingMsgNotificationLayoutBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.message_notification))

    }

    override fun initData() {
        viewModel.notifySetting.observe(this) {
            binding.apply {
                stByDynamic.switchIsChecked = it.notifyNews == 1
                stDirectMessages.switchIsChecked = it.notifyMessage == 1
                stExceptionalMessage.switchIsChecked = it.notifyReward == 1
                stNewsNotice.switchIsChecked = it.notifyFocus == 1
                stNewsNotice.switchIsChecked = it.notifyFocus == 1
                stHotNewsNotice.switchIsChecked = it.isPushHot == 1
            }

            binding.apply {
                stByDynamic.setSwitchCheckedChangeListener { buttonView, isChecked ->
                    viewModel.setNotifySetting("notify_news", getStatus(isChecked))
                }
                stDirectMessages.setSwitchCheckedChangeListener { buttonView, isChecked ->
                    viewModel.setNotifySetting(
                        "notify_message",
                        getStatus(isChecked)
                    )
                }
                stExceptionalMessage.setSwitchCheckedChangeListener { buttonView, isChecked ->
                    viewModel.setNotifySetting(
                        "notify_reward",
                        getStatus(isChecked)
                    )
                }
                stNewsNotice.setSwitchCheckedChangeListener { buttonView, isChecked ->
                    viewModel.setNotifySetting("notify_focus", getStatus(isChecked))
                }

                stHotNewsNotice.setSwitchCheckedChangeListener { buttonView, isChecked ->
                    viewModel.setNotifySetting("is_push_hot", getStatus(isChecked))
                }

            }
        }


    }

    override fun getData() {
        viewModel.getNotifySetting()
    }

    private fun getStatus(checked: Boolean): Int {
        return if (checked) 1 else 0
    }
}