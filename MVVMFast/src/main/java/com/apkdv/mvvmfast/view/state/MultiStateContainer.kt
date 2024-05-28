package com.apkdv.mvvmfast.view.state

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.view.state.state.LoadingState
import com.apkdv.mvvmfast.view.state.state.SuccessState

/**
 * @ProjectName: MultiStatePage
 * @Author: 赵岩
 * @Email: 17635289240@163.com
 * @Description: TODO
 * @CreateDate: 2020/9/17 11:54
 */
@Suppress("UNCHECKED_CAST")
class MultiStateContainer : FrameLayout {

    private var originTargetView: View? = null

    // 重试
    var retry: () -> Unit = {}

    // 偏移量
    var offset = 0

    // 方向
    var orientation = Gravity.BOTTOM

    @ColorRes
    var backGroundColorRes = R.color.transparent

    var lastState: String = ""

    private var statePool: MutableMap<Class<out MultiState>, MultiState> = mutableMapOf()

    private var animator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
        duration = MultiStatePage.config.alphaDuration
    }

    constructor(
        context: Context,
        originTargetView: View, retry: () -> Unit = {},
        offset: Int = 0, orientation: Int = Gravity.BOTTOM
    ) : this(context, null) {
        this.originTargetView = originTargetView
        this.retry = retry
        this.offset = offset
        this.orientation = orientation
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (originTargetView == null && childCount == 1) {
            originTargetView = getChildAt(0)
        }
    }

    fun initialization() {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(originTargetView, 0, layoutParams)
        // MultiStatePage.config?.defaultState?.let { show(it) }
    }

    inline fun <reified T : MultiState> show(
        enableAnimator: Boolean = true,
        noinline notify: (T) -> Unit = {}
    ) {
        show(T::class.java, enableAnimator, notify)
    }

    @JvmOverloads
    fun <T : MultiState> show(
        multiState: T,
        enableAnimator: Boolean = true,
        onNotifyListener: OnNotifyListener<T>? = null
    ) {
        if (childCount == 0) {
            initialization()
        }
        if (childCount > 1) {
            removeViewAt(1)
        }
        if (multiState is SuccessState) {
            //如果上次展示的是SuccessState则跳过
            if (lastState != SuccessState::class.java.name) {
                originTargetView?.visibility = View.VISIBLE
                if (enableAnimator) originTargetView?.executeAnimator()
            }
        } else {
            originTargetView?.visibility = View.INVISIBLE
            val currentStateView =
                multiState.onCreateMultiStateView(context, LayoutInflater.from(context), this)
            multiState.onMultiStateViewCreate(currentStateView, retry, offset, orientation)
            if (offset > 0) {
                when (orientation) {
                    Gravity.LEFT -> currentStateView.setPadding(offset, 0, 0, 0)
                    Gravity.TOP -> currentStateView.setPadding(0, offset, 0, 0)
                    Gravity.RIGHT -> currentStateView.setPadding(0, 0, offset, 0)
                    Gravity.BOTTOM -> currentStateView.setPadding(0, 0, 0, offset)
                }
            }
            currentStateView.setBackgroundResource(backGroundColorRes)
            addView(currentStateView)
            if (enableAnimator) currentStateView.executeAnimator()
            onNotifyListener?.onNotify(multiState)
        }
        //记录上次展示的state
        lastState = multiState.javaClass.name
    }

    @JvmOverloads
    fun <T : MultiState> show(
        clazz: Class<T>,
        enableAnimator: Boolean = true,
        onNotifyListener: OnNotifyListener<T>? = null
    ) {
        findState(clazz)?.let { multiState ->
            show(
                multiState as T,
                enableAnimator,
                onNotifyListener
            )
        }
    }

    private fun <T : MultiState> findState(clazz: Class<T>): MultiState? {
        return if (statePool.containsKey(clazz)) {
            statePool[clazz]
        } else {
            val state = clazz.newInstance()
            statePool[clazz] = state
            state
        }
    }

    private fun View.executeAnimator() {
        this.clearAnimation()
        animator.addUpdateListener {
            this.alpha = it.animatedValue as Float
        }
        animator.start()
    }

    fun isSuccessState(): Boolean {
        return lastState == State.Success
    }

}
object State{
    val Success  = SuccessState::class.java.name
    val Loading  = LoadingState::class.java.name
}