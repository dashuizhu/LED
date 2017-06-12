package com.example.testl;

import java.util.Date;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.ibeacon.bean.TimingBean;
import com.zby.ibeacon.constants.AppConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TimingDetailActivity extends BaseActivity {

	private TimingBean tbin;
	
	private SeekBar sb_brightness, sb_color;
	private CheckBox cb_enable;
	private TextView tv_time , tv_brightness, tv_color, tv_secneName;
	
	private final static int activity_scene = 11;//到模式选折界面
	private final static int activity_time = 12;//到年月日时间选择界面
	
	private  int sceneId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timing_detail);
		initViews();
	}
	
	private void initViews() {
		initBaseViews(this);
		tv_title.setText(R.string.timing);
		tbin = (TimingBean) getIntent().getSerializableExtra("timingBean");
		cb_enable = (CheckBox) findViewById(R.id.checkBox_enable);
		tv_time = (TextView) findViewById(R.id.textView_time);
		sb_brightness = (SeekBar) findViewById(R.id.seekBar_brightness);
		sb_color = (SeekBar) findViewById(R.id.seekBar_color);
		cb_enable.setChecked(tbin.isEnable());
		tv_brightness = (TextView) findViewById(R.id.textView_brightness);
		tv_color = (TextView) findViewById(R.id.textView_color);
		tv_secneName = (TextView) findViewById(R.id.textView_mode);
		
		tbin = (TimingBean) getIntent().getSerializableExtra("timingBean");
		if(tbin.getYear()==0) {
			tv_time.setText(R.string.select_time);
		} else {
			tv_time.setText( tbin.getTimeString());
		}
		sceneId = tbin.getMode();
		tv_secneName.setText(tbin.getName());
		
		sb_brightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				//sb_color.setProgress(0);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				tv_brightness.setText(getString(R.string.brightness)+" "+progress+"%");
			}
		});
		sb_color.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				//sb_brightness.setProgress(0);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				tbin.setColorYellow(progress);
				tv_color.setText(getString(R.string.color)+": "+tbin.getColorYellow()+"%");
			}
		});
		
		sb_brightness.setProgress(tbin.getBrightness());
		sb_color.setProgress(tbin.getColorYellow());
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode) {
		case activity_scene:
			if(resultCode == Activity.RESULT_OK) {
				SceneModeBean sbin = (SceneModeBean) data.getSerializableExtra("sceneModeBean");
				sb_brightness.setProgress(sbin.getRedBright());
				sb_color.setProgress(sbin.getMixBirght());
				tv_secneName.setText(sbin.getName());
				sceneId = sbin.getId();
				tbin.setMode(sceneId);
				tbin.setName( sbin.getName());
			}
			break;
		case activity_time:
			if(resultCode == Activity.RESULT_OK) {
				Date d = new Date();
				//D。getYear是以1900为0
				int year = data.getIntExtra("year", d.getYear()+1900);
				//getMonth是以0开始
				int month = data.getIntExtra("month", d.getMonth()+1);
				int day = data.getIntExtra("day", d.getDate());
				int hour = data.getIntExtra("hour", d.getHours());
				int minute = data.getIntExtra("minute", d.getMinutes());
				tbin.setYear(year);
				tbin.setMonth(month);
				tbin.setDay(day);
				tbin.setHour(hour);
				tbin.setMinute(minute);
				showTime();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onBtnClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.button_confirm:
			if(tbin.getYear()==0) {
				showToast(R.string.select_time);
				return;
			}
			tbin.setBrightness(sb_brightness.getProgress());
			tbin.setColorYellow(sb_color.getProgress());
			tbin.setEnable(cb_enable.isChecked());
			intent = getIntent();
			intent.putExtra("timingBean", tbin);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.layout_mode:
			intent = new Intent(this, DialogSceneListActivity.class);
			intent.putExtra("sceneId", sceneId);
			startActivityForResult(intent, activity_scene);
			break;
		case R.id.layout_time:
			intent  = new Intent(this, GetWheelActivity.class);
			if(tbin.getYear()!=0){
				intent.putExtra("year", tbin.getYear());
				intent.putExtra("month", tbin.getMonth());
				intent.putExtra("day", tbin.getDay());
				intent.putExtra("hour", tbin.getHour());
				intent.putExtra("minute", tbin.getMinute());
			}
			startActivityForResult(intent, activity_time);
			break;
		}
	}
	
	private void showTime() {
		tv_time.setText(
				tbin.getTimeString());
	}

}
