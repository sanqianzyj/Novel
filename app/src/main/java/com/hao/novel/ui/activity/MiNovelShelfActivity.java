package com.hao.novel.ui.activity;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hao.lib.Util.StatusBarUtil;
import com.hao.lib.Util.ToastUtils;
import com.hao.lib.base.AppUtils;
import com.hao.lib.view.refresh.RefreshProgressBar;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.base.BaseActivity;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.ui.adapter.MiBookAdapter;
import com.hao.novel.ui.used.ReadInfo;

import java.util.List;

public class MiNovelShelfActivity extends BaseActivity implements View.OnClickListener {
    long fristBack = 0;
    private RecyclerView mi_book_show;
    private FrameLayout fl_warn;

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

    @Override
    protected void onResume() {
        super.onResume();
        ((MiBookAdapter) mi_book_show.getAdapter()).refresh();
    }


    private void initView() {
        findViewById(R.id.shop).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        mi_book_show = findViewById(R.id.mi_book_show);
        mi_book_show.setLayoutManager(new LinearLayoutManager(this));
        mi_book_show.setAdapter(new MiBookAdapter(this));
        fl_warn = findViewById(R.id.fl_warn);
        findViewById(R.id.iv_warn_close).setOnClickListener(this);
    }

    //转场动画
    private void initTranslation() {
        ImageView book_icon = findViewById(R.id.book_icon);
        /**
         * 1、设置相同的TransitionName
         */
//        ViewCompat.setTransitionName(book_icon, "avatar");
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
        if (!App.getInstance().checkedDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.shop:
                startActivity(new Intent(this, NovelListActivity.class));
                break;
            case R.id.search:
                startActivity(new Intent(this, SearchNovelActivity.class));
                break;
            case R.id.iv_warn_close:
                fl_warn.setVisibility(View.GONE);
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