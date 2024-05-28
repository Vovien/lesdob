package com.tubewiki.mine.adapter.delegate

import android.content.Context
import android.view.View
import com.drakeet.multitype.ViewDelegate
import com.tubewiki.mine.R

class DivideDelegate : ViewDelegate<Int, View>() {
    override fun onBindView(view: View, item: Int) {
        view.setBackgroundResource(R.color.ColorF5F5)
    }

    override fun onCreateView(context: Context): View {
        return View(context)
    }
}