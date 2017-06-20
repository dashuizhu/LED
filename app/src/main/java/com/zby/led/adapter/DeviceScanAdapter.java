package com.zby.led.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.DeviceBean;
import java.util.List;

/**
 * @author Administrator
 *
 * @2015-6-3 @time下午11:36:01
 * 
 * 设别列表界面
 */
public class DeviceScanAdapter extends RecyclerView.Adapter<DeviceScanAdapter.ViewHolder> implements
				View.OnClickListener{

	private List<DeviceBean> mList;

	public DeviceScanAdapter(List<DeviceBean> list) {
		mList = list;
	}

	@Override public DeviceScanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_list_item, parent, false);
		ViewHolder vh = new ViewHolder(view);
		view.setOnClickListener(this);
		return vh;
	}

	@Override public void onBindViewHolder(DeviceScanAdapter.ViewHolder holder, int position) {
		holder.mTvName.setText(mList.get(position).getName());
		holder.mTvAddress.setText(mList.get(position).getDeviceAddress());
		holder.itemView.setTag(position);
	}

	@Override public int getItemCount() {
		return mList.size();
	}

	@Override public void onClick(View view) {
		if (mOnItemClickListener != null) {
			Integer position = (Integer) view.getTag();
			if (position!=null) {
				mOnItemClickListener.onItemClick(view, mList.get(position));
			}
		}

	}

	//自定义的ViewHolder，持有每个Item的的所有界面元素
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView mTvName;
		public TextView mTvAddress;

		public ViewHolder(View view){
			super(view);
			mTvName = (TextView) view.findViewById(R.id.textView_name);
			mTvAddress = (TextView) view.findViewById(R.id.textView_mac);
		}
	}

	public interface OnRecyclerViewItemClickListener {
		void onItemClick(View view , DeviceBean data);
	}

	private OnRecyclerViewItemClickListener mOnItemClickListener = null;

	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}
}
