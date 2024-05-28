package com.jmbon.widget;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * PACKAGE_NAME: cn.com.enorth.easymakeapp.view.customsmartview
 * PROJECT_NAME:learnbaodi
 * 创建日期 :2019/10/11
 * 创建时间: 15/49
 * author  kunkun5love
 * 说明: smartRefrush中自定义的head 需要实现RefreshHeader
 ***/
public class CustomSmartRefreshFooter extends LinearLayout implements RefreshFooter {

    Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
    private View mRefrushSmartHeadView;
    private ImageView mRefrush_smart_head_iv;
    private TextView mRefrush_smart_head_tv;
    private boolean isHasVibrator = true;
    private PullUpStatusLisener pullUpStatusLisener;

    /**
     * 我们这边写好了  两个布局堃堃5love:
     * 刷新head  R.layout.layout_ptr_header
     * <p>
     * 堃堃5love:
     * 加载更多:   R.layout.layout_load_more
     *
     * @param context
     */
    public CustomSmartRefreshFooter(Context context) {
        super(context);
        initRefrushView(context);
    }


    //  下面的都是RefreshHeader继承的方法  start

    public CustomSmartRefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRefrushView(context);
    }

    public CustomSmartRefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRefrushView(context);
    }

    /**
     * 打气一个view,并进行初始化
     *
     * @param context
     */
    private void initRefrushView(Context context) {
        //  设置居中
        setGravity(Gravity.CENTER);
        mRefrushSmartHeadView = LayoutInflater.from(context).inflate(R.layout.custom_smart_refresh_footer, null);
        //LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //mRefrushSmartHeadView.setLayoutParams(layoutParams);
        addView(mRefrushSmartHeadView);
        mRefrush_smart_head_iv = mRefrushSmartHeadView.findViewById(R.id.refresh_head);
        mRefrush_smart_head_tv = mRefrushSmartHeadView.findViewById(R.id.tv_ptr_header);
    }

    /**
     * 获取真实视图（必须返回，不能为null）  当前的head就是一个view
     *
     * @return
     */
    @NonNull
    @Override
    public View getView() {
        return this;
    }

    /**
     * 获取变换方式（必须指定一个：平移、拉伸、固定、全屏）  我们类似于经典刷新  选择平移
     *
     * @return
     */
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null;
    }

    /**
     * 设置主题颜色 （如果自定义的Header没有注意颜色，本方法可以什么都不处理）
     *
     * @param colors 对应Xml中配置的 srlPrimaryColor srlAccentColor
     */
    @Override
    public void setPrimaryColors(int... colors) {

    }

    /**
     * 尺寸定义初始化完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
     *
     * @param kernel        RefreshKernel 核心接口（用于完成高级Header功能）
     * @param height        HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
    }

    /**
     * 手指拖动下拉（会连续多次调用，用于实时控制动画关键帧）
     *
     * @param percent       下拉的百分比 值 = offset/headerHeight (0 - percent - (headerHeight+maxDragHeight) / headerHeight )
     * @param offset        下拉的像素偏移量  0 - offset - (headerHeight+maxDragHeight)
     * @param height        Header的高度
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    /**
     * 开始动画（开始刷新或者开始加载动画）
     *
     * @param refreshLayout RefreshLayout
     * @param height        HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    /**
     * 动画结束
     *
     * @param refreshLayout RefreshLayout
     * @param success       数据是否成功刷新或加载
     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {

        if (success) {
            //mRefrush_smart_head_tv.setText("刷新成功");
        } else {
            //mRefrush_smart_head_tv.setText("刷新失败");
        }

        return 500;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    /**
     * 不同的状态控制内部控件的显示和旋转
     *
     * @param refreshLayout
     * @param oldState
     * @param newState
     */
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState,
                               @NonNull RefreshState newState) {

        switch (newState) {
            case None:
            case PullUpCanceled:
                if (pullUpStatusLisener != null) {
                    pullUpStatusLisener.pullUpCanceled();
                }
                //上拉取消
                mRefrush_smart_head_tv.setText("上拉查看下一个答案");
                mRefrush_smart_head_iv.animate().rotation(0);
                break;
            case ReleaseToLoad:
                if (pullUpStatusLisener != null) {
                    pullUpStatusLisener.releaseToLoad();
                }
                //上拉将要释放开始刷新
                callVibrator(mRefrush_smart_head_tv);
                mRefrush_smart_head_tv.setText("释放切换下一个答案");
                mRefrush_smart_head_iv.animate().rotation(180);
                break;
            case PullUpToLoad:
                //开始上拉
                mRefrush_smart_head_tv.setText("上拉查看下一个答案");
                mRefrush_smart_head_iv.animate().rotation(0);

                if (pullUpStatusLisener != null) {
                    pullUpStatusLisener.pullUpToLoad();
                }
                break;
            case Refreshing: //  正在刷新
                break;

        }
    }

    public void setHasVibrator(boolean hasVibrator) {
        isHasVibrator = hasVibrator;
    }

    private void callVibrator(View view) {
        if (isHasVibrator) {
            view.performHapticFeedback(
                    HapticFeedbackConstants.LONG_PRESS,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return true;
    }

    public void setPullUpStatusLisener(PullUpStatusLisener pullUpStatusLisener) {
        this.pullUpStatusLisener = pullUpStatusLisener;
    }

    public interface PullUpStatusLisener {
        void pullUpCanceled();

        void releaseToLoad();

        void pullUpToLoad();
    }

}
