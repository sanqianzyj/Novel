package com.hao.novel.base;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.hao.lib.base.MI2Activity;
import com.hao.novel.R;

public class BaseActivity extends MI2Activity {
    @Override
    public void setContentView(View view) {
        setTheme(R.style.AppTheme);
        super.setContentView(view);
        setBackGround(ContextCompat.getDrawable(this, R.mipmap.bg_readbook_yellow));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setBackGround(ContextCompat.getDrawable(this, R.mipmap.bg_readbook_yellow));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setBackGround(ContextCompat.getDrawable(this, R.mipmap.bg_readbook_yellow));
    }
}
