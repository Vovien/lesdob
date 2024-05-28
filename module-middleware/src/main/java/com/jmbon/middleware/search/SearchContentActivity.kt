package com.jmbon.middleware.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.*
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.adapter.SearchRecommendedAdapter
import com.jmbon.middleware.databinding.ActivitySearchContentBinding
import com.jmbon.middleware.utils.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Route(path = "/middleware/search/activity")
class SearchContentActivity :
    ViewModelActivity<SearchMessageViewModel, ActivitySearchContentBinding>() {

    @Autowired(name = TagConstant.SEARCH_CONTENT)
    @JvmField
    var subFragment: String = ""

    @Autowired(name = TagConstant.SEARCH_INDEX)
    @JvmField
    // 搜索页面，没有开始搜索时的状态 Nullable
    var indexFragment: String = ""

    @Autowired(name = TagConstant.SEARCH_INDEX_BUNDLE)
    @JvmField
    var indexBundle: Bundle? = null

    @Autowired(name = TagConstant.SEARCH_CONTENT_BUNDLE)
    @JvmField
    var subBundle: Bundle? = null

    // 直接开启搜索功能
    @Autowired(name = TagConstant.DIRECT_SEARCH)
    @JvmField
    var directSearch: Boolean = false


    @Autowired(name = TagConstant.CAN_SHOW_KEYBOARD)
    @JvmField
    var canShowKeyboard: Boolean = true


    // 直接开启搜索功能的搜索字段
    @Autowired(name = TagConstant.SEARCH_KEY)
    @JvmField
    var searchKey: String = ""

    private var needRecommended = false
    private val recommendAdapter by lazy { SearchRecommendedAdapter() }
    private var isAutoSet = false
    private var hasIndex = false
    private var startSearchState = false
    private var canSearchHint = false

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setIgnoreView(binding.searchTop.edInput, binding.searchTop.textCancel)

        hasIndex = indexFragment.isNotNullEmpty()
        binding.apply {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.searchContent, ARouter.getInstance().build(subFragment)
                        .withBundle(TagConstant.SEARCH_CONTENT_BUNDLE, subBundle)
                        .navigation() as Fragment
                )
                .commit()

            if (hasIndex) {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.searchIndex, ARouter.getInstance().build(indexFragment)
                            .withBundle(TagConstant.SEARCH_INDEX_BUNDLE, indexBundle)
                            .navigation() as Fragment
                    )
                    .commit()
            }
            searchTop.edInput.setIconClickListener {
//                startSearch("")
                //取消搜索
                cancelSearch()
                KeyboardUtils.showSoftInput(binding.searchTop.edInput)
            }
            searchTop.edInput.addTextChangedListener(afterTextChanged = {
                // 不管是不是空 直接发送
                if (needRecommended && !isAutoSet)
                    startRecommend()
            })

            searchTop.edInput.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    editSearch()
                    // 用户通过键盘输入抵达搜索结果页
                    return@setOnEditorActionListener false
                } else {
                    return@setOnEditorActionListener true
                }
            }

            // 返回
            searchTop.icBack.setOnSingleClickListener({
                this@SearchContentActivity.finish()
            })
            // 取消
            searchTop.textCancel.setOnSingleClickListener({
                if (keyBoardHeight > 200)
                    this@SearchContentActivity.finish()
                else {
                    if (binding.searchContent.isVisible) {
                        cancelSearch()
                        KeyboardUtils.showSoftInput(binding.searchTop.edInput)
                    } else this@SearchContentActivity.finish()

                }
            })

        }
        recommendAdapter.setOnItemClickListener { _, _, position ->
            val key = HtmlTools.delHTMLTag(recommendAdapter.data[position].keyword)
            if (key.isNotNullBlank()) {
                isAutoSet = true
                binding.searchTop.edInput.setText(key)
                startSearch(key)
                isAutoSet = false

            }
        }

        // 搜索页面初始化
        binding.searchContent.gone()
        binding.searchIndex.visible()
        if (directSearch && searchKey.isNotNullBlank()) {
            startSearch(searchKey)
        }
    }

    private fun ActivitySearchContentBinding.editSearch() {
        val editText = searchTop.edInput.text.toString()
        if (editText.isNotNullBlank()) {
            startSearch(editText)
        } else if (searchTop.edInput.hint.isNotNullBlank() && canSearchHint && editText.isEmpty()) {
            searchTop.edInput.hint?.toString()?.let {
                searchTop.edInput.setText(it)
            }
            startSearch(searchTop.edInput.hint.toString())
        } else {
            R.string.search_key_empty.showToast()
        }
    }

    private fun startRecommend() {
        startSearchState = false
        started { viewModel.recommendKey.emit(binding.searchTop.edInput.text.toString()) }

    }

    private fun cancelSearch() {
        binding.searchIndex.visible()
        binding.searchContent.invisible()
        binding.searchTop.edInput.text?.clear()
        if (needRecommended) {
            binding.recycleView.gone()

            //未进入搜索结果的取消
            when (subFragment) {
                "/search/result/fragment" -> {
                    MobAnalysisUtils.mInstance.sendEvent("Event_HomePage_SearchResults_Cancel_Click")
                }
                "/banner/search/result/fragment" -> {
                    MobAnalysisUtils.mInstance.sendEvent("Event_SecondarStatusPage_SearchResults_Cancel_Click")
                }
            }
        }

    }

    private var keyBoardHeight = 0

    override fun initData() {
        if (!directSearch && canShowKeyboard){
            binding.searchTop.edInput.postDelayed(
                { KeyboardUtils.showSoftInput(binding.searchTop.edInput) },
                300
            )
        }

        // 键盘监听
        KeyboardUtils.registerSoftInputChangedListener(window) { height ->
            keyBoardHeight = height
            binding.searchTop.edInput.isCursorVisible = keyBoardHeight >= 200

            if (height > 200 && binding.searchTop.edInput.text.isNotNullEmpty()) {
                // 开启推荐功能 不管有没有推荐
                if (binding.recycleView.isGone) {
                    binding.recycleView.visible()
                    if (binding.searchContent.isVisible)
                        binding.searchContent.gone()
                }
                startRecommend()
            } else {
                if (needRecommended || binding.recycleView.isVisible) {
                    if (binding.recycleView.isVisible && binding.searchContent.isGone) {
                        binding.recycleView.gone()
                        binding.searchContent.visible()
                    }
                }

            }
        }
    }

    private fun startSearch(key: String) {
        //回到edittext的首位
        binding.searchTop.edInput.setText(key)
        binding.searchTop.edInput.setSelection(key.length)

        binding.recycleView.gone()
        startSearchState = true
        recommendAdapter.setNewInstance(arrayListOf())
        binding.searchIndex.invisible()
        binding.searchContent.visible()
        binding.searchContent.post {
            started {
                viewModel.searchKey.emit(key)
            }
        }
        // 隐藏键盘
        KeyboardUtils.hideSoftInput(this@SearchContentActivity)
    }

    override fun getData() {
        viewModel.needShowSearchContentWhenInit.onEach {
            if (it) binding.searchContent.visible()
        }.launchIn(lifecycleScope)

        // 是否允许输入框事件，用于纯展示作用
        viewModel.enableEdited.onEach {
            // 新的 API 设置 enable = false 时，点击事件也会取消
//            binding.searchTop.edInput.isEnabled = it
            // 禁用时候打开点击事件
            if (it.not()){
                binding.searchTop.edInput.apply {
                    isClickable = true
                    movementMethod = null
                    isFocusable = false
                    keyListener = null
                    setTextIsSelectable(false)
                    isCursorVisible = false
                    isFocusableInTouchMode = false
                    setOnSingleClickListener({
                        started { viewModel.onEditClick.emit(true) }
                    })
                }

            }
        }.launchIn(lifecycleScope)

        //cancel
        //自己设置了文本后，自动取消原有的点击事件
        viewModel.cancelText.onEach {
            if (it.isNotNullBlank()) {
                binding.searchTop.textCancel.text = it
//                binding.searchTop.textCancel.setOnSingleClickListener({
//                    started { viewModel.onCancelClick.emit(binding.searchTop.edInput.text.toString()) }
//                })
            }
            binding.searchTop.textCancel.text = it
        }.launchIn(lifecycleScope)
        // 设置 cancel 的文字颜色
        viewModel.cancelTextColor.onEach {
            if (it != 0) {
                binding.searchTop.textCancel.setTextColor(it)
            }
        }.launchIn(lifecycleScope)
        // 设置 cancel 的文字大小
        viewModel.cancelTextSize.onEach {
            if (it != 0) {
                binding.searchTop.textCancel.textSize = it.toFloat()
            }
        }.launchIn(lifecycleScope)

        viewModel.makeCancelTextLikeSearch.onEach {
            if (it) {
                binding.searchTop.textCancel.setOnSingleClickListener({
                    binding.editSearch()
                }, delayMillis = 200)
            }
        }.launchIn(lifecycleScope)

        // hint
        viewModel.editHint.onEach {
            binding.searchTop.edInput.hint = it
        }.launchIn(lifecycleScope)
        // 推荐数据
        viewModel.recommendList.onEach {
            // 已经初始化的数据
            if (!startSearchState) {
                if (binding.recycleView.isGone)
                    binding.recycleView.visible()
                recommendAdapter.setNewInstance(it)
            }
        }.launchIn(lifecycleScope)

        // 初始化推荐列表
        viewModel.needRecommended.onEach {
            needRecommended = it
            if (it) {
                binding.recycleView.init(
                    recommendAdapter,
                    dividerColor = ColorUtils.getColor(R.color.white_color_line),
                    dividerHeight = 1f,
                    left = 20f,
                    right = 20f,
                    vertical = false
                )
            }
        }.launchIn(lifecycleScope)
        viewModel.showCancel.onEach {
            binding.searchTop.textCancel.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(lifecycleScope)
        viewModel.showBack.onEach {
            binding.searchTop.icBack.visibility = if (it) View.VISIBLE else View.GONE
        }.launchIn(lifecycleScope)
        // 被动搜索
        viewModel.passiveSearchKey.onEach {
            if (it.isNotNullBlank()) {
                binding.searchTop.edInput.setText(it)
                startSearch(it)
            }
        }.launchIn(lifecycleScope)

        // 可以通过 hint 搜索
        viewModel.canSearchHint.onEach {
            canSearchHint = it
        }.launchIn(lifecycleScope)
    }

    override fun onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(this.window)
        super.onDestroy()
    }
}