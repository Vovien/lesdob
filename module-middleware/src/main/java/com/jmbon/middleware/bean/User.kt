package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Keep
data class User(
    @SerializedName("avatar_file")
    var avatarFile: String = "",
    @SerializedName("doctor_id")
    var doctorId: Int = 0,
    @SerializedName("uid")
    var uid: Int = 0,
    @SerializedName("description")
    var description: String = "",
    @SerializedName("time")
    var time: Long = 0L,

    @SerializedName("user_name")
    var userName: String = "",
    @SerializedName("doctor_name")
    var doctorName: String = "",

    @SerializedName("verify_type")
    var verifyType: Int = 0,

    @SerializedName("category_title")
    var categoryTitle: String = "",

    @SerializedName("job_type")
    var jobType: Int = 1,//1：普通用户，2：优秀回答者，3：优秀创作者

    @SerializedName("is_auth")
    var isAuth: Boolean = false,
    @SerializedName("is_focus")
    var isFocus: Boolean = false,
    @SerializedName("is_mutual_focus")
    var isMutualFocus: Boolean = false,
    @SerializedName("is_focus_me")
    var isFocusMe: Boolean = false,
    @SerializedName("job_name")
    var jobName: String = "",
    @SerializedName("notify_message")
    var notifyMessage: Int = 0,
    @SerializedName("job_id")
    var jobId: Int = 0,
    @SerializedName("mechanism")
    var mechanism: String? = "",
    @SerializedName("open_reward")
    var openReward: Int = 0,

    @SerializedName("focus_time")
    var focusTime: Int = 0,
    @SerializedName("is_cancel")
    var isCancel: Int = 0,// is_cancel:  0：正常，1：已注销

    @SerializedName("article_count")
    var articleCount: Int = 0,
    @SerializedName("fans_count")
    var fansCount: Int = 0,
    @SerializedName("name_highlight", alternate = ["user_name_highlight"])
    var highlight: ArrayList<String> = arrayListOf(),
    @SerializedName("is_read")
    var isRead: Boolean = false,
    @SerializedName("pregnant_status")
    var pregnantStatus: Int = 0,
    @SerializedName("group_id")
    var groupId: Int = 0,// group_id ： 0 普通人员  ,  1 : 运营人员
    @SerializedName("answer_count") var answerCount: Int = 0,
    @SerializedName("article_edit_type") var articleEditType: Int = 0,
    @SerializedName("focus_count") var focusCount: Int = 0,
    @SerializedName("has_article") var hasArticle: Boolean = false,
    @SerializedName("is_block_circle") var isBlockCircle: Int = 0,
    @SerializedName("question_count") var questionCount: Int = 0,

    ) : Serializable, Parcelable



