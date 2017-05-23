package com.example.testl;

import java.io.UnsupportedEncodingException;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.ibeacon.constants.AppConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SceneDetailActivity extends BaseActivity {

	private SceneModeBean sbin;
	
	private SeekBar sb_brightness, sb_color;
	private TextView tv_brightness, tv_color;
	private EditText et_name;
	private Button btn_delete;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scene_detail);
		initViews();
	}
	
	public void initViews() {
		et_name = (EditText) findViewById(R.id.editText_name);
		btn_delete = (Button) findViewById(R.id.button_delete);
		sb_brightness = (SeekBar) findViewById(R.id.seekBar_brightness);
		sb_color = (SeekBar) findViewById(R.id.seekBar_color);
		tv_brightness = (TextView) findViewById(R.id.textView_brightness);
		tv_color = (TextView) findViewById(R.id.textView_color);
		
		sbin = (SceneModeBean) getIntent().getSerializableExtra("sceneModeBean");
		if(sbin.getId()==-1) { //是添加
			btn_delete.setVisibility(View.GONE);
		} else { //是修改
			if(sbin.isDeleteable()) {
				btn_delete.setVisibility(View.VISIBLE);
			} else {
				btn_delete.setVisibility(View.GONE);
			}
		}
		et_name.setText(sbin.getName());
		if(sbin.getName().length()>0){
			et_name.setSelection(sbin.getName().length());
		}
		
		sb_brightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
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
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				sbin.setMixBirght(progress);
				tv_color.setText(getString(R.string.color)+": "+sbin.getMixBirght()+"%");
			}
		});
		
		sb_brightness.setProgress(sbin.getRedBright());
		sb_color.setProgress(sbin.getMixBirght());
	}
	
	public void onBtnClick(View v) {
		switch(v.getId()){
			case R.id.button_cancel:
				finish();
				break;
			case R.id.button_delete:
				setResult(Activity.RESULT_FIRST_USER, getIntent());
				finish();
				break;
			case R.id.button_confirm:
				String name = et_name.getText().toString();
				if(name.equals("")) {
					showToast(R.string.input_name);
					et_name.requestFocus();
					return;
				}
			try {
				if (name.getBytes(AppConstants.charSet).length>AppConstants.name_maxLength) {
					showToast(R.string.name_too_long);
					et_name.requestFocus();
					return;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showToast(R.string.name_error);
				et_name.requestFocus();
				return;
			}
				sbin.setName(name);
				sbin.setRedBright(sb_brightness.getProgress());
				sbin.setMixBirght(sb_color.getProgress());
				Intent intent = getIntent();
				intent.putExtra("scedeModeBean", sbin);
				setResult(Activity.RESULT_OK, intent );
				finish();
				break;
		}
	}
}
