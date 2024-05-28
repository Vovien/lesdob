package com.jmbon.middleware.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserData(
    @SerializedName("token") var token: String = "",
    @SerializedName("user_info", alternate = ["user"]) var user: User = User(),
    @SerializedName("device_id")
    var deviceId: String = "",

) : Parcelable {
    @Keep
    @Parcelize
    data class User(
        @SerializedName("app_name")
        var appName:String = "",
        @SerializedName("allow_update_nickname_count")
        var allowUpdateNicknameCount: Int = 0,
        @SerializedName("country_id")
        var countryId: Int = 0,
        @SerializedName("province_id")
        var provinceId: Int = 0,
        @SerializedName("total_update_nickname_count")
        var totalUpdateNicknameCount: Int = 0,

        @SerializedName("add_time")
        var addTime: Int = 0,
        @SerializedName("article_edit_type")
        var articleEditType: Int = 0,
        @SerializedName("avatar")
        var avatarFile: String = "",
        @SerializedName("background")
        var background: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("device_type")
        var deviceType: Int = 0,
        @SerializedName("email")
        var email: String = "",
        @SerializedName("isFirstLogin")
        var isFirstLogin: Int = 1, //1第一次登录 0非第一次
        @SerializedName("isPushHot")
        var isPushHot: Int = 0,
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("openid")
        var openid: String = "",  //是否绑定微信
        @SerializedName("wechatName")
        var wechatName: String = "",
        @SerializedName("pregnantStatus")
        var pregnantStatus: Int = 0,
        @SerializedName("sex")
        var sex: Int = 0, //2女 1男
        @SerializedName("age")
        var age: Int = 0,
        @SerializedName("sex_show")
        var sexShow: Int = 0,
        @SerializedName("uid")
        var uid: String = "",
        var realUid: Int = 0,
        /**
         * 由于新的接口将UID类型改成了加密的字符串, 改动较多, 为了兼容处理添加临时字段
         */
        val fakeUserId: Int = 0,
        @SerializedName("id")
        var id: String = "",
        @SerializedName("nickname")
        var userName: String = "",
        @SerializedName("job_name")
        var jobName: String = "",
        @SerializedName("doctor_name")
        var doctorName: String = "",

        @SerializedName("job_type")
        var jobType: Int = 1,//1：普通用户，2：优秀回答者，3：优秀创作者,5优秀视频创作者

        @SerializedName("is_circle_admin")
        var isCircleAdmin: Int = 0,
        @SerializedName("focus_count")
        var focusCount: Int = 0,
        @SerializedName("fans_count")
        val fansCount: Int = 0,
        @SerializedName("is_block_circle")
        var isBlockCircle: Int = 0,
        @SerializedName("category_title")
        var categoryTitle: String = "",
        @SerializedName("city_id")
        var cityId: Int = 0,
        @SerializedName("city_name")
        var cityName: String = "",
        @SerializedName("collect_count")
        var collectCount: Int = 0,
        @SerializedName("given_count")
        var givenCount: Int = 0,
        @SerializedName("has_article")
        var hasArticle: Boolean = false,
        @SerializedName("is_focus")
        var isFocus: Boolean = false,
        @SerializedName("is_mutual_focus")
        var isMutualFocus: Boolean = false,
        @SerializedName("is_switch_tube")
        var isSwitchTube: Int = 0,
        @SerializedName("group_id")
        var groupId: Int = 0,// group_id ： 0 普通人员  ,  1 : 运营人员
        @SerializedName("verify_type")
        var verifyType: Int = 0,
        /**
         * 引导状态: 0=未引导, 1=已引导
         */
        var guideStatus: Int = 0,
    ) : Parcelable
}



