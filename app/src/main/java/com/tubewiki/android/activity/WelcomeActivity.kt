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
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.utils.mmkv
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.config.ShareSDKInit
import com.jmbon.middleware.oiad.OAIDUtils
import com.jmbon.middleware.utils.getRealChannel
import com.tencent.android.tpush.XGPushConfig
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebView
import com.tubewiki.android.databinding.ActivityWelcomeBinding
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.*

@Route(path = "/main/welcome")
class WelcomeActivity : AppBaseActivity<ActivityWelcomeBinding>() {

    override fun beforeViewInit() {
        super.beforeViewInit()
        ImmersionBar.with(this)
            .transparentBar()
            .init()
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (mmkv.decodeBool(Constant.AGREE_TO_PRIVACY_AGREEMENT, false)) {
            initSDK()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, PrivacyAgreementActivity::class.java))
        }
        overridePendingTransition(0, 0)
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
                XGPushConfig.enableAutoStart(this@WelcomeActivity, true)
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


}