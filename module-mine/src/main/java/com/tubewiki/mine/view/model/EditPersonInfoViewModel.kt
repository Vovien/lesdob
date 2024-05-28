package com.tubewiki.mine.view.model

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.apkdv.mvvmfast.base.BaseViewModel

class EditPersonInfoViewModel : BaseViewModel() {

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