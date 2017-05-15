package com.zby.led.adapter;

import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.DeviceBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 * @2015-6-3 @time下午11:36:01
 * 
 * 设别列表界面
 */
public class DeviceScanAdapter extends BaseAdapter{
	
	private List<DeviceBean> list;
	private Context mContext;
	private Holder mHolder;
	
	private DeviceScanAdapter() {
		// TODO Auto-generated constructor stub
	}
	
	public  DeviceScanAdapter(Context context, List<DeviceBean> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public DeviceBean getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.scan_list_item, null);
			mHolder = new Holder();
			mHolder.tv_name = (TextView) convertView.findViewById(R.id.textView_name);
			mHolder.tv_address = (TextView) convertView.findViewById(R.id.textView_mac);
			mHolder.tv_rssi = (TextView) convertView.findViewById(R.id.textView_rssi);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		if(position>=list.size()) return null;
		DeviceBean bin = list.get(position);
		mHolder.tv_name.setText(bin.getBluetoothName());
		mHolder.tv_address.setText(bin.getDeviceAddress());
		mHolder.tv_rssi.setText(String.format("%02d", bin.getRSSI()));
		return convertView;
	}
	
	class Holder{
		private TextView tv_name;
		private TextView tv_address;
		private TextView tv_rssi;
	}

}
