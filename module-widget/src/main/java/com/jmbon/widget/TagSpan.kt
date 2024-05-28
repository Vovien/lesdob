package com.jmbon.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.graphics.scale
import com.blankj.utilcode.util.ImageUtils.view2Bitmap
import com.blankj.utilcode.util.SizeUtils.dp2px
import com.blankj.utilcode.util.SizeUtils.px2dp

/**
 * @author : leimg
 * time   : 2021/5/10
 * desc   : 标签span
 * version: 1.0
 */
class TagSpan(tagStr: String, context: Context) : ReplacementSpan() {
    private val view: View =
        LayoutInflater.from(context).inflate(R.layout.item_wait_answer_tag_layout, null)

    //tag与文本的间距
    private val marginEnd: Int = dp2px(8f)
    private val mWidth: Int

    init {
        val tvName = view.findViewById<TextView>(R.id.tv_reward)
        tvName.text = tagStr
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(width, height)
        mWidth = view.measuredWidth // 获取宽度
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return mWidth + marginEnd
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int, bottom: Int,
        paint: Paint
    ) {
        paint.isAntiAlias = true
        val bmp: Bitmap? = view2Bitmap(view)
        bmp?.let {

            canvas.drawBitmap(
                bmp,
                x,
                y + paint.ascent() + paint.descent()/2 - (bmp.height - y) / 2,
                paint
            )
            //canvas.drawBitmap(bmp, x, y + paint.ascent(), paint) //和字顶部平齐
//            canvas.drawBitmap(
//                bmp,
//                x,
//                y + paint.ascent() + paint.descent() - (bmp.height - y) / 2,
//                paint
//            ) //图片文字居中对齐
            // Log.e("draw", "${paint.descent()}，${paint.ascent()},${y},${bottom},${bmp.height}")
            Log.e(
                "draw2",
                "${paint.ascent()},${paint.descent()},${bmp.height},${y}"
            )

        }
    }

}