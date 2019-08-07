package com.hao.lib.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.hao.lib.R;


//边框 变换的buttom
public class CheekChangeButton extends View {
    Context context;
    private int borderWidth; // 边框宽度
    private int borderColor = Color.WHITE; // 边框颜色
    private int innerBorderWidth; // 内层边框宽度
    private int innerBorderColor = Color.WHITE; // 内层边框充色
    private int defoultBorderColor = Color.WHITE; // 内层边框充色
    private int cornerBackground; //背景
    private boolean isCheck;
    private boolean animalInLeft = true;

    private int cornerRadius; // 边框的圆角半径
    private int cornerTopLeftRadius; // 左上角圆角半径
    private int cornerTopRightRadius; // 右上角圆角半径
    private int cornerBottomLeftRadius; // 左下角圆角半径
    private int cornerBottomRightRadius; // 右下角圆角半径
    private int ovalOffsetTopRight; // 画圆时需要偏移的长度 主要用于衔接直线
    private int ovalOffsetTopLeft; // 画圆时需要偏移的长度 主要用于衔接直线
    private int ovalOffsetBottomRight; // 画圆时需要偏移的长度 主要用于衔接直线
    private int ovalOffsetBottomLeft; // 画圆时需要偏移的长度 主要用于衔接直线
    private boolean isCreate; //是否为创建

    public CheekChangeButton(Context context) {
        this(context, null);
    }

    public CheekChangeButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheekChangeButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        isCreate = true;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheekChangeButton, 0, 0);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.CheekChangeButton_cheekborderwidth) {
                borderWidth = ta.getDimensionPixelSize(attr, borderWidth);
            } else if (attr == R.styleable.CheekChangeButton_cheekbordercolor) {
                borderColor = ta.getColor(attr, borderColor);
            } else if (attr == R.styleable.CheekChangeButton_cheekinnerborderwidth) {
                innerBorderWidth = ta.getDimensionPixelSize(attr, innerBorderWidth);
            } else if (attr == R.styleable.CheekChangeButton_cheekinnerbordercolor) {
                innerBorderColor = ta.getColor(attr, innerBorderColor);
            } else if (attr == R.styleable.CheekChangeButton_cheekcornerradius) {
                cornerRadius = ta.getDimensionPixelSize(attr, cornerRadius);
                cornerTopLeftRadius = cornerTopRightRadius = cornerBottomLeftRadius = cornerBottomRightRadius = cornerRadius;
            } else if (attr == R.styleable.CheekChangeButton_cheekcornertopleftradius) {
                cornerTopLeftRadius = ta.getDimensionPixelSize(attr, cornerTopLeftRadius);
            } else if (attr == R.styleable.CheekChangeButton_cheekcornertoprightradius) {
                cornerTopRightRadius = ta.getDimensionPixelSize(attr, cornerTopRightRadius);
            } else if (attr == R.styleable.CheekChangeButton_cheekcornerbottomleftradius) {
                cornerBottomLeftRadius = ta.getDimensionPixelSize(attr, cornerBottomLeftRadius);
            } else if (attr == R.styleable.CheekChangeButton_cheekcornerbottomrightradius) {
                cornerBottomRightRadius = ta.getDimensionPixelSize(attr, cornerBottomRightRadius);
            } else if (attr == R.styleable.CheekChangeButton_cheekbackground) {
                cornerBackground = ta.getResourceId(attr, -1);
            } else if (attr == R.styleable.CheekChangeButton_cheekischeck) {
                isCheck = ta.getBoolean(attr, false);
            } else if (attr == R.styleable.CheekChangeButton_cheekinitbordercolor) {
                defoultBorderColor = ta.getColor(attr, Color.WHITE);
            }
        }
        ta.recycle();
        ovalOffsetTopLeft = cornerTopLeftRadius == 0 ? 0 : borderWidth / 4;
        ovalOffsetTopRight = cornerTopRightRadius == 0 ? 0 : borderWidth / 4;
        ovalOffsetBottomLeft = cornerBottomLeftRadius == 0 ? 0 : borderWidth / 4;
        ovalOffsetBottomRight = cornerBottomRightRadius == 0 ? 0 : borderWidth / 4;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(!isCheck);
            }
        });
    }

    public void init(Canvas canvas) {
        isCreate = false;

        Paint paint = new Paint();
        if (isCheck) {
            paint.setColor(borderColor);
        } else {
            paint.setColor(defoultBorderColor);
        }
        paint.setAntiAlias(true);
        paint.setStrokeWidth(borderWidth);
        paint.setStyle(Paint.Style.STROKE);//描边
        drawDefoult(canvas, paint);
    }


    float va = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
