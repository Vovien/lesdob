package com.jmbon.middleware.utils

import android.text.Html
import com.blankj.utilcode.util.Utils
import java.io.IOException


object HtmlTools {
    //var flJs = getJs("flexible.js")
    var flJs = getJs("android_flexible.js")
    var articleCss = getJs("article.css")
    var scrollTopjs = getJs("getScrollTop.js")

    fun createHtml(content: String): String {

        val spaceRegex = "\\s*|\t|\r|\n"
        val width720Regex = "width: 720px"
        val width360Regex = "width: 360px"
        val html = content.replace(spaceRegex, "")
            .replace(width720Regex, "width: 100%")
            .replace(width360Regex, "width: 50%")
            .replace("width:720px", "width: 100%")
            .replace("width:360px", "width: 50%")
            .replace("border: 2px solid transparent", "")
            .replace("contenteditable=\"false\"", "")
            .replace("<video", "<video  controlslist=\"nodownload noremote footbar nofullscreen\"")
        val builder = StringBuilder()
        builder.append(
            """
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0, user-scalable=0">
    <title>Document</title>
     <script type="text/javascript">     $flJs    </script>
         <style type="text/css">    $articleCss     </style>
</head>
<body><div class='content'>
        """.trimIndent()
        ).append(
            html
        ).append(
            """</div>
                    </body>
                       <script type="text/javascript"> ${scrollTopjs}</script>

                    </html>
                    """.trimIndent()
        )
        return builder.toString()
    }

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    fun delHTMLTag(htmlStr: String): String {
        //先替换，否则依然有换行符
        val html = htmlStr.replace("<p>", "").replace("</p>", "")
        return Html.fromHtml(html).toString()
    }

    private fun getJs(fileName: String): String {
        return try {
            val inputStream = Utils.getApp().assets.open(fileName)
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            ""
        }
    }

    fun createPrivacyPolicyHtml(content: String): String {
        val spaceRegex = "\\s*|\t|\r|\n"
        val width720Regex = "width: 720px; border: 2px solid transparent;"
        val width360Regex = "width: 360px; border: 2px solid transparent;"
        val html = content.replace(spaceRegex, "")
            .replace(width720Regex, "width: 100%;")
            .replace(width360Regex, "width: 50%;")
            .replace(width360Regex, "width: 50%;")
            .replace("width:720px", "")
            .replace("width:360px", "")
            .replace("border: 2px solid transparent;", "")
            .replace("<video", "<video  controlslist=\"nodownload nofullscreen\"")
        val builder = StringBuilder()
        builder.append(
            """
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0, user-scalable=0">
    <title>Document</title>
</head>
<body><div>
        """.trimIndent()
        ).append(
            html
        ).append(
            """</div>
                    </body>
                       <script type="text/javascript"> ${getJs("flexible.js")}</script>
                       <style type="text/css">${getJs("article.css")} </style>
                       <script type="text/javascript"> ${getJs("getScrollTop.js")}</script>
                    </html>
                    """.trimIndent()
        )
        return builder.toString()
    }

}