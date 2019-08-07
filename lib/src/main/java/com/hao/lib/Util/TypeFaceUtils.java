package com.hao.lib.Util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

public class TypeFaceUtils {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    private static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(fontname, typeface);
        }
        return typeface;
    }


    //楷体
    public static Typeface getKT(Context context) {
        return getTypeface("KT.ttf", context);
    }

    //方正粗圆
    public static Typeface getFZHCY(Context context) {
        return getTypeface("FZHCY.ttf", context);
    }

    //方正古隶
    public static Typeface getFZHGL(Context context) {
        return getTypeface("FZHGL.ttf", context);
    }

    //方正华隶
    public static Typeface getFZHHL(Context context) {
        return getTypeface("FZHHL.ttf", context);
    }

    //方正静蕾简体
    public static Typeface getFZHJLJT(Context context) { return getTypeface("FZHJLJT.TTF", context); }

    //方正卡通简体
    public static Typeface getFZHKTYT(Context context) {
        return getTypeface("FZHKTYT.ttf", context);
    }

    //方正流行体简体
    public static Typeface getFZHLXTJT(Context context) {
        return getTypeface("FZHLXTJT.ttf", context);
    }

    //方正胖头鱼
    public static Typeface getFZHPTY(Context context) {
        return getTypeface("FZHPTY.TTF", context);
    }

    //方正启体简体
    public static Typeface getFZHQTJT(Context context) {
        return getTypeface("FZHQTJT.ttf", context);
    }

    //方正小篆
    public static Typeface getFZHXZH(Context context) {
        return getTypeface("FZHXZH.TTF", context);
    }

    //方正硬笔行书
    public static Typeface getFZHYBXS(Context context) {
        return getTypeface("FFZHYBXS.TTF", context);
    }

    //方正正园|方正准园
    public static Typeface getFZHZHY(Context context) {
        return getTypeface("FZHZHY.ttf", context);
    }

    //方正正园|方正准园
    public static Typeface getHZZHY(Context context) {
        return getTypeface("HZZHY.TTF", context);
    }

    //华康少女字体
    public static Typeface getHKSVZT(Context context) {
        return getTypeface("HKSVZT.ttf", context);
    }

    //华康娃娃体
    public static Typeface getHKWWT(Context context) { return getTypeface("HKWWT.TTF", context); }

    //华康中黑字体
    public static Typeface getHKZHZT(Context context) {
        return getTypeface("HKZHZT.TTF", context);
    }

    //华文彩云
    public static Typeface getHWCY(Context context) { return getTypeface("HWCY.TTF", context); }

    //华文行楷
    public static Typeface getHWXK(Context context) {
        return getTypeface("HWXK.ttf", context);
    }

    //华文新宋
    public static Typeface getHWXS(Context context) {
        return getTypeface("HWXS.ttf", context);
    }

    //华文新魏
    public static Typeface getHWXW(Context context) {
        return getTypeface("HWXW.TTF", context);
    }
}
