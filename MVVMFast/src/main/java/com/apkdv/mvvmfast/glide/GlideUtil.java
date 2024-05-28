package com.apkdv.mvvmfast.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GlideUtil {

    private static GlideUtil imageLoadUtil;

    private GlideUtil() {
    }

    public static GlideUtil getInstance() {
        if (imageLoadUtil == null) {
            synchronized (GlideUtil.class) {
                if (imageLoadUtil == null)
                    imageLoadUtil = new GlideUtil();
                return imageLoadUtil;
            }

        }
        return imageLoadUtil;
    }


    public void loadCircleImg(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        GlideApp.with(imageView.getContext())
                .load(imgUrl)
                .thumbnail(loadCircleTransform(imageView.getContext(), placeHolder))
                .apply(new RequestOptions().circleCrop())
                .into(imageView);

    }

    public void loadCircleImg(ImageView imageView, int resId, @DrawableRes int placeHolder) {
        GlideApp.with(imageView.getContext())
                .load(resId)
                .thumbnail(loadCircleTransform(imageView.getContext(), placeHolder))
                .apply(new RequestOptions().circleCrop())
                .into(imageView);

    }


    public void loadUrlRadius(ImageView imageView, String imgUrl, int radius, @DrawableRes int Res) {
        GlideApp.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .placeholder(Res).error(Res)
                )
                .load(imgUrl)
                .thumbnail(loadRoundedTransform(imageView.getContext(), Res, radius))
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius)))
                .apply(new RequestOptions().timeout(5000).placeholder(Res).error(Res))
                .into(imageView);

    }

    public void loadUrlRadiusHD(ImageView imageView, String imgUrl, int radius, @DrawableRes int Res) {
        GlideApp.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .placeholder(Res).error(Res)
                )
                .load(imgUrl)
                .override(SizeUtils.dp2px(63 * 1.3f), SizeUtils.dp2px(63))
                .thumbnail(loadRoundedTransform(imageView.getContext(), Res, radius))
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius)))
                .apply(new RequestOptions().placeholder(Res).error(Res))
                .into(imageView);
    }

    /**
     * glide指定方向加载圆角图片
     *
     * @param imageView
     * @param imgUrl
     * @param leftTop_radius
     * @param leftBottom_radius
     * @param rightTop_radius
     * @param rightBottom_radius
     * @param Res
     */
    public void loadUrlCornerRadius(ImageView imageView, String imgUrl, float leftTop_radius,
                                    float leftBottom_radius, float rightTop_radius,
                                    float rightBottom_radius, @DrawableRes int Res) {
        GlideApp.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .skipMemoryCache(false)
                        .placeholder(Res).error(Res)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                )
                .load(imgUrl)
                .apply(new RequestOptions().centerCrop().placeholder(Res).error(Res))

                .transform(new GlideRoundedCorners(leftTop_radius, rightTop_radius, leftBottom_radius, rightBottom_radius))
                .dontAnimate()
                .into(imageView);
    }

    /**
     * glide指定方向加载圆角图片
     *
     * @param imageView
     * @param imgUrl
     * @param leftTop_radius
     * @param leftBottom_radius
     * @param rightTop_radius
     * @param rightBottom_radius
     * @param Res
     */
    public void loadUrlCornerRadius2(ImageView imageView, String imgUrl, float leftTop_radius,
                                     float leftBottom_radius, float rightTop_radius,
                                     float rightBottom_radius, @DrawableRes int Res) {
        GlideApp.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .skipMemoryCache(false)
                        .placeholder(Res).error(Res)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                )

                .load(imgUrl)
                .apply(new RequestOptions().centerCrop().placeholder(Res).error(Res))
                .transform(
                        new MultiTransformation(
                                new CenterCrop(),
                                new GlideRoundedCorners(leftTop_radius, rightTop_radius, leftBottom_radius, rightBottom_radius)
                        )
                )
                //  .transform(new GlideRoundedCorners(leftTop_radius, rightTop_radius, leftBottom_radius, rightBottom_radius))
               // .dontAnimate()
                .into(imageView);
    }

    /**
     * glide指定方向加载圆角图片
     *
     * @param imageView
     * @param imgSrc
     * @param leftTop_radius
     * @param leftBottom_radius
     * @param rightTop_radius
     * @param rightBottom_radius
     * @param Res
     */
    public void loadUrlCornerRadius(ImageView imageView, int imgSrc, float leftTop_radius,
                                    float leftBottom_radius, float rightTop_radius,
                                    float rightBottom_radius, @DrawableRes int Res) {
        GlideApp.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .skipMemoryCache(false)
                        .placeholder(Res).error(Res)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                )
                .load(imgSrc)
                .apply(new RequestOptions().centerCrop().placeholder(Res).error(Res))
                .transform(new GlideRoundedCorners(leftTop_radius, rightTop_radius, leftBottom_radius, rightBottom_radius))
                .into(imageView);

    }

    public void loadUrlSourceCenterCrop(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        GlideApp.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .placeholder(placeHolder).error(placeHolder)
                        .fitCenter()
                )
                .load(imgUrl)
                .centerCrop()
                .into(imageView);
    }

    public void loadUrlSourceCenterCrop(ImageView imageView, String imgUrl, int radius, @DrawableRes int placeHolder) {

        GlideApp.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .placeholder(placeHolder).error(placeHolder)
                )
                .load(imgUrl)
                .thumbnail(loadRoundedTransform(imageView.getContext(), placeHolder, radius))
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius)))
                .apply(new RequestOptions().placeholder(placeHolder).error(placeHolder))
                .into(imageView);
    }


    public void loadUrl(ImageView imageView, @DrawableRes int imgUrl) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .into(imageView);
    }

    public void loadUrl(ImageView imageView, String imgUrl, int placeHolder) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(placeHolder).error(placeHolder).dontAnimate())
                .into(imageView);
    }

    public void loadUrlCrossFade(ImageView imageView, String imgUrl) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public void loadUrlCrossFade(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions().placeholder(placeHolder))
                .into(imageView);
    }


    public void loadImgUrlWithoutDefault(ImageView imageView, String imgUrl) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .into(imageView);
    }

    public void loadImgUrlWithoutDefault(ImageView imageView, @DrawableRes int imgUrl) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .into(imageView);
    }

    public void loadGifUrl(ImageView imageView, @DrawableRes int gifRes) {
        GlideApp.with(Utils.getApp())
                //.asGif()
                //.format(DecodeFormat.PREFER_ARGB_8888)
                .load(gifRes)
                .placeholder(0)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(imageView);
    }

    public void loadGifUrl(ImageView imageView, File file) {
        GlideApp.with(Utils.getApp()).asGif().load(file).into(imageView);
    }

    public void loadGifUrl(ImageView imageView, File file, @DrawableRes int Res) {
        GlideApp.with(Utils.getApp())
                .load(file)
                .apply(new RequestOptions().placeholder(Res).error(Res))
                .into(imageView);
    }

    public void loadImgUrl(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(placeHolder).error(placeHolder).dontAnimate())
                .centerCrop()
                .into(imageView);

    }

    public void loadWebpGifImgUrl(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        //webp动图
        Transformation<Bitmap> transformation = new CenterCrop();
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(placeHolder).error(placeHolder).dontAnimate())
                .transform(WebpDrawable.class, new WebpDrawableTransformation(transformation))
                .into(imageView);

    }

    public void loadImgUrlNoPlace(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {


        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .apply(new RequestOptions().error(placeHolder).dontAnimate())
                .into(imageView);

    }

    public void loadImgUrlNoPlace(ImageView imageView, String imgUrl, float radius, @DrawableRes int placeHolder) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .apply(new RequestOptions().error(placeHolder).dontAnimate())
                .transform(new GlideRoundedCorners(radius, radius, radius, radius))
                .into(imageView);

    }

    public void loadUrlSimpleTarget(String imgUrl, final GlideUtilSimpleTarget target) {
        GlideApp.with(Utils.getApp()).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                target.onResourceReady(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                target.onLoadFailed();
            }
        });

    }


    public void loadUrlDiskCacheNONE(ImageView imageView, String url, @DrawableRes int placeHolder) {
        GlideApp.with(Utils.getApp())
                .load(url)
                .apply(new RequestOptions().error(placeHolder).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageView);
    }

    public interface GlideUtilSimpleTarget {
        void onResourceReady(Drawable resource);

        void onLoadFailed();
    }


    public Bitmap loadUrlgetBitmap(String imgUrl, int size) throws InterruptedException, ExecutionException, TimeoutException {

        return GlideApp.with(Utils.getApp())
                .asBitmap()
                .load(imgUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .override(size, size))
                .submit()
                .get(200, TimeUnit.MILLISECONDS);// 最大等待200ms
    }

    public void loadUrl(ImageView image, String imgUrl, @DrawableRes int defaultResId, int width, int height) {
        GlideApp.with(Utils.getApp()).asBitmap()
                .load(imgUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(defaultResId)
                        .error(defaultResId)
                        .centerCrop()
                        .override(width, height))
                .into(image);
    }


    public void loadUrltoCache(String imgUrl, int size) {
        GlideApp.with(Utils.getApp())
                .load(imgUrl)
                .submit(size, size);
    }

    public RequestBuilder<Drawable> loadRoundedTransform(Context context, @DrawableRes int placeholderId, int radius) {
        return GlideApp.with(context)
                .load(placeholderId)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius)))
                .apply(new RequestOptions()
                        .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius))));

    }

    public RequestBuilder<Drawable> loadCircleTransform(Context context, @DrawableRes int placeholderId) {
        return GlideApp.with(context)
                .load(placeholderId)
                .transform(new MultiTransformation<>(new CenterCrop(), new CircleCrop()));

    }

    public RequestBuilder<Bitmap> loadRoundedTransformBitmap(Context context, @DrawableRes int placeholderId, int radius) {
        return GlideApp.with(context)
                .asBitmap()
                .load(placeholderId)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius)))
                .apply(new RequestOptions()
                        .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius))));
    }
}
