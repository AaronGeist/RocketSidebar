package com.panda.utils;

import android.content.Context;

public class CommonUtils {

	/**
	 * this function is to convert dip value into px value
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
