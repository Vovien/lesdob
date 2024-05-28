package com.jmbon.middleware.model


import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.apkdv.mvvmfast.base.BaseViewModel

import com.jmbon.middleware.api.API

class CheckErrorViewModel : BaseViewModel() {

    fun errorCorrection(
        type: Int,
        images: String,
        description: String,
        email: String, contentType: Int,
        itemId: Int,
    ) = launchWithFlow({
        API.errorCorrection(type, images, description, email, contentType, itemId)
    }, handleError = false)

    val textSize = MutableLiveData<Int>()


    val textWatch by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                textSize.postValue(s?.trim()?.length)
            }

        }
    }
}