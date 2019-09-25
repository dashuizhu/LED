package com.zby.ibeacon.bluetooth;


import android.text.TextUtils;
import com.zby.ibeacon.agreement.CmdParse;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.util.Log;

public class BleBin implements ConnectionInterface{
	
	private final String TAG = BleBin.class.getSimpleName();
	
	private Activity mActivity;
	private Handler mHandler;
	private BluetoothLeServiceMulp mService;
	private BluetoothAdapter mAdapter;
	private String mDeviceAddress;
	
	private boolean isLink;
	
	public BleBin (Activity activity, Handler mHandler, BluetoothLeServiceMulp service) {
		this.mActivity = activity;
		this.mHandler = mHandler;
		this.mService = service;
	}

	@Override
	public void find() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect(String address, String pwd) {
		// TODO Auto-generated method stub
		if (mService!=null) {
			isLink = mService.connect(address);
			if(isLink) {
				mDeviceAddress = address;
			}
		}
	}

	@Override
	public void stopConncet() {
		// TODO Auto-generated method stub
		if(mService!=null && !TextUtils.isEmpty(mDeviceAddress)) {
			mService.disconnect(mDeviceAddress);
		}
	}

	@Override
	public void write(byte[] buffer, boolean showBackToast) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(mDeviceAddress)) {
			return;
		}
		mService.writeLlsAlertLevel(mDeviceAddress, buffer, showBackToast);
	}

	//@Override
	//public void writeAgreement(byte[] buffer) {
	//	if (TextUtils.isEmpty(mDeviceAddress)) {
	//		return;
	//	}
	//	// TODO Auto-generated method stub
	//	if(buffer!=null) {
	//		//mService.writeLlsAlertLevel(Encrypt.ProcessCommand(buffer, buffer.length));
	//		mService.writeLlsAlertLevel(mDeviceAddress, buffer, showBackToast);
	//	}
	//}

	@Override
	public boolean isLink() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(mDeviceAddress)) {
			return false;
		}
		if(mService==null) {
			Log.d(TAG, "service is null");
			return false;
		}
		return mService.isLink(mDeviceAddress);
	}

	@Override
	public void Reconnect() {
		// TODO Auto-generated method stub
		
	}
	
	public String getDeviceAddress() {
		return mDeviceAddress;
	}

//	@Override
//	public void setDataParse(DataProtocolInterface dataParse) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void onBleDestory() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(mDeviceAddress)) {
			return;
		}
		mService.close(mDeviceAddress);
	}

	@Override
	public boolean haveIp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDataParse(CmdParse cmdParse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConnecting() {
		// TODO Auto-generated method stub
		if(mService==null) {
			Log.d(TAG, "service is null");
			return false;
		}
		if (TextUtils.isEmpty(mDeviceAddress)) {
			return false;
		}
		return mService.isConnecting(mDeviceAddress);
	}
	
	public void closeAll() {
		if(mService==null) {
			Log.d(TAG, "service is null");
			return;
		}
		 mService.closeAll();
	}

}
