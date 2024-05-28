package com.jmbon.middleware.utils

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * @author : leimg
 * time   : 2021/4/29
 * desc   :
 * version: 1.0
 */
object AssetsFileUtils {
    /**
     * 加载本地assets文件为字符串
     */
    fun loadLocalFile( context: Context, fileName: String): String {
        var jsStr = ""
        try {
            val input = context.assets.open(fileName)
            val buff = ByteArray(1024)
            val fromFile = ByteArrayOutputStream()
            do {
                val numRead = input.read(buff)
                if (numRead <= 0) {
                    break
                }
                fromFile.write(buff, 0, numRead)
            } while (true)
            jsStr = fromFile.toString()
            input.close()
            fromFile.close()
            return jsStr
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return jsStr

    }
}