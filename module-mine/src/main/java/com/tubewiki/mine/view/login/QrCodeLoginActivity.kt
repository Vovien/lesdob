package com.tubewiki.mine.view.login

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.loadCircle
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityQrcodeLoginBinding
import com.tubewiki.mine.view.login.model.LoginViewModel
import kotlinx.coroutines.flow.onCompletion

/**
 * 二维码登陆
 */
@Route(path = "/mine/login/qrcode")
class QrCodeLoginActivity : ViewModelActivity<LoginViewModel, ActivityQrcodeLoginBinding>() {

    private val CANCEL_LOGIN = 3
    private val LOGIN = 4

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var qrToken: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        qrToken = qrToken.replace("jmbQRCodeToken=", "")
       qrcodeLogin(2)
        setTitleName(getString(R.string.sweep_login_code))
        titleBarView.leftImageButton.setImageResource(R.drawable.icon_nav_close)
        binding.apply {
            Constant.userInfo.let {
                imageAvatar.loadCircle(it.avatarFile)
                textName.text = it.userName
            }
            btnConfirm.setOnClickListener {
                qrcodeLogin(4)
            }
            btnCancelLogin.setOnClickListener {
                qrcodeLogin(CANCEL_LOGIN)
            }
        }
    }

    override fun initData() {
    }

    override fun getData() {
    }

    /**
     * status 设置状态【2：设置为已扫码，3：设置为取消登陆，4：设置为已登陆】
     */
    private fun qrcodeLogin(status: Int) {
        started {
            viewModel.qrcodeLogin(qrToken, status).netCatch {
                if (status == LOGIN) {
                    message.showToast()
                } else {
                    this@QrCodeLoginActivity.finish()
                }
            }.onCompletion {
                binding.btnConfirm.stateButton()
                binding.btnCancelLogin.stateButton()
            }.next {
                if (status == CANCEL_LOGIN || status == LOGIN) {
                    this@QrCodeLoginActivity.finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        qrcodeLogin(CANCEL_LOGIN)
    }

    override fun finish() {
        super.finish()
        this@QrCodeLoginActivity.overridePendingTransition(
            R.anim.activity_background,
            R.anim.activity_bottom_out
        )
    }

}