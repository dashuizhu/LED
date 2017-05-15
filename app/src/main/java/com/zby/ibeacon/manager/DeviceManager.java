package com.zby.ibeacon.manager;

import com.zby.ibeacon.bean.DeviceBean;

public class DeviceManager {
	
	private  DeviceBean bean;
	
	private static DeviceManager manager;
	
	private DeviceManager() {};
	
	public static DeviceManager getInstance() {
		if (manager==null) {
			 manager =new DeviceManager();
		}
		return manager;
	}
	
	public void setDeviceBean(DeviceBean bean) {
		this.bean =bean;
	}
	
	public DeviceBean getDeviceBean() {
		return bean;
	}

}
