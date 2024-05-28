package com.tubewiki.android.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.text.TextUtils
import android.view.KeyEvent
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.*
import com.google.android.material.shape.ShapeAppearanceModel
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.config.ShareSDKInit
import com.jmbon.middleware.oiad.OAIDUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.getRealChannel
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tencent.android.tpush.XGPushConfig
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebView
import com.tubewiki.android.R
import com.tubewiki.android.databinding.ActivityPrivacyAgreementBinding
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.*

@Route(path = "/main/privacy_agree")
class PrivacyAgreementActivity : AppBaseActivity<ActivityPrivacyAgreementBinding>() {

    override fun beforeViewInit() {
        super.beforeViewInit()
        StatusBarCompat.setStatusBarColor(this.window, ColorUtils.getColor(R.color.ColorFAFA))
        StatusBarCompat.setLightStatus(window)
    }

    override fun initView(savedInstanceState: Bundle?) {

        SpanUtils.with(binding.tvDesc2)
            .append("请查阅LesDoB")
            .append("《用户协议》")
            .setBold()
            .setClickSpan(ColorUtils.getColor(R.color.color_currency), false) {
                ARouter.getInstance().build("/webview/activity")
                    .withString("url", com.apkdv.mvvmfast.BuildConfig.USER_PROTOCOL)
                    .withString("title", StringUtils.getString(R.string.about_terms_service))
                    .withBoolean("enableTBS", false)
                    .navigation()
            }.append("和")
            .append("《隐私政策》")
            .setBold()
            .setClickSpan(ColorUtils.getColor(R.color.color_currency), false) {
                ARouter.getInstance().build("/webview/activity")
                    .withString("url", com.apkdv.mvvmfast.BuildConfig.PRIVACY_PROTOCOL)
                    .withString("title", StringUtils.getString(R.string.about_privacy_policy))
                    .withBoolean("enableTBS", false)
                    .navigation()
            }
            .append("，点击同意即表示您已仔细阅读并接受其完整条款。")
            .append("若点击“不同意”，则无法使用我们的产品和服务。")
            .setForegroundColor(ColorUtils.getColor(R.color.color_59373133))
            .create()


//        SpanUtils.with(binding.tvDesc3)
//            .append("医疗免责声明")
//            .setForegroundColor(ColorUtils.getColor(R.color.color_currency_error))
//            .append("：任何关于疾病的建议都不能替代执业医师的面对面诊断，请谨慎参阅。本站不承担由此引起的法律责任。")
//            .create()

        binding.ivCover.shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(8.dp.toFloat())
        binding.sbRefuse.setOnSingleClickListener({
            finish()
        })
        binding.sbAgree.setOnSingleClickListener({
            //应用宝要求不能登录注册默认勾选
            Constant.isCheckPrivacy = false
            true.saveToMMKV(Constant.AGREE_TO_PRIVACY_AGREEMENT)
            initSDK()
            toMain()
        })
    }

    private fun toMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun initData() {

    }

    override fun getData() {
    }


    private fun initSDK() {
        // 初始化埋点系统
        //PageBuryManager.init(application)
        MainScope().launch(Dispatchers.Main.immediate) {
            val job = mutableListOf<Deferred<String>>()

            //清楚页面提示标记
            false.saveToMMKV(MMKVKey.PRIVATE_ONE_KEY_LOGIN_PAGE)
            false.saveToMMKV(MMKVKey.PRIVATE_PASSWORD_LOGIN_PAGE)

            // 1. 对非必须在主线程初始化的第三方依赖发起并行初始化
            // 并行job
            job.add(async(Dispatchers.Default) {
                XGPushConfig.setAutoInit(true)
                XGPushConfig.enableAutoStart(this@PrivacyAgreementActivity, true)
                return@async "tpns"
            })

            job.add(async(Dispatchers.Default) {
                UMInit()
                return@async "UMInit"
            })
            job.add(async(Dispatchers.Default) {
                initQbSdk(application)
                return@async "initX5"
            })
            job.add(async(Dispatchers.Default) {
                ShareSDKInit()
                return@async "ShareSDKInit"
            })
            job.add(async(Dispatchers.Default) {
                // 阿里手机
//                MobileSDKInit.init(application)
                return@async "MobileSDKInit"
            })

            //等待每一个子线程初始化的依赖完成
            job.forEach { it.await() }
        }
        OAIDUtils.uploadOAID()
    }

    private fun UMInit() {
        UMConfigure.init(
            application,
            BuildConfig.UMENG_API_KEY,
            getRealChannel(),
            UMConfigure.DEVICE_TYPE_PHONE,
            null
        )
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }


    private fun initQbSdk(application: Application) {
        val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
            @SuppressLint("SetJavaScriptEnabled")
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                if (arg0 != true) {
//                    //设置自带webView属性
//                    WebView webView = new WebView(getApplicationContext());
//                    webView.getSettings().setJavaScriptEnabled(true);
//                    webView.getSettings().setBlockNetworkImage(false);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//                    }
//                }
            }

            override fun onCoreInitFinished() {}
        }
        //流量下载内核
        QbSdk.setDownloadWithoutWifi(true)
        //x5内核初始化接口
        QbSdk.initX5Environment(application, cb)
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        initPieWebView(application)
    }

    private fun initPieWebView(application: Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName(application)
            processName?.apply {
                if (PROCESS != this) {
                    WebView.setDataDirectorySuffix(getString(this@apply, "jmbon"))
                }
            }

        }
    }

    private fun getString(s: String, defValue: String): String {
        return if (TextUtils.isEmpty(s)) defValue else s
    }

    private fun getProcessName(context: Context): String? {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    private val PROCESS = "com.tubewiki.android"

    override fun onBackPressed() {
        R.string.agree_and_continue_user.showToast()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            R.string.agree_and_continue_user.showToast()
            return false
        }

        return super.onKeyDown(keyCode, event)

    }
}