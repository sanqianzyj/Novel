package com.hao.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MiNoScrollListView extends ListView {
    public MiNoScrollListView(Context context) {
        super(context);
    }

    public MiNoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiNoScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
