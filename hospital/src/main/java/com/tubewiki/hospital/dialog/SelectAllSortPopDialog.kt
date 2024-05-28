package com.tubewiki.hospital.dialog

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.view.get
import com.apkdv.mvvmfast.base.dialog.BasePartShadowPopupView
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ColorUtils
import com.lxj.xpopup.util.XPopupUtils
import com.tubewiki.hospital.R
import com.tubewiki.hospital.databinding.DialogAllSortSearchSelectBinding

class SelectAllSortPopDialog(
    context: Context,
    var type: Int,
    var isWhiteBg: Boolean = false,
    var result: (Int) -> Unit
) :
    BasePartShadowPopupView<DialogAllSortSearchSelectBinding>(context) {


    override fun onCreate() {
        super.onCreate()
        if (!isWhiteBg) {
            binding.root.setBackgroundColor(ColorUtils.getColor(R.color.color_fa))
        }
        when (type) {
            1 -> {
                selectedView((binding.llAll1[0] as TextView), binding.llAll1[1])
                unSelectedView((binding.llAll2[0] as TextView), binding.llAll2[1])
                unSelectedView((binding.llAll3[0] as TextView), binding.llAll3[1])
            }
            2 -> {
                selectedView((binding.llAll2[0] as TextView), binding.llAll2[1])
                unSelectedView((binding.llAll1[0] as TextView), binding.llAll1[1])
                unSelectedView((binding.llAll3[0] as TextView), binding.llAll3[1])
            }
            3 -> {
                selectedView((binding.llAll3[0] as TextView), binding.llAll3[1])
                unSelectedView((binding.llAll2[0] as TextView), binding.llAll2[1])
                unSelectedView((binding.llAll1[0] as TextView), binding.llAll1[1])
            }

        }


        binding.llAll1.setOnClickListener {
            selectedView((binding.llAll1[0] as TextView), binding.llAll1[1])
            unSelectedView((binding.llAll2[0] as TextView), binding.llAll2[1])
            unSelectedView((binding.llAll3[0] as TextView), binding.llAll3[1])
            dismiss()
            result(1)

        }
        binding.llAll2.setOnClickListener {
            selectedView((binding.llAll2[0] as TextView), binding.llAll2[1])
            unSelectedView((binding.llAll1[0] as TextView), binding.llAll1[1])
            unSelectedView((binding.llAll3[0] as TextView), binding.llAll3[1])
            dismiss()
            result(2)
        }
        binding.llAll3.setOnClickListener {
            selectedView((binding.llAll3[0] as TextView), binding.llAll3[1])
            unSelectedView((binding.llAll2[0] as TextView), binding.llAll2[1])
            unSelectedView((binding.llAll1[0] as TextView), binding.llAll1[1])
            dismiss()
            result(3)
        }

    }


    fun selectedView(textView: TextView, view: View) {
        textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        textView.setTextColor(context.resources.getColor(R.color.color_currency))

        view.visible()
    }

    fun unSelectedView(textView: TextView, view: View) {
        textView.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        textView.setTextColor(context.resources.getColor(R.color.color_262626))
        view.gone()
    }


//    override fun getPopupHeight(): Int {
//        return (XPopupUtils.getScreenHeight(context) * 0.6f).toInt()
//    }
}