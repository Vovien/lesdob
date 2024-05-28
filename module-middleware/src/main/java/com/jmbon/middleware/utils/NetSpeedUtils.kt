package com.jmbon.middleware.utils

import android.util.Log
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.AppUtils
import java.util.*

/**
 * @author : leimg
 * time   : 2021/12/1
 * desc   : 网络测速类
 * version: 1.0
 */
object NetSpeedUtils {
    /**
     * count:测速时间 (单位s)
     */
    fun startMeasureSpeed(count: Int) {
        //小于1秒不能测出速度
        if (count <= 1) {
            return
        }
        var speed = NetSpeed()
        var timer = Timer()
        var averageSpeed = 0L
        var countTotal = count

        timer.schedule(object : TimerTask() {
            override fun run() {
                countTotal--
                val data = speed.getNetSpeedKb(AppUtils.getAppUid())
                averageSpeed += data
                // Log.e("TAg", "网络速度:${data} kb/s")
                if (countTotal == 0) {
                    timer.cancel()
                    val speed = averageSpeed / (count - 1)
                    Log.i("speed", "网络测速平均速度:${speed} kb/s")
                    if (speed < 200) {
                        "当前网络较差".showToast()
                    }
                }
            }
        }, 0, 1000)

    }

}