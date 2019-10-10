package com.hao.novel.ui;

import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.lib.Util.StatusBarUtil;
import com.hao.lib.Util.SystemUtil;
import com.hao.lib.Util.ToastUtils;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.ui.adapter.MuneAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    long backPressTime;
    RecyclerView show_mune;
    RecyclerView show_content;
    ImageView book_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setContentView(R.layout.activity_main);

        finidView();
        initView();
        initTranslation();
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

    private void finidView() {
        show_mune = findViewById(R.id.show_mune);
        show_content = findViewById(R.id.show_content);
        book_icon = findViewById(R.id.book_icon);
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.gray_11));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        show_mune.setLayoutManager(linearLayoutManager);
        MuneAdapter muneAdapter = new MuneAdapter(this, false);
        muneAdapter.setMuneAdapterListener(new MuneAdapter.MuneAdapterListener() {
            @Override
            public void animalInEnd() {

            }

            @Override
            public void animalOutEnd() {
                show_mune.setVisibility(View.GONE);
            }
        });
        show_mune.setAdapter(muneAdapter);
        book_icon.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_icon:
                show_mune.setVisibility(View.VISIBLE);
                ((MuneAdapter) show_mune.getAdapter()).changeDisOrHide();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (((MuneAdapter) show_mune.getAdapter()).isOpen()) {
            if (!(SystemUtil.isTouchView(show_mune, ev) || SystemUtil.isTouchView(book_icon, ev))) {
                ((MuneAdapter) show_mune.getAdapter()).changeDisOrHide();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
