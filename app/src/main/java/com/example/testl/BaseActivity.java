package com.example.testl;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.agreement.ConnectBroadcastReceiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {

	protected float phone_density;//屏幕密度
	private int phone_width, phone_height;//屏幕宽高
	
	
	 TextView tv_title;//标题
	 LinearLayout layout_back;//返回layout
	 LinearLayout layout_menu;//右上角按钮layout
	 TextView tv_back , tv_menu;//标题栏中 左边的图片 和 右边的图片
	 
	 private Toast mToast;
	 
	 
	 /**
	 * 协议数据 广播接受
	 */
	private ConnectBroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//屏幕宽度
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		phone_width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		phone_height  = wm.getDefaultDisplay().getHeight();// 屏幕宽度
		phone_density =  getResources().getDisplayMetrics().density; //屏幕密度
	}
	
	protected void initBaseViews(Activity v) {
		//View v = LayoutInflater.from(this).inflate(R.layout.fragment_title, null);
		tv_title = (TextView) v.findViewById(R.id.textView_title);
		layout_back = (LinearLayout) v.findViewById(R.id.layout_back);
		layout_menu = (LinearLayout) v.findViewById(R.id.layout_menu);
		tv_back = (TextView) v.findViewById(R.id.textView_back);
		tv_menu = (TextView) v.findViewById(R.id.textView_menu);
		
		
		
		layout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	/**
	 * 因为ViewPager中子activity启动， onactivityResult是直接返回到ViewPagerActivity中，这里模拟
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void handleActivityResult(int requestCode, int resultCode ,Intent data) {
		onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 显示toast
	 * @param str
	 */
	protected void showToast(String str) {
		if(mToast ==null) {
			mToast = Toast.makeText(this, str, 3);
		}
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setText(str);
		mToast.show();
	}
	
	/**
	 * 显示toast
	 * @param str
	 */
	protected void showToast(int str) {
		if(mToast ==null) {
			mToast = Toast.makeText(this, str, 3);
		}
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setText(str);
		mToast.show();
	}
	
	
	public void btn_back(View v) {
		finish();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		registerCmdBroad();
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterCmdBroad();
		super.onStop();
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



	protected Handler mHandler;
	
	private void registerCmdBroad() {
		if(receiver==null) {
			receiver = new ConnectBroadcastReceiver(mHandler);
			registerReceiver(receiver, new IntentFilter(ConnectBroadcastReceiver.BROADCAST_ACTION));
		}
	}
	
	private void unregisterCmdBroad() {
		if(receiver!=null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}
	
}
