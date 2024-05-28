package com.jmbon.widget.Shadow;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author lengyue
 * @date 2021-05-17 17:37:19
 */
public class ShadowResHelper {
    private static TypedValue sTmpValue;

    public static float getAttrFloatValue(Context context, int attr) {
        return getAttrFloatValue(context.getTheme(), attr);
    }

    public static float getAttrFloatValue(Resources.Theme theme, int attr) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return 0;
        }
        return sTmpValue.getFloat();
    }

    public static int getAttrColor(Resources.Theme theme, int attr) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!theme.resolveAttribute(attr, sTmpValue, true)) {
            return 0;
        }
        if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
            return getAttrColor(theme, sTmpValue.data);
        }
        return sTmpValue.data;
    }

    public static int getAttrDimen(Context context, int attrRes) {
        if (sTmpValue == null) {
            sTmpValue = new TypedValue();
        }
        if (!context.getTheme().resolveAttribute(attrRes, sTmpValue, true)) {
            return 0;
        }
        return TypedValue.complexToDimensionPixelSize(sTmpValue.data, getDisplayMetrics(context));
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
