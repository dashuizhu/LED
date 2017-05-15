package com.zby.ibeacon.agreement;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * <p>Description: 广播传递通讯消息 </p>
 * @author zhujiang
 * @date 2014-5-30
 */
public	class ConnectBroadcastReceiver extends BroadcastReceiver {
	
	private static String TAG = "ConnectBroadcastReceiver";
	
		/**
		 * 数据传递的key 值
		 */
		public static final String BROADCAST_DATA_KEY = "data";
		public static final String BROADCAST_DATA_TYPE = "type";
		public static final String BROADCAST_DEVICE_MAC = "deviceMac";
		
		public static final int Broad_Cmd = 999;
		
		/**
		 * 广播事件名
		 */
		public static final String BROADCAST_ACTION = "com.wt.isensor.broadcast";
	
		private Handler mHandler;
		
		public ConnectBroadcastReceiver(Handler handler) {
			this.mHandler = handler;
		}
	
		@Override
		public void onReceive(Context context, Intent intent) {
			int type=  intent.getIntExtra(BROADCAST_DATA_TYPE, -1);
			int i = intent.getIntExtra(BROADCAST_DATA_KEY, -1);
			Log.i(TAG, "broad收到 type ="+type + "  " + i);
			if(mHandler!=null) {
				Message msg = mHandler.obtainMessage();
				msg.what = Broad_Cmd;
				msg.arg1 = type;
				msg.arg2 = i;
				String mac = intent.getStringExtra(BROADCAST_DEVICE_MAC);
				if(mac!=null) {
					msg.obj = mac;
				}
				mHandler.sendMessage(msg);
			} else {
				Log.d(TAG, "handler ==null , " + type + " " + i);
			}
		}
		
		
//		public static void handlerSendBroadcast(Activity activity,int type) {
//			Intent intent = new Intent (ConnectBroadcastReceiver.BROADCAST_ACTION);
//			intent.putExtra(ConnectBroadcastReceiver.BROADCAST_DATA_TYPE, type);
////			intent.putExtra(ConnectBroadcastReceiver.BROADCAST_DATA_KEY, data);
////			Log.d("tag", "MainActivity.sendBroadcast    " + type + " " + data);
//			activity.sendBroadcast(intent);
//		}
	}