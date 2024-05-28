package com.jmbon.middleware.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.setTextStyle
import com.apkdv.mvvmfast.utils.TextHighLight
import com.apkdv.mvvmfast.utils.divider.RecyclerViewDivider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.flexbox.FlexboxLayout
import com.jmbon.middleware.R
import com.jmbon.middleware.arouter.LoginValid
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.extention.getDivider
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.valid.action.Action
import com.jmbon.middleware.valid.action.SingleCall
import com.leon.channel.helper.ChannelReaderUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.greenrobot.eventbus.EventBus
import org.jetbrains.annotations.Nullable
import java.util.regex.Pattern

fun TextView.setTextWhenNotEmpty(value: String?) {
    if (!value.isNullOrBlank()) {
        text = value
    }
}
fun parseColor(colorStr: String?, defaultColor: Int): Int {
    try {
        return Color.parseColor(colorStr)
    } catch (e: Exception) {

    }

    return defaultColor
}

fun FlexboxLayout.initTagLayout(num: Int) {
    removeAllViews()

    isVisible = true
    setDividerDrawable(getDivider(Color.TRANSPARENT, 2.dp, 2.dp))
    val tagList = num.toString().toCharArray().map {
        it.toString().toInt()
    }.toIntArray().toMutableList()
    val newTagList = mutableListOf<String>()
    when (tagList.size) {
        4 -> {
            newTagList.add("-")
            newTagList.add("-")
            tagList.forEach {
                newTagList.add(it.toString())
            }
        }
        5->{
            newTagList.add("-")
            tagList.forEach {
                newTagList.add(it.toString())
            }
        }
        else->{
            tagList.forEach {
                newTagList.add(it.toString())
            }
        }
    }
    newTagList.forEach {
        TextView(context).apply {
            setPadding(9.dp, 6.dp, 8.dp, 6.dp)
            text = it
            textSize = 14f
            maxLines = 1
            setTextColor(Color.parseColor("#FF5384FF"))
            setTextStyle(Typeface.BOLD)
            setBackground(backgroundColor = Color.parseColor("#145384FF"))
            addView(this)
        }
    }
}

val Number.dp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

val Number.dpf
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density)

/**
 * 解决ARouter页面跳转后finish出现白页的问题
 */
fun Postcard.navigationWithFinish(activity: Activity?) {
    navigation(activity, object : NavCallback() {
        override fun onArrival(postcard: Postcard?) {
            activity?.finish()
        }
    })
}

fun RecyclerView.realScrollToPosition(position: Int) {
    if (layoutManager !is LinearLayoutManager) {
        return
    }

    val layoutManagerTemp = layoutManager as LinearLayoutManager
    val firstPos = layoutManagerTemp.findFirstVisibleItemPosition()
    val lastPos: Int = layoutManagerTemp.findLastVisibleItemPosition()

    if (position <= firstPos) {
        //当要置顶的项在当前显示的第一个项的前面时
        scrollToPosition(position)

    } else if (position <= lastPos) {
        //当要置顶的项已经在屏幕上显示时,通过LayoutManager
        val childAt: View? = layoutManagerTemp.findViewByPosition(position)
        var top = childAt?.top ?: 0
        scrollBy(0, top)

    } else {
        //当要置顶的项在当前显示的最后一项之后
        layoutManagerTemp.scrollToPositionWithOffset(position, 0)
    }
}


fun RecyclerView.init(
    adapter: BaseQuickAdapter<*, *>?,
    @Nullable decor: RecyclerView.ItemDecoration?,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
) {
    this.layoutManager = layoutManager
    this.isMotionEventSplittingEnabled = false
    this.adapter = adapter
    adapter?.let {
        this.adapter = it
    }
    decor?.let {
        this.addItemDecoration(it)
    }
}
//fun RecyclerView.init(
//    adapter: BindingQuickAdapter<*, *>?,
//    @Nullable decor: RecyclerView.ItemDecoration?,
//    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
//) {
//    this.layoutManager = layoutManager
//    this.adapter = adapter
//    adapter?.let {
//        this.adapter = it
//    }
//    decor?.let {
//        this.addItemDecoration(it)
//    }
//}


fun RecyclerView.init(
    adapter: BaseQuickAdapter<*, *>?,
    layoutManager: RecyclerView.LayoutManager = getLayoutManager() ?: LinearLayoutManager(this.context),
    dividerHeight: Float = 0f, dividerColor: Int = Color.TRANSPARENT,
    left: Float = 0f, right: Float = 0f, top: Float = 0f, bottom: Float = 0f,
    vertical: Boolean = true, showEnd: Boolean = false
) {
    if (layoutManager is LinearLayoutManager)
        layoutManager.recycleChildrenOnDetach = true

    this.layoutManager = layoutManager
    //禁止多指点击
    this.isMotionEventSplittingEnabled = false
    adapter?.let {
        this.adapter = it
    }
    if (dividerHeight > 0f) {
        val divider = RecyclerViewDivider.builder(this.context)
            .leftMargin(SizeUtils.dp2px(left))
            .rightMargin(SizeUtils.dp2px(right))
            .topMargin(SizeUtils.dp2px(top))
            .bottomMargin(SizeUtils.dp2px(bottom))
            .color(dividerColor, SizeUtils.dp2px(dividerHeight))
        if (!showEnd)
            divider.hideLastDecoration()
        if (vertical)
            divider.horizontal()
        this.addItemDecoration(
            divider.build()
        )

    }

}

