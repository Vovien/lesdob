package com.tubewiki.home.fragment

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ActivityUtils
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.decoration.GridSpacingItemDecoration
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.utils.convertToChineseNumber
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.adapter.HomeBankAdapter
import com.tubewiki.home.adapter.HomeCategoryAdapter
import com.tubewiki.home.adapter.HomeTubeStepAdapter
import com.tubewiki.home.databinding.FragmentHomeBinding
import com.jmbon.middleware.dialog.WeChatDialog
import com.tubewiki.home.viewmodel.MainFragmentViewModel


class HomeFragment : ViewModelFragment<MainFragmentViewModel, FragmentHomeBinding>() {

    private val bankAdapter by lazy {
        HomeBankAdapter()
    }

    private val categoryAdapter by lazy {
        HomeCategoryAdapter()
    }

    private val contentAdapter by lazy { HomeTubeStepAdapter() }

    private val statusHeight by lazy { StatusBarCompat.getStatusBarHeight(view?.context) }

    override fun initView(view: View) {
        binding.apply {
            (binding.ivLogo.layoutParams as? MarginLayoutParams)?.topMargin = statusHeight + 4.dp
            rvBank.addItemDecoration(GridSpacingItemDecoration(spacing = 10.dp))
            rvBank.adapter = bankAdapter
            rvCategory.adapter = categoryAdapter
            rvContent.init(
                contentAdapter,
                layoutManager = GridLayoutManager(activity, 2),
                dividerHeight = 10f,
                vertical = false,
                showEnd = true
            )

            tvAdvisory.setBackground(
                backgroundColor = Color.WHITE,
                borderWidth = 3.dp,
                borderGradientColors = intArrayOf(
                    Color.parseColor("#E02020"),
                    Color.parseColor("#FA6400"),
                    Color.parseColor("#F7B500"),
                    Color.parseColor("#6DD400"),
                    Color.parseColor("#0091FF"),
                    Color.parseColor("#6236FF"),
                    Color.parseColor("#B620E0"),
                ),
                borderOrientation = GradientDrawable.Orientation.RIGHT_LEFT,
                radius = 16.dp
            )
            tvAdvisory.onClick {
                ARouter.getInstance().navigation(IMiniToolProvider::class.java).toAdvisoryHelp()
            }
        }
    }

    override fun getData() {
        started {
            viewModel.index().next {
                binding.gpTitle.isVisible = !app_title.isNullOrBlank()
                binding.tvTitle.text = app_title
                bankAdapter.setList(bank_list)
                binding.tvBankTitle.text = if (!bank_title.isNullOrBlank()) {
                    bank_title
                } else {
                    "全球${convertToChineseNumber(bank_list?.size ?: 0)}大精子银行"
                }
                binding.rvBank.isVisible = !bank_list.isNullOrEmpty()
                binding.tvBankTitle.isVisible = binding.rvBank.isVisible
                categoryAdapter.setList(topic_list)
                binding.rvCategory.isVisible = !topic_list.isNullOrEmpty()
                contentAdapter.setList(cateList)
            }
        }

        viewModel.popupImageInfoLD.observe(this) {
            val imgUrl = it?.pop_info?.popupImg
            if (!imgUrl.isNullOrBlank()) {
                val dialog = WeChatDialog(ActivityUtils.getTopActivity(), it.pop_info!!)
                dialog.showDialog()
            }
        }
        viewModel.getPopupImage()
    }
}
