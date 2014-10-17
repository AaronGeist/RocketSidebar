package com.panda.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sidebarrock.R;
import com.panda.service.BackgroundService;
import com.panda.setting.GeneralSettings;

/**
 * This is the entry for this application. In this entry, we mainly create a
 * service to handle creating a view group for sidebar, and also listeners to
 * some gestures.
 * 
 * @author yzhou7
 * 
 */
public class MainActivity extends Activity {

	TextView favAppNumText = null;
	SeekBar favAppNumSeek = null;
	GeneralSettings gs = null;
	Intent mServiceIntent = null;

	Activity mAcitvity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		generateSettingPage();

		if (gs.isSidebarOn()) {
			// here we start a backend service and create the viewgroup
			Intent mIntent = new Intent(this, BackgroundService.class);
			this.startService(mIntent);
		}
	}

	private void generateSettingPage() {
		gs = GeneralSettings.getInstance(getApplicationContext());
		gs.loadGeneralSettings();

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		// we can only get the screen size in the activity
		gs.setScreenWidth(metric.widthPixels);
		gs.setScreenHeight(metric.heightPixels);

		favAppNumText = (TextView) findViewById(R.id.sidebar_width_text);
		favAppNumText.setText(Integer.toString(gs.getSavedFavAppNum()));

		favAppNumSeek = (SeekBar) findViewById(R.id.sidebar_width_selector);
		favAppNumSeek.setMax(gs.getMaxFavAppNum());
		favAppNumSeek.setProgress(gs.getSavedFavAppNum());
		favAppNumSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean isUser) {
				favAppNumText.setText(Integer.toString(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekbar) {
				gs.setSavedFavAppNum(seekbar.getProgress());
			}

		});

		Switch sidebarSwitch = (Switch) findViewById(R.id.sidebarSwitch);
		sidebarSwitch.setChecked(gs.isSidebarOn());
		sidebarSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				gs.setSidebarOn(isChecked);
				Intent mIntent = new Intent(getApplicationContext(),
						BackgroundService.class);

				if (isChecked) {
					getApplicationContext().startService(mIntent);
				} else {
					getApplicationContext().stopService(mIntent);
				}
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
