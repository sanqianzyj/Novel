package com.hao.novel.ui.activity;

import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hao.lib.Util.ToastUtils;
import com.hao.lib.base.AppUtils;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.base.BaseActivity;

public class MiBookActivity extends BaseActivity implements View.OnClickListener {
    long fristBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_mi_book, null);
        view.setPadding(0, AppUtils.getStatusBarHeight(this), 0, 0);
        setContentView(view);
        initTranslation();
        initView();
    }


    private void initView() {
        findViewById(R.id.shop).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
    }

    //转场动画
    private void initTranslation() {
        ImageView book_icon = findViewById(R.id.book_icon);
        /**
         * 1、设置相同的TransitionName
         */
        ViewCompat.setTransitionName(book_icon, "avatar");
        /**
         * 2、设置WindowTransition,除指定的ShareElement外，其它所有View都会执行这个Transition动画
         */
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());
        /**
         * 3、设置ShareElementTransition,指定的ShareElement会执行这个Transiton动画
         */
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeTransform());
        transitionSet.addTarget(book_icon);
        getWindow().setSharedElementEnterTransition(transitionSet);
        getWindow().setSharedElementExitTransition(transitionSet);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop:
                startActivity(new Intent(this, NovelListActivity.class));
                break;
            case R.id.search:
                startActivity(new Intent(this, SearchNovelActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - fristBack < 2000) {
            App.getInstance().finishAll();
        } else {
            fristBack = System.currentTimeMillis();
            ToastUtils.INSTANCE.showMessage("再次点击退出");
        }
    }
}