package com.zby.led.sql;

import java.util.ArrayList;
import java.util.List;

import com.zby.ibeacon.bean.DeviceBean;
import com.zby.ibeacon.bean.DeviceBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DeviceSqlService {
	
	SqliteHelp sqLite;
	
	private DeviceSqlService(){};
	
	public DeviceSqlService(Context mContext){
		sqLite = new SqliteHelp(mContext);
	}
	
	/**
	 * 添加一个设备
	 * @param homeBin
	 */
	public long insert(DeviceBean bin) {
		SQLiteDatabase mSqLiteDatabase = sqLite.getWritableDatabase();
		Cursor mCursor = mSqLiteDatabase.query(DeviceTable.Table_Name, new String[]{DeviceTable.Id}, DeviceTable.Mac + " =?", new String[]{bin.getDeviceAddress()}, null, null, null);
		long id ;
		if(mCursor.moveToNext()) {
			//更新数据
			id = mCursor.getLong(mCursor.getColumnIndex(DeviceTable.Id));
			mSqLiteDatabase.update(DeviceTable.Table_Name, bin2ContentValues(bin), DeviceTable.Id+  "=? ", new String[]{""+id});
		} else {
			//插入数据
			id = mSqLiteDatabase.insert(DeviceTable.Table_Name, null, bin2ContentValues(bin));
			bin.setId((int)id);
		}
		return id;
	}
	
	/**
	 * 通过ID删除
	 * @param id
	 */
	public void delete(int id) {
		SQLiteDatabase mSqLiteDatabase = sqLite.getWritableDatabase();
		mSqLiteDatabase.delete(DeviceTable.Table_Name, DeviceTable.Id + " =? ",
				new String[] { ""+id });
		mSqLiteDatabase.close();
	}
	
	/**
	 * 修改
	 * @param homeBin
	 */
	public void update(DeviceBean bin) {
		SQLiteDatabase mSqLiteDatabase = sqLite.getWritableDatabase();
		mSqLiteDatabase.beginTransaction();
			mSqLiteDatabase.update(DeviceTable.Table_Name, bin2ContentValues(bin), DeviceTable.Id
					+ "=?", new String[] {""+bin.getId() });
		mSqLiteDatabase.setTransactionSuccessful();
		mSqLiteDatabase.endTransaction();
		mSqLiteDatabase.close();
	}
	
	/**
	 * 修改记录的控制值
	 * @param homeBin
	 */
	public void updateOldControl(DeviceBean bin) {
		SQLiteDatabase mSqLiteDatabase = sqLite.getWritableDatabase();
		mSqLiteDatabase.beginTransaction();
		ContentValues values = new ContentValues();
		//values.put(DeviceTable.Id, bin.getId());
		values.put(DeviceTable.OldBrightness, bin.getOldBrightness());
		values.put(DeviceTable.OldColorYellow, bin.getOldColorYellow());
			mSqLiteDatabase.update(DeviceTable.Table_Name, values, DeviceTable.Id
					+ "=?", new String[] {""+bin.getId() });
		mSqLiteDatabase.setTransactionSuccessful();
		mSqLiteDatabase.endTransaction();
		mSqLiteDatabase.close();
	}

	/**
	 * 查询所有记录的设备
	 * @return
	 */
	public List<DeviceBean> selectAll() {
		Cursor mCursor = null;
		SQLiteDatabase mSqLiteDatabase = null;
		List<DeviceBean> list = new ArrayList<DeviceBean>();
		try {
			mSqLiteDatabase = sqLite.getReadableDatabase();
		 	mCursor = mSqLiteDatabase.query(DeviceTable.Table_Name, null, null, null,
				null, null, null);
			while (mCursor.moveToNext()) {
				list.add(cursor2bin(mCursor));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(mSqLiteDatabase!=null){
				mSqLiteDatabase.close();
			}
			if(mCursor!=null){
				mCursor.close();
			}
		}
		return list;
	}
	
	/**
	 * 查询所有记录的设备
	 * @return
	 */
	public List<DeviceBean> selectByMac(String mac) {
		Cursor mCursor = null;
		SQLiteDatabase mSqLiteDatabase = null;
		List<DeviceBean> list = new ArrayList<DeviceBean>();
		try {
			mSqLiteDatabase = sqLite.getReadableDatabase();
		 	mCursor = mSqLiteDatabase.query(DeviceTable.Table_Name, null, DeviceTable.Mac+" =?", new String[]{mac}, null, null, null);
			while (mCursor.moveToNext()) {
				list.add(cursor2bin(mCursor));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(mSqLiteDatabase!=null){
				mSqLiteDatabase.close();
			}
			if(mCursor!=null){
				mCursor.close();
			}
		}
		return list;
	}

	/**
	 * @param mCursor
	 * @return 将数据库内容转换成实体类
	 */
	private DeviceBean cursor2bin(Cursor mCursor) {
		DeviceBean bin = new DeviceBean();
		bin.setId(mCursor.getInt(mCursor.getColumnIndex(DeviceTable.Id)));
		bin.setName(mCursor.getString(mCursor.getColumnIndex(DeviceTable.Name)));
		bin.setDeviceAddress(mCursor.getString(mCursor.getColumnIndex(DeviceTable.Mac)));
		bin.setPassword(mCursor.getString(mCursor.getColumnIndex(DeviceTable.Password)));
		bin.setImage(mCursor.getString(mCursor.getColumnIndex(DeviceTable.Image)));
		bin.setOldColorYellow(mCursor.getInt(mCursor.getColumnIndex(DeviceTable.OldColorYellow)));
		bin.setOldBrightness(mCursor.getInt(mCursor.getColumnIndex(DeviceTable.OldBrightness)));
		return bin;
	}
	
	private ContentValues bin2ContentValues(DeviceBean bin) {
		ContentValues values = new ContentValues();
		//values.put(DeviceTable.Id, bin.getId());
		values.put(DeviceTable.Name, bin.getName());
		values.put(DeviceTable.Mac, bin.getDeviceAddress());
		values.put(DeviceTable.Image, bin.getImage());
		values.put(DeviceTable.Password, bin.getPassword());
		values.put(DeviceTable.OldBrightness, bin.getOldBrightness());
		values.put(DeviceTable.OldColorYellow, bin.getOldColorYellow());
		return values;
	}

}
