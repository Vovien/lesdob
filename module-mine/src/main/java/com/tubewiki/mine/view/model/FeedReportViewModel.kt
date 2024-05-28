package com.tubewiki.mine.view.model

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData

import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.base.MineViewModel

class FeedReportViewModel : MineViewModel() {

    fun getContentTortInfo(
        id: Int
    ) = launchWithFlow({
        MineApi.getContentTortInfo(id)
    }, handleError = false)

    fun contentTortFeedback(
        type: Int, feedback: String, contentId: Int
    ) = launchWithFlow({
        MineApi.contentTortFeedback(type, feedback, contentId)
    }, handleError = false)


    fun getContentTortFeedback(
        id: Int,
    ) = launchWithFlow({
        MineApi.getContentTortFeedback(id)
    }, handleError = false)

    fun getOfficialEmail(
    ) = launchWithFlow({
        MineApi.getOfficialEmail()
    }, handleError = false)

    fun getExamineDetail(
        id: Int, type: Int
    ) = launchWithFlow({
        MineApi.getExamineDetail(id, type)
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