package com.jmbon.middleware.config

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import com.apkdv.mvvmfast.bean.BindMiniToolEvent
import com.apkdv.mvvmfast.utils.*
import com.jmbon.middleware.bean.PushData
import com.jmbon.middleware.bean.event.TubeDialogEvent
import com.jmbon.middleware.dialog.HotType
import com.jmbon.middleware.dialog.TodayHotDialog
import com.jmbon.middleware.utils.TagConstant
import com.lxj.xpopup.XPopup
import org.greenrobot.eventbus.EventBus

class ActivityLifecyclesImpl : Application.ActivityLifecycleCallbacks {


    private val needCallActivity = arrayListOf(
        "com.tubewiki.android.view.MainActivity",
        "com.tubewiki.home.view.banner.PregnantStatusDetailActivity",
        "com.tubewiki.home.view.article.details.ArticleDetailsActivity",
        "com.tubewiki.questions.activity.AskDetailActivity",
        "com.tubewiki.questions.activity.AnswerDetailActivity"
    )

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        getResources(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        if (needCallActivity.contains(activity.javaClass.name)) {
            TagConstant.TODAY_HOT.getString()?.let {
                if (it.isNullOrEmpty()) {
                    return@let
                }
                val pushData =
                    GsonUtil.gson().fromJson(it, PushData::class.java)

                XPopup.Builder(activity)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .isCenterHorizontal(true)
                    .dismissOnTouchOutside(true)
                    .dismissOnBackPressed(true)
                    .isLightStatusBar(true)
                    .hasShadowBg(false)
                    .isRequestFocus(false)
                    .isClickThrough(true)
                    .asCustom(
                        TodayHotDialog(
                            activity,
                            if (pushData.content.type == 1) HotType.ARTICLE else HotType.QUESTIONS,
                            pushData.content.item_id,
                            pushData.content.title,
                            pushData.content.description,
                        )
                    )
                    .show()
                TagConstant.TODAY_HOT.cleanMMKV()

            }
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity::class.java.name == "com.alipay.mobile.nebulacore.ui.H5Activity") {
            val toolId = activity.intent.getStringExtra("toolId")
            toolId?.let {
                //设置小工具使用事件状态接口绑定后台次数
                EventBus.getDefault().post(BindMiniToolEvent(it))
            }
            if (Constant.SHOW_TUBE_DIALOG) {
                EventBus.getDefault().post(
                    TubeDialogEvent()
                )
            }
        }
    }

    /**
     * 设置标准字体
     */
    open fun getResources(activity: Activity): Resources? {
        val res = activity.resources
        val newConfig = Configuration()
        newConfig.setToDefaults() //设置默认
        res.updateConfiguration(newConfig, res.displayMetrics)
        return res
    }
}