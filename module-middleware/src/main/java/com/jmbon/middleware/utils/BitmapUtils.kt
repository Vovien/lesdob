package com.jmbon.middleware.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author : leimg
 * time   : 2021/4/19
 * desc   :
 * version: 1.0
 */
object BitmapUtils {
    /*
   * 旋转图片
   * @param angle
   * @param bitmap
   * @return Bitmap
   */
    fun rotaingImageView(angle: Int, bitmap: Bitmap): Bitmap {
        //旋转图片 动作
        var matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        // 创建新的图片
        var resizedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0,
            bitmap.width, bitmap.height, matrix, true
        )
        return resizedBitmap
    }


    /**
     * 获取图片旋转角度
     */
    fun getExifOrientation(filepath: String): Int {
        var degree = 0
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(filepath)
        } catch (ex: IOException) {
            Log.d("getExifOrientation", "cannot read exif" + ex)
        }
        if (exif != null) {
            var orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            if (orientation != -1) {
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 ->
                        degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 ->
                        degree = 180

                    ExifInterface.ORIENTATION_ROTATE_270 ->
                        degree = 270
                }
            }
        }
        return degree
    }


    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param bitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    fun savePhotoToSD(path: String, mbitmap: Bitmap?, context: Context?) {
        var outStream: FileOutputStream? = null
        try {
            outStream = FileOutputStream(path)
            // 把数据写入文件，100表示不压缩
            mbitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close()
                }
                mbitmap?.recycle()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 自定义view转图片
     *
     * @param view
     * @return
     */
    fun transViewToBitmap(view: View): Bitmap {
        val size  = getMaxSize(view)
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
        view.measure(measuredWidth, measuredHeight)
        val w = view.measuredWidth
        val h = view.measuredHeight
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT) //如果不设置canvas画布为白色，则生成透明
        view.layout(0, 0, w, h)
        view.draw(canvas)
        return bmp
    }

    /**
     * 保存图片
     */
    fun saveBitmap(context: Context, bitmap: Bitmap, filePath: String): Boolean {
        var outStream: FileOutputStream? = null
        try {
            outStream = FileOutputStream(filePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            MediaStore.Images.Media.insertImage(context.contentResolver, filePath, filePath.substring(filePath.lastIndexOf("/")), null)
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = Uri.fromFile(File(filePath))
                context.sendBroadcast(this)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return false
    }

    /**
     * 获取自适应view最大高度/宽度
     *
     * @param view
     * @return
     */
    fun getMaxSize(view: View): Int {
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST)
        view.measure(measuredWidth, measuredHeight)
        val w = view.measuredWidth
        val h = view.measuredHeight
        return if (w > h) w else h
    }

    /**
     * 创建一张指定大小的纯色图片，支持圆角
     *
     * @param resources    Resources对象，用于创建BitmapDrawable
     * @param width        图片的宽度
     * @param height       图片的高度
     * @param cornerRadius 图片的圆角，不需要则传0
     * @param filledColor  图片的填充色
     * @return 指定大小的纯色图片
     */
    fun createDrawableWithSize( resources: Resources, width: Int, height: Int,cornerRadius: Int, @ColorInt filledColor: Int): BitmapDrawable {
        var filledColor = filledColor
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        if (filledColor == 0) {
            filledColor = Color.TRANSPARENT
        }
        if (cornerRadius > 0) {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.style = Paint.Style.FILL
            paint.color = filledColor
            canvas.drawRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                paint
            )
        } else {
            canvas.drawColor(filledColor)
        }
        return BitmapDrawable(resources, output)
    }

}