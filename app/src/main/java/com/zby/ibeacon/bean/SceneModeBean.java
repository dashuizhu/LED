package com.zby.ibeacon.bean;

import java.io.Serializable;

import com.zby.ibeacon.constants.AppConstants;

public class SceneModeBean implements Serializable{
	
	public static final int type_add = 2;//添加
	
	private int id; 
	
	private int	 type;
	
	private String name;
	
	private int colorYellow;//色温
	
	private int brightness;//亮度
	
	private String image;//图片
	
	private String mac;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name==null?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 实际用户看到的具体数值，  
	 * @return
	 */
	public int getColor() {
		return AppConstants.Color_min+ colorYellow * AppConstants.Color_step;
	}

	public void setColorYellow(int color) {
		this.colorYellow = color;
	}

	public int getColorYellow() {
		return  this.colorYellow;
	}
	
	public int getColorWhite() {
		return AppConstants.Color_range - colorYellow;
	}
	
	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public String getImage() {
		return image==null?"":image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public boolean isDeleteable() {
		return type==1;
	}
	
	/**
	 * 0是默认的 不可删除
	 * 1是自定义的可以删除
	 * 2是添加按钮
	 * @return
	 */
	public int getType() {
		return type;
	}

	public void setDeleteable(boolean deleteable) {
		this.type = deleteable?1:0;
	}

}
