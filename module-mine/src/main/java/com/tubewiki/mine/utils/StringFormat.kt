package com.tubewiki.mine.utils

object StringFormat {
    /**
     * 将每三个数字（或字符）加上逗号处理（通常使用金额方面的编辑）
     * 5000000.00 --> 5,000,000.00
     * 20000000 --> 20,000,000
     * @param str  无逗号的数字
     * @return 加上逗号的数字
     */
    fun dataFormat(str: String, keepTwoSize: Boolean = false): String {
        if (str.isEmpty()) {
            return ""
        }
        var addCommaStr: String? = "" // 需要添加逗号的字符串（整数）
        var tmpCommaStr = "" // 小数，等逗号添加完后，最后在末尾补上
        if (str.contains(".")) {
            addCommaStr = str.substring(0, str.indexOf("."))
            tmpCommaStr = str.substring(str.indexOf("."), str.length)
            if (keepTwoSize) {
                if (tmpCommaStr.length in 2..2)
                    tmpCommaStr += "0"
            }
        } else {
            addCommaStr = str
            if (keepTwoSize)
                tmpCommaStr = ".00"
        }
        // 将传进数字反转
        val reverseStr = StringBuilder(addCommaStr).reverse().toString()
        var strTemp = ""
        for (i in reverseStr.indices) {
            if (i * 3 + 3 > reverseStr.length) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length)
                break
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ","
        }
        // 将 "5,000,000," 中最后一个","去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length - 1)
        }
        // 将数字重新反转,并将小数拼接到末尾
        return StringBuilder(strTemp).reverse().toString() + tmpCommaStr
    }

    /**
     * 将加上逗号处理的数字（字符）的逗号去掉 （通常使用金额方面的编辑）
     * 5,000,000.00 --> 5000000.00
     * 20,000,000 --> 20000000
     * @param str  加上逗号的数字（字符）
     * @return 无逗号的数字（字符）
     */
    fun strRemoveComma(str: String?): String {
        return if (str.isNullOrEmpty()) {
            ""
        } else
            str.replace(",".toRegex(), "") // 需要去除逗号的字符串（整数）
    }
}
