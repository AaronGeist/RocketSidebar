package com.panda.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.panda.backend.AppInfo;
import com.panda.backend.BrowseApplicationInfoAdapter;
import com.panda.setting.GeneralSettings;
import com.panda.utils.CommonUtils;

public class Sidebar extends ViewGroup {

	private Context mContext = null;
	private ArrayList<AppInfo> mlistAppInfo = null;
	private GeneralSettings gs = null;

	public Sidebar(Context context) {
		super(context);
		mContext = context;

		ListView listView = new ListView(context);
		mlistAppInfo = new ArrayList<AppInfo>();
		loadAllApps();

		BrowseApplicationInfoAdapter browseAppAdapter = new BrowseApplicationInfoAdapter(
				context, mlistAppInfo);

		listView.setAdapter(browseAppAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent intent = mlistAppInfo.get(position).getIntent();
				// must have this flag set to start new task from non-activity
				// context
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().getApplicationContext().startActivity(intent);

				// broadcast to remove root viewgroup
				Intent intentRemove = new Intent("dismiss");
				getContext().sendBroadcast(intentRemove);
			}
		});

		// here to restrict the width of the listView
		LayoutParams params = new LayoutParams(CommonUtils.dip2px(getContext(),
				70), WindowManager.LayoutParams.WRAP_CONTENT);
		this.addView(listView, params);

		gs = GeneralSettings.getInstance(getContext());
		gs.loadGeneralSettings();

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// broadcast to remove root viewgroup
				Intent intentRemove = new Intent("dismiss");
				getContext().sendBroadcast(intentRemove);
			}
		});
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

		int cCount = getChildCount();
		int cWidth = 0;
		int cHeight = 0;

		int height = 0;

		for (int i = 0; i < cCount; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();

			int cl = 0, ct = 0, cr = 0, cb = 0;
			cl = (int) this.getX();
			cr = cl + cWidth;
			ct = height;
			cb = height + cHeight;
			height = cb;
			childView.layout(cl, ct, cr, cb);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		// make the Viewgroup fil the screen
		setMeasuredDimension(gs.getScreenWidth(), gs.getScreenHeight());
	}

	public void loadAllApps() {
		PackageManager pm = mContext.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_UNINSTALLED_PACKAGES);

		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));

		if (mlistAppInfo != null) {
			mlistAppInfo.clear();

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
				mlistAppInfo.add(appInfo);
			}
		}
	}

}
