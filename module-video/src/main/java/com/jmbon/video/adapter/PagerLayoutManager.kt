package com.jmbon.video.adapter

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jmbon.video.listener.OnViewPagerListener
import com.jmbon.widget.consecutivescroller.ConsecutiveScrollerLayout

/**
 * ViewPager效果的LayoutManager
 *
 */
class PagerLayoutManager(context: Context?) : LinearLayoutManager(context),
    OnTouchListener {
    private var mPagerSnapHelper: PagerSnapHelper? = null
    private var mOnViewPagerListener: OnViewPagerListener? = null
    private var mState = 0
    private var mdy = 0

    /**
     * 移动方向
     */
    private var direction = 0
    private fun init() {
        mPagerSnapHelper = PagerSnapHelper()
    }

    override fun onAttachedToWindow(recyclerView: RecyclerView) {
        super.onAttachedToWindow(recyclerView)
        if (recyclerView.onFlingListener == null) {
            mPagerSnapHelper!!.attachToRecyclerView(recyclerView)
        }
        recyclerView.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener)
        recyclerView.setOnTouchListener(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mState = newState
                when (mState) {
                    ConsecutiveScrollerLayout.SCROLL_STATE_IDLE -> {
                        val viewIdle =
                            mPagerSnapHelper!!.findSnapView(this@PagerLayoutManager) ?: return
                        val positionIdle = getPosition(viewIdle)
                        if (mOnViewPagerListener != null && childCount == 1) {
                            mOnViewPagerListener!!.onPageSelected(
                                positionIdle,
                                positionIdle == itemCount - 1,
                                viewIdle
                            )
                        }
                    }

                    else -> {}
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mdy = dy
            }
        })
    }

    /**
     * 判断是否设置了监听
     */
    fun viewPagerListenerIsNull(): Boolean {
        return mOnViewPagerListener == null
    }

    /**
     * 监听竖直方向的相对偏移量
     */
    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        direction = dy
        return super.scrollVerticallyBy(dy, recycler, state)
    }

    /**
     * 监听水平方向的相对偏移量
     */
    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        direction = dx
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    /**
     * 设置监听
     */
    fun setOnViewPagerListener(listener: OnViewPagerListener?) {
        mOnViewPagerListener = listener
    }

    fun clearOnViewPagerListener() {
        mOnViewPagerListener = null
    }

    private val mChildAttachStateChangeListener: RecyclerView.OnChildAttachStateChangeListener =
        object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                if (mOnViewPagerListener != null && childCount == 1) {
                    mOnViewPagerListener!!.onInitComplete()
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                if (direction >= 0) {
                    if (mOnViewPagerListener != null) {
                        mOnViewPagerListener!!.onPageRelease(true, getPosition(view), view)
                    }
                } else {
                    if (mOnViewPagerListener != null) {
                        mOnViewPagerListener!!.onPageRelease(false, getPosition(view), view)
                    }
                }
            }
        }

    init {
        init()
    }

    /**
     * 监听ouTouch事件，因为如果从第二个视频滑动到第一个视频(快速连续滑动),
     * onScrollStateChanged是不会触发SCROLL_STATE_IDLE状态的,会导致
     * 第一个视频不会播放的问题,不会调用onPageSelected监听回调。
     * 经反复测试发现,会回调onTouch的事件,所以在这里进行触发
     */
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> if (mPagerSnapHelper != null) {
                val snapView = mPagerSnapHelper!!.findSnapView(this@PagerLayoutManager)
                if (snapView != null) {
                    val position = getPosition(snapView)
                    //如果是第一个视频,并且
                    if (position == 0 && mdy < 0) {
                        if (mOnViewPagerListener != null && childCount == 1) {
                            mOnViewPagerListener!!.onPageSelected(position, false, snapView)
                        }
                    }
                }
            }

            else -> {}
        }
        return false
    }

    /**
     * bug java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
     *
     * https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
        }
    }
}