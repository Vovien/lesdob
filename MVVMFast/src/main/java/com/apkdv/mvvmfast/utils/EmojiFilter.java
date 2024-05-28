package com.apkdv.mvvmfast.utils;

import android.text.InputFilter;
import android.text.Spanned;

/***
 * 过滤Emoji表情
 */
public class EmojiFilter implements InputFilter {

    public EmojiFilter() {
        super();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int index = start; index < end; index++) {
            int type = Character.getType(source.charAt(index));
            if (type == Character.SURROGATE) {
                return "";
            }
        }
        return null;
    }

}
