package com.zby.led.sql;

public class DeviceTable {
	
	protected static final String Table_Name= "SceneModeTable";
	
	
	protected static final String Id = "id";
	protected static final String Name="name";//����
	protected static final String Mac = "mac";//�����ڵ��豸
	protected static final String Password = "password";//ɫ��
	protected static final String Image = "image";//ͼƬ
	protected static final String OldBrightness = "oldBrightness";//�ϴο��Ƶ�����ֵ
	protected static final String OldColorYellow = "oldColorYellow";//�ϴο��Ƶ�colorֵ
	
	/**
	 * @return sql �������
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
