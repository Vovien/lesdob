@file:Suppress("unused")

package com.jmbon.middleware.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.apkdv.mvvmfast.glide.GlideUtil
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.jmbon.middleware.R
import java.text.SimpleDateFormat
import java.util.Locale

@BindingAdapter(value = ["visible"])
fun View.bindVisibility(visible: Boolean) {
    isVisible = visible
}

@BindingAdapter(value = ["invisible"])
fun View.bindInVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter(value = ["loadUrlCircle", "placeholder"], requireAll = false)
fun ImageView.bindLoadCircle(url: String?, placeholder: Int = R.drawable.icon_topic_placeholder) {
    GlideUtil.getInstance().loadCircleImg(
        this,
        url.urlToWep,
        placeholder
    )
}

@BindingAdapter(value = ["loadUrlCircle"])
fun ImageView.bindLoadCircle(placeholder: Int) {
    GlideUtil.getInstance().loadCircleImg(
        this,
        null,
        placeholder
    )
}

@BindingAdapter(value = ["load"])
fun ImageView.bindLoadUrl(url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance().loadImgUrl(
        this,
        url.urlToWep, R.drawable.icon_topic_placeholder
    )
}

@BindingAdapter(value = ["load", "placeholder"], requireAll = false)
fun ImageView.bindLoadUrl(url: String?, placeholder: Int = R.drawable.icon_topic_placeholder) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance().loadImgUrl(
        this,
        url.urlToWep,
        placeholder
    )
}

@BindingAdapter(value = ["loadUrl", "urlRadius"], requireAll = false)
fun ImageView.bindLoadUrlRadius(url: String?, urlRadius: Int) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance()
        .loadUrlRadius(
            this,
            url.urlToWep,
            urlRadius.toFloat().dp(),
            R.drawable.icon_topic_placeholder
        )
}

@BindingAdapter("android:layout_width")
fun View.setLayoutWidth(width: Int) {
    val layoutParams = this.layoutParams
    layoutParams.width = width
    this.layoutParams = layoutParams
}

@BindingAdapter("android:layout_height")
fun View.setLayoutHeight(height: Int) {
    val layoutParams = this.layoutParams
    layoutParams.height = height
    this.layoutParams = layoutParams
}

@BindingAdapter(
    value = ["loadUrl", "urlTopLeftRadius", "urlTopRightRadius", "urlBottomLeftRadius", "urlBottomRightRadius"],
    requireAll = false
)
fun ShapeableImageView.bindLoadUrlRadius(
    url: String?,
    urlTopLeftRadius: Int = 0,
    urlTopRightRadius: Int = 0,
    urlBottomLeftRadius: Int = 0,
    urlBottomRightRadius: Int = 0
) {
    this.shapeAppearanceModel = ShapeAppearanceModel.Builder().apply {
        setTopLeftCornerSize(urlTopLeftRadius.dp.toFloat())
        setTopRightCornerSize(urlTopRightRadius.dp.toFloat())
        setBottomLeftCornerSize(urlBottomLeftRadius.dp.toFloat())
        setBottomRightCornerSize(urlBottomRightRadius.dp.toFloat())
    }.build()
    GlideUtil.getInstance()
        .loadUrl(this, url, R.drawable.icon_circle_placeholder)
}

@BindingAdapter(
    value = ["loadUrl", "urlTopLeftRadius", "urlTopRightRadius", "urlBottomLeftRadius", "urlBottomRightRadius", "placeholder"],
    requireAll = false
)
fun ShapeableImageView.bindLoadUrlRadius(
    url: String?,
    urlTopLeftRadius: Int = 0,
    urlTopRightRadius: Int = 0,
    urlBottomLeftRadius: Int = 0,
    urlBottomRightRadius: Int = 0,
    placeholder: Int = R.drawable.icon_circle_placeholder
) {
    this.shapeAppearanceModel = ShapeAppearanceModel.Builder().apply {
        setTopLeftCornerSize(urlTopLeftRadius.dp.toFloat())
        setTopRightCornerSize(urlTopRightRadius.dp.toFloat())
        setBottomLeftCornerSize(urlBottomLeftRadius.dp.toFloat())
        setBottomRightCornerSize(urlBottomRightRadius.dp.toFloat())
    }.build()
    GlideUtil.getInstance()
        .loadUrl(this, url, placeholder)
}

@BindingAdapter(value = ["loadUrl", "urlRadius", "placeholder"], requireAll = false)
fun ImageView.bindLoadUrlRadius(
    url: String?,
    urlRadius: Int,
    placeholder: Int = R.drawable.icon_topic_placeholder
) {
    GlideUtil.getInstance()
        .loadUrlRadius(
            this,
            url.urlToWep,
            urlRadius.toFloat().dp(),
            placeholder
        )
}

@BindingAdapter(value = ["setDate", "pattern"], requireAll = false)
fun TextView.bindDate(date: Long, pattern: String?) {
    val format = pattern ?: "yyyy年MM月dd日 HH:mm"
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    text = simpleDateFormat.format(date * 1000L)

}

@BindingAdapter(value = ["bg"])
fun View.bindBackground(bg: Int) {
    setBackgroundResource(bg)
}

@BindingAdapter(value = ["localScr"])
fun ImageView.bindSrc(localScr: Int) {
    setImageResource(localScr)
}
