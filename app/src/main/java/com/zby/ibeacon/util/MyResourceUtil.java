package com.zby.ibeacon.util;

import android.content.Context;

/**
 * @author Administrator
 * 通过名字反射获得资源id
 */
public class MyResourceUtil {

    /**
     * 反射获取layout.id
     * @param paramContext
     * @param paramString
     * @return 未找到元素返回0
     */
    public static int getLayoutId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "layout",
                paramContext.getPackageName());
    }

    /**
     * 反射获取String.id
     * @param paramContext
     * @param paramString
     * @return 未找到元素返回0
     */
    public static int getStringId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "string",
                paramContext.getPackageName());
    }

    /**
     * 反射获取Drawable.id
     * @param paramContext
     * @param paramString
     * @return 未找到元素返回0
     */
    public static int getDrawableId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "drawable", paramContext.getPackageName());
    }

    /**
     * 反射获取Style.id
     * @param paramContext
     * @param paramString
     * @return 未找到元素返回0
     */
    public static int getStyleId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "style", paramContext.getPackageName());
    }

    /**
     * 反射获取控件id
     * @param paramContext
     * @param paramString
     * @return 未找到元素返回0
     */
    public static int getId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,"id", paramContext.getPackageName());
    }

    /**
     * 反射获取Color.id
     * @param paramContext
     * @param paramString
     * @return 未找到元素返回0
     */
    public static int getColorId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "color", paramContext.getPackageName());
    }

    /**
     * 反射获取Array.id
     * @param paramContext
     * @param paramString
     * @return 未找到元素返回0
     */
    public static int getArrayId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "array", paramContext.getPackageName());
    }
}