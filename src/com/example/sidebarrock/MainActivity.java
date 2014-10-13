package com.example.sidebarrock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * This is the entry for this application.
 * In this entry, we mainly create a service to handle creating a view group for sidebar,
 * and also listeners to some gestures.
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
		
		// here we start the rocketsidebar service and create the viewgroup
		Intent mIntent = new Intent(this, GestureService.class);
		this.startService(mIntent);
		
		//hmm, below part is a sample code for seekbar
		gs = GeneralSettings.getInstance(getApplicationContext());
		gs.loadGeneralSettings();
		
		sidebarWidthText = (TextView) findViewById(R.id.sidebar_width_text);
		sidebarWidthText.setText(Integer.toString(gs.getSavedWidth()));
		
		sidebarWidthSeek = (SeekBar) findViewById(R.id.sidebar_width_selector);
		sidebarWidthSeek.setMax(gs.getMaxWidth());
		sidebarWidthSeek.setProgress(gs.getSavedWidth());
		sidebarWidthSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean isUser) {
				sidebarWidthText.setText(Integer.toString(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekbar) {
				gs = GeneralSettings.getInstance(getApplicationContext());
				gs.setSavedWidth(seekbar.getProgress());
			}
			
		});

		// here's two buttons to create/destroy the service as well as the viewgroup for sidebar
		Button startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(getApplicationContext(), GestureService.class);
				getApplicationContext().startService(mIntent);
			}
			
		});
		
		Button stopBtn = (Button) findViewById(R.id.stopBtn);
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(getApplicationContext(), GestureService.class);
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

}
