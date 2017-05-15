package com.zby.ibeacon.bluetooth;

import java.util.ArrayList;
import java.util.List;

import com.zby.ibeacon.agreement.CmdParse;


public interface ConnectionInterface {
	
	/**
	 * �����������������豸
	 */
	
	static String linkIp="";
	
	/**
	 * �ҵ�һ��IP
	 */
	public final static int FindIp = 0;
	
	
	/**
	 * ���ӳɹ�
	 */
	public final static int LinkSuccess = 1;
	
	/**
	 * ����ʧ��
	 */
	public final static int LinkFailure = 2;
	
	/**
	 * �õ��������
	 */
	public final static int GetData = 3;// �õ�����;
	
	public final static int linkLost = 33;
	
	/**
	 * ����IP��־λ
	 */
	public final static int startFindIp=4;//����IP��־λ
	
	/**
	 * �������
	 */
	public final static int FindOk=5;//�������
	
	/**
	 * ����ip
	 */
	public final static int LinkIp=6;//����IP
	
	/**
	 * ʧȥ����
	 */
	public final static int LostLink = 7;//ʧȥ����
	
	/**
	 * ��������
	 */
	public final static int Link=8;//��������
	
	/**
	 * �Ƿ������ܳ�
	 */
	public final static boolean IsA5 = true;//�Ƿ������ܳ�
	
	/**
	 * ԭʼ����
	 */
	public final static int RawData = 9;
	public final static int PwdError = 10;
	public final static int ChangeNameOk = 11;
	
	/**
	 * û������
	 */
	public static  final int NO_BLUETOOTH = 12; 
	
	/**
	 * û�����ӵ��豸
	 */
	public static final int NO_LINK_DEVICE = 13;
	
	
	/**
	 * deviceList�����˱仯
	 */
	public static final int List_Changed = 14;
	
	/**
	 * û��ip��ַ
	 */
	public static final int WifiError = 101;

	
	/**
	 * �������������豸�����������豸�����connectDeviceList
	 */
	void find();
	
	/**
	 * �����豸 
	 * @param address ���ӵĵ�ַ���������ڵ�ip ���� ����mac
	 * @param pwd �������ӵ�����
	 */
	void connect(String address,String pwd);// ����
	
	
	/**
	 * ֹͣ����
	 */
	void stopConncet();// ֹͣ����
	
	void closeAll();
	
	/**
	 * ֱ�ӷ�������
	 * @param buffer
	 */
	void write(byte[] buffer);
	
	/**
	 * ����������Э�����
	 * @param buffer
	 */
	void writeAgreement(byte[] buffer);
	
	/**
	 * ��ȡ���ݣ�����
	 * @param buffer
	 */
	//void read(byte[] buffer);//��ȡ���ݣ�����һЩ��ͨ���йص�����
	
	/**
	 * �Ƿ��Ѿ����ӵ��豸
	 * @return 
	 *   ��������豸������true
	 */
	boolean isLink();//�Ƿ�������
	
	/**
	 * �Ƿ�����������
	 * @return
	 */
	boolean isConnecting();
	
	/**
	 * �Ƿ����ӳɹ�����ip
	 * @return
	 */
	boolean haveIp();//�Ƿ����ӳɹ���IP
	void Reconnect();
	
	
	void onBleDestory();

	void setDataParse(CmdParse cmdParse);
}
