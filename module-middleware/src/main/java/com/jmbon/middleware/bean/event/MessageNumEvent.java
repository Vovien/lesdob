package com.jmbon.middleware.bean.event;

import androidx.annotation.Keep;

import com.jmbon.middleware.bean.MessagePointBean;

/**
 * 消息数量事件
 */
@Keep
public class MessageNumEvent {
    public int messageNum; //普通消息数
    public int chatMessageNum;//单聊消息数
    public MessagePointBean messagePointBean; //消息中心消息，通过透传实时更新

    public MessageNumEvent(int messageNum, int chatMessageNum) {
        this.messageNum = messageNum;
        this.chatMessageNum = chatMessageNum;
    }

    public MessageNumEvent(int messageNum, int chatMessageNum, MessagePointBean messagePointBean) {
        this.messageNum = messageNum;
        this.chatMessageNum = chatMessageNum;
        this.messagePointBean = messagePointBean;
    }

}
