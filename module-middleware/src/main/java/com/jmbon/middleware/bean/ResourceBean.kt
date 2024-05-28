package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * @author : leimg
 * time   : 2021/4/23
 * desc   : 资源类型bean
 * version: 1.0
 */
@Parcelize
@Keep
data class ResourceBean(var type: String = "", var src: String = "") : Parcelable, Serializable