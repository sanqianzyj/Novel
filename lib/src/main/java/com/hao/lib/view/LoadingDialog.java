package com.hao.lib.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.hao.lib.R;
import com.hao.lib.base.MI2App;

public class LoadingDialog {
    /**
     * 旋转动画的时间
     */
    static final int ROTATION_ANIMATION_DURATION =8000;
    /**
     * 动画插值
     */
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    Window window;
    Dialog d;
    private ImageView imageView;
    //	private Animation animation;
    private TextView textView;

    private static class LoadingHelper {
        private final static LoadingDialog load = new LoadingDialog();
    }

    public static LoadingDialog getInstance() {
        return LoadingHelper.load;
    }


    //对Dailog进行初始化
    private boolean initDailog() {
        if (MI2App.getInstance().getNowActivitie() == null) {
            new NullPointerException("还未初始化Activity,无法显示dialog");
            return false;
        }
        if (d == null) {
            View view = View.inflate(MI2App.getInstance().getNowActivitie(), R.layout.common_loadingdialog, null);
            imageView =  view.findViewById(R.id.imageView);
            imageView.setImageResource(R.mipmap.icon_loading_footbar_0);
            textView = view.findViewById(R.id.load_text);
//		textView.setText(str);
            float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
            float toDegree = 720.0f; // SUPPRESS CHECKSTYLE
            RotateAnimation mRotateAnimation =
                    new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue, Animation.RELATIVE_TO_SELF, pivotValue);
            mRotateAnimation.setFillAfter(true);
            mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
            mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
            mRotateAnimation.setRepeatCount(Animation.INFINITE);
            mRotateAnimation.setRepeatMode(Animation.RESTART);
            imageView.startAnimation(mRotateAnimation);
//            AnimationDrawable ad = (AnimationDrawable) MI2App.getInstance().getNowActivitie().getResources().getDrawable(R.drawable.anim_loading_progress_round);
//            imageView.setBackgroundDrawable(ad);
//            ad.start();

            d = new Dialog(MI2App.getInstance().getNowActivitie(), R.style.dialog);// 加入样式
            d.setCanceledOnTouchOutside(false);
            window = d.getWindow();
            window.setGravity(Gravity.CENTER);
            window.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return true;
    }

    public LoadingDialog show() {
        initDailog();
        if (null != d && !d.isShowing()) {
            d.show();
        }
        return this;
    }

    public LoadingDialog show(String content) {
        initDailog();
        if (null != d && !d.isShowing()) {
            textView.setText(content);
            d.show();
        }
        return this;
    }

    public LoadingDialog dismiss() {
        initDailog();
        if (d != null) {
            d.dismiss();
        }
        return this;
    }

    public LoadingDialog setCancel() {
        if (d != null) {
            d.setCancelable(false);
        }
        return this;
    }

}
