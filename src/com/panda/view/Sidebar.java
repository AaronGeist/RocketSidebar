package com.panda.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.panda.backend.AppInfo;
import com.panda.backend.BrowseApplicationInfoAdapter;

public class Sidebar extends ViewGroup{

	private Context mContext = null;
	private ArrayList<AppInfo> mlistAppInfo = null;
	
	public Sidebar(Context context) {
		super(context);
		mContext = context;
		
		this.setBackgroundColor(Color.BLUE);
		
//		TextView tv = new TextView(context);
//		tv.setHeight(200);
//		tv.setWidth(200);
//		tv.setText("hello world");
//		tv.setBackgroundColor(Color.RED);
//		
//		tv.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View view, MotionEvent event) {
//				// the top view will consume the up/down action
//				switch(event.getAction()) {
//					case MotionEvent.ACTION_UP:
//						Toast.makeText(getContext(), "Hello world onUp", Toast.LENGTH_SHORT).show();
//						((TextView) view).setBackgroundColor(Color.RED);
//						
//						// broadcast to remove root viewgroup
//						Intent intent = new Intent("dismiss");
//						getContext().sendBroadcast(intent);
//						
//						break;
//					case MotionEvent.ACTION_DOWN:
//						((TextView) view).setBackgroundColor(Color.BLACK);
//						Toast.makeText(getContext(), "Hello world onDown", Toast.LENGTH_SHORT).show();
//						
//						break;
//					default:
//						break;
//				}
//				return true;
//			}
//			
//		});
//		
//		this.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View view, MotionEvent event) {
//				switch(event.getAction()) {
//					case MotionEvent.ACTION_UP:
//						Toast.makeText(getContext(), "Sidebar onUp", Toast.LENGTH_SHORT).show();
//						return false;
//					case MotionEvent.ACTION_DOWN:
//						Toast.makeText(getContext(), "Sidebar onDown", Toast.LENGTH_SHORT).show();
//						break;
//					default:
//						break;
//				}
//				return true;
//			}
//		});
//		
//		this.addView(tv);
		
		ListView listView = new ListView(context);
		mlistAppInfo = new ArrayList<AppInfo>();
		loadAllApps();
		
		//Toast.makeText(context, "App size = " + mlistAppInfo.size(), Toast.LENGTH_SHORT).show();
		BrowseApplicationInfoAdapter browseAppAdapter = new BrowseApplicationInfoAdapter(context, mlistAppInfo);
		
		listView.setAdapter(browseAppAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent = mlistAppInfo.get(position).getIntent();
				// must have this flag set to start new task from non-activity context
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().getApplicationContext().startActivity(intent);
				
				// broadcast to remove root viewgroup
				Intent intentRemove = new Intent("dismiss");
				getContext().sendBroadcast(intentRemove);
			}
		});

		this.addView(listView);		
	}
	
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
		int cCount = getChildCount();
		int cWidth = 0;
		int cHeight = 0;
		
		int height = 0;
		
		for(int i = 0; i < cCount; i++) {
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
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		int width = 0;
		int height = 0;
		
		int cCount = getChildCount();
		
		int cWidth = 0;
		int cHeight = 0;
		
		for(int i = 0; i < cCount; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			
			width  = width > cWidth ? width : cWidth;
			height += cHeight;
			
			Log.i("Sidebar", "OnMeasure: child=" + i + ", cWitdth=" + cWidth + ", width=" + width);
		}
		
		Log.i("Sidebar", "OnMeasure: witdth=" + width);
		
		setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : height);
		
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
