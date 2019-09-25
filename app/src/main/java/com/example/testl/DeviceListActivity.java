package com.example.testl;

import android.Manifest;
import android.text.TextUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.agreement.CmdPackage;
import com.zby.ibeacon.agreement.CmdParse;
import com.zby.ibeacon.agreement.ConnectBroadcastReceiver;
import com.zby.ibeacon.agreement.Encrypt;
import com.zby.ibeacon.bean.DeviceBean;
import com.zby.ibeacon.bluetooth.BleBin;
import com.zby.ibeacon.bluetooth.BluetoothLeServiceMulp;
import com.zby.ibeacon.bluetooth.ConnectionInterface;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.constants.AppString;
import com.zby.ibeacon.manager.AlertDialogService;
import com.zby.ibeacon.manager.AlertDialogService.onMyInputListener;
import com.zby.ibeacon.manager.DeviceManager;
import com.zby.ibeacon.util.MyByte;
import com.zby.ibeacon.util.Myhex;
import com.zby.ibeacon.util.SetupData;
import com.zby.led.adapter.DeviceAdapter;
import com.zby.led.adapter.DeviceAdapter.DeviceControlListener;
import com.zby.led.help.ValueCheckHelp;
import com.zby.led.sql.DeviceSqlService;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import rx.functions.Action1;

/**
 * @author Administrator
 * 设备列表， 鉴于某些手机上会存在 要先搜索一下，才能连接的情况， 所以这里也做一下搜索
 */
public class DeviceListActivity extends BaseActivity {
	
	private final static String TAG = DeviceListActivity.class.getSimpleName();
	
	private static final int activity_scan = 11;//搜索
	private static final int activity_bt_enable =12;//打开蓝牙
	
	private List<DeviceBean> list;
	private ListView listView;
	private DeviceAdapter adapter;
	
	private DeviceSqlService deviceSqlService;
	
	private BluetoothAdapter btAdapter;
	
	private BluetoothLeServiceMulp mBluetoothLeService;
	
