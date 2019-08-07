package com.hao.lib.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hao.lib.R;


public class NavigationBar extends RelativeLayout {

    public static final int LEFT_VIEW = 1;
    public static final int MIDDLE_VIEW = 2;
    public static final int RIGHT_VIEW = 3;
    private LinearLayout left;
    private RelativeLayout center;
    private LinearLayout right;

    public interface NavigationListener {
        void onButtonClick(int button);
    }

    private NavigationListener mListener = null;

    private ImageView mLeftImgBtn;
    private TextView mLeftTextView;
    private TextView mTitleView;
    private ImageView mRightImgBtn;
    private TextView mRightTextView;
    private LinearLayout mBar_main_layout;

    public NavigationBar(Context context) {
        super(context);
        init();
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_bar, this, true);
        left = (LinearLayout) findViewById(R.id.left);
        center = (RelativeLayout) findViewById(R.id.center);
        right = (LinearLayout) findViewById(R.id.right);


        mBar_main_layout = (LinearLayout) findViewById(R.id.bar_main_layout);
        mLeftImgBtn = (ImageView) findViewById(R.id.img_btn_navigation_left);
        mLeftTextView = (TextView) findViewById(R.id.tv_navigation_left);
        mTitleView = (TextView) findViewById(R.id.tv_navigation_title);
        mRightImgBtn = (ImageView) findViewById(R.id.img_btn_navigation_right);
        mRightTextView = (TextView) findViewById(R.id.tv_navigation_right);

        mLeftImgBtn.setOnClickListener(buttonListener);
        mLeftTextView.setOnClickListener(buttonListener);
        mRightImgBtn.setOnClickListener(buttonListener);
        mRightTextView.setOnClickListener(buttonListener);
    }

    public void setOurSelfLeft(View v) {
        left.removeAllViews();
        left.addView(v);
    }

    public void setOurSelfCenter(View v) {
        center.removeAllViews();
        center.addView(v);
    }

    public void setOurSelfRight(View v) {
        right.removeAllViews();
        right.addView(v);
    }

    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    public void setListener(NavigationListener listener) {
        this.mListener = listener;
    }

    OnClickListener buttonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_btn_navigation_left || v.getId() == R.id.tv_navigation_left) {
                if (mListener != null) {
                    mListener.onButtonClick(LEFT_VIEW);
                } else {
                    // ((Activity) getContext()).onBackPressed();
                }
            }
            if (v.getId() == R.id.img_btn_navigation_right || v.getId() == R.id.tv_navigation_right) {
                if (mListener != null) {
                    mListener.onButtonClick(RIGHT_VIEW);
                }
            }

            if (v.getId() == R.id.center || v.getId() == R.id.center) {
                if (mListener != null) {
                    mListener.onButtonClick(MIDDLE_VIEW);
                }
            }
        }
    };


    public void setLeftImage(int resId) {
        mLeftTextView.setVisibility(View.GONE);
        mLeftImgBtn.setVisibility(View.VISIBLE);
        mLeftImgBtn.setImageResource(resId);
    }

    public void setLeftBack(final Activity activity) {
        mLeftTextView.setVisibility(View.GONE);
        mLeftImgBtn.setVisibility(View.VISIBLE);
        mLeftImgBtn.setImageResource(R.mipmap.back);
        mLeftImgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void setLeftText(String string) {
        mLeftImgBtn.setVisibility(View.GONE);
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setText(string);
    }


    public void setHideLeftButton() {
        mLeftImgBtn.setVisibility(View.INVISIBLE);
    }

    public void setTitle(String string) {
        mTitleView.setText(string);
    }

    public void setTitleColor(int color) {
        mTitleView.setTextColor(color);
    }

    public void setTitle(int strResId) {
        mTitleView.setText(strResId);
    }

    public void setRightText(String string) {
        mRightImgBtn.setVisibility(View.GONE);
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText(string);
    }

    public void setRightTextColor(int color) {
        mRightTextView.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    public void setHiedRightText() {
        mRightImgBtn.setVisibility(View.INVISIBLE);
        mRightTextView.setVisibility(View.INVISIBLE);
    }

    public String getRightText() {
        return mRightTextView.getText().toString();
    }

    public void setRightButton(int resId) {
        mRightTextView.setVisibility(View.INVISIBLE);
        mRightImgBtn.setVisibility(View.VISIBLE);
        mRightImgBtn.setImageResource(resId);
    }

}
