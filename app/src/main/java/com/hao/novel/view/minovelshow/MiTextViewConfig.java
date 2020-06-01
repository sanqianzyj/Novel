package com.hao.novel.view.minovelshow;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.spider.data.NovelChapter;

import java.util.ArrayList;
import java.util.List;

public class MiTextViewConfig {
    float lineSpacingExtra;//行间距
    float wordSpacingExtra;//字间距
    float textSize;//文字大小
    int lineTextNum;//每行字数
    int lineNum;//容纳的行数
    float textPadingVar = 0;//垂直方向的间隔距离
    float textPadingHor = 0;//横向方向的间隔距离
    float textPadingleft = 0;//文字距离左边距离
    float textPadingright = 0;//文字距离右边距离
    float textPadingtop = 0;//文字距离上边距离
    float textPadingbottom = 0;//文字距离下边距离
    float offsetHor = 0;//画布横向方向偏移量
    float offsetVar = 0;//画布垂直方向偏移量
    boolean orientationVer = false;//是否为横屏
    float viewhigh = -1;  //view的垂直高度
    float viewWidth = -1;//view的横向高度
    int textColor;
    Typeface typeface;

    private MiTextViewConfig() {
    }

    static MiTextViewConfig miTextViewConfig;

    public static MiTextViewConfig getDefoutConfig() {
        if (miTextViewConfig == null) {
            miTextViewConfig = new MiTextViewConfig();
            miTextViewConfig.textColor = 0xFFFFFF;
            miTextViewConfig.textPadingVar = 0;
            miTextViewConfig.textPadingHor = 0;
            miTextViewConfig.textPadingleft = 0;
            miTextViewConfig.textPadingright = 0;
            miTextViewConfig.textPadingtop = 0;
            miTextViewConfig.textPadingbottom = 0;
            miTextViewConfig.textSize = 70;
            miTextViewConfig.typeface = Typeface.DEFAULT;
            miTextViewConfig.orientationVer = false;
        }
        return miTextViewConfig;
    }

    public static MiTextViewConfig getDefoutConfig(float viewWidth, float viewhigh) {
        if (miTextViewConfig == null) {
            miTextViewConfig = new MiTextViewConfig();
            miTextViewConfig.textColor = 0xFFFFFF;
            miTextViewConfig.textPadingVar = 0;
            miTextViewConfig.textPadingHor = 0;
            miTextViewConfig.textPadingleft = 0;
            miTextViewConfig.textPadingright = 0;
            miTextViewConfig.textPadingtop = 0;
            miTextViewConfig.textPadingbottom = 0;
            miTextViewConfig.textSize = 70;
            miTextViewConfig.viewWidth = viewWidth;
            miTextViewConfig.viewhigh = viewhigh;
            miTextViewConfig.typeface = Typeface.DEFAULT;
            miTextViewConfig.orientationVer = false;
        } else {
            miTextViewConfig.viewWidth = viewWidth;
            miTextViewConfig.viewhigh = viewhigh;
        }
        if (viewhigh != 0 && viewWidth != 0) {
            return miTextViewConfig.initViewConfig();
        }
        return miTextViewConfig;
    }

    private MiTextViewConfig initViewConfig() {
        if (viewhigh == 0 || viewWidth == 0) {
            throw new NullPointerException("填充的界面参数中宽高为0");
        }
        //绘制文字空间的垂直方向的大小
        float textContentVar = viewhigh - textPadingtop - textPadingbottom;
        //绘制文字空间的横向方向的大小
        float textContentHor = viewWidth - textPadingleft - textPadingright;
        //计算的每行容纳的文字大小
        lineTextNum = (int) (textContentHor / (textSize + wordSpacingExtra));
        //计算没页容纳文字行数
        lineNum = (int) (textContentVar / (textSize + lineSpacingExtra));

        //计算出去边缘距离和文字占用的位置剩余的位置 并计算出每页文字的位置
        //文本垂直方向距离边缘的位置  通过计算一行被填满时所占用的位置，算出空出的位置长度,主要用于文本居中处理
        textPadingVar = (textContentVar - lineNum * (textSize + lineSpacingExtra)) / 2;
        offsetVar = textPadingVar + textPadingtop;
        //文本水平方向距离边缘的位置  通过计算一行被填满时所占用的位置，算出空出的位置长度,主要用于文本居中处理
        textPadingHor = (textContentHor - lineTextNum * (textSize + wordSpacingExtra)) / 2;
        offsetHor = textPadingHor + textPadingleft;
        return this;
    }


