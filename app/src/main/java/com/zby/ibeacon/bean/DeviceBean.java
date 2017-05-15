package com.zby.ibeacon.bean;

import java.util.ArrayList;
import java.util.List;
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
 * @2015-6-3 @time����11:37:45
 * 
 * �豸�б�
 */
public class DeviceBean {
	
	
	public final static int Status_nocheck = 0 ;//û�м��
	public final static int Status_error =1;//�������
	public final static int Status_checking =2;//���ڼ������
	public final static int Status_success = 3;//�ɹ�
	
	/**
	 * ��ǰѡ�еķ���ģʽ
	 */
	private int nowSceneId =-1;
	
	/**
	 * �豸id
	 */
	private int id;
	
	private String deviceAddress;//����
	
	private String Name;//����
	
	private String bluetoothName;//����������������
	
	
	private boolean isShow;//�Ƿ���ʾ������
	
	private int ele;//����
	
	private int RSSI;//�����ź�ǿ��
	
	private String password;//����
	
	private int status;//״̬
	
	private String image="";
	
	private String devicePassword;//��λ��������
	
	private boolean onOff;//����
	
	private int brightness;//����
	
	private int colorYellow;//ɫ��
	
	private int oldBrightness;//�ϴο��Ƶ�ֵ�� ����ֱ�ӿ��ƿ����Ƿ���
	private int oldColorYellow;//�ϴο��Ƶ�ֵ���������ֽڿ��ƿ��ط���
	
	private List<TimingBean> list = new ArrayList<TimingBean>();
	
	
	/**
	 * ��������ͨѶ����
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
	 * ��ǰֵȡ�� �� �����true�� �ͱ�false
	 */
	public void reversalShow() {
		this.isShow = !isShow;
		System.out.println("isSshow : " + isShow);
	}
	
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public String getDeviceAddress() {
		return deviceAddress;
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

	public int getColor() {
		return  AppConstants.Color_min+  colorYellow * AppConstants.Color_step;
	}

	public void setColorYellow(int yellow) {
		this.colorYellow = yellow;
	}
	
	public int getColorYellow() {
		return colorYellow;
	}
	
	public int getColorWhite() {
		return AppConstants.Color_range - getColorYellow();
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
	
	public int getOldColorWhite() {
		return AppConstants.Color_range - getOldColorYellow();
	}

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
	 * ��������ڼ���У� ���3�볬ʱ�������ڼ���л�� δУ��
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
			Log.d("tag","DeviceBean demo�������ݣ�"+ Myhex.buffer2String(buffer));
			if(buffer!=null && buffer.length>=4 && buffer[0] == (byte)0xD0) {
				//������豸����ָ��ͼ�¼��ǰ����ֵ������oldֵ
				this.oldBrightness = MyByte.byteToInt(buffer[1]);
				this.oldColorYellow =  MyByte.byteToInt(buffer[2]);
			}
			return true;
		}
		if(mConnectionInterface!=null && mConnectionInterface.isLink()) {
			write(Encrypt.sendMessage(buffer, buffer.length));
			if(buffer!=null && buffer.length>=4 && buffer[0] == (byte)0xD0) {
				//������豸����ָ��ͼ�¼��ǰ����ֵ������oldֵ
				this.oldBrightness =  MyByte.byteToInt(buffer[1]);
				this.oldColorYellow =  MyByte.byteToInt(buffer[2]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * ���Ͳ������ֵ�ģ� ����ֱ�ӵ㿪��
	 * @param buffer
	 * @return
	 */
	private boolean writeAgreementNoSave(byte[] buffer) {
		// TODO Auto-generated method stub
		if(AppConstants.isDemo) {
			Log.d("tag","DeviceBean demo�������ݣ�"+ Myhex.buffer2String(buffer));
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
	 * ֱ�ӵ�����ؿ���
	 * @return
	 */
	public boolean controlSwithc() {
		byte[] buff;
		if(onOff) {//�����ǿ� ���ͷ��ر�
			 buff = CmdPackage.setD1ControlNoCheck(0, oldColorYellow);
		} else {//�����ǹرգ��ͷ��� ���������Ⱥ���ɫֵ ����һ�εĿ���ֵ��Ĭ��ֵ
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
	 * ���»������ ��ʱ
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

	//��������У�  ��ʱ�߳�
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
	 * �Ƿ����������豸
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
