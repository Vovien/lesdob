package com.jmbon.middleware.arouter

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.template.IProvider

interface MiniProgramService : IProvider {
    fun startMiniProgram(programId: String, param: Bundle? = null)
    fun startMiniProgram(programId: String, miniType: Int = 1, param: Bundle? = null)
    fun startScanProgram(context: Any)
    fun startRequestLocation(
        context: Context, success: (String, String) -> Unit,
        fail: () -> Unit
    )
}