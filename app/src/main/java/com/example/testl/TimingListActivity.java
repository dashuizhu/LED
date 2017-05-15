package com.example.testl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.agreement.CmdPackage;
import com.zby.ibeacon.agreement.CmdParse;
import com.zby.ibeacon.agreement.ConnectBroadcastReceiver;
import com.zby.ibeacon.bean.DeviceBean;
import com.zby.ibeacon.bean.TimingBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.manager.AlertDialogService;
import com.zby.ibeacon.manager.DeviceManager;
import com.zby.led.adapter.TimingAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class TimingListActivity extends BaseActivity {
	
	private List<TimingBean> list;
	private ListView listView;
	private TimingAdapter adapter;
	
	private final static int activity_timing_add = 11;
	private final static int activity_timing_update = 12;
	
	private DeviceBean bin;
	
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timing_list);
		initViews();
		initHandler();
	}
	
	private void initViews() {
		initBaseViews(this);
		tv_title.setText(R.string.timing);
		tv_menu.setBackgroundResource(R.drawable.btn_add_timing);
		layout_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(list.size()>=5) {
					showToast(R.string.timing_max);
					return;
				}
				Intent intent = new Intent(TimingListActivity.this ,TimingDetailActivity.class);
				TimingBean tbin = new TimingBean();
				intent.putExtra("timingBean", tbin);
				//表示新添加
				tbin.setId(-1);
				startActivityForResult(intent, activity_timing_add);
			}
		});
		layout_menu.setVisibility(View.VISIBLE);
		
		bin = DeviceManager.getInstance().getDeviceBean();
		bin.writeAgreement(CmdPackage.getReadTiming());
		list = bin.getTimingList();
		adapter = new TimingAdapter(this, list);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimingListActivity.this ,TimingDetailActivity.class);
				TimingBean tbin;
				synchronized (list) {
					tbin = list.get(arg2);
					intent.putExtra("timingBean", tbin);
				}
				startActivityForResult(intent, activity_timing_update);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				final TimingBean tbin = list.get(arg2);
				tbin.setDelete(true);
				dialog = AlertDialogService.getConfirmDialog(TimingListActivity.this, getString(R.string.dialog_delete_info) , "", new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(bin.writeAgreement(CmdPackage.setD2Timing(tbin))) {
							synchronized (list) {
								deleteBean(tbin);
							}
							dialog.dismiss();
						}
					}
				});
				dialog.show();
				return true;
			}
		});
	}
	
	private void initHandler( ) {
		mHandler = new Handler() {
			public void handleMessage(Message msg){
				switch(msg.what){
				 case ConnectBroadcastReceiver.Broad_Cmd:
					 switch(msg.arg1){
						 case CmdParse.Cmd_A0_status:
							 break;
						 case CmdParse.Cmd_A1_timing:
							 adapter.notifyDataSetChanged();
							 break;
						 case CmdParse.Cmd_A2_password:
							 break;
						 case CmdParse.Cmd_A3_name:
							 break;
					 }
					break;
				}
			}
		};
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		TimingBean tbin; 
		switch(requestCode) {
			case activity_timing_add:
				if(resultCode==Activity.RESULT_OK) {
					tbin = (TimingBean) data.getSerializableExtra("timingBean");
					tbin.setId(getNewId());
					if(bin.writeAgreement(CmdPackage.setD2Timing(tbin))) {
						updateBean(tbin);
					}
					//if(AppConstants.isDemo) {
					//	DeviceManager.getInstance().getDeviceBean().getParse().parseData(CmdPackage.setD2Timing(tbin));
						//updateBean(tbin);
					//}
				}
				break;
			case activity_timing_update:
				if(resultCode==Activity.RESULT_OK) {
					tbin = (TimingBean) data.getSerializableExtra("timingBean");
					if(bin.writeAgreement(CmdPackage.setD2Timing(tbin))) {
						updateBean(tbin);
					}
//					if(AppConstants.isDemo) {
//						DeviceManager.getInstance().getDeviceBean().getParse().parseData(CmdPackage.setD2Timing(tbin));
//						//updateBean(tbin);
//					}
				}
				break;
			}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onBtnClick(View v) {
		
	}
	
	/**
	 * 获得最新的空白的id
	 * @return
	 */
	private int getNewId(){
		Set<Integer> set = new HashSet<Integer>();
		int id =0;
		synchronized (list) {
			for(int i=0; i<list.size(); i++) {
				set.add(list.get(i).getId());
			}
		}
		while(set.contains(id)) {
			id++;
		}
		return id;
	}
	
	private void updateBean(TimingBean tbin) {
		TimingBean bean;
		synchronized (list) {
			for(int i=0; i<list.size(); i++) {
				bean = list.get(i);
				if(bean.getId() == tbin.getId()) {
					list.remove(i);
					list.add(i, tbin);
					adapter.notifyDataSetChanged();
					return;
				}
			}
			list.add(tbin);
			adapter.notifyDataSetChanged();
		}
	}
	
	private void deleteBean(TimingBean tbin) {
		TimingBean bean;
		synchronized (list) {
			for(int i=0; i<list.size(); i++) {
				bean = list.get(i);
				if(bean.getId() == tbin.getId()) {
					list.remove(i);
					adapter.notifyDataSetChanged();
					return;
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(adapter!=null) {
			 adapter.notifyDataSetChanged();
		}
		super.onResume();
	}
	
	
	
}
