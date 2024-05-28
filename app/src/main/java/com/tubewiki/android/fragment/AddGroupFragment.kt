package com.tubewiki.android.fragment

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.gone
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.getRealStatusHeight
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.utils.setTextWhenNotEmpty
import com.tubewiki.android.databinding.FragmentAddGroupLayoutBinding
import com.tubewiki.android.viewmodel.MainViewModel

class AddGroupFragment : ViewModelFragment<MainViewModel, FragmentAddGroupLayoutBinding>() {

    override fun initView(view: View) {
        binding.apply {
            initPageState(stateLayout)
            clContent.setPadding(0, getRealStatusHeight() + 3.dp, 0, 0)
            initTabWithViewPage()
            clSingle.setOnSingleClickListener({
                ARouter.getInstance().navigation(IMiniToolProvider::class.java).toHelpGroup(binding.tvSingleTitle.text.toString(),
                    viewModel.circleConfigLD.value?.selected_group_type?.getOrNull(0)?.group_type ?: 0)
            })
            clFemale.setOnSingleClickListener({
                ARouter.getInstance().navigation(IMiniToolProvider::class.java).toHelpGroup(binding.tvSingleTitle.text.toString(),
                    viewModel.circleConfigLD.value?.selected_group_type?.getOrNull(1)?.group_type ?: 0)
            })
        }
    }

    private fun initTabWithViewPage() {
        binding.commonTabLayout.setViewPager(
            binding.viewPager,
            arrayOf("推荐"),
            childFragmentManager,
            arrayListOf(RecommendFragment()),
            0
        )
    }

    override fun getData() {
        viewModel.circleConfigLD.observe(this) {
            showContentState()
            it?.selected_group_type?.getOrNull(0)?.let { it1 ->
                binding.tvSingleTitle.setTextWhenNotEmpty(it1.title)
                binding.tvSingleDesc.setTextWhenNotEmpty(it1.sub_title)
                if (!it1.icon.isNullOrBlank()) {
                    binding.ivSingleIcon.load(it1.icon)
                }
            } ?: run {
                binding.clHeader.gone()
            }
            it?.selected_group_type?.getOrNull(1)?.let { it1 ->
                binding.tvFemaleTitle.setTextWhenNotEmpty(it1.title)
                binding.tvFemaleDesc.setTextWhenNotEmpty(it1.sub_title)
                if (!it1.icon.isNullOrBlank()) {
                    binding.ivFemaleIcon.load(it1.icon)
                }
            } ?: kotlin.run {
                binding.clFemale.gone()
            }
        }
        viewModel.getCircleConfig()
    }
}