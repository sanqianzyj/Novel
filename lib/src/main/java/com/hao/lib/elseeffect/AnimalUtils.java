package com.hao.lib.elseeffect;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Build;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * 自定义的一个动画工具类
 */
public class AnimalUtils {
    Object[] date = new Object[]{};

    public AnimalUtils setDate(Object[] o) {
        date = o;
        return this;
    }

    public AnimalUtils() {
        animatorSet = new AnimatorSet();
        mAnimatorListener = null;
        setDefaultConfig();
    }

    private AnimatorSet animatorSet;


    public AnimalUtils addSequentially(Animator... animators) {
        animatorSet.playSequentially(animators);
        return this;
    }

    public AnimalUtils addTogether(Animator... animators) {
        animatorSet.playTogether(animators);
        return this;
    }

    public AnimalUtils setInterpolator(Interpolator interpolator) {
        animatorSet.setInterpolator(interpolator);
        return this;
    }


    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (mAnimatorListener != null) {
                mAnimatorListener.onAnimationStart(animation, date);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (mAnimatorListener != null) {
                mAnimatorListener.onAnimationEnd(animation, date);
            }

        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (mAnimatorListener != null) {
                mAnimatorListener.onAnimationCancel(animation, date);
            }

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            if (mAnimatorListener != null) {
                mAnimatorListener.onAnimationRepeat(animation, date);
            }
        }
    };


    static MAnimatorListener mAnimatorListener;

    public AnimalUtils setMAnimatorListener(MAnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
        return this;
    }

    public AnimalUtils setDuration(int time) {
        animatorSet.setDuration(time);
        return this;
    }


    private void setDefaultConfig() {
        if (animatorSet == null) {
            new NullPointerException("未设置可执行的动画集合");
            return;
        }
        animatorSet.setInterpolator(new LinearInterpolator());//动画执行的差值器
        animatorSet.setDuration(1000);//动画执行时间

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            animatorSet.setCurrentPlayTime(0);//动画开始执行的位置
        }
        animatorSet.setStartDelay(0);//延迟执行的时间
//        animatorSet.setTarget();
        animatorSet.playSequentially();
    }

    public AnimalUtils setAnimatorSet(AnimatorSet animatorSet) {
        this.animatorSet = animatorSet;
        return this;
    }


    public boolean isRun() {
        if (animatorSet != null)
            return animatorSet.isRunning();
        return false;
    }

    /**
     * 动画开始
     */
    public AnimalUtils startAnimatorSet() {
        animatorSet.addListener(animatorListener);
        animatorSet.start();
        return this;
    }

    public interface MAnimatorListener {
        void onAnimationStart(Animator animation, Object... o);

        void onAnimationEnd(Animator animation, Object... o);

        void onAnimationCancel(Animator animation, Object... o);

        void onAnimationRepeat(Animator animation, Object... o);
    }



}

