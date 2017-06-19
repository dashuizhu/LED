package com.zby.led.adapter;

import android.support.v7.widget.RecyclerView;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.DeviceBean;


/**
 * @author Administrator
 *
 * @2015-6-3 @time下午11:36:01
 * 
 * 设别列表界面
 */
public class DeviceScanAdapter extends BGARecyclerViewAdapter<DeviceBean> {

	public DeviceScanAdapter(RecyclerView recyclerView) {
		super(recyclerView,R.layout.scan_list_item);
	}

	@Override protected void setItemChildListener(BGAViewHolderHelper helper, int viewType) {
		helper.setItemChildClickListener(R.id.layout_item);
	}

	@Override protected void fillData(BGAViewHolderHelper helper, int position, DeviceBean model) {
		helper.setText(R.id.textView_name, model.getName());
		helper.setText(R.id.textView_mac, model.getDeviceAddress());
	}
}
