package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class ViolateQuestionData(
    @SerializedName("page_count")
    var pageCount: Int = 0,
    @SerializedName("question")
    var question: Question = Question(),
    @SerializedName("answer")
    var answer: AnswerBean = AnswerBean(),
    @SerializedName("examine")
    var examine: Question = Question(),
    @SerializedName("origin_data")
    var originData: OriginData = OriginData(),
    @SerializedName("block_data")
    var blockData: BlockData = BlockData(),
    @SerializedName("block_images")
    var blockImages: ArrayList<String> = arrayListOf(),
    @SerializedName("check_log_id")
    var check_log_id: Int = 0,
    @SerializedName("origin_answer_content")
    var originAnswerContent: String = "",
    @SerializedName("origin_answer_content_svg")
    var originAnswerContentSvg: String = "",
    @SerializedName("is_edit")
    var is_edit: Int = 0,

    @SerializedName("total")
    var total: Int = 0
) : Parcelable

@Parcelize
data class OriginData(
    @SerializedName("text")
    var text: String = "",
    @SerializedName("image")
    var image: ArrayList<String>? = arrayListOf(),
    @SerializedName("video")
    var video: ArrayList<String>? = arrayListOf(),
) : Parcelable
