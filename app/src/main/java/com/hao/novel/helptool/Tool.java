package com.hao.novel.helptool;


import android.graphics.drawable.Drawable;

import com.hao.novel.R;
import com.hao.novel.base.App;

import java.util.ArrayList;
import java.util.List;

public class Tool {

    public static Drawable randomImage() {
        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.back_1);
        images.add(R.mipmap.back_2);
        int tag = (int) Math.random() * images.size();
        return App.getInstance().getResources().getDrawable(images.get(tag));
    }
}
