package com.zby.led.sql;

import java.util.ArrayList;
import java.util.List;

import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.manager.SceneBeanManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SceneModeSqlService {
	
	SqliteHelp sqLite;
	private Context mContext;
	
	
	private SceneModeSqlService(){};
	
	public SceneModeSqlService(Context mContext){
		sqLite = new SqliteHelp(mContext);
		this.mContext = mContext;
	}
	
	/**
	 * 添加一个设备
	 * @param homeBin
	 */
	public long insert(SceneModeBean bin) {
		SQLiteDatabase mSqLiteDatabase = sqLite.getWritableDatabase();
		Cursor mCursor = mSqLiteDatabase.query(SceneModeTable.Table_Name, new String[]{SceneModeTable.Id}, SceneModeTable.Id+" =?", new String[]{""+bin.getId()}, null, null, null);
		//插入数据
		long id ;
		if(mCursor.moveToNext()) {
			id = mCursor.getLong(mCursor.getColumnIndex(SceneModeTable.Id));
			mSqLiteDatabase.update(SceneModeTable.Table_Name, bin2ContentValues(bin), SceneModeTable.Id+  "=? ", new String[]{""+id});
		} else {
			id = mSqLiteDatabase.insert(SceneModeTable.Table_Name, null, bin2ContentValues(bin));
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
		mSqLiteDatabase.delete(SceneModeTable.Table_Name, SceneModeTable.Id + " =? ",
				new String[] { ""+id });
		mSqLiteDatabase.close();
	}
	
	/**
	 * 修改
	 * @param homeBin
	 */
	public void update(SceneModeBean bin) {
		SQLiteDatabase mSqLiteDatabase = sqLite.getWritableDatabase();
		mSqLiteDatabase.beginTransaction();
			mSqLiteDatabase.update(SceneModeTable.Table_Name, bin2ContentValues(bin), SceneModeTable.Id
					+ "=?", new String[] {""+bin.getId() });
		mSqLiteDatabase.setTransactionSuccessful();
		mSqLiteDatabase.endTransaction();
		mSqLiteDatabase.close();
	}

	/**
	 * 查询所有记录的设备
	 * @return
	 */
	public List<SceneModeBean> selectAll() {
		Cursor mCursor = null;
		SQLiteDatabase mSqLiteDatabase = null;
		List<SceneModeBean> list = new ArrayList<SceneModeBean>();
		try {
			mSqLiteDatabase = sqLite.getReadableDatabase();
			//将默认的排在前面
		 	mCursor = mSqLiteDatabase.query(SceneModeTable.Table_Name, null, null, null,
				null, null, SceneModeTable.Id +" and " + SceneModeTable.DeleteAble);
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
	 * 查询默认的4个
	 * @return
	 */
	public List<SceneModeBean> selectDefualt() {
		Cursor mCursor = null;
		SQLiteDatabase mSqLiteDatabase = null;
		List<SceneModeBean> list = new ArrayList<SceneModeBean>();
		try {
			mSqLiteDatabase = sqLite.getReadableDatabase();
			selectDefault( mSqLiteDatabase, mCursor, list);
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
	 * 添加默认的
	 */
	private List<SceneModeBean> selectDefault(SQLiteDatabase mSqLiteDatabase,Cursor mCursor, List<SceneModeBean> list) {
		mCursor = mSqLiteDatabase.query(SceneModeTable.Table_Name, null, SceneModeTable.DeleteAble+" =?", new String[]{"0"}, null, null, null);
		while (mCursor.moveToNext()) {
			list.add(cursor2bin(mCursor));
		}
		if(list.size()!=4) {
			list.clear();
			List<SceneModeBean> defaultList = SceneBeanManager.loadDefaultScene(mContext);
			mSqLiteDatabase.beginTransaction();
			SceneModeBean bean;
			for(int i=0; i<defaultList.size(); i++ ) {
				bean = defaultList.get(i);
				long id = mSqLiteDatabase.insert(SceneModeTable.Table_Name, null, bin2ContentValues(bean));
				bean.setId((int)id);
				
				list.add(i, bean);
			}
			mSqLiteDatabase.setTransactionSuccessful();
			mSqLiteDatabase.endTransaction();
		}
		return list;
	}
	
	/**
	 * 查询所有记录的设备
	 * @return
	 */
	public List<SceneModeBean> selectByMac(String mac) {
		Cursor mCursor = null;
		SQLiteDatabase mSqLiteDatabase = null;
		List<SceneModeBean> list = new ArrayList<SceneModeBean>();
		try {
			mSqLiteDatabase = sqLite.getReadableDatabase();
		 	mCursor = mSqLiteDatabase.query(SceneModeTable.Table_Name, null, SceneModeTable.Mac+" =?", new String[]{mac}, null, null, null);
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
	private SceneModeBean cursor2bin(Cursor mCursor) {
		SceneModeBean bin = new SceneModeBean();
		bin.setId(mCursor.getInt(mCursor.getColumnIndex(SceneModeTable.Id)));
		bin.setName(mCursor.getString(mCursor.getColumnIndex(SceneModeTable.Name)));
		bin.setMac(mCursor.getString(mCursor.getColumnIndex(SceneModeTable.Mac)));
		bin.setColorYellow((mCursor.getInt(mCursor.getColumnIndex(SceneModeTable.Color)) - AppConstants.Color_min ) / AppConstants.Color_step);
		bin.setBrightness(mCursor.getInt(mCursor.getColumnIndex(SceneModeTable.Brightness)));
		bin.setImage(mCursor.getString(mCursor.getColumnIndex(SceneModeTable.Image)));
		bin.setDeleteable(mCursor.getInt(mCursor.getColumnIndex(SceneModeTable.DeleteAble))==1);
		return bin;
	}
	
	private ContentValues bin2ContentValues(SceneModeBean bin) {
		ContentValues values = new ContentValues();
		values.put(SceneModeTable.Name, bin.getName());
		values.put(SceneModeTable.Mac, bin.getMac());
		values.put(SceneModeTable.Image, bin.getImage());
		values.put(SceneModeTable.Color, bin.getColor());
		values.put(SceneModeTable.Brightness, bin.getBrightness());
		values.put(SceneModeTable.DeleteAble, bin.isDeleteable()?1:0);
		return values;
	}

}
