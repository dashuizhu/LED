package com.example.testl;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.constants.AppString;
import com.zby.ibeacon.util.SetupData;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * @author Administrator
 *	设置界面
 */
public class SettingActivity extends BaseActivity {
	
	private SetupData mSetupData;
	
	private CheckBox cb_autoLink;
	
	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initViews();
	}
	
	private void initViews() {
		initBaseViews(this);
		tv_title.setText(R.string.setting);
		layout_back.setVisibility(View.GONE);
		mSetupData = SetupData.getSetupData(this);
		cb_autoLink = (CheckBox ) findViewById(R.id.checkBox_autoLink);
		cb_autoLink.setChecked(mSetupData.readBoolean(AppString.auto_link, true));
		tv_version = (TextView) findViewById(R.id.textView_version);

		PackageManager pm = getPackageManager();
		PackageInfo pinfo;
		try {
			pinfo = pm.getPackageInfo(getPackageName(), 0);
			tv_version.setText("V"+pinfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cb_autoLink.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				mSetupData.saveboolean(AppString.auto_link, arg1);
			}
		});
	}
	
	public void onBtnClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.layout_help:
			intent = new Intent(this, SettingGuideViewActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_update:
			break;
		case R.id.layout_about:
			intent = new Intent(this, SettingHelpActivity.class);
			startActivity(intent);
			break;
		}
	}

}
