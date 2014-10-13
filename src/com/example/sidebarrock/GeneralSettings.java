package com.example.sidebarrock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GeneralSettings {
	
	static private GeneralSettings mGeneralSettings = null;
	private Context mContext = null;
	private SharedPreferences sharedPref = null;
	
	private int mMaxWidth = 0;
	private int mSavedWidth = 0;
	

	public static GeneralSettings getInstance(Context context) {
		if(mGeneralSettings == null) {
			mGeneralSettings = new GeneralSettings(context);
		}
		return mGeneralSettings;
	}
	public GeneralSettings(Context context) {
		mContext = context;
		sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
	}
	public void loadGeneralSettings() {
		int defaultWidth = mContext.getResources().getInteger(R.integer.sidebar_width_default);
		mMaxWidth = mContext.getResources().getInteger(R.integer.sidebar_width_max);
		mSavedWidth = sharedPref.getInt(mContext.getString(R.string.siderbar_width_saved), defaultWidth);
	}
	public int getSavedWidth() {
		return mSavedWidth;
	}
	public void setSavedWidth(int newWidth) {
		this.mSavedWidth = newWidth;
		Editor editor = sharedPref.edit();
		editor.putInt(mContext.getString(R.string.siderbar_width_saved), newWidth);
		editor.commit();
	}
	public int getMaxWidth() {
		return mMaxWidth;
	}
}
