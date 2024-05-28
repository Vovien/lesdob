package com.tubewiki.mine.view.setting

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.PhoneUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.ContactBean
import com.tubewiki.mine.databinding.ActivityContactAppBinding
import com.tubewiki.mine.databinding.ItemContactUsLayoutBinding
import com.tubewiki.mine.view.model.SettingViewModel

@Route(path = "/mine/about/contact_app")
class ContactAppActivity : ViewModelActivity<SettingViewModel, ActivityContactAppBinding>() {

    lateinit var contactBean: ContactBean

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.contact_app))

        binding.sbContact.setOnClickListener {

            contactBean.let {
                if (it.customerServiceMobile.isNotNullEmpty()) {
                    showCallDialog(it.customerServiceMobile)
                } else {
                    "号码为空".showToast()
                }
            }

        }
    }

    override fun initData() {

    }

    override fun getData() {

        started {

            viewModel.contactUs().netCatch {
                message.showToast()
            }.next {
                contactBean = this

                //动态添加view
                createContactView()


            }
        }
    }

    private fun createContactView() {
        binding.llContainer.removeAllViews()
        for (data in contactBean.contact) {
            val contactView = ItemContactUsLayoutBinding.inflate(layoutInflater)
            contactView.tvTitle.text = data.title
            if (data.content.isNotNullEmpty()) {
                contactView.tvPhone.text = data.content[0]
            } else {
                contactView.tvPhone.gone()
            }

            if (data.content.isNotNullEmpty() && data.content.size > 1) {
                contactView.tvEmail.text = data.content[1]
            } else {
                contactView.tvEmail.gone()
            }
            binding.llContainer.addView(contactView.root)
        }

    }

    private fun showCallDialog(number: String) {

        val listData = arrayListOf(
            CustomDialogTypeBean(
                resources.getString(R.string.call_kefu),
                CustomDialogTypeBean.TYPE_TITLE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                number,
                CustomDialogTypeBean.TYPE_MESSAGE
            ) as MultiItemEntity,
            CustomDialogTypeBean(
                resources.getString(R.string.currency_cancle), CustomDialogTypeBean.TYPE_CANCEL
            ) as MultiItemEntity,
        )
        XPopup.Builder(this)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .isDestroyOnDismiss(true)
            .asCustom(
                CustomListBottomDialog(this, listData) { select ->
                    if (select == 1)
                        PhoneUtils.dial(number)
                }
            ).show()
    }
}