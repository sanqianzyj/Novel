package com.hao.novel.ui.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import com.hao.novel.R;
import com.hao.novel.base.BaseActivity;


public class WelcomeActivity extends BaseActivity {
    ImageView novel_icon;
    TextView logo_text;
    TextView logo_advert;
    boolean isJumpMian;//是否执行了跳转 防止动画执行之后 重复执行
    boolean animalIsStart;//动画是否开始执行了
    ValueAnimator valueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // 隐藏状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Log.i("过程", "  onCreate");
        setContentView(R.layout.activity_welcome);
        findView();
    }

    private void findView() {
        novel_icon = findViewById(R.id.novel_icon);
        logo_text = findViewById(R.id.logo_text);
        logo_advert = findViewById(R.id.logo_advert);

//        logo_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gotoMain();
//            }
//        });

        String[] promissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission_group.CAMERA,
        };
        initPromission(promissions);
    }

    private void startAnimal() {
        if (animalIsStart) {
            return;
        }
        animalIsStart = true;
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(100);
        }
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float progress = (float) valueAnimator.getAnimatedValue();
                Log.i("动画进度", progress + "");
                if (progress > 10 && progress < 75) {
                    novel_icon.setTranslationX(novel_icon.getWidth() * (progress / 75 - 1));
                    logo_text.setAlpha(progress / 75);
                } else if (progress < 99) {
                    logo_advert.setVisibility(View.VISIBLE);
                } else if (progress == 100) {
                    animalIsStart = false;
                    if (!isJumpMian) {
                        gotoMain();
                    }
                }
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onRestart() {
        Log.i("过程", "  onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("过程", "  onResume");
        super.onResume();
        isJumpMian = false;
        startAnimal();
    }

    private void gotoMain() {
        isJumpMian = true;
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
