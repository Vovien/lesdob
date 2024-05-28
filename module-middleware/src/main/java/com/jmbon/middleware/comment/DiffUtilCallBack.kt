package com.jmbon.middleware.comment

import androidx.recyclerview.widget.DiffUtil
import com.jmbon.middleware.comment.bean.CommentList


object DiffUtilCallBack {
    fun replyCallBack(): DiffUtil.ItemCallback<CommentList.Comment> {
        return object : DiffUtil.ItemCallback<CommentList.Comment>() {
            override fun areItemsTheSame(
                oldItem: CommentList.Comment,
                newItem: CommentList.Comment
            ): Boolean {
                return oldItem.answerId == newItem.answerId
            }

            override fun areContentsTheSame(
                oldItem: CommentList.Comment,
                newItem: CommentList.Comment
            ): Boolean {
                return oldItem.answerContent == newItem.answerContent
                        && oldItem.secondAnswerPage == newItem.secondAnswerPage
                        && oldItem.secondAnswers.size == newItem.secondAnswers.size
                        && oldItem.isGiven == newItem.isGiven
                        && oldItem.giveCount == newItem.giveCount
                        && oldItem.uid == newItem.uid
            }
        }

    }
}