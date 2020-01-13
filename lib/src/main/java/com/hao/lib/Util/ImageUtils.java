package com.hao.lib.Util;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageUtils {


    /**
     * 获取一个带有透明度的图片
     *
     * @param sourceImg 目标图片
     * @param number    透明度 0-100  0表示透明  100表示为原图
     * @return
     */
    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number) {
        try {
            int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

            sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
            number = number * 255 / 100;
            for (int i = 0; i < argb.length; i++) {
                argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
            }
            sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            return sourceImg;
        }
        return sourceImg;
    }

    /**
     * 获取一个带有透明度的图片
     *
     * @param drawable 目标图片
     * @param number   透明度 0-100  0表示透明  100表示为原图¬
     * @return
     */
    public static Drawable getTransParentDrawable(Drawable drawable, int number) {
        return bitmap2Drawable(getTransparentBitmap(drawable2Bitmap(drawable), number));
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    /**
     * 获取一个适配尺寸的图片
     *
     * @param width
     * @param height
     * @param drawable
     */
    public static Drawable getCustomImage(int width, int height, Drawable drawable) {
        return bitmap2Drawable(getCustomImage(width, height, drawable2Bitmap(drawable)));
    }

    /**
     * 获取一个适配尺寸的图片
     *
     * @param width
     * @param height
     * @param bitmap
     */
    public static Bitmap getCustomImage(int width, int height, Bitmap bitmap) {
        int imageWidth = bitmap.getWidth();
        int imageheight = bitmap.getHeight();
        float multiple;
        if (width > imageWidth || height > imageheight) {//需要的尺寸大于图片
            multiple = ((float) imageWidth / width) < ((float) imageheight / height / imageheight) ? ((float) width / imageWidth) : ((float) height / imageheight);
        } else {
            multiple = ((float) width / imageWidth) > ((float) height / imageheight) ? ((float) width / imageWidth) : ((float) height / imageheight);
        }
        bitmap = bitMapScale(bitmap, multiple);
        bitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() - width) / 2, (bitmap.getHeight() - height) / 2, width, height);
        return bitmap;
    }


    /**
     * 对图片进行缩放 此方发容易导致OOM 慎用
     *
     * @param bitmap
     * @param scale
     * @return
     */
    public static Bitmap bitMapScale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }
}
