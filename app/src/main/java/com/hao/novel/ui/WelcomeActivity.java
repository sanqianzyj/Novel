package com.hao.novel.ui;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import com.hao.lib.base.MI2Activity;
import com.hao.novel.R;



public class WelcomeActivity extends MI2Activity {
    ImageView novel_icon;
    TextView logo_text;
    TextView logo_advert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findView();
        startAnimal();
    }

    private void findView() {
        novel_icon = findViewById(R.id.novel_icon);
        logo_text = findViewById(R.id.logo_text);
        logo_advert = findViewById(R.id.logo_advert);

        String[] promissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission_group.CAMERA,
        };
        initPromission(promissions);
    }

    private void startAnimal() {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(100);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float progress = (float
                        ) valueAnimator.getAnimatedValue();
                if (progress < 50) {
                    novel_icon.setTranslationX(novel_icon.getWidth() * (progress / 75 - 1));
                    logo_text.setAlpha(progress / 50);
                } else if (progress < 99) {
                    logo_advert.setVisibility(View.VISIBLE);
                } else {
                    gotoMain();
                }
            }
        });
        valueAnimator.start();
    }


    private void gotoMain() {
        View novel_icon = findViewById(R.id.novel_icon);
        ViewCompat.setTransitionName(novel_icon, "avatar");
        Intent intent = new Intent(this, MiBookActivity.class);
        Pair<View, String> pair1 = new Pair<>((View) novel_icon, ViewCompat.getTransitionName(novel_icon));
        /**
         *4、生成带有共享元素的Bundle，这样系统才会知道这几个元素需要做动画
         */
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1, pair1);
        ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
    }

}