/**
 * 关闭默认刷新动画
 */
fun RecyclerView.closeDefaultAnimator() {
    this.itemAnimator?.let {
        (it as SimpleItemAnimator).supportsChangeAnimations = false
        it.addDuration = 0
        it.changeDuration = 0
        it.moveDuration = 0
        it.removeDuration = 0
    }
    this.itemAnimator = null
}


fun RefreshLayout.finish() {
    this.finishRefresh()
    this.finishLoadMore()
}

fun RefreshLayout.autoRefreshLayout() {
    this.autoRefresh(0, 300, 1f, false)
}

fun Float.dp(): Int {
    return SizeUtils.dp2px(this)
}

fun CharSequence?.isNotNullEmpty(): Boolean {
    return !this.isNullOrEmpty() && this != "null"
}

fun CharSequence?.isNotNullBlank(): Boolean {
    return !this.isNullOrBlank() && this != "null"
}

fun <T> Collection<T>?.isNotNullEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

fun CharSequence?.ifNotNullEmpty(defaultValue: (String) -> Unit) {
    this?.let {
        defaultValue(it.toString())
    }
}

//数量单位转换为万,保留一位小数
fun Int.coverToTenThousand(): String {
    if (this >= 10000) {
        return "${String.format("%.1f", this / 10000.0f)}w"
    }
    return this.toString()
}

//数量单位转换为万,保留一位小数
fun Int.coverToTenThousandPlus(): String {
    if (this >= 10000) {
        return "${String.format("%.1f", this / 10000.0f)}w+"
    }
    return this.toString()
}


/**
 * 加载图片 圆形 URL 可以是本地。或者网络地址
 */
fun ImageView.loadCircle(redId: Int, placeHolderId: Int = R.drawable.icon_tube_placeholder) {
    GlideUtil.getInstance().loadCircleImg(
        this,
        redId,
        placeHolderId
    )
}

/**
 * 加载图片 圆形 URL 可以是本地。或者网络地址
 */
fun ImageView.loadCircle(url: String?, placeHolderId: Int = R.drawable.icon_default_login_avatar) {
    GlideUtil.getInstance().loadCircleImg(
        this,
        url.urlToWep,
        placeHolderId
    )
}

/**
 * 加载图片圆角 URL 可以是本地。或者网络地址
 */
fun ImageView.loadRadius(url: String?, radius: Int) {
    GlideUtil.getInstance().loadUrlRadius(
        this,
        url.urlToWep, radius,
        R.drawable.icon_tube_placeholder
    )
}

/**
 * 加载图片圆角 URL 可以是本地。或者网络地址
 */
fun ImageView.loadRadius(url: String, radius: Int, placeholder: Int) {
    GlideUtil.getInstance().loadUrlRadius(
        this,
        url.urlToWep, radius,
        placeholder
    )
}

/**
 * 加载圆角图片
 */
fun ImageView.loadUrlRadius(url: String?, radius: Int) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance()
        .loadUrlRadius(
            this,
            url.urlToWep,
            radius,
            R.drawable.icon_tube_placeholder
        )
}

/**
 * 加载圆角图片
 */
fun ImageView.loadUrlRadius(url: String?, radius: Int, placeholder: Int) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance()
        .loadUrlRadius(
            this,
            url.urlToWep,
            radius,
            placeholder
        )
}

/**
 * 加载图片 URL 可以是本地。或者网络地址
 */
fun ImageView.loadUrlSourceCenterCrop(url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance().loadUrlSourceCenterCrop(
        this,
        url.urlToWep, R.drawable.icon_tube_placeholder
    )
}


/**
 * 加载图片 URL 可以是本地。或者网络地址
 */
fun ImageView.load(url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance().loadImgUrl(
        this,
        url.urlToWep, R.drawable.icon_tube_placeholder
    )
}

/**
 * 加载图片 URL 可以是本地。或者网络地址
 */
fun ImageView.load(url: String?, placeholder: Int = R.drawable.icon_tube_placeholder) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance().loadImgUrl(
        this,
        url.urlToWep, placeholder
    )
}

/**
 * 加载图片webp gif URL 可以是本地。或者网络地址
 */
