package com.hao.lib.Util;

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewConfiguration
import androidx.core.app.ActivityCompat
import com.hao.lib.R
import com.hao.lib.base.BackCall
import com.hao.lib.base.MI2App
import com.hao.out.base.utils.listener.SearchFileLisener
import com.hao.out.base.utils.listener.TraverseFileListener
import java.io.File


/**
 * 集成了跟系统相关的工具
 * 1.像素和DP之间的转换
 * 2.获取状态栏高度
 * 3.遍历文件夹
 */
object SystemUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun dp2px(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.getResources().getDisplayMetrics()
        ).toInt()
    }

    fun px2dp(context: Context, pxValue: Float): Float {
        return pxValue / context.getResources().getDisplayMetrics().density + 0.5f
    }

    fun px2sp(context: Context, pxValue: Float): Float {
        return pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f
    }

    fun px2sp(context: Context, size: Int): Float {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                size.toFloat(),
                context.getResources().getDisplayMetrics()
        )
    }

    fun sp2px(context: Context, spValue: Float): Float {
        return spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f
    }

    fun sp2px(context: Context, size: Int): Float {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                size.toFloat(),
                context.getResources().getDisplayMetrics()
        )
    }


    /**
     *
     * 应用区的顶端位置即状态栏的高度
     * *注意*该方法不能在初始化的时候用
     * */
    fun getDecorViewHight(activity: Activity): Int {
        val rectangle = Rect()
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle)
        return rectangle.top
    }

    /**
     *
     * 应用区的顶端位置即状态栏的高度
     * *注意*该方法不能在初始化的时候用
     * */
    fun getDecorViewHightByReflect(activity: Activity): Int {
        var statusBarHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val obj = clazz.newInstance()
            val height = Integer.parseInt(
                    clazz.getField("status_bar_height")
                            .get(obj).toString()
            )
            statusBarHeight = activity.getResources().getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusBarHeight;
    }

    /**
     * 状态栏高度 = 屏幕高度 - 应用区高度
     * *注意*该方法不能在初始化的时候用
     * */
    fun getDecorViewHightByAllHight(activity: Activity): Int {
        //屏幕的高度
        val dm = DisplayMetrics()
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm)
        //应用区域
        val outRect1 = Rect()
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1)
        return dm.heightPixels - outRect1.height()  //状态栏高度=屏幕高度-应用区域高度
    }


    fun getScreenSize(activity: Activity): DisplayMetrics {
        val manager = activity.getWindowManager()
        val outMetrics = DisplayMetrics()
        manager.getDefaultDisplay().getMetrics(outMetrics)
        return outMetrics;
    }

    /**
     * 获取虚拟键盘
     */
    fun getKeyBroadHight(activity: Activity): Int {
        var result = 0
        if (hasNavBar(activity)) {
            val res = activity.getResources()
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
        }
        return result;
    }


    /**
     * 是否使用虚拟按键
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun hasNavBar(context: Context): Boolean {
        var res = context.getResources();
        var resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId);
            // check override flag
            var sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    fun getNavBarOverride(): String {
        var sNavBarOverride = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                var c = Class.forName("android.os.SystemProperties");
                var m = c.getDeclaredMethod("get", String::class.java)
                m.setAccessible(true);
                sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String;
            } catch (e: Throwable) {
            }
        }
        return sNavBarOverride;
    }


    fun getViewSize(v: View): View {
        val w = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
        )
        val h = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
        )
        v.measure(w, h)
        return v
    }


    /**
     * path 搜索文件的最初目录
     * filestag 搜索的文件的表示 可以是特定的标示  也可以是后缀
     */
    fun traverseFile(
            path: String,
            filesTag: MutableList<String>,
            traverseFileListener: TraverseFileListener
    ): MutableList<Any?> {
        var searchFiles = mutableListOf<Any?>()
        val dir = File(path)//文件夹dir
        if (MI2App.getInstance().nowActivitie != null) {
            MI2App.getInstance().nowActivitie!!.runOnUiThread(object : Runnable {
                override fun run() {
                    traverseFileListener.traverseDirectoryItem(path)
                }
            })
        } else {
            ToastUtils.showMessage("获取到的当前activity为空，调用此方法前，请先调用   App.instance.nowActivity=this(当前的activity)")
        }

        val files = dir.listFiles() ?: return mutableListOf<Any?>()//文件夹下的所有文件或文件夹
        for (i in files) {
            if (i.isDirectory()) {
                searchFiles.addAll(traverseFile(i.absolutePath, filesTag, traverseFileListener))//递归文件夹！！！
            } else {
                for (item in filesTag) {
                    if (i.absolutePath.endsWith(item)) {
                        if (MI2App.getInstance().nowActivitie != null) {
                            MI2App.getInstance().nowActivitie!!.runOnUiThread(object : Runnable {
                                override fun run() {
                                    searchFiles.add(traverseFileListener.traverseFileItem(i.absolutePath))
                                }
                            })

                        } else {
                            ToastUtils.showMessage("获取到的当前activity为空，调用此方法前，请先调用   App.instance.nowActivity=this(当前的activity)")
                        }
                    }
                }
            }
        }
        return searchFiles
    }

    var searchFileAsyncTask: SearchFileAsyncTask? = null
    fun searchFlie(
            tags: MutableList<String>,
            searchFileLisener: SearchFileLisener,
            traverseFileListener: TraverseFileListener
    ) {
        if (ActivityCompat.checkSelfPermission(
                        MI2App.getInstance(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            searchFileAsyncTask = SearchFileAsyncTask(searchFileLisener, traverseFileListener)
            searchFileAsyncTask!!.execute(tags)
        } else {
            DialogUtils.showInfoDialog(
                    MI2App.getInstance(),
                    "提示",
                    "您还未获取到相关的操作权限是,无法使用此功能/n是否进行申请",
                    "申请",
                    "取消",
                    object : BackCall<Int> {
                        override fun call(t: Int?) {
                            when (t) {
                                R.id.confirm -> {
                                    if (MI2App.getInstance().nowActivitie != null) {
                                        ActivityCompat.requestPermissions(
                                                MI2App.getInstance().nowActivitie!!,
                                                arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                                                100
                                        )
                                    }
                                }

                            }
                        }
                    })
        }
    }
    //结束查找
//    fun cancalSearchFileAsyncTask() {
//        if (searchFileAsyncTask != null && !searchFileAsyncTask!!.isCancelled) {
//            searchFileAsyncTask!!.cancel(true)
//            searchFileAsyncTask = null
//        }
//    }


    /**
     * 遍历查询文件的方法
     */
    class SearchFileAsyncTask constructor(
            searchFileLisener: SearchFileLisener,
            traverseFileListener: TraverseFileListener
    ) : AsyncTask<MutableList<String>, String, MutableList<Any?>>() {
        var listener = searchFileLisener
        var traverselistener = traverseFileListener
        override fun doInBackground(vararg p0: MutableList<String>): MutableList<Any?>? {
            if (isCancelled()) {//执行中断方法 cancalSearchFileAsyncTask后只是给task设置一个状态  并没有对task进行处理 需要在此位置进行处理
                return mutableListOf<Any?>()
            }
            return SystemUtils.traverseFile(Environment.getExternalStorageDirectory().path, p0[0], traverselistener)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            listener.onStart()
        }


        override fun onCancelled() {
            super.onCancelled()
            listener.onCancal()
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            listener.updateProgress(*values)
        }

        override fun onPostExecute(result: MutableList<Any?>?) {
            super.onPostExecute(result)
            Log.i("进度", "总共搜索" + result!!.size)
            listener.finish(result)
        }
    }



}

