package com.dreamer.library.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Random;

/**
 * 作者： zhangzixu
 * 时间： 2016/1/18
 * 详情：
 */
public class Utils {
    /**
     * 获取区间随机数
     * @param min
     * @param max
     * @return
     */
    public static float nextFloat(final float min, final float max) {
        if (max < min) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + ((max - min) * new Random().nextFloat());
    }

    /**
     * 获取区间随机数
     * @param min
     * @param max
     * @return
     */
    public static int nextInt(final int min, final int max) {
        if (max < min) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + ((max - min) * new Random().nextInt());
    }

    /**
     * 获取屏幕宽度（像素）
     * @param ctx
     * @return
     */
    public static int getDisplayWidth(Context ctx){
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        winManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
    /**
     * 获取屏幕高度（像素）
     * @param ctx
     * @return
     */
    public static int getDisplayHeight(Context ctx){
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        winManager.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
}