	private SetupData mSetupData;
	/**
	 * 密码框
	 */
	private Dialog dialog_input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		initViews();
		initBleService();
		initHandler();
		isAutoLink = mSetupData.readBoolean(AppString.auto_link, true);
		if(isAutoLink) {
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					autoLinkLastDevice();
				}
			}, 1000);
		}

		new RxPermissions(this)
				.request(Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {
			@Override
			public void call(Boolean aBoolean) {
				if (!aBoolean) {
					showToast("请打开定位权限");
				}
			}
		});
	}
	
	private void initViews() {
		initBaseViews(this);
		layout_back.setVisibility(View.VISIBLE);
		tv_title.setText(R.string.device_list);
		layout_back.setVisibility(View.GONE);
		tv_menu.setBackgroundResource(R.drawable.btn_scan);
		layout_menu.setVisibility(View.VISIBLE);
		layout_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DeviceListActivity.this, DeviceScanActivity.class);
				getParent().startActivityForResult(intent, activity_scan);
				if(btAdapter!=null) {
					if(isStoped) {
						btAdapter.stopLeScan(scanCallBack);
						isStoped = false;
					}
				}
			}
		});
		
		deviceSqlService = new DeviceSqlService(this);
		mSetupData = SetupData.getSetupData(this);
		list = deviceSqlService.selectAll();
		if(AppConstants.isDemo) {
			DeviceBean bean = new DeviceBean();
			bean.setName("测试1");
			bean.setDeviceAddress("aabbccddeef1");
			bean.setConnectionInterface(new BleBin(this, mHandler, mBluetoothLeService), this);
			list.add(bean);
			
			DeviceBean bean2 = new DeviceBean();
			bean2.setConnectionInterface(new BleBin(this, mHandler, mBluetoothLeService), this);
			bean2.setName("测试2");
			bean2.setDeviceAddress("aabbccddeef2");
			list.add(bean2);
			
		}
		
		adapter = new DeviceAdapter(this, list);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		adapter.setOnControlListener(new DeviceControlListener() {
			
			@Override
			public void onSwitch(int position, DeviceBean bean) {
				// TODO Auto-generated method stub
				if(bean.islink()){
					bean.controlSwithc();
					bean.setColorYellow(bean.getOldColorYellow());
					if(	bean.isOnOff()){
						bean.setBrightness(bean.getOldBrightness());
					} else {
						bean.setBrightness(0);
					}
					adapter.notifyDataSetChanged();
				} else {
					showToast(R.string.nolink);
				}
			}
			
			@Override
			public void onLink(int position, DeviceBean bean) {
				// TODO Auto-generated method stub
				if(!enableBTAdapter()) {
					return;
				}
				if(DeviceManager.getInstance().getDeviceBean()!=null) {
					if( DeviceManager.getInstance().getDeviceBean().islink() ||  DeviceManager.getInstance().getDeviceBean().isConnecting()) {
						DeviceManager.getInstance().getDeviceBean().closeAll();
						adapter.notifyDataSetChanged();
						//如果 之前选中的设备 跟现在是同一个就 直接停止, 表明只是想 断开连接中的设备
						if(DeviceManager.getInstance().getDeviceBean().getDeviceAddress().equals(bean.getDeviceAddress())) {
							return;
						}
					}
				} 
				//连接新选中的设备
					if(bean.getConnectionInterface()==null) {
						bean.setConnectionInterface(new BleBin(DeviceListActivity.this, mHandler, mBluetoothLeService), DeviceListActivity.this);
					}
					DeviceManager.getInstance().setDeviceBean(bean);
					if(!bean.islink()){
						showToast(R.string.linking);
						bean.connect();
						mSetupData.save(AppString.LastDevice, bean.getDeviceAddress());
					} 
			}
			
			@Override
			public void onEdit(int position, DeviceBean bean) {
				// TODO Auto-generated method stub
				if(bean.islink()){
					showNameDialog(bean);
				} else {
					showToast(R.string.nolink);
				}
			}
			
			@Override
			public void onDelete(int position,final DeviceBean bean) {
				// TODO Auto-generated method stub
				dialog_input = AlertDialogService.getConfirmDialog(DeviceListActivity.this, getString(R.string.dialog_delete_info) , "", new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						bean.closeConnect();
						deviceSqlService.delete(bean.getId());
						synchronized (list) {
							list.remove(bean);
							adapter.notifyDataSetChanged();
						}
						dialog_input.dismiss();
					}
				});
				dialog_input.show();
			}

			@Override
			public void onLightDetail(int position, DeviceBean bean) {
				// TODO Auto-generated method stub
				//两次点击的不是同一个设备，就先将前面的设备关闭连接
				DeviceManager.getInstance().setDeviceBean(bean);
				if(AppConstants.isDemo) {
					Intent intent = new Intent(DeviceListActivity.this, ViewPageGroupActivity.class);
					startActivity(intent);
				} else {
					if(bean.getConnectionInterface()!=null && bean.getConnectionInterface().isLink()) {
						//没有密码
						//if(bean.getPassword()==null || bean.getPassword().equals("")) {
						//	if(bean.getDevicePassword()==null) {
						//		bean.writeAgreement(CmdPackage.getReadPassword());
						//	}
						//	showPasswordDialog(bean);
						//} else {//有密码
						//	if(bean.getDevicePassword()==null || bean.getDevicePassword().equals("")) {
						//		//读取下位机密码
						//		bean.writeAgreement(CmdPackage.getReadPassword());
						//	} else if(bean.getDevicePassword().equals(bean.getPassword())) {//密码正确
								Intent intent =new Intent (DeviceListActivity.this, ViewPageGroupActivity.class);
								startActivity(intent);
							//} else {//密码不对
							//	showPasswordDialog(bean);
							//}
						//}
					} else {//没有连接设备
						showToast(R.string.nolink);
					}
						
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				synchronized (list) {
					list.get(arg2).reversalShow();
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void initHandler() {
		mHandler = new Handler() {
			public void handleMessage(Message msg){ 
				Intent intent;
				switch(msg.what) {
					case ConnectionInterface.LinkSuccess:
//						intent = new Intent(DeviceListActivity.this, DeviceSettingActivity2.class);
//						startActivity(intent);
						showToast(R.string.linkSuccess);
						adapter.notifyDataSetChanged();
						break;
					case ConnectionInterface.LinkFailure:
						adapter.notifyDataSetChanged();
						showToast(R.string.linkFail);
						break;
					case ConnectionInterface.linkLost:
						showToast(R.string.linkLost);
						adapter.notifyDataSetChanged();
						intent = new Intent(DeviceListActivity.this, DeviceViewPageActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
						startActivity(intent);
						break;
					case ConnectionInterface.RawData:
						byte[] buff = (byte[]) msg.obj;
						byte[] buffer= Encrypt.ProcessCommand(buff, buff.length);
						if (buffer != null) {
							DeviceManager.getInstance().getDeviceBean().getParse().parseData(buffer);
						}
						break;
					 case ConnectBroadcastReceiver.Broad_Cmd:
						 switch(msg.arg1){
							 case CmdParse.Cmd_A0_status:
								 adapter.notifyDataSetChanged();
								 break;
							 case CmdParse.Cmd_A1_timing:
								 break;
							 case CmdParse.Cmd_A2_password:
								 deviceSqlService.update(DeviceManager.getInstance().getDeviceBean());
								 break;
							 case CmdParse.Cmd_A3_name:
								 adapter.notifyDataSetChanged();
								 deviceSqlService.update(DeviceManager.getInstance().getDeviceBean());
								 break;
						 }
						break;
					}
				}
		};
	}
	
	
	
	
		// TODO Auto-generated method stub
	public void handleActivityResultCode(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode) 
		{
			case activity_scan:
				if(resultCode == Activity.RESULT_OK) {
					String name= data.getStringExtra("deviceName");
					String mac=  data.getStringExtra("deviceMac");
					foundDeviceBean(name.trim(), mac);
				}
			break;
			case activity_bt_enable:
				if(resultCode == activity_bt_enable) {
					startScan();
				}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
		switch(requestCode) 
		{
			case activity_scan:
				if(resultCode == Activity.RESULT_OK) {
					String name= data.getStringExtra("deviceName");
					String mac=  data.getStringExtra("deviceMac");
					foundDeviceBean(name.trim(), mac);
				}
			break;
		}
			super.onActivityResult(requestCode, resultCode, data);
		}

	public void onBtnClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()) {
		case R.id.btn_lightList:
			intent.setClass(this, DeviceScanActivity.class);
			startActivityForResult(intent, activity_scan);
			break;
		case R.id.btn_setting:
			intent.setClass(this, SettingActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
//		Log.d(TAG, "onStart");
//		if(enableBTAdapter()){
//			//startScan();
//		}
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		if(enableBTAdapter()){
			//startScan();
		}
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStop");
		if(btAdapter!=null) {
			if(isStoped) {
				btAdapter.stopLeScan(scanCallBack);
				isStoped = false;
			}
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestory");
		unregisterReceiver(mGattUpdateReceiver);
		if (mBluetoothLeService != null) {
			mBluetoothLeService.closeAll();
		}
		super.onDestroy();
	}

	/**
	 * 获得打开的蓝牙适配器
	 */
	private boolean enableBTAdapter() {
		if (btAdapter == null) {
			BluetoothManager bm = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
			if (bm == null) {
				Toast.makeText(this, R.string.bluetooth_not_support,
						Toast.LENGTH_LONG);
				finish();
				return false;
			}
			btAdapter = bm.getAdapter();
			if (btAdapter == null) {
				Toast.makeText(this, R.string.bluetooth_not_support,
						Toast.LENGTH_LONG);
				finish();
				return false; 
			}
		}
		if (!btAdapter.enable()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			getParent().startActivityForResult(intent, activity_bt_enable);
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * bindService
	 */
	private void initBleService() {
		Intent intent = new Intent(this, BluetoothLeServiceMulp.class);
//		startService(intent);
		getApplicationContext().bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}
	
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeServiceMulp.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				finish();
			}
//			// Automatically connects to the device upon successful start-up
//			// initialization.
//			mBluetoothLeService.connect(mDeviceAddress);
//			ble = new BleBin(DeviceListActivity.this, mHandler, mBluetoothLeService);
//			Message msg = handler.obtainMessage();
//			msg.what = ConnectionInterface.LinkSuccess;
//			msg.obj = 
//			handler.sendEmptyMessage(ConnectionInterface.LinkSuccess);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};
	
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			Log.d(TAG,"收到回执的action "+ this.hashCode() + action);
			if (BluetoothLeServiceMulp.ACTION_GATT_CONNECTED.equals(action)) {
				//mHandler.sendEmptyMessage(ConnectionInterface.LinkSuccess);
				showToast(R.string.toast_search_services);
			} else if (BluetoothLeServiceMulp.ACTION_GATT_DISCONNECTED.equals(action)) {
				mHandler.sendEmptyMessage(ConnectionInterface.linkLost);
			}
			//else if(BluetoothLeServiceMulp.ACTION_GATT_RECONNECTING.equals(action)) {
			//	showToast("重连中。。。。");
			//}
			else if(BluetoothLeServiceMulp.ACTION_SEND_SUCCESS.equals(action)) {
				showToast(R.string.send_success);
			}
		else if (BluetoothLeServiceMulp.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				if(intent.hasExtra("mac")) {
					String mac = intent.getStringExtra("mac");
					mSetupData.save("lastLinkMac", mac);
					mBluetoothLeService.setReceiver(mac);
					mHandler.sendEmptyMessage(ConnectionInterface.LinkSuccess);
					//刚连上一瞬间，发送数据是读取不到的。所以做了个延迟多次读取
					new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							DeviceBean bin = DeviceManager.getInstance().getDeviceBean();
							bin.writeAgreement(CmdPackage.getReadStatus());
							try {
								Thread.sleep(AppConstants.delayTime);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bin.writeAgreement(CmdPackage.getReadPassword());
							try {
								Thread.sleep(AppConstants.delayTime);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							bin.writeAgreement(CmdPackage.getReadName());
							try {
								Thread.sleep(AppConstants.delayTime);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bin.writeAgreement(CmdPackage.setVelidateDate());
							try {
								Thread.sleep(AppConstants.delayTime);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							bin.writeAgreement(CmdPackage.getReadStatus());
							try {
								Thread.sleep(AppConstants.delayTime);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bin.writeAgreement(CmdPackage.getReadPassword());
						}
						
					}).start();
				}
			} else if (BluetoothLeServiceMulp.ACTION_DATA_AVAILABLE.equals(action)) { //解析数据
				String buffer = intent.getStringExtra(BluetoothLeServiceMulp.EXTRA_DATA);
				Log.e("tag", "接受数据"+ buffer);
				//showToast("收到"+buffer);
				if(mBluetoothLeService!=null && buffer !=null) {
					byte[] buff = Myhex.hexStringToByte(buffer);
					if(buff.length>2) {//模块的数据
						Message msg = mHandler.obtainMessage();
						msg.what = ConnectionInterface.RawData;
						msg.obj = buff;
						mHandler.sendMessage(msg);
					} else if(buff.length==1) {//电量通道的数据
						if(DeviceManager.getInstance().getDeviceBean()!=null) {
							DeviceManager.getInstance().getDeviceBean().setEle(MyByte.byteToInt(buff[0]));
							Intent aintent = new Intent (ConnectBroadcastReceiver.BROADCAST_ACTION);
							aintent.putExtra(ConnectBroadcastReceiver.BROADCAST_DATA_TYPE, 0xEE);
//						intent.putExtra(ConnectBroadcastReceiver.BROADCAST_DATA_KEY, data);
							sendBroadcast(aintent);
						}
					}
				}
 			}
		}
	};
	
	/**
	 * BLE broadcast Filter
	 * @return
	 */
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeServiceMulp.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeServiceMulp.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeServiceMulp.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeServiceMulp.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeServiceMulp.ACTION_SEND_SUCCESS);
		return intentFilter;
	}
	int count =1;
	
	/**
	 * 弹出密码输入框
	 * @param bean
	 */
	private void showPasswordDialog(final DeviceBean bean){
		dialog_input = AlertDialogService.getInputPasswordDialog(this, "", getString(R.string.input_password), new onMyInputListener() {

			@Override
			public void onClick(Dialog d, EditText tv) {
				// TODO Auto-generated method stub
				String string = tv.getText().toString();
				String devicePsd = bean.getDevicePassword();
				if(string!=null && string.equals(bean.getDevicePassword())) {
					bean.setPassword(string);
					d.dismiss();
					showToast(R.string.password_success);
					deviceSqlService.insert(bean);
					Intent intent =new Intent (DeviceListActivity.this, ViewPageGroupActivity.class);
					startActivity(intent);
				}  else {
					Log.d("tag", " error count;"+count + " " + devicePsd);
					if(count>=3) {
						count =1;
						showToast(getString(R.string.password_error)+devicePsd);
					} else {
						count++;
						showToast(getString(R.string.password_error));
					}
					tv.setText("");
					tv.requestFocus();
				}
			}
		});
		dialog_input.show();
	}
	
	
	/**
	 * 弹出密码输入框
	 * @param bean
	 */
	private void showNameDialog(final DeviceBean bean){
		dialog_input = AlertDialogService.getInputDialog(this, bean.getName().trim(), getString(R.string.input_name), new onMyInputListener() {
			
			@Override
			public void onClick(Dialog dialog, EditText et) {
				// TODO Auto-generated method stub
				String name = et.getText().toString().trim();
				Log.d("tag", "name="+name);
				if(ValueCheckHelp.helpValueCheck(et, DeviceListActivity.this)) {
					bean.writeAgreement(CmdPackage.setD4Name(name));
					bean.setName(name.trim());
					adapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			}
		});
		dialog_input.show();
	}
	
	/**
	 * 更新或者添加设备到本地
	 * @param bluetoothName
	 * @param mac
	 */
	private void foundDeviceBean(String bluetoothName, String mac){
		DeviceBean bin;
		synchronized (list) {
			for(int i =0; i<list.size(); i++) {
				bin = list.get(i);
				if(bin.getDeviceAddress().equalsIgnoreCase(mac)) {
					bin.setBluetoothName(bluetoothName);
					adapter.notifyDataSetChanged();
					return;
				}
			}
			//不在当前列表中，就新添加一个，
			bin = new DeviceBean();
			bin.setBluetoothName(bluetoothName);
			bin.setDeviceAddress(mac);
			bin.setConnectionInterface(new BleBin(this, mHandler, mBluetoothLeService), this);
			int id = (int) deviceSqlService.insert(bin);
			bin.setId(id);
			list.add(bin);
			adapter.notifyDataSetChanged();
		}
	}
	
	
	/**
	 * 开始蓝牙搜索，
	 */
	private void startScan() {
		isStoped = true;
		btAdapter.startLeScan(scanCallBack);
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(isStoped){
					btAdapter.stopLeScan(scanCallBack);
				}
			}
		}, 10000);
	}
	
	/**
	 * 离开此页面 ，到搜索页面立即停止搜索，  并且延时10秒的搜索停止也不执行
	 */
	private boolean isStoped = false;
	
	/**
	 * 是否已经自动连接过
	 */
	private boolean hasAutoLink = false;
	
	/**
	 * 是否需要自动连接
	 */
	private boolean isAutoLink = false;
	/**
	 * 蓝牙设备搜索 监听
	 */
	private BluetoothAdapter.LeScanCallback scanCallBack = new LeScanCallback() {

		@Override
		public void onLeScan(BluetoothDevice arg0, int arg1, byte[] arg2) {
			// TODO Auto-generated method stub
			Log.d(TAG,
					"后台搜索设备: " + arg0.getName() == null ? "" : arg0
							.getName() + " " + arg0.getAddress());
			autoLinkLastDevice(arg0.getAddress());
		}
	};
	
	/**
	 * 这个是针对搜索过后的
	 * @param mac
	 */
	private void autoLinkLastDevice(String mac) {
		if(isAutoLink&& hasAutoLink) return;
		String lastMac = mSetupData.read("lastLinkMac");
		synchronized (list) {
			for(int i=0;i<list.size();i++) {
				if(list.get(i).getDeviceAddress().equals(mac)) {
					if(lastMac.endsWith(mac)) {
						if(!list.get(i).islink()) {
							list.get(i).connect();
							Log.v(TAG, "自动连接最后一次设备"+mac);
						}
						//自动连接
						hasAutoLink = true;
					}
				}
			}
			
		}
	}
	
	private void autoLinkLastDevice() {
		if(isAutoLink&& hasAutoLink) return;
		String lastMac = mSetupData.read("lastLinkMac");
		synchronized (list) {
			for(int i=0;i<list.size();i++) {
				if(list.get(i).getDeviceAddress().equals(lastMac)) {
						DeviceBean bean = list.get(i);
						if(bean.getConnectionInterface()==null) {
							bean.setConnectionInterface(new BleBin(DeviceListActivity.this, mHandler, mBluetoothLeService), DeviceListActivity.this);
						}
						DeviceManager.getInstance().setDeviceBean(bean);
						if(!list.get(i).islink()) {
							list.get(i).connect();
							Log.v(TAG, "自动连接最后一次设备"+lastMac);
						}
						//自动连接
						hasAutoLink = true;
				}
			}
			
		}
	}

}
