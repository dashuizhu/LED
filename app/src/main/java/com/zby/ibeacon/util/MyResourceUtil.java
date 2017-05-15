package com.zby.ibeacon.util;

import android.content.Context;

/**
 * @author Administrator
 * ͨ�����ַ�������Դid
 */
public class MyResourceUtil {

    /**
     * �����ȡlayout.id
     * @param paramContext
     * @param paramString
     * @return δ�ҵ�Ԫ�ط���0
     */
    public static int getLayoutId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "layout",
                paramContext.getPackageName());
    }

    /**
     * �����ȡString.id
     * @param paramContext
     * @param paramString
     * @return δ�ҵ�Ԫ�ط���0
     */
    public static int getStringId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "string",
                paramContext.getPackageName());
    }

    /**
     * �����ȡDrawable.id
     * @param paramContext
     * @param paramString
     * @return δ�ҵ�Ԫ�ط���0
     */
    public static int getDrawableId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "drawable", paramContext.getPackageName());
    }

    /**
     * �����ȡStyle.id
     * @param paramContext
     * @param paramString
     * @return δ�ҵ�Ԫ�ط���0
     */
    public static int getStyleId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "style", paramContext.getPackageName());
    }

    /**
     * �����ȡ�ؼ�id
     * @param paramContext
     * @param paramString
     * @return δ�ҵ�Ԫ�ط���0
     */
    public static int getId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,"id", paramContext.getPackageName());
    }

    /**
     * �����ȡColor.id
     * @param paramContext
     * @param paramString
     * @return δ�ҵ�Ԫ�ط���0
     */
    public static int getColorId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "color", paramContext.getPackageName());
    }

    /**
     * �����ȡArray.id
     * @param paramContext
     * @param paramString
     * @return δ�ҵ�Ԫ�ط���0
     */
    public static int getArrayId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "array", paramContext.getPackageName());
    }
}