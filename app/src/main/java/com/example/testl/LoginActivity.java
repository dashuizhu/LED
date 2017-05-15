/*
 * Copyright (c) Guangzhou Waytronic Electronics 
 * 
 * 		Waytronic specializing in intelligent terminal application research and extension,
 * 	intelligent speech technology research and design. 
 * 		Intelligent business was established in 2011, research and development and 
 *	a variety of intelligent mobile phone listing for individual center products. 
 *	Intelligence division to provide a series of intelligent terminal product solutions, 
 *	with each brand enterprise, realizing the intelligent products. Provide 
 * 	customized software, hardware customization, customer service support and a series 
 *	of cooperation model.
 *
 *		 http://www.wt-smart.com 
 *		 http://www.w1999c.com
 */
package com.example.testl;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.util.SetupData;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * <p>
 * Description: Logo显示3秒
 * </p>
 * @author zhujiang
 * @date 2014-5-22
 */
public class LoginActivity extends Activity {

	/**
	 * 读取本地记录
	 */
	private SetupData mSetupData;
	
	//handler
	private static final int handler_ap_linking = 10;
	private static final int handler_ap_scan = 11;
	private static final int handler_ap_notfound = 12;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_logo);
		mSetupData = SetupData.getSetupData(this);
		guideViewActivity();
		//屏幕宽度
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

	}
	
	public Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch(msg.what) {
//				case ConnectionInterface.LinkSuccess:
//					//在这个界面收到 ，连接成功， 那就只有 在  AP直连下 ，连接成功
//					finish();
//					break;
//				case handler_ap_linking:
//					ToastNew.makeText(LoginActivity.this, R.string.ap_linking, 3).show();
//					break;
//				case handler_ap_scan:
//					ToastNew.makeText(LoginActivity.this, R.string.ap_scan, 3).show();
//					break;
//				case handler_ap_notfound:
//					ToastNew.makeText(LoginActivity.this, getString(R.string.ap_notfind)+AppConstants.AP_SSID, 3).show();
//					break;
//			}
//		}
	};
	
	
	/**
	 * 延时三秒跳转
	 */
	private void guideViewActivity() {
		 handler.postDelayed(new Runnable() {
			 
			 @Override
			 public void run() {
				 // TODO Auto-generated method stub
					 Intent intent = new Intent(LoginActivity.this, DeviceViewPageActivity.class);
					 startActivity(intent);
					 finish();
			 }
		 }, 2000);
	}
	
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void onBackPressed() {
	}
	
//	//按两下  back键， 退出
//	private long exitTime = 0;  
//	@Override  
//	public boolean onKeyDown(int keyCode, KeyEvent event) {  
//			if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
//				if((System.currentTimeMillis()-exitTime) > 3000){  
//					Toast.makeText(getApplicationContext(), R.string.exit_app, 3).show();        
//					exitTime = System.currentTimeMillis();  
//				}  
//				else{  
//					setResult(Activity.RESULT_CANCELED);
//					finish();
//					//System.exit(0);  
//				}  
//				return true;  
//			}
//		    return super.onKeyDown(keyCode, event);  
//		}  
//	
	
}