fun ImageView.loadWebpGifImgUrl(
    url: String?,
    placeholder: Int = R.drawable.icon_tube_placeholder
) {
    if (url.isNullOrEmpty()) {
        return
    }
    GlideUtil.getInstance().loadWebpGifImgUrl(
        this,
        url, placeholder
    )
}

/**
 * 加载图片 URL 可以是本地。或者网络地址
 * 不需要加载动画
 */
fun ImageView.loadNoPlace(url: String?) {
    GlideUtil.getInstance().loadImgUrlNoPlace(
        this,
        url.urlToWep, R.drawable.icon_tube_placeholder
    )
}

/**
 * 加载图片 URL 可以是本地。或者网络地址
 */
fun String?.load(image: ImageView) {
    this?.let {
        image.load(it)
    }
}


fun String?.isHttpUrl(): Boolean {
    return if (this.isNullOrEmpty()) {
        false
    } else {
        var isurl = false
        val regex = ("(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)") //设置正则表达式
        val pat = Pattern.compile(regex.trim { it <= ' ' }) //比对
        val mat = pat.matcher(this.trim { it <= ' ' })
        isurl = mat.matches() //判断是否匹配
        if (isurl) {
            isurl = true
        }
        isurl
    }

}

/**
 * 防止双击
 */
inline fun View.setOnSingleClickListener(
    crossinline onClick: (View) -> Unit,
    delayMillis: Long = 300
) {
    ClickUtils.applyGlobalDebouncing(this, delayMillis) {
        onClick.invoke(this)
    }
}

const val FAST_CLICK_THRSHOLD = 300L

fun <T> Flow<T>.throttleFirst(thresholdMillis: Long = FAST_CLICK_THRSHOLD): Flow<T> = flow {
    var lastTime = 0L // 上次发射数据的时间
    // 收集数据

    collect {
        // 当前时间
        val currentTime = System.currentTimeMillis()
        // 时间差超过阈值则发送数据并记录时间
        if (currentTime - lastTime > thresholdMillis) {
            lastTime = currentTime
            emit(it)
        }
    }
}

fun Runnable.logInToIntercept() {
    Action {
        this.run()
    }.logInToIntercept()
}

fun Postcard.jumpToActivity() {
    Action {
        this.navigation()
    }.logInToIntercept()
}

fun Action.logInToIntercept() {
    SingleCall.getInstance()
        .addAction(this)
        .addValid(LoginValid())
        .doCall()

}

fun Activity.checkTaskAndRun() {
    this.finish()
    EventBus.getDefault().post(UserLoginEvent(true))
    SingleCall.getInstance().doCall()
    this.overridePendingTransition(0, 0)
}

fun String?.setKeyHighLight(key: ArrayList<String>?): SpannableStringBuilder {
    if (this.isNullOrEmpty())
        return SpannableStringBuilder()
    if (key.isNullOrEmpty())
        return SpannableStringBuilder(HtmlTools.delHTMLTag(this))
    return TextHighLight.setStringHighLight(HtmlTools.delHTMLTag(this), key.toTypedArray())
}

fun String?.setKeyHighLight(key: String?): SpannableStringBuilder {
    if (this.isNullOrEmpty())
        return SpannableStringBuilder()
    if (key.isNullOrEmpty())
        return SpannableStringBuilder(HtmlTools.delHTMLTag(this))
    return TextHighLight.setStringHighLight(HtmlTools.delHTMLTag(this), key)
}

/***
 * 跳转到用户中心
 */
fun Int.toUserCenter() {
    ARouter.getInstance().build("/mine/message/personal_page")
        .withInt(TagConstant.PARAMS, this).navigation()
}

/***
 * 如果已经注销就置灰
 *
 */
fun TextView.setUserNickColor(normalColor: Int, isCancel: Int = 0) {
    if (isCancel == 1) {
        this.setTextColor(resources.getColor(R.color.color_BFBFBF))
    } else {
        this.setTextColor(resources.getColor(normalColor))
    }

}


/***
 * 如果已经注销就置灰
 *
 */
fun TextView.setUserNickColorByHighLight(
    builder: SpannableStringBuilder, name: String,
    normalColor: Int,
    isCancel: Int = 0,
) {
    if (isCancel == 1) {
        this.setTextColor(resources.getColor(R.color.color_BFBFBF))
        this.text = name
    } else {
        this.setTextColor(resources.getColor(normalColor))
        this.text = builder
    }
}


/**如果为空, 则执行[action].
 * 原样返回*/
fun <T> T?.elseNull(action: () -> Unit = {}): T? {
    if (this == null) {
        action()
    }
    return this
}

/**
 * 转为 webp 格式 质量 95
 * wSize  指定图片的宽度缩放
 */
