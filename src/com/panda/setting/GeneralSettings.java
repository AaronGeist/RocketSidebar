package com.panda.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.sidebarrock.R;

public class GeneralSettings {

	static private GeneralSettings mGeneralSettings = null;
	private Context mContext = null;
	private SharedPreferences sharedPref = null;

	private int mMaxWidth = 0;
	private int mSavedWidth = 0;

	private int mScreenWidth = 0;
	private int mScreenHeight = 0;

	public static GeneralSettings getInstance(Context context) {
		if (mGeneralSettings == null) {
			mGeneralSettings = new GeneralSettings(context);
		}
		return mGeneralSettings;
	}

	public GeneralSettings(Context context) {
		mContext = context;
		sharedPref = mContext.getSharedPreferences(
				mContext.getString(R.string.preference_file_key),
				Context.MODE_PRIVATE);
	}

	public void loadGeneralSettings() {
		int defaultWidth = mContext.getResources().getInteger(
				R.integer.sidebar_width_default);
		mMaxWidth = mContext.getResources().getInteger(
				R.integer.sidebar_width_max);
		mSavedWidth = sharedPref
				.getInt(mContext.getString(R.string.siderbar_width_saved),
						defaultWidth);

		mScreenWidth = sharedPref.getInt(
				mContext.getString(R.string.screen_width), 0);
		mScreenHeight = sharedPref.getInt(
				mContext.getString(R.string.screen_height), 0);
	}

	public int getSavedWidth() {
		return mSavedWidth;
	}

	public void setSavedWidth(int newWidth) {
		this.mSavedWidth = newWidth;
		Editor editor = sharedPref.edit();
		editor.putInt(mContext.getString(R.string.siderbar_width_saved),
				newWidth);
		editor.commit();
	}

	public int getMaxWidth() {
		return mMaxWidth;
	}

	public int getScreenWidth() {
		return mScreenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.mScreenWidth = screenWidth;

		Editor editor = sharedPref.edit();
		editor.putInt(mContext.getString(R.string.screen_width), screenWidth);
		editor.commit();
	}

	public int getScreenHeight() {
		return mScreenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.mScreenHeight = screenHeight;

		Editor editor = sharedPref.edit();
		editor.putInt(mContext.getString(R.string.screen_height), screenHeight);
		editor.commit();
	}
}
