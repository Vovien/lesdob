package com.jmbon.middleware.utils

import com.blankj.utilcode.util.Utils
import java.io.IOException


object ArticleHtmlTools {
    var initJs = getJs("init.js")
    var advJs = getJs("set_ad.js")
    var swiperMinCss = getJs("swiper/swiper.min.css")
    var swiperMinJs = getJs("swiper/swiper.min.js")
    var setAdJs = getJs("set_article_ad.js")
    var article1Css = getJs("article1.css")
    var article2Css = getJs("article2.css")
    // var scrollTopjs = getJs("getScrollTop.js")

    fun createHtmlType1(content: String): String {

        val width720Regex = "width: 720px"
        val width360Regex = "width: 360px"
        val html = content.replace("\n", "")
            .replace("\r", "")
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
     <script type="text/javascript">     $initJs    </script>
         <script type="text/javascript">   $advJs    </script>
         <style type="text/css">    $article1Css     </style>
</head>
<body><div class='content'>
        """.trimIndent()
        ).append(
            html
        )
            .append(

                """</div>
                    </body>

                    </html>
                    """.trimIndent()
            )
        //           .append(
//            """</div>
//                    </body>
//                       <script type="text/javascript"> ${scrollTopjs}</script>
//
//                    </html>
//                    """.trimIndent()
//        )
        return builder.toString()
    }

    fun createHtmlType2(content: String): String {

        val spaceRegex = "\\s*|\t|\r|\n"
        val width720Regex = "width: 720px"
        val width360Regex = "width: 360px"
        val html = content.replace("\n", "")
            .replace("\r", "")
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
     <script type="text/javascript">     $initJs    </script>
              <style type="text/css">    $swiperMinCss     </style>  
         <style type="text/css">    $article2Css     </style>
</head>
<body><div class='content'>
        """.trimIndent()
        ).append(
            html
        )
            .append(
                """</div>
                    </body>                       
                    <script type="text/javascript"> ${setAdJs}</script>
                    <script type="text/javascript"> ${swiperMinJs}</script>
                       
                    </html>
                    """.trimIndent()
            )
        //           .append(
//            """</div>
//                    </body>
//                       <script type="text/javascript"> ${scrollTopjs}</script>
//
//                    </html>
//                    """.trimIndent()
//        )
        return builder.toString()
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


}