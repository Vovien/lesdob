package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class QuestionMultiData(
    @SerializedName("article")
    var article: ArticleList = ArticleList(),
    @SerializedName("question")
    var question: Question = Question(),
    @SerializedName("watting_answer_question")
    var waitAnswerQuestion: Question = Question(),
    @SerializedName("video")
    var video: QuestionVideoBean = QuestionVideoBean(),
    @SerializedName("videos")
    var videos: ArrayList<VideoDetail.VideoData> = arrayListOf(),
    @SerializedName("selected_video")
    var selectedVideo: ArrayList<VideoDetail.VideoData> = arrayListOf(),
    @SerializedName("hot")
    var hot: ArrayList<Question> = arrayListOf(),

    @SerializedName("adv")
    var adv: AdvBean = AdvBean(),
    @SerializedName("hots")
    var hots: ArrayList<Question> = arrayListOf(),//实时热点
    @SerializedName("type")
    var type: String = "",
    var currPos: Int = 0,
) : Parcelable {
    companion object {
        //对应内容类型【article:文章，question:问题，adv:广告，video:视频，watting_answer_question:待回答问题，selected_video：精选视频，hot：实时热点】
        var TYPE_QA = 1 //普通问答
        var TYPE_QA_HOT = 2 //热门问题回答
        var TYPE_QA_WAIT = 3//待回答
        var TYPE_ARTICLE = 4 //文章
        var TYPE_VIDEO = 5 //普通4:3视频
        var TYPE_VIDEO_FULL = 6 //普通16:9视频
        var TYPE_VIDEO_SELECTED = 7 //精选视频
        var TYPE_HOT = 8 //实时热点

        var TYPE_ADV = 9 //普通问答格式广告
        var TYPE_ADV_VIDEO = 10 //视频格式广告

    }
}
