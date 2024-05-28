package com.apkdv.mvvmfast.utils;

import android.text.InputFilter;
import android.text.Spanned;

/***
 * 输入中文
 */
public class ChineseFilter implements InputFilter {
    public ChineseFilter() {
        super();
    }
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (!isChinese(source.charAt(i))) {
                return "";
            }
        }
        return null;
    }


    /**
     * 判定输入汉字
     *
     * @param c char
     * @return true
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }
}
