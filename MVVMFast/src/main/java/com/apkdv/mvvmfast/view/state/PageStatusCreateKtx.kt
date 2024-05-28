package com.apkdv.mvvmfast.view.state

import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


fun View.initState(
    offset: Int = 0,
    orientation: Int = Gravity.BOTTOM,
    retry: () -> Unit = {}
): MultiStateContainer {
    return createState(this, retry, offset, orientation)
}


fun FragmentActivity.initState(
    offset: Int = 0,
    orientation: Int = Gravity.BOTTOM,
    retry: () -> Unit = {}
): MultiStateContainer {
    return MultiStatePage.bindMultiState(this, retry, offset, orientation)
}


fun Fragment.initState(
    offset: Int = 0,
    orientation: Int = Gravity.BOTTOM,
    retry: () -> Unit = {}
): MultiStateContainer? {
    return if (this.view == null) {
        null
    } else view!!.initState(offset, orientation, retry)
}

fun createState(
    contentView: View,
    retry: () -> Unit,
    offset: Int,
    orientation: Int
): MultiStateContainer {
    return MultiStatePage.bindMultiState(contentView, retry, offset, orientation)
}