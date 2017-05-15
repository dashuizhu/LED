package com.zby.led.sql;

public class DeviceTable {
	
	protected static final String Table_Name= "SceneModeTable";
	
	
	protected static final String Id = "id";
	protected static final String Name="name";//名字
	protected static final String Mac = "mac";//所属于的设备
	protected static final String Password = "password";//色温
	protected static final String Image = "image";//图片
	protected static final String OldBrightness = "oldBrightness";//上次控制的亮度值
	protected static final String OldColorYellow = "oldColorYellow";//上次控制的color值
	
	/**
	 * @return sql 建表语句
	 */
	protected static final String getTable() {
		String table = "create Table " + Table_Name + " (" +
				Id + " integer primary key autoincrement ," +
				Name +" text," +
				Mac + " text,"+
				Password + " text,"+
				OldBrightness + " integer," +
				OldColorYellow + " integer," +
				Image + " text)";
		return table;
	}
}
