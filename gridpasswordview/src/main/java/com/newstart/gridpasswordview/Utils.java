package com.newstart.gridpasswordview;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by shiyuankao on 2016/8/4.
 */
public class Utils {
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }
}
