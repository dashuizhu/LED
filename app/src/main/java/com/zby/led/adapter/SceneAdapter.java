package com.zby.led.adapter;

import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.ibeacon.manager.DeviceManager;
import com.zby.ibeacon.manager.SceneBeanManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SceneAdapter extends BaseAdapter {

	private List<SceneModeBean> list;
	private Context mContext;

	/**
	 * 是否显示 选中的图标
	 */
	private boolean isShowChecked = true;

	private int selectId = -1;

	private SceneAdapter() {
	};

	public SceneAdapter(Context context, List<SceneModeBean> list) {
		this.list = list;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.scene_list_item, null);
			mHolder = new Hodler();
			mHolder.tv_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			mHolder.iv_scene = (ImageView) convertView
					.findViewById(R.id.imageView_checked);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Hodler) convertView.getTag();
		}
		final SceneModeBean bin = list.get(position);
		mHolder.tv_name.setBackgroundResource(SceneBeanManager.getImageId(
				mContext, bin));
		Log.v("tag", this.hashCode() + ":" + bin.getId() + bin.getName()
				+ " list.size=" + list.size() + " ~~" + position + "~~~` "
				+ DeviceManager.getInstance().getDeviceBean().getNowSceneId());
		mHolder.tv_name.setText(bin.getName());
		if (isShowChecked) {
			selectId = DeviceManager.getInstance().getDeviceBean()
					.getNowSceneId();
		}

		if (bin.getId() ==selectId) {
			mHolder.iv_scene.setSelected(true);
		} else {
			mHolder.iv_scene.setSelected(false);
		}
		return convertView;
	}

	private Hodler mHolder;

	class Hodler {
		private TextView tv_name;
		private ImageView iv_scene;
	}

	public void updateList(List<SceneModeBean> list2) {
		// TODO Auto-generated method stub
		this.list = list2;
	}

	public void setShowChecked(boolean visible, int selectId) {
		this.isShowChecked = visible;
		this.selectId = selectId;
	}

}
