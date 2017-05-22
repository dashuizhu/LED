package com.example.testl;

import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.agreement.CmdPackage;
import com.zby.ibeacon.bean.DeviceBean;
import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.manager.DeviceManager;
import com.zby.ibeacon.manager.SceneBeanManager;
import com.zby.led.adapter.SceneAdapter;
import com.zby.led.sql.DeviceSqlService;
import com.zby.led.sql.SceneModeSqlService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

public class ViewPagerSceneActivity extends BaseActivity {
	
	private final static String TAG = ViewPagerSceneActivity.class.getSimpleName();
	
	private GridView gridView;
	private List<SceneModeBean> list;
	private SceneAdapter adapter;
	private SceneModeSqlService sqlService;
	private DeviceSqlService deviceSqlService;
	private DeviceBean bin;
	
	private final static int activity_scene_add = 11;
	private final static int activity_scene_update = 12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager_scene);
		initViews();
		Log.v(TAG,"oncreate2");
	}
	
	private void initViews() {
		initBaseViews(this);
		tv_title.setText(R.string.mode_scene);
		bin = DeviceManager.getInstance().getDeviceBean();
		sqlService = new SceneModeSqlService(this);
		deviceSqlService = new DeviceSqlService(this);
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setNumColumns(AppConstants.gridView_numColums);
		
		list = sqlService.selectAll();
		//添加一个add
		SceneModeBean addBin = new SceneModeBean();
		addBin.setImage("local_scene_add");
		addBin.setName("");
		list.add(addBin);
		
		
		adapter = new SceneAdapter(this, list);
		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SceneModeBean bean;
				synchronized (list) {
					if(arg2==list.size()-1) {//是最后一个 添加按钮
						Intent intent = new Intent(ViewPagerSceneActivity.this , SceneDetailActivity.class);
						SceneModeBean bin = new SceneModeBean();
						//-1来表示未添加到数据库的新模式
						bin.setId(-1);
						bin.setDeleteable(true);
						intent.putExtra("sceneModeBean", bin);
						getParent().startActivityForResult(intent, activity_scene_add);
					} else {
						bean = list.get(arg2);
						bin.setNowSceneId(bean.getId());
						adapter.notifyDataSetChanged();
						bin.writeAgreement(CmdPackage.setD1Control(bean.getRedBright(), bean.getMixBirght()));
						deviceSqlService.update(bin);
					}
				}
			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				synchronized (list) {
					if(arg2<list.size()-1) {//是最后一个 添加按钮
						Intent intent = new Intent(ViewPagerSceneActivity.this , SceneDetailActivity.class);
						intent.putExtra("sceneModeBean", list.get(arg2));
						getParent().startActivityForResult(intent, activity_scene_update);
					}
				}
				return true;
			}
		});
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		SceneModeBean sbin;
		switch(requestCode)	 {
			case activity_scene_add:
				if(resultCode == Activity.RESULT_OK) {//添加
					sbin = (SceneModeBean) data.getSerializableExtra("sceneModeBean");
					sbin.setDeleteable(true);
					int id = (int) sqlService.insert(sbin);
					sbin.setId(id);
					synchronized (list) {
						SceneBeanManager.updateSceneMode(list, sbin);
						adapter.notifyDataSetChanged();
					}
				}
				break;
			case activity_scene_update:
				if(resultCode == Activity.RESULT_OK) {//修改
					sbin = (SceneModeBean) data.getSerializableExtra("sceneModeBean");
					sqlService.update(sbin);
					synchronized (list) {
						SceneBeanManager.updateSceneMode(list, sbin);
						adapter.notifyDataSetChanged();
					}
					
				} else if(resultCode == Activity.RESULT_FIRST_USER) {//删除 
					sbin = (SceneModeBean) data.getSerializableExtra("sceneModeBean");
					sqlService.delete(sbin.getId());
					synchronized (list) {
						SceneBeanManager.deleteSceneMode(list, sbin);
						adapter.notifyDataSetChanged();
					}
				}
				break;
			
		}
	}

	public void onBtnClick(View v){
		switch(v.getId()){
		
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.v(TAG,"onstar2");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.v(TAG,"onstop2");
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.v(TAG,"onresume2");
		if(adapter!=null) {
			list = sqlService.selectAll();
			//添加一个add
			SceneModeBean addBin = new SceneModeBean();
			addBin.setImage("local_scene_add");
			addBin.setName("");
			list.add(addBin);
			adapter.updateList(list);
			adapter.notifyDataSetChanged();
		}
		super.onResume();
	}
	
	
	
	

}
