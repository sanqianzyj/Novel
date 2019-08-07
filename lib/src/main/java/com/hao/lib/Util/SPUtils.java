package com.hao.lib.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

    /**
     * 这是一个SharePreference的根据类，使用它可以更方便的数据进行简单存储
     * 这里只要知道基本调用方法就可以了
     * 1.通过构造方法来传入上下文和文件名
     * 2.通过putValue方法传入一个或多个自定义的ContentValue对象，进行数据存储
     * 3.通过get方法来获取数据
     * 4.通过clear方法来清除这个文件的数据
     * 这里没有提供清除单个key的数据，是因为存入相同的数据会自动覆盖，没有必要去理会
     */

    //定义一个SharePreference对象
    static SharedPreferences sharedPreferences;
    //定义一个上下文对象

    private SPUtils() {
    }


    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("MI2", Context.MODE_PRIVATE);
    }


    /**
     * 存储数据
     * 这里要对存储的数据进行判断在存储
     * 只能存储简单的几种数据
     * 这里使用的是自定义的ContentValue类，来进行对多个数据的处理
     */
    //创建一个内部类使用，里面有key和value这两个值
    class ContentValue {
        String key;
        Object value;

        //通过构造方法来传入key和value
        ContentValue(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    //一次可以传入多个ContentValue对象的值
    public void putValues(ContentValue... contentValues) {
        if (sharedPreferences == null) {
            throw new NullPointerException("SP为空，请调用init方法进行初始化");
        }
        //获取SharePreference对象的编辑对象，才能进行数据的存储
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //数据分类和存储
        for (ContentValue contentValue : contentValues) {
            //如果是字符型类型
            if (contentValue.value instanceof String) {
                editor.putString(contentValue.key, contentValue.value.toString()).commit();
            }
            //如果是int类型
            if (contentValue.value instanceof Integer) {
                editor.putInt(contentValue.key, Integer.parseInt(contentValue.value.toString())).commit();
            }
            //如果是Long类型
            if (contentValue.value instanceof Long) {
                editor.putLong(contentValue.key, Long.parseLong(contentValue.value.toString())).commit();
            }
            //如果是布尔类型
            if (contentValue.value instanceof Boolean) {
                editor.putBoolean(contentValue.key, Boolean.parseBoolean(contentValue.value.toString())).commit();
            }

        }
    }


    //获取数据的方法
    public String getString(String key) {
        if (sharedPreferences == null) {
            throw new NullPointerException("SP为空，请调用init方法进行初始化");
        }
        return sharedPreferences.getString(key, null);
    }

    public boolean getBoolean(String key) {
        if (sharedPreferences == null) {
            throw new NullPointerException("SP为空，请调用init方法进行初始化");
        }
        return sharedPreferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        if (sharedPreferences == null) {
            throw new NullPointerException("SP为空，请调用init方法进行初始化");
        }
        return sharedPreferences.getInt(key, -1);
    }

    public long getLong(String key) {
        if (sharedPreferences == null) {
            throw new NullPointerException("SP为空，请调用init方法进行初始化");
        }
        return sharedPreferences.getLong(key, -1);
    }

    //清除当前文件的所有的数据
    public void clear() {
        sharedPreferences.edit().clear().commit();
    }

}
