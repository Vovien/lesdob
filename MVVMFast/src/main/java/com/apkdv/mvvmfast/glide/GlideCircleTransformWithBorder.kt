package com.apkdv.mvvmfast.glide

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @Author MilkCoder
 * @Date 2023/5/12
 * @Version 6.1.0
 * @Copyright All copyrights reserved to ManTang.
 */

class GlideCircleTransformWithBorder() : BitmapTransformation() {
    private var mBorderPaint: Paint? = null
    private var mBorderWidth = 0f

    constructor(borderWidth: Int, borderColor: Int) : this() {
        mBorderWidth = Resources.getSystem().displayMetrics.density * borderWidth
        mBorderPaint = Paint()
        mBorderPaint!!.isDither = true
        mBorderPaint!!.isAntiAlias = true
        mBorderPaint!!.color = borderColor
        mBorderPaint!!.style = Paint.Style.STROKE
        mBorderPaint!!.strokeWidth = mBorderWidth
    }


    private fun circleCrop(pool: BitmapPool, source: Bitmap): Bitmap {
        val size = (Math.min(source.width, source.height) - mBorderWidth / 2).toInt()
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        // TODO this could be acquired from the pool too
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool[size, size, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader =
            BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        if (mBorderPaint != null) {
            val borderRadius = r - mBorderWidth / 2
            canvas.drawCircle(r, r, borderRadius, mBorderPaint!!)
        }
        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return circleCrop(pool, toTransform)
    }
}