//        绘制边框
        countBorder(canvas);

    }

    public void setNowPesent(float va) {
        this.va = va;
        invalidate();
    }

    public void drawDefoult(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(borderWidth);
        canvas.drawLine(0, getHeight() / 2, 0, cornerTopLeftRadius / 2 + ovalOffsetTopLeft, paint);
        canvas.drawLine(cornerTopLeftRadius / 2 + ovalOffsetTopLeft, 0, (float) (getWidth() - cornerTopRightRadius / 2 - ovalOffsetTopRight), 0, paint);
        canvas.drawLine(getWidth(), (float) (cornerTopRightRadius / 2 + ovalOffsetTopRight), getWidth(), getHeight() / 2, paint);
        canvas.drawLine(0, getHeight() / 2, 0, getHeight() - cornerBottomLeftRadius / 2 - ovalOffsetBottomLeft, paint);
        canvas.drawLine(cornerBottomLeftRadius / 2 + ovalOffsetBottomLeft, getHeight(), (float) (getWidth() - cornerBottomRightRadius / 2 - ovalOffsetBottomLeft), getHeight(), paint);
        canvas.drawLine(getWidth(), getHeight() - ovalOffsetBottomRight - cornerBottomRightRadius / 2, getWidth(), getHeight() / 2, paint);


        RectF rightTopOval = new RectF(getWidth() - cornerTopRightRadius - ovalOffsetTopRight, ovalOffsetTopRight, getWidth() - ovalOffsetTopRight, cornerTopRightRadius + ovalOffsetTopRight);
        RectF leftTopOval = new RectF(ovalOffsetTopLeft, ovalOffsetTopLeft, cornerTopLeftRadius + ovalOffsetTopLeft, cornerTopLeftRadius + ovalOffsetTopLeft);
        RectF leftBottomOval = new RectF(ovalOffsetBottomLeft, getHeight() - ovalOffsetBottomLeft - cornerBottomLeftRadius, cornerBottomLeftRadius + ovalOffsetBottomLeft, getHeight() - ovalOffsetBottomLeft);
        RectF rightBottomOval = new RectF(getWidth() - cornerTopRightRadius - ovalOffsetTopRight, getHeight() - cornerTopRightRadius - ovalOffsetTopRight, getWidth() - ovalOffsetTopRight, getHeight() - ovalOffsetTopRight);


        paint.setStrokeWidth(borderWidth / 2);
        canvas.drawArc(rightBottomOval, 0, 90, false, paint);
        canvas.drawArc(leftBottomOval, 90, 90, false, paint);
        canvas.drawArc(leftTopOval, 180, 90, false, paint);
        canvas.drawArc(rightTopOval, 270, 90, false, paint);

        paint.setStrokeWidth(borderWidth);
    }


    //测量并绘制
    public void countBorder(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(borderWidth);
        paint.setStyle(Paint.Style.STROKE);//描边
        if (animalInLeft) {
            drawTopLeftIn(canvas, paint);
            drawBottomLeftIn(canvas, paint);
        } else {
            drawTopRightIn(canvas, paint);
            drawBottomRightIn(canvas, paint);
        }
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
        invalidate();
//        startAnimal(isCheck);
    }

    public void drawTopLeftIn(Canvas canvas, Paint paint) {
        double leftTopArcLenth = Math.PI * cornerTopLeftRadius;//左上圆
        double rightTopArcLenth = Math.PI * cornerTopRightRadius;//右上圆
        double topHalfDrawWidth = getHeight() + getWidth() - cornerTopLeftRadius - cornerTopRightRadius + leftTopArcLenth + rightTopArcLenth;

        //当前阶段绘制的线的长度
        //左侧上一部分线
        double nowDrawWidth = va * topHalfDrawWidth;
        if (nowDrawWidth < getHeight() / 2 - cornerTopLeftRadius / 2 - ovalOffsetTopLeft) {//绘制的长度小于左侧直线的长度
            canvas.drawLine(0, getHeight() / 2, 0, (float) (getHeight() / 2 - nowDrawWidth), paint);//左上
        } else {

            canvas.drawLine(0, getHeight() / 2, 0, cornerTopLeftRadius / 2 + ovalOffsetTopLeft, paint);
        }


        //左上圆弧的区域
        RectF leftTopOval = new RectF(ovalOffsetTopLeft, ovalOffsetTopLeft, cornerTopLeftRadius + ovalOffsetTopLeft, cornerTopLeftRadius + ovalOffsetTopLeft);
        double secondTopLine = nowDrawWidth - getHeight() / 2 + cornerTopLeftRadius;
        if (secondTopLine > 0) {
            paint.setStrokeWidth(borderWidth / 2);
            if (secondTopLine <= leftTopArcLenth / 4) {
                float drawAngle = (float) (secondTopLine / leftTopArcLenth * 360);
                canvas.drawArc(leftTopOval, 180, drawAngle, false, paint);
            } else {
                canvas.drawArc(leftTopOval, 180, 90, false, paint);
            }
        }


        double threeLine = secondTopLine - leftTopArcLenth / 4;
        if (threeLine >= 0) {
            paint.setStrokeWidth(borderWidth);
            if (threeLine <= getWidth() - cornerTopLeftRadius / 2 - cornerTopRightRadius / 2 - ovalOffsetTopLeft - ovalOffsetTopRight) {
                canvas.drawLine(cornerTopLeftRadius / 2 + ovalOffsetTopLeft, 0, (float) (cornerTopLeftRadius / 2 + threeLine), 0, paint);
            } else {
                canvas.drawLine(cornerTopLeftRadius / 2 + ovalOffsetTopLeft, 0, (float) (getWidth() - cornerTopRightRadius / 2 - ovalOffsetTopRight), 0, paint);
            }
        }

        double fourLine = threeLine - getWidth() + cornerTopLeftRadius / 2 + cornerTopRightRadius / 2;
        RectF rightOval = new RectF(getWidth() - cornerTopRightRadius - ovalOffsetTopRight, ovalOffsetTopRight, getWidth() - ovalOffsetTopRight, cornerTopRightRadius + ovalOffsetTopRight);

        if (fourLine >= 0) {
            paint.setStrokeWidth(borderWidth / 2);
            if (fourLine <= rightTopArcLenth / 4) {
                float drawAngle = (float) (fourLine / rightTopArcLenth * 360);
                canvas.drawArc(rightOval, 270, drawAngle, false, paint);
            } else {
                canvas.drawArc(rightOval, 270, 90, false, paint);
            }
        }


        double fiveLine = fourLine - rightTopArcLenth / 4;
        if (fiveLine >= 0) {
            paint.setStrokeWidth(borderWidth);
            if (fiveLine <= getHeight() / 2 - cornerTopRightRadius / 2 - ovalOffsetTopRight) {
                canvas.drawLine(getWidth(), (float) (cornerTopRightRadius / 2 + ovalOffsetTopRight), getWidth(), (float) (cornerTopRightRadius / 2 + ovalOffsetTopRight + fiveLine), paint);
            } else {
                canvas.drawLine(getWidth(), (float) (cornerTopRightRadius / 2 + ovalOffsetTopRight), getWidth(), getHeight() / 2, paint);
            }
        }
    }

    public void drawBottomLeftIn(Canvas canvas, Paint paint) {
        double leftBottomArcLenth = Math.PI * cornerBottomLeftRadius;//左下圆
        double rightBottomArcLenth = Math.PI * cornerBottomRightRadius;//右下圆
        double bottomHalfDrawWidth = getHeight() + getWidth() - cornerBottomLeftRadius - cornerBottomRightRadius + leftBottomArcLenth + rightBottomArcLenth;

        double nowDrawWidth = va * bottomHalfDrawWidth;
        //左侧下一部分线
        paint.setStrokeWidth(borderWidth);
        if (nowDrawWidth < getHeight() / 2 - cornerBottomLeftRadius / 2 - ovalOffsetBottomLeft) {//绘制的长度小于左侧直线的长度
            canvas.drawLine(0, getHeight() / 2, 0, (float) (getHeight() / 2 + nowDrawWidth), paint);//左上
        } else {
            canvas.drawLine(0, getHeight() / 2, 0, getHeight() - cornerBottomLeftRadius / 2 - ovalOffsetBottomLeft, paint);
        }

        //左下圆弧的区域
        RectF leftBottomOval = new RectF(ovalOffsetBottomLeft, getHeight() - ovalOffsetBottomLeft - cornerBottomLeftRadius, cornerBottomLeftRadius + ovalOffsetBottomLeft, getHeight() - ovalOffsetBottomLeft);
        double secondBottomLine = nowDrawWidth - getHeight() / 2 + cornerBottomLeftRadius - ovalOffsetBottomLeft;
        if (secondBottomLine > 0) {
            paint.setStrokeWidth(borderWidth / 2);
            if (secondBottomLine <= leftBottomArcLenth / 4) {
                float drawAngle = (float) (secondBottomLine / leftBottomArcLenth * 360);
                canvas.drawArc(leftBottomOval, 180, -drawAngle, false, paint);
            } else {
                canvas.drawArc(leftBottomOval, 90, 90, false, paint);
            }
        }

        double threeLine = secondBottomLine - leftBottomArcLenth / 4;
        if (threeLine >= 0) {
            paint.setStrokeWidth(borderWidth);
            if (threeLine <= getWidth() - cornerTopLeftRadius / 2 - cornerTopRightRadius / 2 - ovalOffsetTopRight) {
                canvas.drawLine(cornerBottomLeftRadius / 2 + ovalOffsetBottomLeft, getHeight(), (float) (cornerBottomLeftRadius / 2 + threeLine), getHeight(), paint);
            } else {
                canvas.drawLine(cornerBottomLeftRadius / 2 + ovalOffsetBottomLeft, getHeight(), (float) (getWidth() - cornerBottomRightRadius / 2 - ovalOffsetBottomLeft), getHeight(), paint);
            }
        }

        double fourLine = threeLine - getWidth() + cornerTopLeftRadius / 2 + cornerTopRightRadius / 2;
        RectF rightOval = new RectF(getWidth() - cornerTopRightRadius - ovalOffsetTopRight, getHeight() - cornerTopRightRadius - ovalOffsetTopRight, getWidth() - ovalOffsetTopRight, getHeight() - ovalOffsetTopRight);
        if (fourLine >= 0) {
            paint.setStrokeWidth(borderWidth / 2);
            if (fourLine <= rightBottomArcLenth / 4) {
                float drawAngle = (float) (fourLine / rightBottomArcLenth * 360);
                canvas.drawArc(rightOval, 90, -drawAngle, false, paint);
            } else {
                canvas.drawArc(rightOval, 0, 90, false, paint);
            }
        }


        double fiveLine = fourLine - rightBottomArcLenth / 4;
        if (fiveLine >= 0) {
            paint.setStrokeWidth(borderWidth);
            if (fiveLine <= getHeight() / 2 - cornerBottomRightRadius / 2) {
                canvas.drawLine(getWidth(), getHeight() - ovalOffsetBottomRight - cornerBottomRightRadius / 2, getWidth(), (float) (getHeight() - ovalOffsetBottomRight - cornerBottomRightRadius / 2 - fiveLine), paint);
            } else {
                canvas.drawLine(getWidth(), getHeight() - ovalOffsetBottomRight - cornerBottomRightRadius / 2, getWidth(), getHeight() / 2, paint);
            }
        }

    }

    public void drawTopRightIn(Canvas canvas, Paint paint) {
        double leftTopArcLenth = Math.PI * cornerTopLeftRadius;//左上圆
        double rightTopArcLenth = Math.PI * cornerTopRightRadius;//右上圆
        double topHalfDrawWidth = getHeight() + getWidth() - cornerTopLeftRadius - cornerTopRightRadius + leftTopArcLenth / 4 + rightTopArcLenth / 4;

        //当前阶段绘制的线的长度
        double nowDrawWidth = va * topHalfDrawWidth;
        if (nowDrawWidth < getHeight() / 2 - cornerTopRightRadius / 2 - ovalOffsetTopRight) {//绘制的长度小于左侧直线的长度
            canvas.drawLine(getWidth(), getHeight() / 2, getWidth(), (float) (getHeight() / 2 - nowDrawWidth), paint);//左上
        } else {
            canvas.drawLine(getWidth(), getHeight() / 2, getWidth(), cornerTopRightRadius / 2 + ovalOffsetTopRight, paint);
        }

        RectF rightOval = new RectF(getWidth() - cornerTopRightRadius - ovalOffsetTopRight, ovalOffsetTopRight, getWidth() - ovalOffsetTopRight, cornerTopRightRadius + ovalOffsetTopRight);
        double secondTopLine = nowDrawWidth - getHeight() / 2 + cornerTopLeftRadius;
        if (secondTopLine > 0) {
            paint.setStrokeWidth(borderWidth / 2);
            if (secondTopLine <= leftTopArcLenth / 4) {
                float drawAngle = (float) (secondTopLine / leftTopArcLenth * 360);
                canvas.drawArc(rightOval, 0, -drawAngle, false, paint);
            } else {
                canvas.drawArc(rightOval, 270, 90, false, paint);
            }
        }


        double threeLine = secondTopLine - leftTopArcLenth / 4;
        if (threeLine >= 0) {
            paint.setStrokeWidth(borderWidth);
            if (threeLine <= getWidth() - cornerTopLeftRadius / 2 - cornerTopRightRadius / 2 - ovalOffsetTopLeft - ovalOffsetTopRight) {
                canvas.drawLine(getWidth() - cornerTopRightRadius / 2 - ovalOffsetTopRight, 0, (float) (getWidth() - cornerTopRightRadius / 2 - ovalOffsetTopRight - threeLine), 0, paint);
            } else {
                canvas.drawLine(getWidth() - cornerTopRightRadius / 2 - ovalOffsetTopRight, 0, cornerTopLeftRadius / 2 + ovalOffsetTopLeft, 0, paint);
            }
        }

        double fourLine = threeLine - getWidth() + cornerTopLeftRadius / 2 + cornerTopRightRadius / 2;
        RectF leftTopOval = new RectF(ovalOffsetTopLeft, ovalOffsetTopLeft, cornerTopLeftRadius + ovalOffsetTopLeft, cornerTopLeftRadius + ovalOffsetTopLeft);

        if (fourLine >= 0) {
            paint.setStrokeWidth(borderWidth / 2);
            if (fourLine <= leftTopArcLenth / 4) {
                float drawAngle = (float) (fourLine / leftTopArcLenth * 360);
                canvas.drawArc(leftTopOval, 270, -drawAngle, false, paint);
            } else {
                canvas.drawArc(leftTopOval, 180, 90, false, paint);
            }
        }

        double fiveLine = fourLine - leftTopArcLenth / 4;
        if (fiveLine >= 0) {
            paint.setStrokeWidth(borderWidth);
            if (fiveLine <= getHeight() / 2 - cornerTopLeftRadius / 2 - ovalOffsetTopLeft) {
                canvas.drawLine(0, (float) (cornerTopLeftRadius / 2 + ovalOffsetTopLeft), 0, (float) (cornerTopLeftRadius / 2 + ovalOffsetTopLeft + fiveLine), paint);
            } else {
                canvas.drawLine(0, (float) (cornerTopLeftRadius / 2 + ovalOffsetTopLeft), 0, getHeight() / 2, paint);
            }
        }
    }

    public void drawBottomRightIn(Canvas canvas, Paint paint) {
        double leftBottomArcLenth = Math.PI * cornerBottomLeftRadius;//左下圆
        double rightBottomArcLenth = Math.PI * cornerBottomRightRadius;//右下圆
        double bottomHalfDrawWidth = getHeight() + getWidth() - cornerBottomLeftRadius - cornerBottomRightRadius + leftBottomArcLenth / 4 + rightBottomArcLenth / 4;

        double nowDrawWidth = va * bottomHalfDrawWidth;
        //左侧下一部分线
        paint.setStrokeWidth(borderWidth);
        if (nowDrawWidth <= getHeight() / 2 - cornerBottomRightRadius / 2 - ovalOffsetBottomRight) {//绘制的长度小于左侧直线的长度
            canvas.drawLine(getWidth(), getHeight() / 2, getWidth(), (float) (getHeight() / 2 + nowDrawWidth), paint);//左上
        } else {
            canvas.drawLine(getWidth(), getHeight() / 2, getWidth(), getHeight() - cornerBottomRightRadius / 2 - ovalOffsetBottomRight, paint);
        }

        //左下圆弧的区域

        RectF rightOval = new RectF(getWidth() - cornerBottomRightRadius / 2 - ovalOffsetBottomRight, getHeight() - cornerBottomRightRadius / 2 - ovalOffsetBottomRight, getWidth() - ovalOffsetBottomRight, getHeight() - ovalOffsetBottomRight);
        double secondBottomLine = nowDrawWidth - getHeight() / 2 + cornerTopRightRadius + ovalOffsetTopRight;
        if (secondBottomLine > 0) {
            paint.setStrokeWidth(borderWidth / 2);
            if (secondBottomLine <= rightBottomArcLenth / 4) {
                float drawAngle = (float) (secondBottomLine / leftBottomArcLenth * 360);
                canvas.drawArc(rightOval, 0, drawAngle, false, paint);
            } else {
                canvas.drawArc(rightOval, 0, 90, false, paint);
            }
        }

        double threeLine = secondBottomLine - rightBottomArcLenth / 4;
        if (threeLine >= 0) {
            paint.setStrokeWidth(borderWidth);
            Log.i("正下", "长度=" + (getWidth() - cornerBottomRightRadius / 2 - cornerBottomLeftRadius / 2 - ovalOffsetBottomRight - ovalOffsetBottomLeft)
                    + "         控件宽：" + getWidth());
            if (threeLine <= getWidth() - cornerBottomRightRadius / 2 - cornerBottomLeftRadius / 2 - ovalOffsetBottomRight - ovalOffsetBottomLeft) {
                canvas.drawLine(getWidth() - cornerBottomRightRadius / 2 - ovalOffsetBottomRight, getHeight(), (float) (getWidth() - cornerBottomRightRadius / 2 - ovalOffsetBottomRight - threeLine), getHeight(), paint);
            } else {
                canvas.drawLine(getWidth() - cornerBottomRightRadius / 2 - ovalOffsetBottomRight, getHeight(), cornerBottomLeftRadius / 2 + ovalOffsetBottomLeft, getHeight(), paint);
            }
        }

        double fourLine = threeLine - (getWidth() - cornerBottomRightRadius / 2 - cornerBottomLeftRadius / 2 - ovalOffsetBottomRight - ovalOffsetBottomLeft);
        RectF leftBottomOval = new RectF(ovalOffsetBottomLeft, getHeight() - ovalOffsetBottomLeft - cornerBottomLeftRadius, cornerBottomLeftRadius + ovalOffsetBottomLeft, getHeight() - ovalOffsetBottomLeft);
        if (fourLine >= 0) {
            paint.setStrokeWidth(borderWidth / 2);
            if (fourLine <= leftBottomArcLenth / 4) {
                float drawAngle = (float) (fourLine / leftBottomArcLenth * 360);
                canvas.drawArc(leftBottomOval, 90, drawAngle, false, paint);
            } else {
                canvas.drawArc(leftBottomOval, 90, 90, false, paint);
            }
        }


        double fiveLine = fourLine - leftBottomArcLenth / 4;
        if (fiveLine >= 0) {
            paint.setStrokeWidth(borderWidth);
            if (fiveLine <= getHeight() / 2 - cornerBottomLeftRadius / 2 - ovalOffsetBottomLeft) {
                canvas.drawLine(0, getHeight() - ovalOffsetBottomLeft - cornerBottomLeftRadius / 2, 0, (float) (getHeight() - ovalOffsetBottomLeft - cornerBottomLeftRadius / 2 - fiveLine), paint);
            } else {
                canvas.drawLine(0, getHeight() - ovalOffsetBottomLeft - cornerBottomLeftRadius / 2, 0, getHeight() / 2, paint);
            }
        }
    }

    public void startAnimal(final boolean isCheck) {
        if (cornerTopLeftRadius != 0) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(300);
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (isCheck) {
                        va = ((float) animation.getAnimatedValue()) / 300;
                    } else {
                        va = 1 - ((float) animation.getAnimatedValue()) / 300;
                    }
                    invalidate();
                }
            });
            valueAnimator.start();
        }
    }

    public boolean getCheck() {
        return isCheck;
    }
}
