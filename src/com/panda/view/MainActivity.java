package com.panda.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

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

	TextView sidebarWidthText = null;
	SeekBar sidebarWidthSeek = null;
	GeneralSettings gs = null;
	Intent mServiceIntent = null;

	Activity mAcitvity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_SHORT).show();

		// here we start the rocketsidebar service and create the viewgroup
		Intent mIntent = new Intent(this, BackgroundService.class);
		this.startService(mIntent);

		// hmm, below part is a sample code for seekbar
		gs = GeneralSettings.getInstance(getApplicationContext());
		gs.loadGeneralSettings();

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		gs.setScreenWidth(metric.widthPixels);
		gs.setScreenHeight(metric.heightPixels);

		sidebarWidthText = (TextView) findViewById(R.id.sidebar_width_text);
		sidebarWidthText.setText(Integer.toString(gs.getSavedWidth()));

		sidebarWidthSeek = (SeekBar) findViewById(R.id.sidebar_width_selector);
		sidebarWidthSeek.setMax(gs.getMaxWidth());
		sidebarWidthSeek.setProgress(gs.getSavedWidth());
		sidebarWidthSeek
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean isUser) {
						sidebarWidthText.setText(Integer.toString(progress));
					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekbar) {
						gs = GeneralSettings
								.getInstance(getApplicationContext());
						gs.setSavedWidth(seekbar.getProgress());
					}

				});

		// here's two buttons to create/destroy the service as well as the
		// viewgroup for sidebar
		Button startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(getApplicationContext(),
						BackgroundService.class);
				getApplicationContext().startService(mIntent);
			}

		});

		Button stopBtn = (Button) findViewById(R.id.stopBtn);
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(getApplicationContext(),
						BackgroundService.class);
				getApplicationContext().stopService(mIntent);
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// @Override
	// public void onStart() {
	// Toast.makeText(getBaseContext(), "onStart", Toast.LENGTH_SHORT).show();
	// super.onStart();
	// }
	//
	// @Override
	// public void onRestart() {
	// Toast.makeText(getBaseContext(), "onRestart", Toast.LENGTH_SHORT).show();
	// super.onRestart();
	// }
	//
	// @Override
	// public void onResume() {
	// Toast.makeText(getBaseContext(), "onResume", Toast.LENGTH_SHORT).show();
	// super.onResume();
	// }
	//
	// @Override
	// public void onPause() {
	// Toast.makeText(getBaseContext(), "onPause", Toast.LENGTH_SHORT).show();
	// super.onPause();
	// }
	//
	// @Override
	// public void onStop() {
	// Toast.makeText(getBaseContext(), "onStop", Toast.LENGTH_SHORT).show();
	// super.onStop();
	// }
	//
	// @Override
	// public void onDestroy() {
	// Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_SHORT).show();
	// super.onDestroy();
	// }

}
