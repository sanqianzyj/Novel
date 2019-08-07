package com.hao.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class DragCricleView extends View {
    public DragCricleView(Context context) {
        super(context);
    }

    public DragCricleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragCricleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeCap(Paint.Cap.ROUND);


        Path path1 = new Path();
        path1.addCircle(100, 100, 30, Path.Direction.CCW);
        path1.addCircle(140, 100, 30, Path.Direction.CCW);

        Path path2 = new Path();
        path2.addRect(70, 110, 170, 160, Path.Direction.CCW);


        Path path3 = new Path();
        path3.moveTo(70, 110);
        path3.lineTo(120, 170);
        path3.lineTo(170, 110);
        path3.close();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            path1.op(path2, Path.Op.DIFFERENCE);
            path1.op(path3, Path.Op.UNION);
        }

        canvas.drawPath(path1, paint);
    }




}
