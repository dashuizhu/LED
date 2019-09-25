package com.zby.ibeacon.agreement;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.zby.ibeacon.bean.DeviceBean;
import com.zby.ibeacon.bean.TimingBean;
import com.zby.ibeacon.bluetooth.DataProtocolInterface;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.util.MyByte;
import com.zby.ibeacon.util.Myhex;

public class CmdParse implements DataProtocolInterface {
	
	public static final int Cmd_A0_status = 160;
	public static final int Cmd_A1_timing = 161;
	public static final int Cmd_A2_password = 162;
	public static final int Cmd_A3_name = 163;
	public static final int Cmd_D5_timer = (0xD5);

	private DeviceBean bean;
	private final static String TAG = "cmdParseTag";

	private Activity mContext;

	private CmdParse() {
	};

	public CmdParse(Activity activity, DeviceBean bean) {
		this.mContext = activity;
		this.bean = bean;
	}

	public void parseData(byte[] buffer) {
		Log.i(TAG, "解析 " + Myhex.buffer2String(buffer));
		if(buffer==null ) return;
		String name;
		byte[] nameBuff;
		int type;
		switch (buffer[0]) {
		case (byte) 0xA0:// 开关，亮， 黄， 白
			bean.setOnOff(MyByte.byteToInt(buffer[1]) == 1);
			bean.setBrightness(MyByte.byteToInt(buffer[2]));
			bean.setColorYellow(MyByte.byteToInt(buffer[3]));
			// 这里有隐患， 这里是算 字节的 10进制
						type = MyByte.byteToInt(0xA0);
						handlerSendBroadcast(type);
			break;
		case (byte) 0xD0:
			bean.setOnOff(MyByte.byteToInt(buffer[1]) >0);
			bean.setBrightness(MyByte.byteToInt(buffer[1]));
			bean.setColorYellow(MyByte.byteToInt(buffer[2]));
			// 这里有隐患， 这里是算 字节的 10进制
						type = MyByte.byteToInt(0xD0);
						handlerSendBroadcast(type);
			break;
		case (byte) 0xA1:// timing
		case (byte) 0xD2:
			TimingBean bin = new TimingBean();
			bin.setId( MyByte.byteToInt(buffer[1]));
			bin.setYear(MyByte.byteToInt(buffer[2]) * 256 +  MyByte.byteToInt(buffer[3]));
			bin.setMonth(MyByte.byteToInt(buffer[4]));
			//if(bin.getMonth()>12 || bin.getMonth()<=0 ) return;
			bin.setDay(MyByte.byteToInt(buffer[5]));
			bin.setHour(MyByte.byteToInt(buffer[6]));
			bin.setMinute(MyByte.byteToInt(buffer[7]));
			bin.setBrightness(MyByte.byteToInt(buffer[8]));
			bin.setColorYellow(MyByte.byteToInt(buffer[9]));

			//只有0-9
			if (bin.getId()>=10) {
				break;
			}

			if(MyByte.byteToInt(buffer[11]) !=2) { //2是删除的意思
				bin.setEnable(MyByte.byteToInt(buffer[11])==1);
				bean.updateTimingBean(bin);
				// 这里有隐患， 这里是算 字节的 10进制
				type = MyByte.byteToInt(0xA1);
				handlerSendBroadcast(type);
			}
			break;
		case (byte) 0xA2:// password
		case (byte) 0xD3:
			nameBuff = new byte[6];
			System.arraycopy(buffer, 1, nameBuff, 0, nameBuff.length);
				//name = Myhex.parse02DString(nameBuff);
				name = new String(nameBuff);
				name = name.replace("0", "");
				bean.setDevicePassword(name.replace(" ", ""));
				// 这里有隐患， 这里是算 字节的 10进制
				type = MyByte.byteToInt(0xA2);
				handlerSendBroadcast(type);
			break;
		case (byte) 0xA3:// name
		case (byte) 0xD4:
			nameBuff = new byte[buffer.length-1];
			System.arraycopy(buffer, 1, nameBuff, 0, nameBuff.length);
			try {
				name = new String(nameBuff, AppConstants.charSet);
				bean.setName(name);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 这里有隐患， 这里是算 字节的 10进制
			type = MyByte.byteToInt(0xA3);
			handlerSendBroadcast(type);
			break;
        case (byte) 0xD5:
            bean.setTimerCount(MyByte.byteToInt(MyByte.byteToInt(buffer[2])));
            type = MyByte.byteToInt(0xD5);
            handlerSendBroadcast(type);
            break;
		default:
		}
	}

	private void handlerSendBroadcast(int type) {// 发送广播
		if (mContext != null) {
			Intent intent = new Intent(
					ConnectBroadcastReceiver.BROADCAST_ACTION);
			intent.putExtra(ConnectBroadcastReceiver.BROADCAST_DATA_TYPE, type);
			// intent.putExtra(ConnectBroadcastReceiver.BROADCAST_DATA_KEY,
			// data);
			intent.putExtra(ConnectBroadcastReceiver.BROADCAST_DEVICE_MAC,
					bean.getDeviceAddress());
			Log.d("ConnectBraodcast", TAG + ".sendBroadcast    " + type + " "
					+ bean.getDeviceAddress());
			mContext.sendBroadcast(intent);
		}
	}
}
