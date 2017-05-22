package com.zby.led.adapter;

import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.DeviceBean;

import android.R.layout;
import android.content.Context;
import android.graphics.LinearGradient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 * @2015-6-3 @time下午11:36:01
 * 
 *           设别列表界面
 */
public class DeviceAdapter extends BaseAdapter {

	private List<DeviceBean> list;
	private Context mContext;
	private Holder mHolder;

	private DeviceControlListener listener;

	private DeviceAdapter() {
		// TODO Auto-generated constructor stub
	}

	public DeviceAdapter(Context context, List<DeviceBean> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.device_list_item, null);
			mHolder = new Holder();
			mHolder.tv_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			// mHolder.tv_address = (TextView)
			// convertView.findViewById(R.id.textView_mac);
			// mHolder.tv_rssi = (TextView)
			// convertView.findViewById(R.id.textView_rssi);
			mHolder.btn_delete = (Button) convertView
					.findViewById(R.id.button_delete);
			mHolder.btn_link = (Button) convertView
					.findViewById(R.id.button_link);
			mHolder.btn_switch = (Button) convertView
					.findViewById(R.id.button_switch);
			mHolder.btn_edit = (Button) convertView
					.findViewById(R.id.button_edit);
			mHolder.layout_control = (LinearLayout) convertView
					.findViewById(R.id.layout_control);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		if (position >= list.size())
			return null;
		final DeviceBean bin = list.get(position);
		mHolder.tv_name.setText(bin.getName());
		if (bin.isShow()) {
			mHolder.layout_control.setVisibility(View.VISIBLE);
		} else {
			mHolder.layout_control.setVisibility(View.GONE);
		}
		if(bin.getConnectionInterface()!=null && bin.getConnectionInterface().isLink()) {
			mHolder.btn_link.setBackgroundResource(R.drawable.cb_green_press);
		} else {
			mHolder.btn_link.setBackgroundResource(R.drawable.cb_green_normal);
		}
		if(bin.islink() && bin.isOnOff()) {
			mHolder.tv_name.setSelected(true);
			mHolder.btn_switch.setSelected(true);
		} else {
			mHolder.btn_switch.setSelected(false);
			mHolder.tv_name.setSelected(false);
		}
		// mHolder.tv_address.setText(bin.getDeviceAddress());
		// mHolder.tv_rssi.setText(String.format("%02d", bin.getRSSI()));

		mHolder.btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onDelete(position, bin);
				}
			}
		});
		mHolder.btn_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onEdit(position, bin);
				}
			}
		});
		mHolder.btn_switch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onSwitch(position, bin);
				}
			}
		});
		mHolder.btn_link.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onLink(position, bin);
				}
			}
		});
		mHolder.tv_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onLightDetail(position, bin);
				}
			}
		});
		return convertView;
	}

	class Holder {
		private TextView tv_name;
		// private TextView tv_address;
		// private TextView tv_rssi;

		private Button btn_link;
		private Button btn_edit;
		private Button btn_switch;
		private Button btn_delete;
		private LinearLayout layout_control;// 属性控制

	}

	/**
	 * 点击监听事件
	 * @author Administrator
	 *
	 */
	public interface DeviceControlListener {
		public void onEdit(int position, DeviceBean bean);

		public void onDelete(int position, DeviceBean bean);

		public void onLink(int position, DeviceBean bean);

		public void onSwitch(int position, DeviceBean bean);
		
		public void onLightDetail(int position, DeviceBean bean);

	}

	public void setOnControlListener(DeviceControlListener listener) {
		this.listener = listener;
	}

}
