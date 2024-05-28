package com.jmbon.middleware.bean


import android.os.Parcelable
import android.text.TextUtils
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*
import java.util.regex.Pattern

@Keep
@Parcelize
data class CityList(
    @SerializedName("innerCities") var chinaCityList: ArrayList<ChinaCity> = arrayListOf(),
    @SerializedName("abroadCities") var overseasCountryList: ArrayList<ChinaCity> = arrayListOf(),
    @SerializedName("hotCities") var hotCityList: ArrayList<ChinaCity> = arrayListOf()
) : Parcelable {
    @Keep
    @Parcelize
    data class ChinaCity(
        @SerializedName("subCities") var children: ArrayList<ChinaCity> = arrayListOf(),
        @SerializedName("id") var id: Int = 0,
        @SerializedName("name") var name: String = "",
        @SerializedName("pid") var pid: Int = 0,
        @SerializedName("title") var title: String = "",
        var dataType: Int = 0
    ) : Parcelable {
        /***
         * 获取悬浮栏文本，（#、定位、热门 需要特殊处理）
         * @return
         */
        fun getSection(): String {
            return if (TextUtils.isEmpty(name)) {
                "#"
            } else {
                val c: String = name.substring(0, 1)
                val p = Pattern.compile("[a-zA-Z]")
                val m = p.matcher(c)
                if (m.matches()) {
                    c.uppercase(Locale.getDefault())
                } else if (TextUtils.equals(c, "定") || TextUtils.equals(c, "热")) name else "#"
            }
        }
    }
}


const val CITY = 0
const val HOT = 1
const val LOCATION = 2
