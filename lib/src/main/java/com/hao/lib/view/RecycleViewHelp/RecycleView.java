package com.hao.lib.view.RecycleViewHelp;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//可以监听滑动事件的RecycleView
public class RecycleView extends RecyclerView {
    public RecycleView(@NonNull Context context) {
        super(context);
    }

    public RecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    RecycleScrollListener recycleScrollListener;

    public void setScrollListener(RecycleScrollListener recycleScrollListener) {
        this.recycleScrollListener = recycleScrollListener;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (recycleScrollListener != null && layoutManager instanceof LinearLayoutManager) {
            recycleScrollListener.onScroll(((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition(), ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition());
        }
    }

    //滚动监听当前显示的是item的位置
    public interface RecycleScrollListener {
        void onScroll(int start, int end);
    }
}