val String?.urlToWep: String
    get() {
        if (isNullOrEmpty())
            return ""
        if (!isHttpUrl())
            return this

        if (this.contains("random-background")) {
            return this
        }

        return if (this.contains("//image.jmbon.com/") || this.contains("//video.jmbon.com/")) {
            if (endsWith("jpg") || endsWith("png") || endsWith("jpeg"))
                "$this?x-oss-process=image/format,webp/quality,Q_95"
            else if (endsWith("mp4")) {
                "$this?x-oss-process=video/snapshot,t_500,m_fast,ar_auto"
            } else if (contains("mp4?aliyunVideoId")) {
                "${this.substringBefore("mp4?")}mp4?x-oss-process=video/snapshot,t_500,m_fast,ar_auto"
            } else this
        } else this
    }

/**
 * 转为 webp 格式 质量 95
 * wSize  指定图片的宽度缩放
 */
val String?.videoUrlToWep: String
    get() {
        if (isNullOrEmpty())
            return ""
        if (!isHttpUrl())
            return this

        return if (this.contains("//image.jmbon.com/") || this.contains("//video.jmbon.com/")) {
            if (endsWith("mp4")) {
                "${this}?x-oss-process=video/snapshot,t_500,m_fast,ar_auto"
            } else if (contains("mp4?aliyunVideoId")) {
                "${this.substringBefore("mp4?")}mp4?x-oss-process=video/snapshot,t_500,m_fast,ar_auto"
            } else this
        } else this
    }


val Int.Color: Int
    get() {
        return ColorUtils.getColor(this)
    }
val Int.String: String
    get() {
        return StringUtils.getString(this)
    }
val Long.TimeDiff: Long
    get() {
        return System.currentTimeMillis() - this
    }

fun String.isVideoUrl(): Boolean {
    return this.lowercase().contains(".mp4?aliyunvideoid=") || this.lowercase().endsWith(".mp4")
}

/**
 * 图片的默认大小
 * 采样规则: 内存大于4G, 使用请求的大小; 内存小于4G, 大于2G, 使用请求大小的0.85倍; 小于2G, 使用请求大小的0.7倍
 */
private val defaultSize by lazy {
    ActivityUtils.getTopActivity()?.applicationContext?.let {
        ContextCompat.getSystemService(it, ActivityManager::class.java)?.let { it1 ->
            val memoryInfo = ActivityManager.MemoryInfo()
            it1.getMemoryInfo(memoryInfo)
            if (memoryInfo.totalMem < 2 * 1024 * 1024 * 1024) {
                0.7f
            } else if (memoryInfo.totalMem < 4 * 1024 * 1024 * 1024) {
                0.85f
            } else {
                1.0f
            }
        } ?: run {
            1.0f
        }
    } ?: run {
        1.0f
    }
}

/**
 * 获取带优化参数的图片链接地址
 */
fun String?.getRealUrl(width: Int = 0, height: Int = 0, canPreview: Boolean = false): String {
    if (isNullOrBlank()) {
        return ""
    }

    if (!isHttpUrl() || this.contains("random-background")) {
        return this
    }

    return if (this.contains("//image.jmbon.com/") || this.contains("//video.jmbon.com/")) {
        if (endsWith("jpg") || endsWith("png") || endsWith("jpeg") || endsWith("webp"))
            "$this?x-oss-process=image/format,webp".run {
                if (!canPreview && width > 0 && height > 0) {
                    "${this}/resize,m_mfit,w_${(width * defaultSize).toInt()},h_${(height * defaultSize).toInt()}"
                } else {
                    this
                }
            }
        else if (endsWith("mp4")) {
            "$this?x-oss-process=video/snapshot,t_500,m_fast,ar_auto"
        } else if (contains("mp4?aliyunVideoId")) {
            "${this.substringBefore("mp4?")}mp4?x-oss-process=video/snapshot,t_500,m_fast,ar_auto"
        } else this
    } else this
}

/**
 * 数字转换为中文
 */
fun convertToChineseNumber(number: Int): String {
    val units = arrayOf("", "十", "百", "千", "万", "十万", "百万", "千万", "亿")
    val digits = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")

    var num = number
    var result = ""

    var i = 0
    while (num > 0) {
        val digit = num % 10
        if (digit != 0) {
            val unit = units[i]
            val chineseDigit = digits[digit]
            result = chineseDigit + unit + result
        }
        num /= 10
        i++
    }

    return result
}

fun getRealStatusHeight(): Int {
    var statusHeight = BarUtils.getStatusBarHeight()
    if (statusHeight < 24.dp) {
        statusHeight = 32.dp
    }
    return statusHeight
}

/**
 * 获取渠道名称
 */
fun getRealChannel(): String {
    val realChannel = ChannelReaderUtil.getChannel(Utils.getApp())
    return if (realChannel.isNotNullEmpty()) {
        realChannel
    } else {
        "debug"
    }
}