package com.tubewiki.mine.view.setting

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.dialog.TipsDialog
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityLogOffBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.tubewiki.mine.view.model.SettingViewModel
import com.jmbon.widget.GeneralDialog
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup
import com.tencent.android.tpush.XGPushManager
import org.greenrobot.eventbus.EventBus

/**
 * 注销页面
 */
@Route(path = "/mine/setting/login_off")
class LogOffActivity : ViewModelActivity<SettingViewModel, ActivityLogOffBinding>() {

    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }
    private val captchaView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(CaptchaViewModel::class.java)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.log_off_submit))


        binding.llAgreement.setOnSingleClickListener({
            ARouter.getInstance().build("/webview/activity")
                .withString("url", BuildConfig.NOTICE_OF_CANCELLATION_CLAUSE)
                .withString("title", getString(R.string.user_agreement_login_off_title))
                .navigation()
        })

        binding.sbLogout.setOnClickListener {
            if (binding.checkView.isChecked) {
                //showCallDialog()
                if (cancleSign.isNullOrEmpty()) {
                    ARouter.getInstance().build("/mine/setting/login_off_identify")
                        .navigation(this, 100)
                } else {
                    logOff(cancleSign, 0)
                }

            } else {
                getString(R.string.please_read_check_cancellation_notice_by_user).showToast()
            }
        }
    }

    override fun initData() {
    }

    override fun getData() {
    }

    private fun showCallDialog() {

        val listData = arrayListOf(
            CustomDialogTypeBean(
                resources.getString(R.string.call_kefu),
                CustomDialogTypeBean.TYPE_TITLE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                resources.getString(R.string.call_kefu_phone),
                CustomDialogTypeBean.TYPE_MESSAGE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                resources.getString(R.string.currency_cancle), CustomDialogTypeBean.TYPE_CANCEL
            ) as MultiItemEntity,
        )
        XPopup.Builder(this)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                CustomListBottomDialog(this, listData) { select ->
                    if (select == 1)
                        PhoneUtils.dial(resources.getString(R.string.call_kefu_phone))
                }
            ).show()
    }

    var cancleSign: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == 101) {
            //  "身份验证成功".showToast()
            cancleSign = data?.getStringExtra("sign")
        }

    }

    fun logOff(cancelSign: String?, isForce: Int) {
        cancelSign?.let {
            started {
                loginView.cancelAccount(it, isForce).netCatch {
                    message.showToast()
                }.next {
                    when (type) {
                        200 -> {
                            //注销成功
                            logout()
                        }
                        883, 887 -> {
                            showDialog(title, description)
                        }
                        884 -> {
                            showWarnDialog(title, description)
                        }
                    }
                }

            }
        }
    }


    fun showDialog(title: String, content: String) {

        XPopup.Builder(this)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                TipsDialog(this, title, content, false) {

                }
            ).show()
    }

    fun showWarnDialog(title: String, content: String) {
        GeneralDialog.showWarnDialog(this,
            title,
            content,
            getString(R.string.wait_think),
            getString(R.string.log_off_account_now),
            { logOff(cancleSign, 1) },
            { })
    }

    private fun logout() {

        logoutJmbon()
    }

    fun logoutJmbon() {
        // 直接退出
//        XGPushManager.unregisterPush(Utils.getApp())

        Constant.cleanLoginInfo()

        EventBus.getDefault().post(UserLoginEvent(false))

        ARouter.getInstance().build("/question/tort/submit")
            .withBoolean(
                TagConstant.PARAMS4,
                true
            ).withString(
                TagConstant.PARAMS,
                StringUtils.getString(R.string.see_you_again)
            )
            .withBoolean(TagConstant.PARAMS3, false)
            .navigation()

    }

}