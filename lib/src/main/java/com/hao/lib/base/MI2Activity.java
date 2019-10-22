package com.hao.lib.base;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hao.lib.R;
import com.hao.lib.Util.ImageUtils;
import com.hao.lib.Util.SystemUtil;
import com.hao.lib.base.theme.AppThemeSetting;

import java.util.ArrayList;
import java.util.List;

public abstract class MI2Activity extends AppCompatActivity {
    protected String MI2TAG = "MI2Activity";
    public static final String PERMISSION_MI = "com.hao.MI";
    public List<View> notHideView = new ArrayList();


    public String getMI2TAG() {
        return MI2TAG;
    }

    /**
     * 添加loading布局
     */
    public View addLoading(View v) {
        if (v != null) {
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            loading.addView(v);
        }
        return loading;
    }

    /**
     * 显示加载布局
     */
    public void showLoading() {
        if (loading == null) {
            new NullPointerException("还未加载布局,请在布局加载完成后使用");
            return;
        }
        loading.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏加载布局
     */
    public void dismisLoading() {
        if (loading == null) {
            new NullPointerException("还未加载布局,请在布局加载完成后使用");
            return;
        }
        loading.setVisibility(View.GONE);
    }

    @Override
    public void setContentView(int layoutResID) {
        base = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(base);
    }

    RelativeLayout loading;
    protected View base;

    @Override
    public void setContentView(View view) {
        base = LayoutInflater.from(this).inflate(R.layout.mi2_activity, null);
        loading = base.findViewById(R.id.loading);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((RelativeLayout) base.findViewById(R.id.content)).addView(view);
        super.setContentView(base);
    }


    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MI2App.getInstance().removeActivity(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (checkCallingOrSelfPermission(PERMISSION_MI) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("继承此activity需要权限");
        }
//        StatusBarUtil.setTranslucent(this);
        super.onCreate(savedInstanceState);
        MI2App.getInstance().addActivity(this);
    }


    @Override
    protected void onResume() {
        if (AppThemeSetting.getInstance().getBackground() != null) {
            base.setBackgroundDrawable(AppThemeSetting.getInstance().getBackground());
        }
        super.onResume();
    }


    /**
     * 便捷无参数跳转
     *
     * @param a 需要跳转的activityclass
     */
    public void startActivity(Class<? extends Activity> a) {
        startActivity(new Intent(this, a));
    }


    protected void initPromission(String[] promision) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> promission = new ArrayList<>();
            for (int i = 0; i < promision.length; i++) {
                if (checkSelfPermission(promision[i]) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    promission.add(promision[i]);
                }
            }

            String[] str = new String[promission.size()];
            for (int i = 0; i < promission.size(); i++) {
                str[i] = promission.get(i);
            }


            if (str.length != 0) {
                requestPermissions(str, 0);
            }
        }
    }


    /**
     * 切换背景图片  加入渐变效果
     *
     * @param drawable
     */
    protected void setBackGround(Drawable drawable) {
        DisplayMetrics displayMetrics = SystemUtil.getScreenSize(this);
        final Drawable drawableNow = ImageUtils.getCustomImage(displayMetrics.widthPixels, displayMetrics.heightPixels, drawable);
        final AppThemeSetting appThemeSetting = MI2App.getInstance().getMi2Theme();
        if (appThemeSetting.getBackground() == null) {
            appThemeSetting.setBackground(drawable);
            base.setBackgroundDrawable(drawable);
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100).setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if ((int) valueAnimator.getAnimatedValue() > 50) {
                        AppThemeSetting.getInstance().setBackground(drawableNow);
                        base.setBackgroundDrawable(ImageUtils.getTransParentDrawable(AppThemeSetting.getInstance().getBackground(), ((int) valueAnimator.getAnimatedValue() - 50) * 2));
                    } else {
                        base.setBackgroundDrawable(ImageUtils.getTransParentDrawable(AppThemeSetting.getInstance().getBackground(), 100 - (int) valueAnimator.getAnimatedValue() * 2));
                    }
                }
            });
            valueAnimator.start();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        SystemUtil.hideInputWhenTouchOtherView(this, ev, notHideView);
        return super.dispatchTouchEvent(ev);
    }

    //添加点击后不隐藏软键盘的view
    public void addViewForNotHideSoftInput(View v) {
        notHideView.add(v);
    }


}