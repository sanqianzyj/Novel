package com.hao.novel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeImageTransform;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hao.lib.base.MI2Activity;
import com.hao.novel.R;
import com.hao.novel.base.App;


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
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this, novel_icon, "book").toBundle());
                    //finish();
                }
            }
        });
        valueAnimator.start();
    }


}
