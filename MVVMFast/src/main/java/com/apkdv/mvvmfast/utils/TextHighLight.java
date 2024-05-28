package com.apkdv.mvvmfast.utils;

import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.annotation.ColorRes;

import com.apkdv.mvvmfast.R;
import com.blankj.utilcode.util.ColorUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHighLight {

    public static SpannableStringBuilder setStringHighLight(String text, String keyword1, @ColorRes int highLightColor) {
        return setStringHighLight(Html.fromHtml(text).toString(), new String[]{keyword1}, highLightColor, false);
    }

    public static SpannableStringBuilder setStringBoldHighLight(String text, String[] keyword1,
                                                                @ColorRes int highLightColor) {
        return setStringHighLight(Html.fromHtml(text).toString(), keyword1, highLightColor, true);
    }


    public static SpannableStringBuilder setStringHighLight(String text, String[] keyword1, @ColorRes int highLightColor) {
        return setStringHighLight(Html.fromHtml(text).toString(), keyword1, highLightColor, false);
    }

    public static SpannableStringBuilder setStringHighLight(String text, String keyword1) {
        return setStringHighLight(Html.fromHtml(text).toString(), new String[]{keyword1}, R.color.color_currency, false);
    }

    public static SpannableStringBuilder setStringHighLight(String text, String[] keyword1) {
        return setStringHighLight(Html.fromHtml(text).toString(), keyword1, R.color.color_currency, false);
    }

    /**
     * 关键字高亮显示
     *
     * @param text     文字
     * @param keyword1 文字中的关键字数组
     * @return SpannableStringBuilder
     */
    public static SpannableStringBuilder setStringHighLight(String text, String[] keyword1,
                                                            @ColorRes int highLightColor, boolean needBold) {
        String[] keyword = new String[keyword1.length];
        System.arraycopy(keyword1, 0, keyword, 0, keyword1.length);
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);

        String wordReg;
        for (int i = 0; i < keyword.length; i++) {
            StringBuilder key = new StringBuilder();
            //  处理通配符问题
            if (keyword[i].contains("*") || keyword[i].contains("(") || keyword[i].contains(")")) {
                char[] chars = keyword[i].toCharArray();
                for (char aChar : chars) {
                    if (aChar == '*' || aChar == '(' || aChar == ')') {
                        key.append("\\").append(aChar);
                    } else {
                        key.append(aChar);
                    }
                }
                keyword[i] = key.toString();
            }
            if (keyword[i].equals("?")) {
                keyword[i] = "\\?";
            }

            wordReg = "(?i)" + keyword[i];   //忽略字母大小写
            Pattern pattern = Pattern.compile(wordReg);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                spannable.setSpan(new ForegroundColorSpan(ColorUtils.getColor(highLightColor)),
                        matcher.start(), matcher.end(), Spannable.SPAN_MARK_MARK);
                if (needBold) {
                    spannable.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spannable.SPAN_MARK_MARK);
                }
            }
        }
        return spannable;
    }
}
