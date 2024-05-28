package com.jmbon.middleware.push

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.utils.GsonUtil
import com.apkdv.mvvmfast.utils.cleanMMKV
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.arouter.MessageNotificationService
import com.jmbon.middleware.bean.MessagePointBean
import com.jmbon.middleware.bean.PregnantStatusDetailBean
import com.jmbon.middleware.bean.PushData
import com.jmbon.middleware.bean.event.GroupNotifyEvent
import com.jmbon.middleware.bean.event.MenstruationPush
import com.jmbon.middleware.bean.event.MessageNumEvent
import com.jmbon.middleware.bean.event.NewMessageEvent
import com.jmbon.middleware.bean.event.TubeDialogEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.dialog.HotType
import com.jmbon.middleware.dialog.ToastHasMessageDialog
import com.jmbon.middleware.dialog.ToastType
import com.jmbon.middleware.dialog.TodayHotDialog
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.widget.dialog.MyToastPopupAnimator
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.tencent.android.tpush.NotificationAction
import com.tencent.android.tpush.XGPushBaseReceiver
import com.tencent.android.tpush.XGPushClickedResult
import com.tencent.android.tpush.XGPushRegisterResult
import com.tencent.android.tpush.XGPushShowedResult
import com.tencent.android.tpush.XGPushTextMessage
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar

class MessageReceiver : XGPushBaseReceiver() {

    companion object {
        const val UPDATE_LISTVIEW_ACTION = "com.tubewiki.android.activity.UPDATE_LISTVIEW"
        const val TEST_ACTION = "com.tubewiki.android.activity.TEST_ACTION"
        const val LogTag = "MessageReceiver"
    }


    private val messageNotificationService by lazy {
        ARouter.getInstance().build("/notification/start/service")
            .navigation() as MessageNotificationService
    }


    private val ignoreActivity by lazy { arrayListOf("AskDetailsActivity") }
    private val helpMessageIgnoreActivity by lazy { arrayListOf("CircleGroupActivity") }
    private var withinPop: BasePopupView? = null
    fun createMsgDialog(
        type: ToastType,
        itemId: Int,
        msg: String = "",
        title: String = "",
        questionId: Int = 0
    ) {
        LogUtils.e(AppUtils.isAppForeground())
        if (!AppUtils.isAppForeground()) {
            NotificationUtils.generalMessagesNotification(
                Utils.getApp(),
                type,
                itemId,
                msg,
                questionId
            )
        } else {
            if (ignoreActivity.contains(ActivityUtils.getTopActivity().javaClass.simpleName)) {
                return
            }
            if (withinPop?.isShow == true)
                withinPop?.dismiss()
            LogUtils.e(ActivityUtils.getTopActivity().javaClass.name)
            XPopup.Builder(ActivityUtils.getTopActivity())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isCenterHorizontal(true)
                .offsetY(24f.dp())
                .isLightStatusBar(true)
                .hasShadowBg(false)
                .isRequestFocus(false)
                .customAnimator(MyToastPopupAnimator(24f))
                .asCustom(
                    ToastHasMessageDialog(
                        ActivityUtils.getTopActivity(),
                        type,
                        itemId,
                        msg,
                        title,
                        questionId = questionId
                    )
                )
                .show()
        }

    }

