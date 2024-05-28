package com.apkdv.mvvmfast.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.event.Message
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.SizeUtil
import com.apkdv.mvvmfast.view.LoadingDialog
import com.apkdv.mvvmfast.view.StateLayout
import com.apkdv.mvvmfast.view.state.*
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

/**
 *   @auther : lengyue
 *   time   : 2020/11/01
 */
abstract class AppBaseFragment<VB : ViewBinding> : Fragment() {
    //是否第一次加载
    private var isFirst: Boolean = true
    protected var mStateLayout: StateLayout? = null
    protected var stateContainer: MultiStateContainer? = null
    protected lateinit var binding: VB

    private var loadingPopup: BasePopupView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateBindingWithGeneric(layoutInflater)
        return binding.root
    }

    fun initPageState(offset: Int = 0, orientation: Int = Gravity.BOTTOM) {
        stateContainer = initState(offset, orientation) { refreshDataWhenError() }
        showLoadingState()
    }

    fun initPageState(state: MultiStateContainer) {
        this.stateContainer = state
        showLoadingState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
        initViewModel()
        beforeViewInit()
        initView(view)
        getData()
    }

    open fun getData() {
    }

    open fun initViewModel() {}
    open fun beforeViewInit() {}

    abstract fun initView(view: View)


    override fun onResume() {
        super.onResume()
        onVisible()
    }


    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            isFirst = false
            lazyLoadData()
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}


    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    protected fun showLoading(whiteBackground: Boolean = true) {
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .hasShadowBg(false)
                .asCustom(context?.let { LoadingDialog(it, whiteBackground) })
        }
        loadingPopup?.apply {
            if (!isShow)
                show()
        }
    }

    protected fun isShowLoading() = loadingPopup?.isShow == true

    /***
     * 初始化状态布局layout
     * @param stateLayout： StateLayout
     */
    fun initStateLayout(stateLayout: StateLayout?) {
        if (stateLayout == null) {
            return
        }
        this.mStateLayout = stateLayout
        mStateLayout?.setErrorClick { refreshDataWhenError(AppBaseActivity.STATE_REFRESH_MANUAL) }
        mStateLayout?.setEmptyClick { refreshDataWhenError(AppBaseActivity.STATE_REFRESH_MANUAL) }
        mStateLayout?.setNoNetClick { refreshDataWhenError(AppBaseActivity.STATE_REFRESH_NET_CHANGE) }
    }

    /***
     * 显示无数据状态
     */
    open fun showEmptyState() {
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showEmptyView()
            stateContainer?.showEmptyState()
        }
    }


    /***
     * 显示错误状态
     */
    open fun showErrorState() {
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showErrorView()
            stateContainer?.showErrorState()
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
            stateContainer?.showErrorState(errorMsg)
        }

    }


    /***
     * 显示正在加载状态
     */
    open fun showLoadingState() {
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showLoadingView()
            stateContainer?.showLoadingState()
        }
    }

    /***
     * 显示无网络状态
     */
    open fun showNoNetState() {
        lifecycleScope.launchWhenStarted {
            mStateLayout?.showNoNetView()
            stateContainer?.showNoNetState()
        }
    }

    /***
     * 显示内容，即正常页面
     */
    open fun showContentState() {
        started {
            mStateLayout?.showContentView()
            stateContainer?.showContentState()
        }
    }

    /***
     * 当错误页面点击刷新或网络连接后自动刷新时调用
     * @param state
     * [.STATE_REFRESH_MANUAL] 错误页面手动点击刷新
     * [.STATE_REFRESH_NET_CHANGE] 网络连接后自动刷新
     */
    fun refreshDataWhenError(state: Int) {
        refreshDataWhenError()
    }

    open fun refreshDataWhenError() {
        showLoadingState()
    }

    /**
     * 关闭等待框
     */
    protected fun dismissLoading() {
        loadingPopup?.dismiss()
    }

    /**
     * 注册 UI 事件
     */
    protected fun registerDefUIChange(viewModel: BaseViewModel) {
        viewModel.defUI.showDialog.observe(this) {
            showLoading()
        }
        viewModel.defUI.dismissDialog.observe(this) {
            dismissLoading()
        }
        viewModel.defUI.toastEvent.observe(this) {
            it.showToast()
        }
        viewModel.defUI.msgEvent.observe(this) {
            handleEvent(it)
        }
    }


    /**
     * 注册 UI 事件
     */
    protected fun registerLayoutChange(viewModel: BaseViewModel) {
        viewModel.defLayout.showContent.observe(this) {
            showContentState()
        }
        viewModel.defLayout.showEmpty.observe(this) {
            showEmptyState()
        }
        viewModel.defLayout.showError.observe(this) {
            showErrorState()
        }
        viewModel.defLayout.showLoading.observe(this) {
            showLoadingState()
        }
        viewModel.defLayout.showNoNet.observe(this) {
            showNoNetState()
        }
        viewModel.defLayout.showErrorMsg.observe(this) {
            showErrorState(it)
        }
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

    fun reSetHeight(targetView: View, height: Int) {
        targetView.layoutParams.height = height
    }
}