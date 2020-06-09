package com.hao.novel.base;

import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.hao.lib.Util.StatusBarUtil;
import com.hao.lib.base.MI2Activity;
import com.hao.novel.R;

public class BaseActivity extends MI2Activity {
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }
}
