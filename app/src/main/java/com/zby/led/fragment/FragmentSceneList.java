package com.zby.led.fragment;

import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.led.adapter.SceneAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FragmentSceneList extends Fragment {
	
	private List<SceneModeBean> list;
	
	public FragmentSceneList() {}
	
	private GridView gridView;
	private SceneAdapter adapter;
	
	private OnItemClickListener onItemListener;
	
	private int checkId;
	
	public static FragmentSceneList getInstance(List<SceneModeBean> list,OnItemClickListener onItemListener, int checkId){
		FragmentSceneList fragmentSceneList = new FragmentSceneList();
		fragmentSceneList.list = list;
		fragmentSceneList.onItemListener = onItemListener;
		fragmentSceneList.checkId = checkId;
		return fragmentSceneList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_scene_list, null);
		gridView = (GridView) v.findViewById(R.id.gridView);
		gridView.setNumColumns(AppConstants.gridView_numColums);
		adapter = new SceneAdapter(getActivity(), list);
		adapter.setShowChecked(false, checkId);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(onItemListener);
		return v;
	}
	
	

}
