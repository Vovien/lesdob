package com.tubewiki.mine.view.login


import android.content.ComponentName
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.GsonUtil
import com.blankj.utilcode.util.KeyboardUtils
import com.jmbon.middleware.arouter.RouterHub
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityThirdAuthLayoutBinding
import com.tubewiki.mine.view.login.model.LoginViewModel


/**
 * LesDoB第三方授权
 */
@Route(path = "/mine/third/auth")
class ThirdAuthActivity : ViewModelActivity<LoginViewModel, ActivityThirdAuthLayoutBinding>() {


    private var appId = ""

    override fun beforeViewInit() {
        super.beforeViewInit()

        appId = intent?.getStringExtra("appId") ?: ""
        Log.e("TAG1", "${intent?.getStringExtra("appId")}")

        if (appId.isNullOrEmpty()) {
            "授权异常".showToast()
            finish()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }

    override fun initView(savedInstanceState: Bundle?) {
        titleBarView.setLeftView(R.layout.include_btn_left_custom_cancel_title)
        titleBarView.leftImageButton.gone()
        titleBarView.leftCustomView.findViewById<TextView>(R.id.tv_left_title).setOnClickListener {
            onBackPressed()
        }

        binding.sbLogin.setOnClickListener {


            if (!Constant.isLogin) {
                "未登录"
                //未登录就要求登录
                ARouter.getInstance().build(RouterHub.LOGIN).navigation()
                return@setOnClickListener
            }

            if (appId.isNotNullEmpty()) {
                setResultUserData(true)
            }

        }
    }

    private fun setResultUserData(isAuth: Boolean = false) {
        try {
            //授权
            val componentName =
                ComponentName(
                    appId,
                    "${appId}.auth.AuthActivity"
                ) //这里是 包名  以及 页面类的全称

            val intent = Intent()
            intent.component = componentName
            intent.putExtra("value", if (isAuth) GsonUtil.gson().toJson(Constant.user) else "")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            "回调异常".showToast()
        }

        finish()
    }

    override fun onResume() {
        super.onResume()

        Action {
            initUser()
        }.logInToIntercept()

    }

    fun initUser() {
        if (Constant.userInfo != null) {
            binding.ivUser.loadCircle(Constant.userInfo.avatarFile)
            binding.tvUserName.text = Constant.userInfo.userName
        }
    }

    override fun initData() {

        var appInfo = getAppInfo(appId)
        appInfo?.let {
            //应用图标
            val pm = packageManager
            var appIcon = pm.getApplicationIcon(it)

            binding.ivAppIcon.setImageDrawable(appIcon)
            // 应用名称
            val name = pm.getApplicationLabel(it)
            binding.textAppTitle.text = name

        }
    }


    override fun getData() {
    }

    private fun getAppInfo(pakgename: String): ApplicationInfo? {
        val pm = packageManager
        try {
            var appInfo = pm.getApplicationInfo(pakgename, PackageManager.GET_META_DATA)
            return appInfo
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }


    override fun onBackPressed() {
        if (appId.isNotNullEmpty()) {
            setResultUserData(false)
        } else {
            super.onBackPressed()
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed()

            return false
        }

        return super.onKeyDown(keyCode, event)
    }

}