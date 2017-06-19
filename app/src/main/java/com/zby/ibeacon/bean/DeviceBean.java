package com.zby.ibeacon.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zby.ibeacon.agreement.CmdPackage;
import com.zby.ibeacon.agreement.CmdParse;
import com.zby.ibeacon.agreement.Encrypt;
import com.zby.ibeacon.bluetooth.ConnectionInterface;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.util.MyByte;
import com.zby.ibeacon.util.Myhex;
import com.zby.led.sql.DeviceSqlService;

import android.app.Activity;
import android.util.Log;


/**
 * @author Administrator
 *
 * @2015-6-3 @time下午11:37:45
 * 
 * 设备列表
 */
public class DeviceBean {
	
	
	public final static int Status_nocheck = 0 ;//没有检测
	public final static int Status_error =1;//密码错误
	public final static int Status_checking =2;//正在检测密码
	public final static int Status_success = 3;//成功
	
	/**
	 * 当前选中的发送模式
	 */
	private int nowSceneId =-1;
	
	/**
	 * 设备id
	 */
	private int id;
	
	private String deviceAddress;//蓝牙
	
	private String Name;//名字
	
	private String bluetoothName;//蓝牙搜索到的名字
	
	
	private boolean isShow;//是否显示控制栏
	
	private int ele;//电量
	
	private int RSSI;//蓝牙信号强度
	
	private String password;//密码
	
	private int status;//状态
	
	private String image="";
	
	private String devicePassword;//下位机的密码
	
	private boolean onOff;//开关
	
	private int brightness;//亮度
	
	private int colorYellow;//色温
	
	private int oldBrightness;//上次控制的值， 用来直接控制开关是发送
	private int oldColorYellow;//上次控制的值，用来和字节控制开关发送
	
	private List<TimingBean> list = new ArrayList<TimingBean>();
	
	
	/**
	 * 蓝牙连接通讯对象
	 */
	private ConnectionInterface mConnectionInterface;

	public ConnectionInterface getConnectionInterface() {
		return mConnectionInterface;
	}

	private CmdParse cmdParse;
	
	public void setConnectionInterface(ConnectionInterface mConnectionInterface, Activity activity) {
		this.mConnectionInterface = mConnectionInterface;
		cmdParse = new CmdParse(activity, this);
		this.mConnectionInterface.setDataParse(cmdParse);
	}
	