    /**
     * 消息透传处理
     *
     * @param context
     * @param message 解析自定义的 JSON
     */
    override fun onTextMessage(context: Context?, message: XGPushTextMessage?) {
        if (context == null || message == null) {
            return
        }

        val text = "收到消息:$message"
        // 获取自定义key-value
        val customContent = message.customContent
        if (customContent != null && customContent.isNotEmpty()) {
            try {
                //消息中心打赏透传消息
                if ("消息红点".equals(message.title)) {
                    val messagePointBean =
                        GsonUtil.gson().fromJson(customContent, MessagePointBean::class.java)

                    val total =
                        messagePointBean.numbers.answer + messagePointBean.numbers.comment + messagePointBean.numbers.fans + messagePointBean.numbers.reward + messagePointBean.numbers.offical + messagePointBean.numbers.interaction
                    EventBus.getDefault().post(MessageNumEvent(total, -1, messagePointBean))
                    return
                }

                val obj = JSONObject(customContent)
                if (obj.has("type")) {
                    val type = obj.getString("type")
                    val pushData =
                        GsonUtil.gson().fromJson(customContent, PushData::class.java)
                    if (Constant.isLogin) {
                        when (type) {
                            "square_help" -> {
                                if (pushData.content.uid != Constant.getUserId()) {
                                    // 求助消息
                                    EventBus.getDefault().post(
                                        NewMessageEvent.pushMessage(
                                            1,
                                            NewMessageEvent.HELP, pushData
                                        )
                                    )
                                }

                                //如果正在当前圈子页面有人回答求助就不提示消息
                                if (helpMessageIgnoreActivity.contains(ActivityUtils.getTopActivity().javaClass.simpleName) && pushData.groupId != 0
                                    && pushData.groupId == Constant.CURRENT_GROUP_ID
                                ) {
                                    return
                                }


                                //如果医生的名称太长就截取一部分
                                createMsgDialog(
                                    ToastType.CIRCLE,
                                    pushData.content.questionId,
                                    if (pushData.verifyType == 2) "${
                                        if (pushData.content.userName.isNotNullEmpty() && pushData.content.userName.length > 15)
                                            "${
                                                pushData.content.userName.substring(
                                                    0,
                                                    15
                                                )
                                            }..." else pushData.content.userName
                                    }医生回复了你的求助" else "", pushData.groupName
                                )
                            }
                            // 违规消息
                            "forbidden", "violation", "block", "delete_member" -> {
                                EventBus.getDefault().post(
                                    NewMessageEvent.pushMessage(
                                        1,
                                        NewMessageEvent.SYSTEM, pushData
                                    )
                                )
                            }

                            /**
                             * 1.0 问题
                             */
                            "answer_question" -> {
                                createMsgDialog(
                                    ToastType.QUESTIONS,
                                    pushData.content.answerId,
                                    pushData.content.answerContent,
                                    questionId = pushData.content.questionId
                                )
                            }
                            /**
                             * 申诉消息
                             */
                            "appeal_list" -> {
                                EventBus.getDefault().post(
                                    NewMessageEvent.pushMessage(
                                        1, NewMessageEvent.APPEAL, pushData
                                    )
                                )
                            }

                            /**
                             * 姨妈时间提示
                             */
                            "menstrual" -> {
                                NotificationUtils.generalMenstrualMessagesNotification(
                                    Utils.getApp(),
                                    System.currentTimeMillis(),
                                    pushData.content.title,
                                    pushData.content.subTitle,
                                    "menstrual"
                                )
                            }

                            /**
                             * 用药管理时间提示
                             */
                            "medication_push" -> {
                                NotificationUtils.generalMenstrualMessagesNotification(
                                    Utils.getApp(),
                                    System.currentTimeMillis(),
                                    pushData.content.title,
                                    pushData.content.subTitle,
                                    "medication_push"
                                )
                            }


                            /**
                             * 群通知删除添加更新提示
                             */
                            "handler_group_notice" -> {
                                EventBus.getDefault().post(
                                    GroupNotifyEvent(pushData.content.number)
                                )
                            }

                            /**
                             * 试管婴儿弹窗选择
                             */
                            "tube_modal_menu" -> {
                                EventBus.getDefault().post(
                                    TubeDialogEvent()
                                )
                            }

                            /**
                             * 怀孕二级页面广播推送：收藏,加入圈子，回答问题
                             */
                            "second_page_notice" -> {
                                var data = convertToMessage(pushData)
                                EventBus.getDefault().post(data)
                            }
                            "mark_title" -> {
                                EventBus.getDefault().post(MenstruationPush())
                            }
                        }
                    }
                }

                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    val value = obj.getString("key")
                    Log.d(LogTag, "get custom value:$value")
                }
                // ...
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        // APP自主处理消息的过程...
        Log.d(LogTag, text)
        show(context, text)
    }


    /**
     * 通知展示
     *
     * @param context
     * @param notifiShowedRlt 包含通知的内容
     */
    override fun onNotificationShowedResult(
        context: Context?,
        notifiShowedRlt: XGPushShowedResult?
    ) {
        if (context == null || notifiShowedRlt == null) {
            return
        }


        handleHotPush(context, notifiShowedRlt)

        val notific = XGNotification()
        notific.msg_id = notifiShowedRlt.msgId
        notific.title = notifiShowedRlt.title
        notific.content = notifiShowedRlt.content
        // notificationActionType==1为Activity，2为url，3为intent
        notific.notificationActionType = notifiShowedRlt
            .notificationActionType

        // Activity,url,intent都可以通过getActivity()获得
        notific.activity = notifiShowedRlt.activity
        notific.update_time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(Calendar.getInstance().time)

        //NotificationService.getInstance(context).save(notific);


        val testIntent = Intent(TEST_ACTION)
        if (notifiShowedRlt.title == Constants.LOCAL_NOTIFICATION_TITLE) {
            testIntent.putExtra("step", Constants.TEST_LOCAL_NOTIFICATION)
        } else {
            testIntent.putExtra("step", Constants.TEST_NOTIFICATION)
        }

        context.sendBroadcast(testIntent)
        val viewIntent = Intent(UPDATE_LISTVIEW_ACTION)

        context.sendBroadcast(viewIntent)
        show(context, "您有1条新消息, 通知被展示 ， $notifiShowedRlt")
        Log.d(
            LogTag,
            "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString() + ", PushChannel:" + notifiShowedRlt.pushChannel
        )
    }

    val needCallActivity = arrayListOf(
        "com.tubewiki.android.view.MainActivity",
        "com.tubewiki.home.view.banner.PregnantStatusDetailActivity",
        "com.tubewiki.home.view.article.details.ArticleDetailsActivity",
        "com.tubewiki.questions.activity.AskDetailActivity",
        "com.tubewiki.questions.activity.AnswerDetailActivity"
    )

    private fun handleHotPush(context: Context?, notifiShowedRlt: XGPushShowedResult?) {

        //app不在前台就通知栏展示
        if (!AppUtils.isAppForeground()) {
            return
        }
        //app在前台就应用内展示，清空系统通知
        notifiShowedRlt?.let {
            val pushData =
                GsonUtil.gson().fromJson(it.customContent, PushData::class.java)
            //如果热点推送flag==0就表示使用系统通知，不调用自定义弹窗
            if (pushData.type != "hot_push" || (pushData.type == "hot_push" && pushData.msgFlag == 0)) {
                return@let
            }
            //退出登录清空所有通知
            val mgr = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mgr.cancel(it.notifactionId)

            var activity = ActivityUtils.getTopActivity()
            if (needCallActivity.contains(activity.javaClass.name)) {
                ThreadUtils.runOnUiThread {
                    XPopup.Builder(activity)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isCenterHorizontal(true)
                        .isLightStatusBar(true)
                        .hasShadowBg(false)
                        .isRequestFocus(false)
                        .dismissOnTouchOutside(true)
                        .dismissOnBackPressed(true)
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
                }

                TagConstant.TODAY_HOT.cleanMMKV()
            } else {
                it.customContent.saveToMMKV(TagConstant.TODAY_HOT)
            }
        }


    }

    /**
     * 注册回调
     *
     * @param context
     * @param errorCode 0 为成功，其它为错误码
     */
    override fun onRegisterResult(
        context: Context?,
        errorCode: Int,
        message: XGPushRegisterResult?
    ) {
        if (context == null || message == null) {
            return
        }
        var text = ""
        text = if (errorCode == SUCCESS) {
            // 在这里拿token
            val token = message.token
            "注册成功1. token：$token"
        } else {
            message.toString() + "注册失败，错误码：" + errorCode
        }
        Log.d(LogTag, text)
        //show(context, text);
    }

    /**
     * 反注册回调
     *
     * @param context
     * @param errorCode 0 为成功，其它为错误码
     */
    override fun onUnregisterResult(context: Context, errorCode: Int) {
        if (context == null) {
            return
        }
        var text = ""
        text = if (errorCode == SUCCESS) {
            "反注册成功"
        } else {
            "反注册失败$errorCode"
        }
        Log.d(LogTag, text)
        //show(context, text);
    }

    /**
     * 设置标签回调
     *
     * @param context
     * @param errorCode 0 为成功，其它为错误码
     * @param tagName   设置的 TAG
     */
    override fun onSetTagResult(context: Context?, errorCode: Int, tagName: String) {
        if (context == null) {
            return
        }
        var text = ""
        text = if (errorCode == SUCCESS) {
            "\"$tagName\"设置成功"
        } else {
            "\"$tagName\"设置失败,错误码：$errorCode"
        }
        Log.d(LogTag, text)
        //show(context, text);
        val testIntent = Intent(TEST_ACTION)
        testIntent.putExtra("step", Constants.TEST_SET_TAG)
        context.sendBroadcast(testIntent)
    }

    /**
     * 删除标签的回调
     *
     * @param context
     * @param errorCode 0 为成功，其它为错误码
     * @param tagName   设置的 TAG
     */
    override fun onDeleteTagResult(context: Context?, errorCode: Int, tagName: String) {
        if (context == null) {
            return
        }
        var text = ""
        text = if (errorCode == SUCCESS) {
            "\"$tagName\"删除成功"
        } else {
            "\"$tagName\"删除失败,错误码：$errorCode"
        }
        Log.d(LogTag, text)
        //show(context, text);
        val testIntent = Intent(TEST_ACTION)
        testIntent.putExtra("step", Constants.TEST_DEL_TAG)
        context.sendBroadcast(testIntent)
    }

    /**
     * 设置账号回调
     *
     * @param context
     * @param errorCode 0 为成功，其它为错误码
     * @param account   设置的账号
     */
    override fun onSetAccountResult(context: Context, errorCode: Int, account: String) {
        val testIntent = Intent(TEST_ACTION)
        testIntent.putExtra("step", Constants.TEST_SET_ACCOUNT)
        context.sendBroadcast(testIntent)
    }

    /**
     * 删除账号回调
     *
     * @param context
     * @param errorCode 0 为成功，其它为错误码
     * @param account   设置的账号
     */
    override fun onDeleteAccountResult(context: Context, errorCode: Int, account: String) {
        val testIntent = Intent(TEST_ACTION)
        testIntent.putExtra("step", Constants.TEST_DEL_ACCOUNT)
        context.sendBroadcast(testIntent)
    }

    override fun onSetAttributeResult(context: Context, i: Int, s: String) {}
    override fun onQueryTagsResult(context: Context, i: Int, s: String, s1: String) {}
    override fun onDeleteAttributeResult(context: Context, i: Int, s: String) {}


    /**
     * 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
     *
     * @param context
     * @param message 包含被点击通知的内容
     */
    override fun onNotificationClickedResult(context: Context?, message: XGPushClickedResult?) {
        if (context == null || message == null) {
            return
        }
        var text = ""
        if (message.actionType == NotificationAction.clicked.type.toLong()) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :$message"
            //打开首页
//            ARouter.getInstance().build("/main/main")
//                    .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    .navigation(context);
            //未定义通知操作
        } else if (message.actionType == NotificationAction.delete.type.toLong()) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :$message"
        }
        //ToastUtils2.showShort("广播接收到通知被点击:" + message.toString());
        // 获取自定义key-value
        val customContent = message.customContent
        if (customContent != null && customContent.isNotEmpty()) {
            try {
                val obj = JSONObject(customContent)
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    val value = obj.getString("key")
                    Log.d(LogTag, "get custom value:$value")
                }
                // ...
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        // APP自主处理的过程。。。
        Log.d(LogTag, text)
        //show(context, text);
    }

    private fun show(context: Context, text: String) {
        //ToastUtils2.showShort(text);
    }


    private fun convertToMessage(pushData: PushData): PregnantStatusDetailBean.Message {
        val message = PregnantStatusDetailBean.Message()
        message.userName = pushData.content.userName
        message.type = pushData.content.type
        message.uid = pushData.content.uid
        message.title = pushData.content.title
        message.createTime = pushData.content.createTime.toLong()
        message.avatarFile = pushData.content.avatarFile
        message.data = PregnantStatusDetailBean.Message.Data(
            answerId = pushData.content.data.answerId,
            covers = pushData.content.data.covers,
            groupId = pushData.content.data.groupId,
            id = pushData.content.data.id,
            isJoin = pushData.content.data.isJoin,
            itemId = pushData.content.data.itemId,
            memberCount = pushData.content.data.memberCount,
            name = pushData.content.data.name,
            number = pushData.content.data.number,
            questionContent = pushData.content.data.questionContent,
            title = pushData.content.data.title,
            questionCount = pushData.content.data.questionCount,
        )

        return message
    }

}