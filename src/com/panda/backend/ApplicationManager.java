package com.panda.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.sidebarrock.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class ApplicationManager {

	private Context mContext = null;

	private List<AppInfo> mFavoriteAppList = null;
	private List<AppInfo> mDefaultAppList = null;
	private List<AppInfo> mAllAppList = null;

	public ApplicationManager(Context context) {
		mContext = context;
		mFavoriteAppList = new ArrayList<AppInfo>();
		mDefaultAppList = new ArrayList<AppInfo>();
		mAllAppList = new ArrayList<AppInfo>();
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
		for (int i = 0; i < 6; i++) {
			AppInfo appInfo = new AppInfo();
			// appInfo.setAppLabel(appLabel);
			// appInfo.setPkgName(pkgName);
			appInfo.setAppIcon(mContext.getResources().getDrawable(
					R.drawable.ic_launcher));
			// appInfo.setIntent(launchIntent);
			mFavoriteAppList.add(appInfo);
		}

		mAllAppList.addAll(mFavoriteAppList);
	}

	public void loadDefaultApps() {
		PackageManager pm = mContext.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);

		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));

		if (mDefaultAppList != null) {
			mDefaultAppList.clear();

			for (ResolveInfo reInfo : resolveInfos) {
				String activityName = reInfo.activityInfo.name;
				String pkgName = reInfo.activityInfo.packageName;
				String appLabel = (String) reInfo.loadLabel(pm);
				Drawable icon = reInfo.loadIcon(pm);

				Intent launchIntent = new Intent();
				launchIntent.setComponent(new ComponentName(pkgName,
						activityName));

				AppInfo appInfo = new AppInfo();
				appInfo.setAppLabel(appLabel);
				appInfo.setPkgName(pkgName);
				appInfo.setAppIcon(icon);
				appInfo.setIntent(launchIntent);
				mDefaultAppList.add(appInfo);
			}
		}

		// de-dupe the default app list
		if (mFavoriteAppList.size() > 0) {
			mDefaultAppList.removeAll(mFavoriteAppList);
		}

		mAllAppList.addAll(mDefaultAppList);
	}

	/**
	 * This method is the entry to load all applications, including user's
	 * favorite ones and the others.
	 */
	public void loadAllApps() {
		loadFavoriteApps();
		loadDefaultApps();
	}
}
