package com.apkdv.mvvmfast.glide

import android.graphics.Bitmap
import androidx.annotation.NonNull
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest


class GlideRoundedCorners(
    val topLeftRadius: Float = 0f,
    val topRightRadius: Float = 0f,
    val bottomLeftRadius: Float = 0f,
    val bottomRightRadius: Float = 0f
) : BitmapTransformation() {

    private val ID = "com.apkdv.mvvmfast.glide.GlideRoundedCorners"
    private val ID_BYTES = ID.toByteArray(CHARSET)

    constructor(roundingRadius: Float = 0f) : this(
        roundingRadius,
        roundingRadius
    )

    constructor(topRadius: Float = 0f, bottomRadius: Float = 0f) : this(
        topRadius,
        topRadius,
        bottomRadius,
        bottomRadius
    )


    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap, outWidth:
        Int, outHeight: Int
    ): Bitmap {
        try {
            //centerCrop和roundedCorners冲突，所以先处理下centerCrop
            val bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
            return TransformationUtils.roundedCorners(
                pool, bitmap,
                topLeftRadius,
                topRightRadius,
                bottomRightRadius,
                bottomLeftRadius,
            )
        } catch (e: Exception) {
            return TransformationUtils.roundedCorners(
                pool, toTransform,
                topLeftRadius,
                topRightRadius,
                bottomRightRadius,
                bottomLeftRadius,
            )
        }

    }

    override fun hashCode(): Int {
        return Util.hashCode(
            ID.hashCode(),
            Util.hashCode(topLeftRadius + bottomLeftRadius + bottomRightRadius + topRightRadius)
        )
    }

    override fun updateDiskCacheKey(@NonNull messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        val radiusData: ByteArray =
            ByteBuffer.allocate(4)
                .putInt((topLeftRadius + bottomLeftRadius + bottomRightRadius + topRightRadius).toInt())
                .array()
        messageDigest.update(radiusData)
    }
}