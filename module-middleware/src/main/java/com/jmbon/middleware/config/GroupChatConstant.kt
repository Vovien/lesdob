package com.jmbon.middleware.config

/**
 * @author : leimg
 * time   : 2021/9/7
 * desc   :
 * version: 1.0
 */
object GroupChatConstant {

    //消息发送的类型
    //消息类型：text:文本消息，image:图片消息，article:文章，circle:圈子，question:问题，help:求助，external_question:外部问题,notice:群公告

    const val TYPE_NONE = "none"
    const val TYPE_TEXT = "text"
    const val TYPE_IMAGE = "image"
    const val TYPE_ARTICLE = "article"
    const val TYPE_CIRCLE = "circle"
    const val TYPE_QUESTION = "question"
    const val TYPE_HELP = "help"
    const val TYPE_REPLY_HELP = "reply_help"
    const val TYPE_EX_QA = "external_question"
    const val TYPE_NOTICE = "notice"
    const val TYPE_CREATE = "create" //圈子创建
    const val TYPE_DELETE_MEMBER = "delete_member" //圈子成员删除


    //消息展示类型
    /**
     * 发送文本消息
     */
    const val TYPE_SEND_TEXT = 1

    /**
     * 接收文本消息
     */
    const val TYPE_RECEIVE_TEXT = 31


    /**
     * 发送图片消息
     */
    const val TYPE_SEND_IMAGE = 3

    /**
     * 接收图片消息
     */
    const val TYPE_RECEIVE_IMAGE = 33


    /**
     * 发送引用文字消息
     */
    const val TYPE_REFERENCE_SEND_TEXT = 4

    /**
     * 接收引用文字消息
     */
    const val TYPE_REFERENCE_RECEIVE_TEXT = 34


    /**
     * 发送引用图片消息
     */
    const val TYPE_REFERENCE_SEND_IMAGE = 5

    /**
     * 接收引用图片消息
     */
    const val TYPE_REFERENCE_RECEIVE_IMAGE = 35


    /**
     * 发送回答问题消息
     */
    const val TYPE_SEND_ANSWER_TEXT = 6

    /**
     * 接收回答问题消息
     */
    const val TYPE_RECEIVE_ANSWER_TEXT = 36


    /**
     * 发送分享文章消息
     */
    const val TYPE_SEND_SHARE_ARTICLE = 7

    /**
     * 接收分享文章消息
     */
    const val TYPE_RECEIVE_SHARE_ARTICLE = 37


    /**
     * 发送分享求助消息
     */
    const val TYPE_SEND_SHARE_QUESTION = 8

    /**
     * 接收分享求助消息
     */
    const val TYPE_RECEIVE_SHARE_QUESTION = 38


    /**
     * 发送分享回答消息
     */
    const val TYPE_SEND_SHARE_ANSWER = 9

    /**
     * 接收分享回答消息
     */
    const val TYPE_RECEIVE_SHARE_ANSWER = 39


    /**
     * 发送引用分享回答消息
     */
    const val TYPE_REFERENCE_SEND_SHARE_ANSWER = 10

    /**
     * 接收引用分享回答消息
     */
    const val TYPE_REFERENCE_RECEIVE_SHARE_ANSWER = 40

    /**
     * 发送引用分享求助消息
     */
    const val TYPE_REFERENCE_SEND_SHARE_QUESTION = 11

    /**
     * 接收引用分享求助消息
     */
    const val TYPE_REFERENCE_RECEIVE_SHARE_QUESTION = 41

    /**
     * 发送引用分享文章消息
     */
    const val TYPE_REFERENCE_SEND_SHARE_ARTICLE = 12

    /**
     * 接收引用分享文章消息
     */
    const val TYPE_REFERENCE_RECEIVE_SHARE_ARTICLE = 42


    /**
     * 发送公告消息
     */
    const val TYPE_SEND_GROUP_NOTIFY = 13

    /**
     * 接收公告消息
     */
    const val TYPE_RECEIVE_GROUP_NOTIFY = 43

    /**
     * 发送分享圈子消息
     */
    const val TYPE_SEND_SHARE_CIRCLE = 14

    /**
     * 接收分享圈子消息
     */
    const val TYPE_RECEIVE_SHARE_CIRCLE = 44


    /**
     * 发送引用分享圈子消息
     */
    const val TYPE_REFERENCE_SEND_SHARE_CIRCLE = 15

    /**
     * 接收引用分享圈子消息
     */
    const val TYPE_REFERENCE_RECEIVE_SHARE_CIRCLE = 45


    /**
     * 撤回消息提示
     */
    const val TYPE_WITHDRAW_MESSAGE = 16

    /**
     * 删除消息提示
     */
    const val TYPE_DELETE_MESSAGE = 17

    /**
     * 发送回答问题图片消息
     */
    const val TYPE_SEND_ANSWER_IMAGE = 18

    /**
     * 接收回答问题图片消息
     */
    const val TYPE_RECEIVE_ANSWER_IMAGE = 48

    /**
     * 新消息提示
     */
    const val TYPE_TIPS_NEW_MESSAGE = 19

    //【1：文本消息，2：图片消息，3：圈子，4：文章，5：群问题，6：群求助，7：外部问题】

    fun getTypeId(type: String): Int {
        when (type) {
            TYPE_TEXT -> return 1
            TYPE_IMAGE -> return 2
            TYPE_CIRCLE -> return 3
            TYPE_ARTICLE -> return 4
            TYPE_HELP -> return 5
            TYPE_EX_QA -> return 6
        }

        return 0
    }
}