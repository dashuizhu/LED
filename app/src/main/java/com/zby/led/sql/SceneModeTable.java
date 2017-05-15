package com.zby.led.sql;

public class SceneModeTable {
	
	protected static final String Table_Name= "DeviceTable";
	
	
	protected static final String Id = "id";
	protected static final String Name="name";//����
	protected static final String Mac = "mac";//�����ڵ��豸
	protected static final String Color = "color";//ɫ��
	protected static final String Brightness = "brightness";//����
	protected static final String Image = "image";//ͼƬ
	protected static final String DeleteAble = "deleteAble";//�Ƿ���ɾ��
	
	/**
	 * @return sql �������
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
