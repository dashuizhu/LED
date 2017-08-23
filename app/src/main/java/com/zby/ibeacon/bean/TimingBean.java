package com.zby.ibeacon.bean;

import java.io.Serializable;

import com.zby.ibeacon.constants.AppConstants;

public class TimingBean implements Serializable {
	
	private int id;
	
	private String name;
	
	private boolean enable;//是否启用
	
	private boolean delete;//删除
	
	private int brightness;//亮度
	
	private int colorYellow; //色温
	
	private int mode;//情景模式
	
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	
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
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public int getBrightness() {
		return brightness;
	}
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	//public int getColor() {
	//	return  AppConstants.Color_min + colorYellow * AppConstants.Color_step;
	//}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
		
		
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public String getDateString() {
		// TODO Auto-generated method stub
		String date = String.format("%04d-%02d-%02d", year, month, day);
		return date;
	}
	
	public String getTimeString() {
		// TODO Auto-generated method stub
		String date ;
		int showHour;
		if (hour>=12) {
			showHour = hour -12;
			//0点 应该显示成 PM 12
			if (showHour ==0) showHour = 12;
			date = String.format("%02d:%02d PM", showHour, minute);
		} else {
			showHour = hour;
			//0点，应该显示 AM 12
			if (showHour ==0) showHour = 12;
			date = String.format("%02d:%02d AM", showHour, minute);
		}
		return date;
	}
	
	public int getColorYellow() {
		return colorYellow;
	}
	
	public void setColorYellow(int colorYellow) {
		this.colorYellow = colorYellow;
	}
	
	//public int getColorWhite() {
	//	return AppConstants.Color_range - getColorYellow();
	//	}
	

}
