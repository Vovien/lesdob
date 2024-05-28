package com.apkdv.mvvmfast.utils;

import com.apkdv.mvvmfast.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;


public class GsonUtil {

    private static Gson gson;

    public static synchronized Gson gson() {
        if (gson == null) {
            gson = create();
        }
        return gson;
    }

    private static Gson create() {
        return GsonFactory.getSingletonGson();
    }


    /**
     * Return the type of {@link List} with the {@code type}.
     *
     * @param type The type.
     * @return the type of {@link List} with the {@code type}
     */
    public static Type getListType(final Type type) {
        return TypeToken.getParameterized(List.class, type).getType();
    }

    /**
     * @param keyCls key type
     * @param valCls value type
     * @return the type of {@link HashMap} with the {@code type}
     */
    public static Type getHashMapType(Type keyCls, Type valCls) {
        return TypeToken.getParameterized(HashMap.class, keyCls, valCls).getType();
    }

    public static <T> T fromJson(final String json, final Type type) {
        return gson.fromJson(json, type);
    }
}