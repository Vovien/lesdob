package com.apkdv.mvvmfast.glide

import android.content.Context
import com.apkdv.mvvmfast.glide.http.OkHttpUrlLoader
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import java.io.InputStream

/**
 * @author : BaoZhou
 * @date : 2019/3/17 2:52
 */
@GlideModule
class MVVMGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(
                context,
                "Glide",
                IMAGE_DISK_CACHE_MAX_SIZE.toLong()
            )
        )
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize
        val customMemoryCacheSize = (0.8 * defaultMemoryCacheSize).toInt()
        val customBitmapPoolSize = (0.8 * defaultBitmapPoolSize).toInt()
        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))
        builder.setDefaultRequestOptions(RequestOptions().apply {
            format(DecodeFormat.PREFER_RGB_565)
        })
        LogUtils.i("========================Glide")
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(ProgressManager.okHttpClient)
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    companion object {
        /**
         * 图片缓存文件最大值为100Mb
         */
        const val IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024
    }
}