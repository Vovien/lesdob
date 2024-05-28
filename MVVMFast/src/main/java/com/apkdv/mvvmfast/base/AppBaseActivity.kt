package com.apkdv.mvvmfast.base

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.databinding.ToolbarBinding
import com.apkdv.mvvmfast.event.Message
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.apkdv.mvvmfast.utils.SizeUtil
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.view.LoadingDialog
import com.apkdv.mvvmfast.view.StateLayout
import com.apkdv.mvvmfast.view.state.*
import com.apkdv.mvvmfast.view.titlebar.widget.CommonTitleBar
import com.blankj.utilcode.util.KeyboardUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView


abstract class AppBaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB
    private var loadingPopup: BasePopupView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBindingWithGeneric(layoutInflater)
        setContentView(binding.root)
        initActionBar()
        initViewModel()
        //注册 UI事件
        beforeViewInit()
        initView(savedInstanceState)
        initData()
        getData()
//        lifecycleScope.launchWhenResumed {
//            NetworkChangeListenHelper().registerNetworkCallback {
//                if (!it) {
//                    showNoNetState()
//                } else {
//                    refreshDataWhenError()
//                }
//            }
//        }
    }


    open fun beforeViewInit() {

    }

    open fun initViewModel() {

    }

    private fun initActionBar() {
        val ab = supportActionBar
        if (ab != null) {
            ab.elevation = 0f
        }
        StatusBarCompat.StatusBarLightModeWithColor(this, Color.WHITE)
        if (supportActionBar != null) {
            initToolbar()
        }
    }

    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()
    abstract fun getData()

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    open fun showLoading() {
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(this)
                .dismissOnBackPressed(false)
                .hasShadowBg(false)
                .asCustom(LoadingDialog(this))
        }
        loadingPopup?.apply {
            if (!isShow)
                show()
        }
    }

    /**
     * 关闭等待框
     */
    open fun dismissLoading() {
        loadingPopup?.dismiss()
    }


    companion object {
        const val STATE_REFRESH_MANUAL = 1
        const val STATE_REFRESH_NET_CHANGE = 2
    }


    protected var mStateLayout: StateLayout? = null
    protected var stateContainer: MultiStateContainer? = null


    val titleBarView: CommonTitleBar by lazy {
        ToolbarBinding.inflate(layoutInflater).titlebar
    }

    private fun initToolbar() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.action_bar_bg))
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayShowHomeEnabled(false)
            actionBar.setDisplayShowCustomEnabled(true)
            val alp = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
            )
            actionBar.setCustomView(titleBarView, alp)
            if (titleBarView.parent is Toolbar) {
                val parent = titleBarView.parent as Toolbar
                parent.setContentInsetsAbsolute(0, 0)
            }
            actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            if (enableBack()) {
                val imageTintList = ColorStateList.valueOf(Color.BLACK)
                titleBarView.leftImageButton.imageTintList = imageTintList
                titleBarView.leftImageButton.setOnClickListener {
                    onBackPressed()
                }
            } else titleBarView.leftImageButton.visibility = View.GONE
        }
    }

    open fun setTitleBarColor(@ColorInt titleBarColor: Int) {
        titleBarView.setTitleBarColor(titleBarColor)
    }


    open fun enableBack(): Boolean {
        return true
    }

    fun setTitleName(titleName: String) {
        if (supportActionBar != null) {
            titleBarView.setCenterText(titleName)
            titleBarView.centerTextView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
    }


    fun setRightTextMenu(menu: String) {
        titleBarView.setRightText(
            menu, ContextCompat.getColor(
                this,
                R.color.color_dis_enable
            )
        )
        titleBarView.rightTextView.textSize = 17f
    }

    fun setLineVisible(b: Boolean) {
        titleBarView.setShowBottomLine(b)
    }

    fun setToolbarBackground(@ColorInt color: Int) {
        titleBarView.setBackgroundColor(color)
    }

    /*点击空白处隐藏键盘 Start*/
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN && isInputShow()) {
            val view = currentFocus
            if (isShouldHideKeyBord(view, ev)) {
                KeyboardUtils.hideSoftInput(this)
            } else super.dispatchTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 判定当前是否需要隐藏
     * true
     */
    private fun isShouldHideKeyBord(v: View?, ev: MotionEvent): Boolean {
        var hide = false
        if (v != null && v is EditText) {
            hide = !touchInView(v, ev)
        }
        // 如果要隐藏键盘 判断是否是排除的 view
        if (hide)
            hide = !shouldIgnore(ev)

        return hide
    }

    /*
     * 键盘是否弹出
     */
    private fun isInputShow(): Boolean {
        window.decorView.getWindowVisibleDisplayFrame(mRect)
        var height = resources.displayMetrics.heightPixels
        height -= height / 5
        return mRect.height() < height
    }

    private val mRect = Rect()

    private fun touchInView(view: View, event: MotionEvent): Boolean {
        view.getGlobalVisibleRect(mRect) //屏幕中的位置
        return mRect.contains(event.x.toInt(), event.y.toInt())
    }

    private val ignoreView by lazy { arrayListOf<View>() }

    /**
     * 点击在这些view上不会隐藏键盘
     *
     */
    open fun setIgnoreView(vararg ignore: View) {
        ignore.forEach {
            ignoreView.add(it)
        }

    }

    /*
     * 键盘是否弹出
     */
    private fun shouldIgnore(event: MotionEvent): Boolean {
        for (findView in ignoreView) {
            if (touchInView(findView, event)) return true
        }
        return false
    }

    /***
     * 初始化状态布局layout
     * @param stateLayoutId： StateLayout的id
     */
    fun initStateLayout(stateLayoutId: Int) {
        initStateLayout(findViewById<View>(stateLayoutId) as StateLayout)
    }

    fun initPageState(offset: Int = 0, orientation: Int = Gravity.BOTTOM) {
        stateContainer = initState(offset, orientation) { refreshDataWhenError() }
        showLoadingState()
    }

    fun initPageState(state: MultiStateContainer) {
        this.stateContainer = state
        showLoadingState()
    }

    /***
     * 初始化状态布局layout
     * @param stateLayout： StateLayout
     */
    protected fun initStateLayout(stateLayout: StateLayout?) {
        if (stateLayout == null) {
            return
        }
        this.mStateLayout = stateLayout
        mStateLayout?.setErrorClick { refreshDataWhenError() }
        mStateLayout?.setNoNetClick { refreshDataWhenError() }
    }

    /***
     * 显示无数据状态
     */
    open fun showEmptyState() {
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showEmptyView()
            stateContainer.showEmptyState()
        }
    }

    /***
     * 显示错误状态
     */
    open fun showErrorState() {
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showErrorView()
            stateContainer.showErrorState()
        }
    }

    /***
     * 显示错误状态
     */
    open fun showErrorState(errorMsg: String?) {
        mStateLayout?.errorView?.findViewById<TextView>(R.id.textErrorSet)?.text =
            if (errorMsg.isNullOrEmpty()) "出了点问题" else errorMsg
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showErrorView()
            stateContainer.showErrorState(errorMsg)
        }
    }

    /***
     * 显示错误状态,修改errorview的背景颜色
     */
    open fun showErrorState(errorMsg: String?, bgColor: Int) {
        mStateLayout?.errorView?.findViewById<TextView>(R.id.textErrorSet)?.text =
            if (errorMsg.isNullOrEmpty()) "出了点问题" else errorMsg
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showErrorView()
            stateContainer.showErrorState(errorMsg, bgColor)
        }
    }


    /***
     * 显示正在加载状态
     */
    open fun showLoadingState() {
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showLoadingView()
            stateContainer.showLoadingState()
        }
    }

    /***
     * 显示无网络状态
     */
    open fun showNoNetState() {
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showNoNetView()
            stateContainer.showNoNetState()
        }
    }

    /***
     * 显示内容，即正常页面
     */
    open fun showContentState() {
        mStateLayout?.showContentView()
        stateContainer.showContentState()
    }

    /***
     * 当错误页面点击刷新或网络连接后自动刷新时调用
     * @param state
     * [.STATE_REFRESH_MANUAL] 错误页面手动点击刷新
     * [.STATE_REFRESH_NET_CHANGE] 网络连接后自动刷新
     */
    open fun refreshDataWhenError(state: Int) {
        refreshDataWhenError()
    }

    open fun refreshDataWhenError() {
        showLoadingState()
    }

    fun setEmptyHeight(targetView: View, topView: View?, bottomView: View?) {
        val topLocation = IntArray(2)
        val bottomLocation = IntArray(2)
        var top = 0
        if (topView != null) {
            topView.getLocationOnScreen(topLocation)
            top = topLocation[1] + topView.height
        }
        bottomView?.getLocationOnScreen(bottomLocation)
        val size = SizeUtil.getHeight() - top - bottomLocation[1]
        targetView.layoutParams.height = size
    }

}
