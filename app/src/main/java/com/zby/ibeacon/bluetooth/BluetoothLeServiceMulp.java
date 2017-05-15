package com.zby.ibeacon.bluetooth;
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.zby.ibeacon.util.Myhex;



/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeServiceMulp extends Service {

    private final static String TAG = BluetoothLeServiceMulp.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    
    private Map<String , BluetoothGatt> gattMaps  = new HashMap<String ,BluetoothGatt>();
    private Map<String, BluetoothGatt> gattMapsConnting = new HashMap<String, BluetoothGatt>(); 
    
//    private String mBluetoothDeviceAddress;
//    private BluetoothGatt mBluetoothGatt;
  //  private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    /**
     * ���ݷ��ͳɹ��Ļ�ִ
     */
    public final static String ACTION_SEND_SUCCESS = "com.example.bluetooth.le.ACTION_SEND_SUCCESS";
    
    
    public static final UUID SEND_SERVIE_UUID = UUID
			.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	public static final UUID SEND_CHARACTERISTIC_UUID = UUID
			.fromString("0000fff2-0000-1000-8000-00805f9b34fb");

	public static final UUID RECEIVER_SERVICE = UUID
			.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	public static final UUID RECEIVER_CHARACTERISTIC = UUID
			.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
	
	 public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    @SuppressLint("NewApi")
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            String address  = gatt.getDevice().getAddress();
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                //mConnectionState = STATE_CONNECTED;
                gattMaps.put(address, gatt);
                broadcastUpdate(intentAction, address);
                Log.i(TAG, "Connected to GATT server."+address);
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        gatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                //mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction, address);
                close(address);
                gattMaps.remove(address);
            }
            
            gattMapsConnting.remove(address);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
            	String address  = gatt.getDevice().getAddress();
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED, address);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
        	 Log.i(TAG, "onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
        /**
         * �������ݡ�
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            //����
            Log.i("xiawei", characteristic.getValue().toString());
        }

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			Log.d("tag", "���͵Ļ�ִ���͹㲥" +ACTION_SEND_SUCCESS );
			Intent intent = new Intent(ACTION_SEND_SUCCESS);
			sendBroadcast(intent);
			super.onCharacteristicWrite(gatt, characteristic, status);
		}
        
        
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    
    private void broadcastUpdate(final String action, String mac) {
        final Intent intent = new Intent(action);
        intent.putExtra("mac", mac);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        final byte[] data = characteristic.getValue();
        String ss = Myhex.buffer2String(data);
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            if (data != null && data.length > 0) {
                //final StringBuilder stringBuilder = new StringBuilder(data.length);
                //for(byte byteChar : data)
                //    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, ss);
            }
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeServiceMulp getService() {
            return BluetoothLeServiceMulp.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        closeAll();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public synchronized boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
//        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            } else {
//                return false;
//            }
//        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        if(gattMapsConnting.containsKey(address)) {
        	BluetoothGatt ga = gattMapsConnting.get(address);
        	ga.close();
        	gattMapsConnting.remove(address);
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        BluetoothGatt mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        gattMapsConnting.put(address, mBluetoothGatt);
        Log.d(TAG, "Trying to create a new connection.");
        //mBluetoothDeviceAddress = address;
        //mConnectionState = STATE_CONNECTING;
        System.out.println("device.getBondState=="+device.getBondState());
        return true;
    }

    
    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect(String address) {
    	if(gattMaps.containsKey(address)) {
    		Log.d(TAG, "disconnect connect blue : " + address);
    		BluetoothGatt mBluetoothGatt =  gattMaps.get(address);
    		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
    			Log.w(TAG, "BluetoothAdapter not initialized");
    			return;
    		}
    		mBluetoothGatt.disconnect();
    		mBluetoothGatt.close();
    		mBluetoothGatt=null;
    		gattMaps.remove(address);
    	}
    	if(gattMapsConnting.containsKey(address)){
    		Log.d(TAG, "disconnect connecting blue : " + address);
    		BluetoothGatt mBluetoothGatt =  gattMaps.get(address);
    		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
    			Log.w(TAG, "BluetoothAdapter not initialized");
    			return;
    		}
    		mBluetoothGatt.disconnect();
    		mBluetoothGatt.close();
    		mBluetoothGatt=null;
    		 gattMapsConnting.remove(address);
    	}
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close(String address) {
    	if(gattMaps.containsKey(address)) {
    		BluetoothGatt mBluetoothGatt =  gattMaps.get(address);
	        if (mBluetoothGatt == null) {
	            return;
	        }
	       // mBluetoothGatt.disconnect();
	        mBluetoothGatt.close();
	        mBluetoothGatt.close();
	        mBluetoothGatt.close();
	        Log.d(TAG, " close Bluetooth :" + address);
	        mBluetoothGatt.close();
	        gattMaps.remove(address);
	        mBluetoothGatt = null;
    	}
    	if(gattMapsConnting.containsKey(address)){
    		BluetoothGatt mBluetoothGatt =  gattMapsConnting.get(address);
	   	   mBluetoothGatt.disconnect();
	       mBluetoothGatt.close();
	       mBluetoothGatt.close();
	       mBluetoothGatt.close();
	       gattMapsConnting.remove(address);
	       mBluetoothGatt = null;
    	}
    }
    
    public void closeAll(){
    	for(String key:gattMaps.keySet()) {
    		BluetoothGatt gatt = gattMaps.get(key);
    		gatt.disconnect();
    		gatt.close();
    		gatt.close();
    		gatt.close();
    		gatt = null;
    		Log.d(TAG,"close connected: " + key);
    		gattMaps.remove(key);
    	}
    	for(String key:gattMapsConnting.keySet()) {
    		BluetoothGatt gatt = gattMapsConnting.get(key);
    		gatt.disconnect();
    		gatt.close();
    		gatt.close();
    		gatt.close();
    		gatt = null;
    		Log.d(TAG,"close connecting: " + key);
    		gattMapsConnting.remove(key);
    	}
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(String address , BluetoothGattCharacteristic characteristic) {
    	if(gattMaps.containsKey(address)) {
    		BluetoothGatt mBluetoothGatt = gattMaps.get(address);
    				if (mBluetoothAdapter == null || mBluetoothGatt == null) {
    					Log.w(TAG, "BluetoothAdapter not initialized");
    					return;
    				}
    		mBluetoothGatt.readCharacteristic(characteristic);
    	}
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(String address, BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
    	if(gattMaps.containsKey(address)) {
    		BluetoothGatt mBluetoothGatt = gattMaps.get(address);
    		
	        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
	        	 Log.w(TAG, "BluetoothAdapter not initialized");
	            return;
	        }
	        boolean isEnable = mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
	        Log.d(TAG, RECEIVER_CHARACTERISTIC.toString() + " " + characteristic.getUuid().toString());
	        if (RECEIVER_CHARACTERISTIC.toString().equals(characteristic.getUuid().toString())
					) {
	            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
				BluetoothGattDescriptor descriptor = characteristic
						.getDescriptor(UUID
								.fromString(CLIENT_CHARACTERISTIC_CONFIG));
				if (descriptor != null) {
					descriptor
							.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				}
				mBluetoothGatt.writeDescriptor(descriptor);
			}
    	}
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices(String address) {
    	if(gattMaps.containsKey(address)) {
    		BluetoothGatt mBluetoothGatt = gattMaps.get(address);
    		if (mBluetoothGatt == null) return null;
    		return mBluetoothGatt.getServices();
    	}
    	return null;
    }
    
    public void writeLlsAlertLevel(String address, byte[] bb) {
    	if(!gattMaps.containsKey(address)) {
    		Log.e(TAG,"gatt is null " + address);
    		return;
    	}
    	BluetoothGatt mBluetoothGatt = gattMaps.get(address);
		// Log.i("iDevice", iDevice);
		BluetoothGattService linkLossService = mBluetoothGatt
				.getService(SEND_SERVIE_UUID);
		if (linkLossService == null) {
			showMessage("link loss Alert service not found!");
			return;
		}
		// enableBattNoti(iDevice);
		BluetoothGattCharacteristic alertLevel = null;
		boolean status = false;
		alertLevel = linkLossService.getCharacteristic(SEND_CHARACTERISTIC_UUID);
		if (alertLevel == null) {
			showMessage("link loss Alert Level charateristic not found!");
			return;
		}
		int storedLevel = alertLevel.getWriteType();
		Log.d(TAG, "storedLevel() - storedLevel=" + storedLevel);

		
		alertLevel.setValue(bb);

		alertLevel.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
		status = mBluetoothGatt.writeCharacteristic(alertLevel);
		Log.v("tag_send", "����  " + status + " " + Myhex.buffer2String(bb));
		Log.d(TAG, "writeLlsAlertLevel() - status=" + status);
	}
    private void showMessage(String msg) {
		Log.e(TAG, msg);
	}
    
    
    /**
     * ���û��շ��ķ���
     */
    public void setReceiver(String address){
    	if(!gattMaps.containsKey(address)) {
    		Log.e(TAG,"gatt is null " + address);
    		return;
    	}
    	BluetoothGatt mBluetoothGatt = gattMaps.get(address);
    		
    	BluetoothGattService linkLossService = mBluetoothGatt
				.getService(RECEIVER_SERVICE);
    	if(linkLossService==null) return;
    	BluetoothGattCharacteristic characteristic = linkLossService.getCharacteristic(RECEIVER_CHARACTERISTIC);
    	if(characteristic==null) return;
    	
		setCharacteristicNotification(
				address,characteristic, true);
		readCharacteristic(address, characteristic);
    }
    
    protected boolean isLink(String mac) {
    	return gattMaps.containsKey(mac);
    }
	public boolean isConnecting(String mDeviceAddress) {
		// TODO Auto-generated method stub
		return gattMapsConnting.containsKey(mDeviceAddress);
	}
}
