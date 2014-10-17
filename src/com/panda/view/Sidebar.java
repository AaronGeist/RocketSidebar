package com.panda.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.example.sidebarrock.R;
import com.panda.backend.AppInfo;
import com.panda.backend.ApplicationManager;
import com.panda.backend.BrowseApplicationInfoAdapter;
import com.panda.setting.GeneralSettings;
import com.panda.utils.CommonUtils;

public class Sidebar extends ViewGroup {

	private static Context mContext = null;

	private List<AppInfo> mAppInfoList = null;
	private GeneralSettings mGeneralSetting = null;
	private ApplicationManager mAppManager = null;

	public Sidebar(Context context) {
		super(context);
		mContext = context;

		mAppManager = new ApplicationManager(mContext);
		mAppManager.loadApps();

		ListView appListView = new ListView(mContext);
		mAppInfoList = mAppManager.getAllAppList();

		BrowseApplicationInfoAdapter browseAppAdapter = new BrowseApplicationInfoAdapter(
				mContext, mAppInfoList);
		appListView.setAdapter(browseAppAdapter);
		appListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View view,
					int position, long id) {
				AppInfo appInfo = mAppInfoList.get(position);
				if (appInfo.getIntent() != null) {
					Intent intent = appInfo.getIntent();
					// must have this flag set to start new task from
					// non-activity
					// context
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().getApplicationContext().startActivity(intent);

					Sidebar.Dismiss();
				}

			}

		});
		appListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parentView,
					View view, int position, long id) {
				if (position < mGeneralSetting.getSavedFavAppNum()) {
					// freeze the ListView first, not chooseable
					// if not, user could choose another item for long click...
					parentView.setEnabled(false);

					// choose one application as favorite
					pickFavoriteApp(position, parentView);

				}
				return true;
			}

		});

		appListView.setBackgroundResource(R.drawable.shape_background_grey);
		appListView.setVerticalScrollBarEnabled(false);

		// here to restrict the width of the listView
		LayoutParams params = new LayoutParams(CommonUtils.dip2px(getContext(),
				70), WindowManager.LayoutParams.WRAP_CONTENT);
		this.addView(appListView, params);

		mGeneralSetting = GeneralSettings.getInstance(getContext());
		mGeneralSetting.loadGeneralSettings();

		// for the other transparent area, when clicked, remove the sidebar
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Sidebar.Dismiss();
			}
		});
	}

	private void pickFavoriteApp(final int location, final View originView) {
		final List<AppInfo> appInfoList = mAppManager.getDefaultAppList();

		ListView appListView = new ListView(mContext);
		appListView.setVerticalScrollBarEnabled(false);

		BrowseApplicationInfoAdapter browseAppAdapter = new BrowseApplicationInfoAdapter(
				mContext, appInfoList);
		appListView.setAdapter(browseAppAdapter);
		appListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View view,
					int position, long id) {
				AppInfo appInfo = appInfoList.get(position);

				// remember the app in generalsetting
				mGeneralSetting.setFavAppInfo(location, appInfo.getPkgName());
				originView.setEnabled(true);

				Sidebar.Dismiss();
			}
		});

		LayoutParams params = new LayoutParams(CommonUtils.dip2px(getContext(),
				90), WindowManager.LayoutParams.WRAP_CONTENT);

		this.addView(appListView, params);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

		int cCount = getChildCount();
		int cWidth = 0;
		int cHeight = 0;

		int width = 0;

		// we vertically place all children
		for (int i = 0; i < cCount; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();

			int cl = 0, ct = 0, cr = 0, cb = 0;
			cl = width;
			cr = cl + cWidth;
			ct = (int) this.getY();
			cb = ct + cHeight;
			width = cr;
			childView.layout(cl, ct, cr, cb);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		// make the Viewgroup fil the screen
		setMeasuredDimension(mGeneralSetting.getScreenWidth(),
				mGeneralSetting.getScreenHeight());
	}

	/**
	 * This method send broadcast and receiver will remove the sidebar view from
	 * the parent viewgroup
	 */
	static void Dismiss() {
		// broadcast to remove root viewgroup
		Intent intentRemove = new Intent("dismiss");
		mContext.sendBroadcast(intentRemove);
	}

}