	public CmdParse getParse() {
		return cmdParse;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isOnOff() {
		return onOff;
	}

	public void setOnOff(boolean onOff) {
		this.onOff = onOff;
	}

	public boolean isShow() {
		return isShow;
	}

	/**
	 * 当前值取反 ， 如果是true， 就变false
	 */
	public void reversalShow() {
		this.isShow = !isShow;
		System.out.println("isSshow : " + isShow);
	}
	
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public String getDeviceAddress() {
		return deviceAddress==null ? "":deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public String getName() {
		return (Name==null || Name.equals("")? bluetoothName : Name);
	}

	public void setName(String name) {
		Name = name;
	}
	
	public String getBluetoothName() {
		return bluetoothName==null? "": bluetoothName;
	}

	public void setBluetoothName(String bluetoothName) {
		this.bluetoothName = bluetoothName;
	}

	public int getEle() {
		return ele;
	}

	public void setEle(int ele) {
		this.ele = ele;
	}

	
	public int getRSSI() {
		return RSSI;
	}

	public void setRSSI(int rSSI) {
		RSSI = rSSI;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getDevicePassword() {
		return devicePassword;
	}

	public void setDevicePassword(String devicePassword) {
		this.devicePassword = devicePassword;
	}

	public int getStatus() {
		return status;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
		this.onOff = (brightness>0);
	}


	public void setColorYellow(int yellow) {
		this.colorYellow = yellow;
	}
	
	public int getColorYellow() {
		return colorYellow;
	}
	

	public int getOldBrightness() {
		return oldBrightness;
	}

	public void setOldBrightness(int oldBrightness) {
		this.oldBrightness = oldBrightness;
	}

	public int getOldColorYellow() {
		return oldColorYellow;
	}
	
	//public int getOldColorWhite() {
	//	return AppConstants.Color_range - getOldColorYellow();
	//}

	public void setOldColorYellow(int oldColorYellow) {
		this.oldColorYellow = oldColorYellow;
	}
	
	public int getNowSceneId() {
		return nowSceneId;
	}

	public void setNowSceneId(int nowSceneId) {
		this.nowSceneId = nowSceneId;
	}

	/**
	 * 如果是正在检测中， 如果3秒超时后还是正在检测中会变 未校验
	 * @param status
	 */
	public void setStatus(int status) {
			this.status = status;
			if(this.status==Status_checking) {
				new Thread(runnable).start();
			}
	}

	public void stopConnect() {
		// TODO Auto-generated method stub
		if(mConnectionInterface!=null) {
				mConnectionInterface.stopConncet();
		}
	}
	
	public void closeConnect() {
		if(mConnectionInterface!=null) {
				mConnectionInterface.onBleDestory();
		}
	}
	
	public void connect() {
		if(mConnectionInterface!=null) {
			mConnectionInterface.connect(deviceAddress, "");
		}
	}

	public boolean writeAgreement(byte[] buffer) {
		// TODO Auto-generated method stub
		if(AppConstants.isDemo) {
			Log.d("tag","DeviceBean demo发送数据："+ Myhex.buffer2String(buffer));
			if(buffer!=null && buffer.length>=4 && buffer[0] == (byte)0xD0) {
				//如果是设备控制指令，就记录当前控制值，当做old值
				this.oldBrightness = MyByte.byteToInt(buffer[1]);
				this.oldColorYellow =  MyByte.byteToInt(buffer[2]);
			}
			return true;
		}
		if(mConnectionInterface!=null && mConnectionInterface.isLink()) {
			write(Encrypt.sendMessage(buffer, buffer.length));
			if(buffer!=null && buffer.length>=4 && buffer[0] == (byte)0xD0) {
				//如果是设备控制指令，就记录当前控制值，当做old值
				this.oldBrightness =  MyByte.byteToInt(buffer[1]);
				this.oldColorYellow =  MyByte.byteToInt(buffer[2]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 发送不保存旧值的， 用于直接点开关
	 * @param buffer
	 * @return
	 */
	private boolean writeAgreementNoSave(byte[] buffer) {
		// TODO Auto-generated method stub
		if(AppConstants.isDemo) {
			Log.d("tag","DeviceBean demo发送数据："+ Myhex.buffer2String(buffer));
			return true;
		}
		if(mConnectionInterface!=null && mConnectionInterface.isLink()) {
			write(Encrypt.sendMessage(buffer, buffer.length));
			return true;
		}
		return false;
	}
	
	
	
	public boolean write(byte[] buffer) {
		// TODO Auto-generated method stub
		if(mConnectionInterface!=null && mConnectionInterface.isLink()) {
			mConnectionInterface.write(buffer);
			return true;
		}
		return false;
	}
	
	/**
	 * 直接点击开关控制
	 * @return
	 */
	public boolean controlSwithc() {
		byte[] buff;
		if(onOff) {//现在是开 ，就发关闭
			buff = CmdPackage.setD1ControlNoCheck(0, 0);
		} else {//现在是关闭，就发开 ，开的亮度和颜色值 用上一次的控制值或默认值
			if(oldBrightness==0) oldBrightness = AppConstants.default_brightness;
			if(oldColorYellow==0) oldColorYellow = AppConstants.default_colorYellow;
			buff = CmdPackage.setD1ControlNoCheck(oldBrightness, oldColorYellow);
		}
		this.onOff = !onOff;
		return writeAgreementNoSave(buff);
	}
	
	public List<TimingBean> getTimingList() {
		return list;
	}
	
	/**
	 * 更新或者添加 定时
	 * @param bin
	 */
	public void updateTimingBean(TimingBean bin) {
		TimingBean bean;
		synchronized (list) {
			for(int i=0; i<list.size() ; i++) {
				bean = list.get(i);
				if(bean.getId() == bin.getId()) {
					list.remove(i);
					list.add(i, bin);
					return;
				}
			}
			list.add(bin);
		}
	}

	//密码检测汇中，  超时线程
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(AppConstants.PasswordCheckTimeOut);
				if(DeviceBean.this.status  ==  Status_checking) {
					DeviceBean.this.status = Status_nocheck;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	/**
	 * 是否连接上了设备
	 * @return
	 */
	public boolean islink(){
		if(AppConstants.isDemo){
			return true;
		}
		if(mConnectionInterface!=null) {
			return mConnectionInterface.isLink();
		}
		return false;
	}

	public boolean isConnecting() {
		// TODO Auto-generated method stub
		if(mConnectionInterface!=null) {
			return mConnectionInterface.isConnecting();
		}
		return false;
	}
	
	public void closeAll() {
		if(mConnectionInterface!=null) {
			 mConnectionInterface.closeAll();
		}
	}

}
