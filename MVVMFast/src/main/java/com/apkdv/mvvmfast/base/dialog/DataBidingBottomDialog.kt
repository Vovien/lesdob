package com.apkdv.mvvmfast.base.dialog

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.lxj.xpopup.core.BottomPopupView

abstract class DataBidingBottomDialog<DB : ViewDataBinding>(context: Context) :
    BaseBottomDialog<DB>(context)