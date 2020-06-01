package com.hao.lib.Util;

import android.content.Context;
import android.graphics.Typeface;

import com.hao.lib.base.MI2App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TypeFaceUtils {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    private static List<TypeFaceInfo> typeFaceInfoList = new ArrayList<TypeFaceInfo>() {
        @Override
        public TypeFaceInfo get(int index) {
            TypeFaceInfo typeFaceInfo = super.get(index);
            typeFaceInfo.setTypeface(getTypeface(typeFaceInfo.typeFaceFileName, MI2App.getInstance()));
            return typeFaceInfo;
        }
    };
    private static TypeFaceUtils typeFaceUtils = new TypeFaceUtils();

    private TypeFaceUtils() {
        getTypeFaces();
    }

    public static List<TypeFaceInfo> getTypeFaceInfoList() {
        synchronized (typeFaceInfoList) {
            if (typeFaceUtils == null) {
                typeFaceUtils = new TypeFaceUtils();
            }
        }
        return typeFaceInfoList;
    }


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

    private void getTypeFaces() {
        typeFaceInfoList.add(new TypeFaceInfo("msyh.ttf", "雅黑字体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZBWKSFW.TTF", "方正北魏楷书繁体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZBWKSFW.TTF", "方正北魏楷书简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZCQJW.TTF", "方正粗倩简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZCSJW.TTF", "方正粗宋简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZDHTFW.TTF", "方正大黑繁体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZJZFW.TTF", "方正剪纸繁体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZJZJW.TTF", "方正剪纸简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZLSFW.TTF", "方正隶书繁体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZPTYJW.TTF", "方正胖头鱼"));
        typeFaceInfoList.add(new TypeFaceInfo("FZSHFW.TTF", "方正宋黑繁体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZTJLSFW.TTF", "方正铁筋隶书繁体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZTJLSJW.TTF", "方正铁筋隶书简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZXIANGLFW.TTF", "方正祥隶繁体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZXIANGLJW.TTF", "方正祥隶简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZY4JW.TTF", "方正粗圆简体"));
        typeFaceInfoList.add(new TypeFaceInfo("HWCY.TTF", "华文彩云"));
        typeFaceInfoList.add(new TypeFaceInfo("simfang.ttf", "仿宋"));
        typeFaceInfoList.add(new TypeFaceInfo("simhei.ttf", "黑体"));
        typeFaceInfoList.add(new TypeFaceInfo("simsun.ttc", "新宋体"));
        typeFaceInfoList.add(new TypeFaceInfo("TZT.ttf", "天真体"));
        typeFaceInfoList.add(new TypeFaceInfo("STFWXQGBXS.ttf", "书体坊王学勤钢笔行书"));
        typeFaceInfoList.add(new TypeFaceInfo("WRJZY.ttf", "微软简中圆"));
        typeFaceInfoList.add(new TypeFaceInfo("MZDZT.ttf", "毛泽东字体"));
        typeFaceInfoList.add(new TypeFaceInfo("HYZKJ.ttf", "汉仪中楷简"));
        typeFaceInfoList.add(new TypeFaceInfo("HYCYJ.ttf", "汉仪粗圆简"));
        typeFaceInfoList.add(new TypeFaceInfo("HYDYTJ.ttf", "汉仪蝶语体简"));
        typeFaceInfoList.add(new TypeFaceInfo("HYCCYJ.ttf", "汉仪超粗圆简"));
        typeFaceInfoList.add(new TypeFaceInfo("HYXJTJ.ttf", "汉仪雪君体简"));
        typeFaceInfoList.add(new TypeFaceInfo("HYXJTF.ttf", "汉仪雪君体繁"));
        typeFaceInfoList.add(new TypeFaceInfo("HYYKF.ttf", "汉仪颜楷繁"));
        typeFaceInfoList.add(new TypeFaceInfo("HDJXK.TTF", "汉鼎简行楷"));
        typeFaceInfoList.add(new TypeFaceInfo("ZC.ttf", "章草"));
        typeFaceInfoList.add(new TypeFaceInfo("JQT.TTF", "简启体"));
        typeFaceInfoList.add(new TypeFaceInfo("MPZT.ttf", "米芾字体"));
        typeFaceInfoList.add(new TypeFaceInfo("FQT.TTF", "繁启体"));
        typeFaceInfoList.add(new TypeFaceInfo("JDFXS.TTF", "经典繁行书"));
        typeFaceInfoList.add(new TypeFaceInfo("putty.ttf", "99putty"));
        typeFaceInfoList.add(new TypeFaceInfo("XHLT.ttf", "小狐狸"));
        typeFaceInfoList.add(new TypeFaceInfo("PZHXS.ttf", "庞中华行书"));
        typeFaceInfoList.add(new TypeFaceInfo("FZYMJT.ttf", "方正有猫在简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZQKBYSJT.TTF", "方正清刻本悦宋简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZYBXS.ttf", "方正硬笔行书"));
        typeFaceInfoList.add(new TypeFaceInfo("FZPWJT.ttf", "方正胖娃简体"));
        typeFaceInfoList.add(new TypeFaceInfo("FZSEYVBT.ttf", "方正莎儿硬笔体"));
        typeFaceInfoList.add(new TypeFaceInfo("HYJBRYJT.ttf", "汉仪井柏然简体"));
        typeFaceInfoList.add(new TypeFaceInfo("HYWWZJ.ttf", "汉仪娃娃篆简"));
        typeFaceInfoList.add(new TypeFaceInfo("SXSLK.ttf", "苏新诗柳楷"));
    }

    public class TypeFaceInfo {
        Typeface typeface;
        String typeFacename;
        String typeFaceFileName;

        public TypeFaceInfo(String typeFaceFileName, String typeFacename) {
            this.typeFaceFileName = typeFaceFileName;
            this.typeFacename = typeFacename;
        }

        public Typeface getTypeface() {
            return typeface;
        }

        public void setTypeface(Typeface typeface) {
            this.typeface = typeface;
        }

        public String getTypeFacename() {
            return typeFacename;
        }

        public void setTypeFacename(String typeFacename) {
            this.typeFacename = typeFacename;
        }
    }

}
