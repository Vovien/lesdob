package com.jmbon.middleware.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.dialog.ToastType
import com.jmbon.middleware.utils.TagConstant


object NotificationUtils {
    /**
     * 普通消息
     */
    fun generalMessagesNotification(
        context: Context,
        type: ToastType,
        itemId: Int,
        msg: String,
        questionId: Int = 0
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val postcard = when (type) {
            ToastType.CIRCLE -> {
                ARouter.getInstance().build("/circle/ask/details")
                    .withInt(TagConstant.QUESTION_ID, itemId)
            }
            ToastType.QUESTIONS -> {
                ARouter.getInstance().build("/question/activity/answer_detail")
                    .withInt(TagConstant.QUESTION_ID, questionId)
                    .withInt(TagConstant.ANSWER_ID, itemId)
            }
            else -> {
                ARouter.getInstance().build("/circle/ask/details")
                    .withInt("question_id", itemId)
            }
        }
        LogisticsCenter.completion(postcard)
        val intent = Intent(context, postcard.destination)
        intent.putExtras(postcard.extras)

        val receiverIntent = Intent(context, NotificationClickReceiver::class.java)
        receiverIntent.putExtra("type", type.name)
        receiverIntent.putExtra("intent", intent)

        val contentIntent = PendingIntent.getBroadcast(
            context,
            itemId,
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        //第二步：实例化通知栏构造器NotificationCompat.Builder：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //判断API
            val mChannel = NotificationChannel(
                itemId.toString(), "提问回答通知",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel.enableLights(true)
            manager.createNotificationChannel(mChannel)
        }
        val notification = notify(
            context, itemId.toString(),
            when (type) {
                ToastType.CIRCLE -> StringUtils.getString(R.string.message_from_circle)
                ToastType.QUESTIONS -> "您提的问题“${msg}”"
                else -> ""
            },
            when (type) {
                ToastType.CIRCLE -> if (msg.isEmpty()) StringUtils.getString(R.string.someone_answered_your_question) else msg
                ToastType.QUESTIONS -> StringUtils.getString(R.string.has_new_answer)
                else -> ""
            }, contentIntent
        ).build()
        //第三步：对Builder进行配置：
        manager.notify(itemId, notification)
    }

    /**
     * 普通消息
     */
    fun generalMenstrualMessagesNotification(
        context: Context,
        id: Long,
        title: String,
        msg: String,
        type: String,
    ) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, NotificationClickReceiver::class.java)
        intent.putExtra("type", type)
        val contentIntent = PendingIntent.getBroadcast(
            context,
            200,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        //第二步：实例化通知栏构造器NotificationCompat.Builder：


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //判断API
            val mChannel = NotificationChannel(
                id.toString(), "经期提醒",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mChannel.enableLights(true)
            manager.createNotificationChannel(mChannel)
        }
        val notification = notify(
            context, id.toString(), title, msg, contentIntent
        ).build()
        //第三步：对Builder进行配置：
        manager.notify(id.toInt(), notification)

    }


    fun notify(
        context: Context,
        itemId: String,
        title: String,
        content: String,
        contentIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, itemId)
            .setContentTitle(title)
            .setContentText(content)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.icon_jmbon_launcher)
            .setContentIntent(contentIntent)
    }
}