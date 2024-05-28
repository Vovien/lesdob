package com.apkdv.mvvmfast.ktx

import android.text.TextUtils
import com.hjq.toast.ToastUtils


fun Int.showToast() {
//    ToastUtils2.getDefaultMaker().setGravity(Gravity.CENTER,0,0).show(this)
    ToastUtils.show(this)
}

fun CharSequence?.showToast() {
    this?.let { ToastUtils.show(this) }
}

private fun show(showData: CharSequence) {
    if (TextUtils.isEmpty(showData)) {
        return
    }
}
