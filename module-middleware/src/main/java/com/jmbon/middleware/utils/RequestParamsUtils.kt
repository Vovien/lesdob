package com.jmbon.middleware.utils

import java.security.MessageDigest

object RequestParamsUtils {
    fun getRandom(): String {
        return (100 until 999).random().toString()
    }
    fun getTimeString(): String {
        return "${System.currentTimeMillis()}-${getRandom()}"
    }
    private const val KEY = "jmbon88888888@"

    fun getSign(random: String, time: String): String {
        return MD5(random + time + KEY)
    }

     fun MD5(s: String): String {
        val hexDigits = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
        )
        return try {
            val strTemp = s.toByteArray()
            val mdTemp: MessageDigest = MessageDigest.getInstance("MD5")
            mdTemp.update(strTemp)
            val md: ByteArray = mdTemp.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            md.forEach {
                str[k++] = hexDigits[it.toInt() ushr 4 and 0xf]
                str[k++] = hexDigits[it.toInt() and 0xf]
            }
            String(str)
        } catch (e: Exception) {
            ""
        }
    }
}