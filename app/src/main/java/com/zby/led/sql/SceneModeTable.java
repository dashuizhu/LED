package com.zby.led.sql;

public class SceneModeTable {
	
	protected static final String Table_Name= "DeviceTable";
	
	
	protected static final String Id = "id";
	protected static final String Name="name";//名字
	protected static final String Mac = "mac";//所属于的设备
	protected static final String Color = "color";//色温
	protected static final String Brightness = "brightness";//亮度
	protected static final String Image = "image";//图片
	protected static final String DeleteAble = "deleteAble";//是否能删除
	
	/**
	 * @return sql 建表语句
	 */
	protected static final String getTable() {
		String table = "create Table " + Table_Name + " (" +
				Id + " integer primary key autoincrement, " +
				Name +" text," +
				Mac + " text,"+
				Color + " integer,"+
				Brightness + " integer," +
				DeleteAble +" integer," +
				Image + " text)";
		return table;
	}
}
