package com.example.testl;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.agreement.CmdPackage;
import com.zby.ibeacon.bean.DeviceBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.manager.AlertDialogService;
import com.zby.ibeacon.manager.DeviceManager;
import com.zby.ibeacon.manager.AlertDialogService.onMyInputListener;
import com.zby.led.help.ValueCheckHelp;
import com.zby.led.sql.DeviceSqlService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ViewPagerSettingActivity extends BaseActivity {
	
	private Dialog dialog;
	private DeviceBean dbin;
	private DeviceSqlService deviceSqlService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager_settingggg);
		initViews();
	}
	
	private void initViews() {
		initBaseViews(this);
		tv_title.setText(R.string.mode_setting);
		dbin = DeviceManager.getInstance().getDeviceBean();
		deviceSqlService = new DeviceSqlService(this);
	}
	
	public void onBtnClick(View v){
		switch(v.getId()){
			case R.id.layout_timing:
				Intent intent =new Intent(this, TimingListActivity.class);
				startActivity(intent);
				break;
			case R.id.layout_hijack:
				dialog = AlertDialogService.getInputPasswordDialog(this, "", getString(R.string.input_password), new onMyInputListener() {
					
					@Override
					public void onClick(Dialog d, EditText tv) {
						// TODO Auto-generated method stub
						String name= tv.getText().toString().trim();
						if(name.equals("")) {
							showToast(R.string.input_password);
							tv.requestFocus();
							return ;
						}
						if(name.length()!=6) {
							showToast(R.string.password_unformat);
							return;
						}
						dbin.writeAgreement(CmdPackage.setD3Password(name));
						//不保存
//						dbin.setDevicePassword(name);
//						dbin.setPassword(name);
//						deviceSqlService.insert(dbin);
						d.dismiss();
					}
				});
				dialog.show();
				break;
			case R.id.layout_rename:
				dialog = AlertDialogService.getInputDialog(this, dbin.getName().trim(), getString(R.string.input_name), new onMyInputListener() {
					
					@Override
					public void onClick(Dialog d, EditText tv) {
						// TODO Auto-generated method stub
						String name= tv.getText().toString().trim();
						if(ValueCheckHelp.helpValueCheck(tv, ViewPagerSettingActivity.this)) {
							dbin.writeAgreement(CmdPackage.setD4Name(name));
							dbin.setName(name);
							deviceSqlService.insert(dbin);
							dbin.setName(name);
							d.dismiss();
						} 
					}
				});
				dialog.show();
				break;
		}
	}
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
	

}
