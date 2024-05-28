package com.apkdv.mvvmfast.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 输入中文
 */
public class ChineseLettersFilter implements InputFilter {
    public ChineseLettersFilter() {
        super();
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Pattern p = Pattern.compile("[a-zA-Z|\u4e00-\u9fa5]+");
        Matcher m = p.matcher(source.toString());
        if (!m.matches()) return "";
        return null;
    }
}
