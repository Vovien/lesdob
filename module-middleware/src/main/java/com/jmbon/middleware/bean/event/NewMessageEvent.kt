package com.jmbon.middleware.bean.event

import androidx.annotation.Keep
import com.jmbon.middleware.bean.PushData

@Keep
class NewMessageEvent(
    var type: Int,
    var msgCount: Int,
    /**
     * 接受消息、但是不相应消息
     */
    var exclude: String = "",
    var isAdd: Boolean = false,
    var isRemove: Boolean = false,
    var subType: Int = 0,
    var pushData: PushData? = null
) {
    val isHelp: Boolean
        get() = HELP == type
    val isChat: Boolean
        get() = CHAT == type
    val isSystem: Boolean
        get() = SYSTEM == type
    val isAll: Boolean
        get() = ALL == type
    val isCircle: Boolean
        get() = CIRCLE == type
    val isClean: Boolean
        get() = CLEAN == type
    val isAppeal: Boolean
        get() = APPEAL == type
    val isRefreshCircle: Boolean
        get() = REFRESH_CIRCLE == type
    companion object {
        const val CHAT = 100
        const val HELP = 200
        const val SYSTEM = 300
        const val SYSTEM_BAN = 3001
        const val SYSTEM_BLACKLIST = 3002
        const val SYSTEM_VIOLATION = 3003
        const val CIRCLE = 400
        const val ALL = 500
        const val CLEAN = 600
        const val REFRESH_CIRCLE = 700

        // 管理员申诉
        const val APPEAL = 0x000800

        // 清除所有消息
        const val CLEAN_ALL = 900

        /**
         * 刷新圈子的消息页
         */
        fun refreshMessageCount(): NewMessageEvent {
            return NewMessageEvent(type = REFRESH_CIRCLE, 0)
        }

        /**
         * 刷新消息页的数据
         */
        fun cleanSystemMsg(): NewMessageEvent {
            return NewMessageEvent(type = SYSTEM, 0, isRemove = true)
        }

        /**
         * 收到系统的消息，用于底部红点数量、外部圈子数量 +1
         * 例如 ： 求助+1 刷新求助
         */
        fun pushMessage(msgCount: Int, type: Int, pushData: PushData? = null): NewMessageEvent {
            return NewMessageEvent(type, msgCount, isAdd = true, pushData = pushData, exclude = "")
        }


        /**
         * 处理过的消息（申诉处理过、求助、违规消息）,不刷新 Fragment
         */
        fun processMessage(msgCount: Int, type: Int): NewMessageEvent {
            return NewMessageEvent(type, msgCount, isRemove = true, pushData = null, exclude = "not refresh")
        }

        /**
         * 聊天的消息刷新,每次发送的都是当前的聊天消息总数
         */
        fun refreshChatEvent(msgCount: Int, isRemove: Boolean): NewMessageEvent {
            return NewMessageEvent(CHAT, msgCount, isRemove = isRemove, exclude = "")
        }


        /**
         * 推送过来的系统消息
         */
        fun refreshSystemEvent(msgCount: Int, subType: Int, pushData: PushData?): NewMessageEvent {
            return NewMessageEvent(
                SYSTEM,
                msgCount,
                isAdd = true,
                subType = subType,
                pushData = pushData,
                exclude = ""
            )
        }

        fun cleanSystemFroChatTop(subType:Int): NewMessageEvent {
            return NewMessageEvent(
                CLEAN,0,
                subType = subType,
            )
        }
    }
}