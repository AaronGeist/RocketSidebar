package com.panda.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.sidebarrock.R;

public class GeneralSettings {

	static private GeneralSettings mGeneralSettings = null;
	private Context mContext = null;
	private SharedPreferences sharedPref = null;

	private int mMaxFavAppNum = 0;
	private int mSavedFavAppNum = 0;

	private int mScreenWidth = 0;
	private int mScreenHeight = 0;

	private List<String> mFavAppInfoList = null;
	private List<Integer> mFavAppInfoPersistenceList = null;

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
		mFavAppInfoList = new ArrayList<String>();
		mFavAppInfoPersistenceList = new ArrayList<Integer>(Arrays.asList(
				R.string.siderbar_fav_app_info_1,
				R.string.siderbar_fav_app_info_2,
				R.string.siderbar_fav_app_info_3,
				R.string.siderbar_fav_app_info_4,
				R.string.siderbar_fav_app_info_5,
				R.string.siderbar_fav_app_info_6));
	}

	public void loadGeneralSettings() {
		int defaultWidth = mContext.getResources().getInteger(
				R.integer.sidebar_fav_app_num_default);
		mMaxFavAppNum = mContext.getResources().getInteger(
				R.integer.sidebar_fav_app_num_max);
		mSavedFavAppNum = sharedPref.getInt(
				mContext.getString(R.string.siderbar_fav_app_num_saved),
				defaultWidth);
		for (int i = 0; i < mMaxFavAppNum; i++) {
			mFavAppInfoList
					.add(sharedPref.getString(mContext
							.getString(mFavAppInfoPersistenceList.get(i)), null));
		}

		mScreenWidth = sharedPref.getInt(
				mContext.getString(R.string.screen_width), 0);
		mScreenHeight = sharedPref.getInt(
				mContext.getString(R.string.screen_height), 0);
	}

	public int getSavedFavAppNum() {
		return mSavedFavAppNum;
	}

	public void setSavedFavAppNum(int newFavAppNum) {
		this.mSavedFavAppNum = newFavAppNum;
		Editor editor = sharedPref.edit();
		editor.putInt(mContext.getString(R.string.siderbar_fav_app_num_saved),
				newFavAppNum);
		editor.commit();
	}

	public int getMaxFavAppNum() {
		return mMaxFavAppNum;
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

	public String getFavAppInfo(int index) {
		// check boundary before fetch element
		return (index >= mFavAppInfoList.size()) ? null : mFavAppInfoList
				.get(index);
	}

	public void setFavAppInfo(int index, String favAppInfo) {
		if (index >= mFavAppInfoList.size()) {
			return;
		}

		this.mFavAppInfoList.set(index, favAppInfo);

		Editor editor = sharedPref.edit();
		editor.putString(
				mContext.getString(mFavAppInfoPersistenceList.get(index)),
				favAppInfo);
		editor.commit();
	}
}
