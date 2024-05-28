package com.tubewiki.mine.view.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.allen.library.SuperTextView
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.pay.bean.WxLoginEvent
import com.jmbon.pay.ext.loginWx
import com.jmbon.widget.GeneralDialog
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivitySettingLoginSafeLayoutBinding
import com.tubewiki.mine.view.login.model.CaptchaViewModel
import com.tubewiki.mine.view.login.model.OneKeyLoginViewModel
import com.tubewiki.mine.view.model.SettingViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 登录安全设置
 */
@Route(path = "/mine/setting/login_safe")
class LoginSafeActivity :
    ViewModelActivity<SettingViewModel, ActivitySettingLoginSafeLayoutBinding>(),
    View.OnClickListener {
    private val captchaView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(OneKeyLoginViewModel::class.java)
    }
    private val userinfo by lazy { Constant.userInfo }

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.login_security))
        binding.stModifyPhone.setOnClickListener(this)
        binding.stSecureEmail.setOnClickListener(this)
        binding.stSetPwd.setOnClickListener(this)
        binding.stModifyWx.setOnClickListener(this)

        binding.stSetPwd.gone()
        binding.stSecureEmail.gone()

        binding.sbLogOff.setOnSingleClickListener({
            //LesDoB注销没有中间流程，直接输入电话号码
            ARouter.getInstance().build("/mine/setting/login_off_identify")
                .navigation(this, 100)
            //  ARouter.getInstance().build("/mine/setting/login_off").navigation()
        })

        binding.sbLogout.setOnClickListener {
            //退出弹窗
            val listData = arrayListOf(
                CustomDialogTypeBean(
                    resources.getString(R.string.logout_app),
                    1
                ) as MultiItemEntity,

                CustomDialogTypeBean(
                    resources.getString(R.string.logout_sure), R.color.color_FF5A5F,
                    2
                ) as MultiItemEntity,
                CustomDialogTypeBean(
                    resources.getString(com.jmbon.widget.R.string.currency_cancle),
                    3
                ) as MultiItemEntity,
            )
            XPopup.Builder(this@LoginSafeActivity)
                .isDestroyOnDismiss(true)
                .asCustom(
                    CustomListBottomDialog(this@LoginSafeActivity, listData) {
                        //确认退出
                        if (it == 1) {
                            logoutJmbon()
                        }
                    }
                ).show()
        }
    }


    fun logoutJmbon() {
        // 直接退出
        viewModel.logout()
        Constant.cleanLoginInfo()
        ARouter.getInstance().build("/main/main").navigation()
        EventBus.getDefault().post(UserLoginEvent(false))
    }

    private var userSafe: UserData.User? = null
    override fun initData() {
        viewModel.safeSet.observe(this) {
            userSafe = it
            //  setStatus(binding.stSecureEmail, it.safeSet.hasEmail, "")
            //   setStatus(binding.stSetPwd, it.safeSet.hasPassword, "")
            setStatus(binding.stModifyPhone, it.mobile.isNotNullEmpty(), it.mobile)
            setWxStatus(binding.stModifyWx, it.openid.isNotNullEmpty(), it.wechatName)
        }
    }

    override fun getData() {

    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserSafe()
    }

    fun unBindWeChat() {
        GeneralDialog.showNormalDialog2(this,
            "是否解除微信绑定？",
            "解除绑定后将无法使用${userSafe?.wechatName ?: ""}微信登录",
            getString(R.string.currency_cancle),
            getString(R.string.sure_confirm),
            {
                started {
                    viewModel.unBindWx().netCatch {
                        message.showToast()
                    }.next {
                        "解绑成功".showToast()
                        userSafe?.wechatName = ""
                        userSafe?.openid = ""
                        //解绑成功
                        setWxStatus(binding.stModifyWx, false, "")
                    }
                }
            },
            { })
    }

    fun bindWeChat() {
        loginWx(this)
    }

    override fun onClick(v: View) {
        if (v.id == binding.stModifyWx.id) {
            //直接开始解绑或者开始绑定
            if (userSafe?.openid.isNotNullEmpty()) {
                //解绑
                unBindWeChat()
            } else {
                //绑定微信
                bindWeChat()
            }
            return
        }

        val type = when (v.id) {
            // 已经有密码 设置新密码
            // 有密码，忘记密码 更新密码
//            binding.stSetPwd.id -> if (userSafe?.safeSet?.hasPassword == true)
//                CaptchaViewModel.UPDATE_PASSWORD else CaptchaViewModel.SET_NEW_PASSWORD
//            binding.stSecureEmail.id -> if (userSafe?.safeSet?.hasEmail == true)
//                CaptchaViewModel.UPDATE_EMAIL else CaptchaViewModel.SET_NEW_EMAIL
            else -> CaptchaViewModel.UPDATE_PHONE
        }
        // 2021年10月11日17:07:10 又改了需求，修改手机也要先验证身份了。
        ARouter.getInstance().build("/mine/setting/retrieve/password")
            .withInt("type", type)
            .navigation()
    }

    private fun setStatus(btn: SuperTextView, status: Boolean, text: String?) {
        if (status) {
            btn.setRightString(if (text.isNullOrEmpty()) getString(R.string.has_been_set) else text)
            btn.setRightTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
        } else {
            btn.setRightString(getString(R.string.not_set))
            btn.setRightTextColor(ColorUtils.getColor(R.color.colorFF5050))
        }

    }

    private fun setWxStatus(btn: SuperTextView, status: Boolean, text: String?) {
        if (status) {
            btn.setRightString(text)
            btn.setRightTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
            btn.setRightTvDrawableRight(getDrawable(R.drawable.icon_green_wx_login))
        } else {
            btn.setRightString(getString(R.string.not_bind))
            btn.setRightTextColor(ColorUtils.getColor(R.color.colorFF5050))
            btn.setRightTvDrawableRight(null)
        }
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun wxLoginEvent(event: WxLoginEvent) {
        event.either.data1?.let {
            if (it == null || it.openid.isNullOrEmpty()) {
                "微信授权失败".showToast()
                return@let
            }
            //微信登录成功
            Log.e("wxLoginEvent", "${it.toString()}")

            //绑定微信
            lifecycleScope.launch {

                viewModel.bindWx(it.openid, it.nickname)
                    .onStart {
                        showLoading()
                    }.netCatch {
                        dismissLoading()
                        message.showToast()
                    }.next {
                        dismissLoading()
                        "绑定成功".showToast()
                        userSafe?.openid = it.openid
                        userSafe?.wechatName = it.nickname
                        setWxStatus(binding.stModifyWx, true, it.nickname)
                    }
            }
        }
    }

}