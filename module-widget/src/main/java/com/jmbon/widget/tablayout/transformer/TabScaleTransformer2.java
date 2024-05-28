package com.jmbon.widget.tablayout.transformer;


import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jmbon.widget.R;
import com.jmbon.widget.tablayout.SlidingScaleTabLayout2;
import com.jmbon.widget.tablayout.utils.ViewUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by li.zhipeng on 2019/1/3.
 * <p>
 * tab切换的
 */
public class TabScaleTransformer2 implements ViewPager.PageTransformer {

    private final SlidingScaleTabLayout2 slidingScaleTabLayout;

    private final PagerAdapter pagerAdapter;

    private final float textSelectSize;

    private final float textUnSelectSize;

    private final float minScale;

    private final boolean openDmg;

    private List<IViewPagerTransformer> transformers = null;


    public TabScaleTransformer2(SlidingScaleTabLayout2 slidingScaleTabLayout,
                                PagerAdapter pagerAdapter,
                                float textSelectSize, float textUnSelectSize, boolean openDmg) {
        this.slidingScaleTabLayout = slidingScaleTabLayout;
        this.pagerAdapter = pagerAdapter;
        this.textSelectSize = textSelectSize;
        this.textUnSelectSize = textUnSelectSize;
        this.minScale = Math.min(textUnSelectSize, textSelectSize) / Math.max(textSelectSize, textUnSelectSize);
        this.openDmg = openDmg;
    }

    @Override
    public void transformPage(@NonNull View view, final float position) {
        transformPage2(slidingScaleTabLayout, view, position);
    }

    /**
     * 页面变化压缩
     *
     * @param slidingScaleTabLayout
     * @param view
     * @param position
     */
    public void transformPage2(SlidingScaleTabLayout2 slidingScaleTabLayout, @NotNull View view, float position) {
        final TextView currentTab = slidingScaleTabLayout.getTitle(pagerAdapter.getItemPosition(view));
        if (currentTab == null) {
            return;
        }
        if (openDmg) {
            final ImageView imageView = (ImageView) ViewUtils.findBrotherView(currentTab, R.id.tv_tav_title_dmg, 3);
            if (imageView == null) return;
            imageView.post(() -> changeDmgSize(imageView, position));
        } else {
            changeTextSize(currentTab, position);
        }

        // 回调设置的页面切换效果设置
        if (transformers != null && transformers.size() > 0) {
            for (IViewPagerTransformer transformer : transformers) {
                transformer.transformPage(view, position);
            }
        }
    }


    public void setTextSizeByPosition(int position){
        final TextView currentTab = slidingScaleTabLayout.getTitle(position);
        if (currentTab == null) {
            return;
        }
        final ImageView imageView = (ImageView) ViewUtils.findBrotherView(currentTab, R.id.tv_tav_title_dmg, 3);
        if (imageView == null) return;
        imageView.post(() -> changeDmgSize(imageView, -1));

    }


    private void changeTextSize(final TextView textView, final float position) {
        // 字体大小相同，不需要切换
        if (textSelectSize == textUnSelectSize) return;
        // 必须要在View调用post更新样式，否则可能无效
        textView.post(() -> {
            if (position >= -1 && position <= 1) { // [-1,1]
                if (textSelectSize > textUnSelectSize) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSelectSize - Math.abs((textSelectSize - textUnSelectSize) * position));
                } else {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSelectSize + Math.abs((textUnSelectSize - textSelectSize) * position));
                }
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textUnSelectSize);
            }
        });
    }

    private void changeDmgSize(ImageView imageView, float position) {
        // 字体大小相同，不需要切换
        if (textSelectSize == textUnSelectSize) return;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (position >= -1 && position <= 1) { // [-1,1]
            float scale;
            if (textSelectSize > textUnSelectSize) {
                scale = 1 - Math.abs((1 - minScale) * position);
            } else {
                scale = minScale + Math.abs((1 - minScale) * position);
            }
            imageView.setScaleY(scale);
            imageView.setScaleX(scale);
        } else {
            int width;
            if (textSelectSize > textUnSelectSize) {
                width = (int) (imageView.getMaxWidth() * minScale);
            } else {
                width = imageView.getMaxWidth();
            }
            if (width != params.width) {
                imageView.setScaleX(0.7546f);
                imageView.setScaleY(0.7546f);
            }
        }
    }

    public List<IViewPagerTransformer> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<IViewPagerTransformer> transformers) {
        this.transformers = transformers;
    }
}