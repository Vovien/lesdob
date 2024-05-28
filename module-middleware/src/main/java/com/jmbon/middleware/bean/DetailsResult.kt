package com.jmbon.middleware.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.jmbon.middleware.bean.AnswerDetailData
import com.jmbon.middleware.comment.bean.CommentList
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
class DetailsResult(
    var answersData: AnswerDetailData,
    var comment: CommentList?,
    var textReply: String = "",
    var viewHeight: Int = 0,
) : Parcelable {
    companion object {

        var TYPE_ANSWER = 1
        var TYPE_RECOMMEND = 2
        var TYPE_ARTICLE = 3
    }
}