package com.panda.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.example.sidebarrock.R;
import com.panda.setting.GeneralSettings;

public class ApplicationManager {

	private Context mContext = null;

	private GeneralSettings mGeneralSetting = null;

	private List<AppInfo> mFavoriteAppList = null;
	private List<AppInfo> mDefaultAppList = null;
	private List<AppInfo> mAllAppList = null;
	private Map<String, AppInfo> mAppSearchMap = null;

	public ApplicationManager(Context context) {
		mContext = context;
		mFavoriteAppList = new ArrayList<AppInfo>();
		mDefaultAppList = new ArrayList<AppInfo>();
		mAllAppList = new ArrayList<AppInfo>();

		/**
		 * This map is used for seaching the favorite apps from loaded apps
		 */
		mAppSearchMap = new HashMap<String, AppInfo>();

		mGeneralSetting = GeneralSettings.getInstance(mContext);
		mGeneralSetting.loadGeneralSettings();
	}

	public List<AppInfo> getFavoriteAppList() {
		return mFavoriteAppList;

	}

	public List<AppInfo> getDefaultAppList() {
		return mDefaultAppList;
	}

	public List<AppInfo> getAllAppList() {
		return mAllAppList;
	}

	public void loadFavoriteApps() {
		int FavAppNum = mGeneralSetting.getSavedFavAppNum();
		for (int i = 0; i < FavAppNum; i++) {
			String appInfoIdentifier = mGeneralSetting.getFavAppInfo(i);
			AppInfo appInfo = null;

			if (appInfoIdentifier != null
					&& mAppSearchMap.containsKey(appInfoIdentifier)) {
				appInfo = mAppSearchMap.get(appInfoIdentifier);
			} else {
				// add blanket app icon here
				appInfo = new AppInfo();
				appInfo.setAppIcon(mContext.getResources().getDrawable(
						R.drawable.ic_launcher));
			}

			mFavoriteAppList.add(appInfo);
		}
	}

	public void loadDefaultApps() {
		mDefaultAppList.clear();
		mDefaultAppList.addAll(mAllAppList);

		// de-dupe the default app list
		if (mFavoriteAppList.size() > 0) {
			mDefaultAppList.removeAll(mFavoriteAppList);
		}
	}

	private void loadAllApps() {
		PackageManager pm = mContext.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);

		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));

		mAllAppList.clear();

		for (ResolveInfo reInfo : resolveInfos) {
			String activityName = reInfo.activityInfo.name;
			String pkgName = reInfo.activityInfo.packageName;
			String appLabel = (String) reInfo.loadLabel(pm);
			Drawable icon = reInfo.loadIcon(pm);

			Intent launchIntent = new Intent();
			launchIntent.setComponent(new ComponentName(pkgName, activityName));

			AppInfo appInfo = new AppInfo();
			appInfo.setAppLabel(appLabel);
			appInfo.setPkgName(pkgName);
			appInfo.setAppIcon(icon);
			appInfo.setIntent(launchIntent);
			mAllAppList.add(appInfo);
			mAppSearchMap.put(pkgName, appInfo);
		}
	}

	/**
	 * This method re-arrange the app list by putting favorite app fist and
	 * default app next
	 */
	private void sortApps() {
		mAllAppList.clear();
		mAllAppList.addAll(mFavoriteAppList);
		mAllAppList.addAll(mDefaultAppList);
	}

	/**
	 * This method is the entry to load all applications, including user's
	 * favorite ones and the others.
	 */
	public void loadApps() {
		loadAllApps();
		loadFavoriteApps();
		loadDefaultApps();
		sortApps();
	}
}
