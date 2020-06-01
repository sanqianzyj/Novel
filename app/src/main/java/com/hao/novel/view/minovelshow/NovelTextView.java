package com.hao.novel.view.minovelshow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.hao.lib.Util.SystemUtils;
import com.hao.novel.R;

import java.util.ArrayList;
import java.util.List;

public class NovelTextView extends AppCompatTextView {
    private Context context;
    NovelPageInfo novelPageInfo;
    private List<String> textArray = new ArrayList<>();//每一行数据为一个元素  以行数据为单位
    MiTextViewConfig miTextViewConfig = MiTextViewConfig.getDefoutConfig();

    public NovelTextView(Context context) {
        this(context, null);
    }


    public NovelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NovelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void init(MiTextViewConfig miTextViewConfig) {
        this.miTextViewConfig = miTextViewConfig;
        invalidate();
    }


    public void setText(List<String> text, int start, int end) {
        textArray = text.subList(start, end);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(miTextViewConfig.textSize);
        Log.i("显示", "文字大小：" + miTextViewConfig.textSize);
        paint.setFakeBoldText(false);
        paint.setTypeface(miTextViewConfig.typeface);
        paint.setColor(miTextViewConfig.textColor);
        int color = ContextCompat.getColor(context, R.color.black);
        paint.setColor(color);
        if (!miTextViewConfig.orientationVer) {
            for (int i = 0; i < textArray.size(); i++) {
                float drawTextY = miTextViewConfig.offsetVar + (i % miTextViewConfig.lineNum) * (miTextViewConfig.textSize + miTextViewConfig.lineSpacingExtra) + miTextViewConfig.textSize;//间距的数量比文字行数少一行
                for (int j = 0; j < textArray.get(i).length(); j++) {
                    float drawTextX = miTextViewConfig.offsetHor + (miTextViewConfig.textSize + miTextViewConfig.wordSpacingExtra) * j + (int) (i / miTextViewConfig.lineNum) * miTextViewConfig.viewWidth;
                    canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                }
            }
        } else {
            for (int i = 0; i < textArray.size(); i++) {
                float drawTextY = miTextViewConfig.offsetVar + i * (miTextViewConfig.textSize + miTextViewConfig.lineSpacingExtra) + miTextViewConfig.textSize;//间距的数量比文字行数少一行
                for (int j = 0; j < textArray.get(i).length(); j++) {
                    float drawTextX = miTextViewConfig.offsetHor + (miTextViewConfig.textSize + miTextViewConfig.wordSpacingExtra) * j;
                    canvas.drawText(textArray.get(i).substring(j, j + 1), drawTextX, drawTextY, paint);
                }
            }
        }
    }

    public MiTextViewConfig getMiTextViewConfig() {
        return miTextViewConfig;
    }


    public void setDate(NovelPageInfo novelPageInfo) {
        this.novelPageInfo = novelPageInfo;
        textArray = novelPageInfo.pagecontent;
        String str = "";
        for (int i = 0; i < textArray.size(); i++) {
            str += textArray.get(i);
        }
        Log.i("流程", "当前页数据：" + str);
    }

    public NovelPageInfo getNovelPageInfo() {
        return novelPageInfo;
    }
}
