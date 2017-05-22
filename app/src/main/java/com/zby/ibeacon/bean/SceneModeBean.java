package com.zby.ibeacon.bean;

import java.io.Serializable;

import com.zby.ibeacon.constants.AppConstants;

public class SceneModeBean implements Serializable{
	
	public static final int type_add = 2;//添加
	
	private int id; 
	
	private int	 type;
	
	private String name;
	
	private int mixBirght;//色温
	
	private int redBright;//亮度
	
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

	public void setMixBirght(int color) {
		this.mixBirght = color;
	}

	public int getMixBirght() {
		return  this.mixBirght;
	}
	
	public int getRedBright() {
		return redBright;
	}

	public void setRedBright(int redBright) {
		this.redBright = redBright;
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
