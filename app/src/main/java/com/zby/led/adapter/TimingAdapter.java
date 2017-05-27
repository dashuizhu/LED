package com.zby.led.adapter;

import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.agreement.CmdPackage;
import com.zby.ibeacon.bean.TimingBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.manager.DeviceManager;

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
public class TimingAdapter extends BaseAdapter {

	private List<TimingBean> list;
	private Context mContext;
	private Holder mHolder;

	private TimingAdapter() {
		// TODO Auto-generated constructor stub
	}

	public TimingAdapter(Context context, List<TimingBean> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public TimingBean getItem(int position) {
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
					R.layout.timing_list_item, null);
			mHolder = new Holder();
			mHolder.tv_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			// mHolder.tv_address = (TextView)
			// convertView.findViewById(R.id.textView_mac);
			// mHolder.tv_rssi = (TextView)
			// convertView.findViewById(R.id.textView_rssi);
			mHolder.btn_enable = (Button) convertView.findViewById(R.id.button_enable);
			mHolder.tv_color = (TextView)convertView.findViewById(R.id.textView_color);
			mHolder.tv_date = (TextView) convertView.findViewById(R.id.textView_date);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		if (position >= list.size())
			return null;
		final TimingBean bin = list.get(position);
		mHolder.tv_name.setText(bin.getTimeString());
		StringBuffer sb = new StringBuffer();
		sb.append(mContext.getString(R.string.brightness));
		sb.append(bin.getBrightness());
		sb.append("% ");
		sb.append(mContext.getString(R.string.color));
		sb.append(bin.getColorYellow());
		sb.append("%");
		mHolder.tv_color.setText(sb.toString());
		mHolder.tv_date.setText(bin.getDateString());
		mHolder.btn_enable.setSelected(bin.isEnable());
		mHolder.btn_enable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bin.setEnable(!bin.isEnable());
				DeviceManager.getInstance().getDeviceBean().writeAgreement(CmdPackage.setD2Timing(bin));
				//if(AppConstants.isDemo) {
				//	DeviceManager.getInstance().getDeviceBean().getParse().parseData(CmdPackage.setD2Timing(bin));
					notifyDataSetChanged();
				//}
			}
		});
		return convertView;
	}

	class Holder {
		private TextView tv_name;
		// private TextView tv_address;
		// private TextView tv_rssi;

		private Button btn_enable;
		private TextView tv_date;
		private TextView tv_color;

	}

}
