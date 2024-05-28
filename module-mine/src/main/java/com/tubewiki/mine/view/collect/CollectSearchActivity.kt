package com.tubewiki.mine.view.collect

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityCollectSearchBinding

/**
 * 收藏搜索页面
 */
@Route(path = "/mine/collect_search/activity")
class CollectSearchActivity : AppBaseActivity<ActivityCollectSearchBinding>() {

    val content = ARouter.getInstance().build("/mine/collection/fragment")
        .withBoolean(TagConstant.PARAMS2, true)
        .navigation() as CollectionFragment

    override fun initView(savedInstanceState: Bundle?) {

        initFragment()
    }

    override fun initData() {

        //监听搜索输入
        binding.searchView.icBack.setOnClickListener { finish() }
        binding.searchView.edInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && binding.searchView.edInput.text.isNotNullEmpty()) {
                if (binding.searchView.edInput.text.isNullOrBlank()) {
                    binding.searchView.edInput.text?.clear()
                    return@setOnEditorActionListener false
                }
                searchData(binding.searchView.edInput.text.toString())
                return@setOnEditorActionListener false
            } else {
                return@setOnEditorActionListener true
            }
        }

        binding.searchView.edInput.setIconClickListener {
            //清空搜索隐藏
            hasSearch = false
            binding.searchView.edInput.isFocusable = true
            binding.searchView.edInput.requestFocus()
            KeyboardUtils.showSoftInput(binding.searchView.edInput)
        }

        binding.searchView.edInput.isFocusable = true
        binding.searchView.edInput.requestFocus()

        KeyboardUtils.registerSoftInputChangedListener(window) {
            //键盘显示才有光标
            binding.searchView.edInput.isCursorVisible = it > 200


            if (hasSearch && it < 200) {
                binding.content.visibility = View.VISIBLE
            } else {
                binding.content.visibility = View.INVISIBLE
            }
        }
    }

    var hasSearch = false
    private fun searchData(keyWord: String) {
        //回到edittext的首位
        binding.searchView.edInput.setText(keyWord)
        hasSearch = true
        KeyboardUtils.hideSoftInput(binding.searchView.edInput)
        content.searchData(keyWord)

    }

    override fun getData() {

    }

    private fun initFragment() {

        binding.content.visibility = View.INVISIBLE
        supportFragmentManager.beginTransaction().add(R.id.content, content).commit()
    }


    private var isFirst = true
    override fun onResume() {
        super.onResume()
        if (isFirst) {
            isFirst = false
            KeyboardUtils.showSoftInput(binding.searchView.edInput)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardUtils.unregisterSoftInputChangedListener(window)

    }

}