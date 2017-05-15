package com.zby.ibeacon.agreement;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.zby.ibeacon.bean.TimingBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.util.MyByte;
import com.zby.ibeacon.util.Myhex;

public class CmdPackage {
	
	
	/**
	 * 检查色温值过滤，最小必须发5
	 * @return
	 */
	private static synchronized int checkBright(int colorYellow){
		if(colorYellow<5){
			colorYellow = 5;
		}
		return colorYellow;
	}
	
	private static synchronized int checkColor(int colorWrite){
//		if(colorWrite<3){
//			colorWrite =3;
//		}
		return colorWrite;
	}
	
	/**
	 * 控制， 有最小默认值
	 * @param millis
	 * @return
	 */
	public static byte[] setD1Control(int brightness, int color) {
		byte[] buffer = new byte[4];
		buffer[0] = (byte) 0xD0;
		buffer[1] = (byte) (checkBright(brightness));
			buffer[2] = (byte) (checkColor(color));
			buffer[3] = (byte) (checkColor(AppConstants.Color_range-color));
		return buffer;
	}
	
	/**
	 * 控制命令值，不会检查最小值， 用于直接点开关 发送命令
	 * @param brightness
	 * @param color
	 * @return
	 */
	public static byte[] setD1ControlNoCheck(int brightness, int color) {
		byte[] buffer = new byte[4];
		buffer[0] = (byte) 0xD1;
		buffer[1] = (byte) (brightness);
			buffer[2] = (byte) (color);
			buffer[3] = (byte) (AppConstants.Color_range-MyByte.byteToInt(buffer[2]));
		return buffer;
	}
	
	public static byte[] setD2Timing(TimingBean bin) {
		byte[] buff = new byte[12];
		buff[0] = (byte) 0xD2;
		buff[1] = (byte) bin.getId();
		buff[2] = (byte) (bin.getYear()/256);
		buff[3] = (byte) (bin.getYear()%256);
		buff[4] = (byte) (bin.getMonth());
		buff[5] = (byte) (bin.getDay());
		buff[6] = (byte) (bin.getHour());
		buff[7] = (byte) (bin.getMinute());
		buff[8] = (byte) bin.getBrightness();
		buff[9] = (byte) bin.getColorYellow();
		buff[10] = (byte) bin.getColorWhite();
		
		int states = 0;
		if(bin.isDelete()) {
			states = 2;
		} else {
			states = bin.isEnable()?1:0;
		}
		buff[11] = (byte) (states);
		return buff;
	}
	
	
	/**
	 * 设置密码
	 * @param value
	 * @return
	 */
	public static byte[] setD3Password(String value) {
		byte[] buffer = new byte[7];
		buffer[0] = (byte) 0xD3;
		byte[] passBuff = value.getBytes();
		System.arraycopy(passBuff, 0, buffer, 1, passBuff.length);
		return buffer;
	}
	
	/**
	 * 次ID
	 * @param millis
	 * @return
	 */
	public static byte[] setD4Name(String name) {
		byte[] buffer = new byte[19];
		buffer[0] = (byte) 0xD4;
		byte[] strBuff;
		try {
			strBuff = name.getBytes(AppConstants.charSet);
			System.arraycopy(strBuff, 0, buffer, 1, strBuff.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
	}
	
	
	public static byte[] getReadTiming() {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) 0xD5;
		buffer[1] = (byte) 0x01;
		return buffer;
	}
	public static byte[] getReadPassword() {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) 0xD5;
		buffer[1] = (byte) 0x03;
		return buffer;
	}
	public static byte[] getReadName() {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) 0xD5;
		buffer[1] = (byte) 0x04;
		return buffer;
	}
	public static byte[] getReadStatus() {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) 0xD5;
		buffer[1] = (byte) 0x05;
		return buffer;
	}
	
	public static byte[] setVelidateDate() {
		Date d = new Date();
		byte[] buffer = new byte[8] ;
		buffer[0] = (byte) 0xD6;
		buffer[1] = (byte) ((d.getYear()+1900) / 256);
		buffer[2] = (byte) ((d.getYear()+1900) % 256);
		buffer[3] = (byte) (d.getMonth()+1);
		buffer[4] = (byte) (d.getDate());
		buffer[5] = (byte) (d.getHours());
		buffer[6] = (byte) (d.getMinutes());
		buffer[7] = (byte) (d.getSeconds());
		return buffer;
	}
}
