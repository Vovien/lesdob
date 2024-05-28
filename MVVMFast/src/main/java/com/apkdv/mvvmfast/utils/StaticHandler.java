package com.apkdv.mvvmfast.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class StaticHandler<T> extends Handler {

    protected WeakReference<T> ref;

    public StaticHandler(T t) {
        super();
        ref = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        final T t = ref.get();
        if (t == null) {
            return;
        }
        handleMessage(msg, t);
    }

    protected void handleMessage(Message msg, T t) {

    }

}