package com.jmbon.widget.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apkdv.mvvmfast.utils.SizeUtil;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.jmbon.widget.R;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

/**
 * @author：luck
 * @date：2019-11-13 17:02
 * @describe：Glide加载引擎
 */
public class GlideEngine implements ImageEngine {

    private static GlideEngine instance;

    private GlideEngine() {
    }

    public static GlideEngine createGlideEngine() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }


    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (webviewError(url, imageView,null)) return;
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadImageRadius(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView, int radius) {
        Glide.with(context)
                .load(url)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius)))
                .into(imageView);
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @param callback      网络图片加载回调监听 {link after version 2.5.1 Please use the #OnImageCompleteCallback#}
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView, OnImageCompleteCallback callback) {
        if (webviewError(url, imageView, longImageView)) return;
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (callback != null) {
                            callback.onShowLoading();
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            boolean eqWidthImage = isWideImg(resource.getWidth(),
                                    resource.getHeight());

                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage || eqWidthImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.cachedBitmap(resource),
                                        new ImageViewState(getInitImageScale(resource.getWidth(), resource.getHeight()), new PointF(0, 0), 0));
                                longImageView.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.GONE);
                            } else {
                                imageView.setVisibility(View.VISIBLE);
                                longImageView.setVisibility(View.GONE);
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                        }
                    }
                });
    }

    private boolean webviewError(String url, @NonNull ImageView imageView, SubsamplingScaleImageView longImageView) {
        if (url != null && url.equals("web_view_error")) {
            imageView.setVisibility(View.VISIBLE);
            if (longImageView != null)
                longImageView.setVisibility(View.GONE);
            // 普通图片
            imageView.setImageResource(R.drawable.icon_web_view_error);
            return true;
        }
        return false;
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param context
     * @param url
     * @param imageView
     * @param longImageView
     * @ 已废弃
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView) {
        if (webviewError(url, imageView, longImageView)) return;
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(getInitImageScale(resource.getWidth(), resource.getHeight()), new PointF(0, 0), 0));
                                longImageView.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.GONE);
                            } else {
                                imageView.setVisibility(View.VISIBLE);
                                longImageView.setVisibility(View.GONE);
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                        }
                    }
                });
    }

    /**
     * 加载相册目录
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {


//设置图片圆角角度
//        RoundedCorners roundedCorners= new RoundedCorners(8);
//通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//        RequestOptions options=RequestOptions.bitmapTransform(roundedCorners).override(300, 300);

//        Glide.with(context).load(url)
//                .override(180, 180)
//                .centerCrop()
//                .sizeMultiplier(0.5f)
//                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
//                .apply(options).into(imageView);

        Glide.with(context)
                .load(url)
                .sizeMultiplier(0.5f)
                .apply(new RequestOptions().placeholder(R.drawable.icon_tube_placeholder))
                .transform(new CenterCrop(), new RoundedCorners(SizeUtils.dp2px(6))) // 数字根据自己需求来改
                .into(imageView);

//        loadUrlRadius(imageView,url,8);
//        Glide.with(context)
//                .asBitmap()
//                .load(url)
//                .override(180, 180)
//                .centerCrop()
//                .sizeMultiplier(0.5f)
//                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
//                .into(new BitmapImageViewTarget(imageView) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.
//                                        create(context.getResources(), resource);
//                        circularBitmapDrawable.setCornerRadius(ScreenUtils.dip2px(context, 8));
//                        imageView.setImageDrawable(circularBitmapDrawable);
//                    }
//                });
    }

    /**
     * 加载gif
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadAsGifImage(@NonNull Context context, @NonNull String url,
                               @NonNull ImageView imageView) {
        if (webviewError(url, imageView, null)) return;
        Glide.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .apply(new RequestOptions().placeholder(R.drawable.icon_tube_placeholder))
                .into(imageView);
    }


    public boolean isWideImg(int width, int height) {
        if (width <= 0 || height <= 0)
            return false;
        return width > (height * 3);
    }

    /**
     * 计算出图片初次显示需要放大倍数
     */
    public float getInitImageScale(int imageWidth, int imageHeight) {
        int width = SizeUtil.INSTANCE.getWidth();
        int height = SizeUtil.INSTANCE.getHeight();
        // 拿到图片的宽和高
        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (imageWidth > width && imageHeight <= height) {
            scale = width * 1.0f / imageWidth;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (imageWidth <= width && imageHeight > height) {
            scale = width * 1.0f / imageWidth;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (imageWidth < width && imageHeight < height) {
            scale = width * 1.0f / imageWidth;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (imageWidth > width && imageHeight > height) {
            scale = width * 1.0f / imageWidth;
        }
        return scale;
    }
}
