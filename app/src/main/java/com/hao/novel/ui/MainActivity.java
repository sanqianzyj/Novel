package com.hao.novel.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hao.lib.Util.StatusBarUtil;
import com.hao.lib.Util.ToastUtils;
import com.hao.novel.R;
import com.hao.novel.base.App;

public class MainActivity extends AppCompatActivity {
    long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPressTime < 1500) {
            App.getInstance().finishAll();
        } else {
            backPressTime = System.currentTimeMillis();
            ToastUtils.INSTANCE.showMessage("再次点击退出");
        }
    }
}
