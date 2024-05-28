package com.tubewiki.android.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.arouter.MiniProgramService
import com.jmbon.middleware.utils.MobAnalysisUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.tubewiki.android.databinding.ActivityTransBinding
import com.tubewiki.android.install.MyUMLinkListener
import com.umeng.umlink.MobclickLink

/**
 * 推送通知:
 * 透明过渡界面
 */
@Route(path = "/main/trans")
class TransitionTransparentActivity : AppBaseActivity<ActivityTransBinding>() {

    private val miniProgram by lazy {
        ARouter.getInstance().build("/miniprogram/start/service").navigation() as MiniProgramService
    }

    override fun beforeViewInit() {
        StatusBarCompat.setTransparentStatusBar(this.window)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let {
            parseIntent(it)
            MobclickLink.handleUMLinkURI(this, intent.data, umlinkAdapter)
        }
    }

    private val umlinkAdapter by lazy {
        MyUMLinkListener(this, link = { path, map ->
            LogUtils.e(path)
            LogUtils.e(map.toString())
        }, install = { map, uri ->
            LogUtils.e("*******************************")
            LogUtils.e(uri.toString())
            LogUtils.e(map.toString())


        })
    }

    override fun initView(savedInstanceState: Bundle?) {
        MobclickLink.handleUMLinkURI(this, intent.data, umlinkAdapter);
        parseIntent(intent!!)
    }

    private fun parseIntent(intent: Intent) {

        if (ActivityUtils.isActivityExistsInStack(MainActivity::class.java) || ActivityUtils.isActivityExistsInStack(
                WelcomeActivity::class.java
            )
        ) {
            //app是已经打开的状态，就进行小程序跳转

            // onNewIntent 内获取 intent uri 代码
            val uri: Uri? = intent.data
            uri?.let {
                val toolid = it.getQueryParameter("toolid")
                val msgId = it.getQueryParameter("msg_id")
                val id = it.getQueryParameter("id")
                //Log.e("TAg", "msgId=${msgId}")
                //type 区分类型medication 用药管理
                val type = it.getQueryParameter("type")
                if (toolid.isNotNullEmpty()) {
                    //跳转到指定小程序
                    Action {
                        if ("2021111700000004" == toolid) {
                            //点击用药管理通知
                            MobAnalysisUtils.mInstance.sendEvent("Event_SystemPush_MedicationAssistant")
                            MobAnalysisUtils.mInstance.sendEvent("Event_SmallTools_MedicationAssistant")
                        } else if (BuildConfig.MENSES_ID == toolid) {
                            //点击姨妈经期通知
                            MobAnalysisUtils.mInstance.sendEvent("Event_SystemPush_AImenstruation")
                            MobAnalysisUtils.mInstance.sendEvent("Event_SmallTools_AImenstruation")
                        }
                        miniProgram.startMiniProgram(toolid!!)
                    }.logInToIntercept()
                } else if (type == "qa") {
                    ARouter.getInstance().build("/question/activity/ask_detail")
                        .withInt(TagConstant.QUESTION_ID, id?.toInt() ?: 0)
                        .navigation()
                } else if (type == "article") {
                    ArouterUtils.toArticleDetailsActivity(id?.toInt() ?: 0)
                } else if (type == "video") {
                    ARouter.getInstance().build("/video/details")
                        .withInt(TagConstant.VIDEO_ID, id?.toInt() ?: 0)
                        .navigation()
                } else {
                }
            }


            //IM消息推送通知
//            var message = OfflineMessageDispatcher.getExtIMMessage(intent)
//            message?.let {
//                when (it) {
//                    is PushData -> {
//                        //群聊
//                        OfflineMessageDispatcher.redirectPush(message as PushData)
//                    }
//                    is OfflineMessageBean -> {
//                        //单聊
//                        OfflineMessageDispatcher.redirect(message as OfflineMessageBean)
//                    }
//                    else -> {
//                    }
//                }
//            }
        } else {
            //打开app,并跳转
            //Log.e("TAG", "app dead parseIntent")
            //ARouter.getInstance().build("/main/welcome").navigation()
            intent.setClass(this, WelcomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        finish()

    }

    override fun initData() {

    }

    override fun getData() {

    }


}