    public List<View> initText(NovelChapter novelChapter) {
        List<NovelPageInfo> pageDate = new ArrayList<>();
        List<String> textArray = new ArrayList<>();
        String[] strs = novelChapter.getChapterContent().split("\n");
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].length() <= miTextViewConfig.lineTextNum) {
                textArray.add(strs[i]);
                continue;
            }
            if (strs[i].length() % miTextViewConfig.lineTextNum == 0) {//刚好整除  此段文字刚好后被整数行容纳
                for (int j = 0; j < strs[i].length() / miTextViewConfig.lineTextNum; j++) {
                    textArray.add(strs[i].substring(j * miTextViewConfig.lineTextNum, (j + 1) * miTextViewConfig.lineTextNum));
                }
            } else {
                int needLineNum = strs[i].length() / miTextViewConfig.lineTextNum + 1;
                for (int k = 0; k < needLineNum; k++) {//获取到每段的字符串 判断能够容纳几行
                    String nowLineText = "";
                    if (k < needLineNum - 1) {
                        nowLineText = strs[i].substring(k * miTextViewConfig.lineTextNum, (k + 1) * miTextViewConfig.lineTextNum);
                    } else {
                        nowLineText = strs[i].substring(k * miTextViewConfig.lineTextNum, strs[i].length());
                    }
                    textArray.add(nowLineText);
                }
            }
        }
        if (miTextViewConfig.lineNum != 0) {
            NovelPageInfo novelPageInfo = new NovelPageInfo();
            novelPageInfo.novelCId = novelChapter.getCid();
            novelPageInfo.novelNId = novelChapter.getNid();
            novelPageInfo.setPage(0);
            for (int i = 0; i < textArray.size(); i++) {
                if (i % miTextViewConfig.lineNum == 0) {
                    if (novelPageInfo.pagecontent != null) {
                        novelPageInfo.addContent(textArray.get(i));
                        novelPageInfo.setPage(i / miTextViewConfig.lineNum);
                        pageDate.add(novelPageInfo);
                    }
                    novelPageInfo = new NovelPageInfo();
                } else {
                    novelPageInfo.addContent(textArray.get(i));
                    novelPageInfo.novelCId = novelChapter.getCid();
                    novelPageInfo.novelNId = novelChapter.getNid();
                    novelPageInfo.setPage(i / miTextViewConfig.lineNum);
                }
            }
            if (novelPageInfo.pagecontent != null && novelPageInfo.pagecontent.size() != 0) {
                pageDate.add(novelPageInfo);
            }
        }

        List<View> novelTextViews = new ArrayList<>();
        for (int i = 0; i < pageDate.size(); i++) {
            View v = LayoutInflater.from(App.getInstance()).inflate(R.layout.novel_content_layout, null);
            NovelTextView miTextView = v.findViewById(R.id.novel_content);
            TextView novel_title = v.findViewById(R.id.novel_title);
            novel_title.setText(novelChapter.getChapterName());
            miTextView.init(this);
            miTextView.setDate(pageDate.get(i));
            v.setBackgroundResource(R.mipmap.bg_readbook_yellow);
            novelTextViews.add(v);
        }
        return novelTextViews;
    }


    public MiTextViewConfig setViewWiHi(float viewWidth, float viewhigh) {
        this.viewWidth = viewWidth;
        this.viewhigh = viewhigh;
        return getDefoutConfig(viewWidth, viewhigh);
    }


    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public float getTextPadingVar() {
        return textPadingVar;
    }

    public void setTextPadingVar(float textPadingVar) {
        this.textPadingVar = textPadingVar;
    }

    public float getTextPadingHor() {
        return textPadingHor;
    }

    public void setTextPadingHor(float textPadingHor) {
        this.textPadingHor = textPadingHor;
    }

    public float getTextPadingleft() {
        return textPadingleft;
    }

    public void setTextPadingleft(float textPadingleft) {
        this.textPadingleft = textPadingleft;
    }

    public float getTextPadingright() {
        return textPadingright;
    }

    public void setTextPadingright(float textPadingright) {
        this.textPadingright = textPadingright;
    }

    public float getTextPadingtop() {
        return textPadingtop;
    }

    public void setTextPadingtop(float textPadingtop) {
        this.textPadingtop = textPadingtop;
    }

    public float getTextPadingbottom() {
        return textPadingbottom;
    }

    public void setTextPadingbottom(float textPadingbottom) {
        this.textPadingbottom = textPadingbottom;
    }

    public float getLineSpacingExtra() {
        return lineSpacingExtra;
    }

    public void setLineSpacingExtra(float lineSpacingExtra) {
        this.lineSpacingExtra = lineSpacingExtra;
        initViewConfig();
    }

    public float getWordSpacingExtra() {
        return wordSpacingExtra;
    }

    public void setWordSpacingExtra(float wordSpacingExtra) {
        this.wordSpacingExtra = wordSpacingExtra;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        initViewConfig();
    }

    public int getLineTextNum() {
        return lineTextNum;
    }

    public void setLineTextNum(int lineTextNum) {
        this.lineTextNum = lineTextNum;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public float getOffsetHor() {
        return offsetHor;
    }

    public void setOffsetHor(float offsetHor) {
        this.offsetHor = offsetHor;
    }

    public float getOffsetVar() {
        return offsetVar;
    }

    public void setOffsetVar(float offsetVar) {
        this.offsetVar = offsetVar;
    }

    public float getViewhigh() {
        return viewhigh;
    }

    public void setViewhigh(float viewhigh) {
        this.viewhigh = viewhigh;
        initViewConfig();
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(float viewWidth) {
        this.viewWidth = viewWidth;
        initViewConfig();
    }
}
