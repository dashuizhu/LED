package com.example.testl;

import java.util.ArrayList;
import java.util.List;

import com.smartmini.zby.testl.R;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DeviceViewPageActivity extends Activity {

	private ViewPager viewPager;
	private MyPagerAdapter adapter;
	//private List<View> list;
	private List<View> list;
	
	private TextView tv1, tv2,  lastTv;
	
	LocalActivityManager manager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_viewpager);
		manager = new LocalActivityManager(this , true);
		manager.dispatchCreate(savedInstanceState);
		initViews();
		
	}

	private void initViews() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		tv1 = (TextView) findViewById(R.id.text1);
		tv2 = (TextView) findViewById(R.id.text2);
		//tv3 = (TextView) findViewById(R.id.text3);
		initPageViews();
	}

	private void initPageViews() {
		LayoutInflater mInflater = getLayoutInflater();
		list = new ArrayList<View>();
		
		
//		list.add(mInflater.inflate(R.layout.activity_viewpager_light, null));
//		list.add(mInflater.inflate(R.layout.activity_viewpager_scene, null));
//		list.add(mInflater
//				.inflate(R.layout.activity_viewpager_settingggg, null));

		
		 Intent intent = new Intent(this, DeviceListActivity.class);
		 list.add(getView("0", intent));
	        Intent intent2 = new Intent(this, SettingActivity.class);
	        list.add(getView("1", intent2));
//	        Intent intent3 = new Intent(this, ViewPagerSettingActivity.class);
//	        list.add(getView("C", intent3));
	        
		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);
		lastTv = tv1;
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				textSelect(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		int a = viewPager.getCurrentItem();
		((BaseActivity) manager.getActivity(""+a)).handleActivityResult(requestCode, resultCode, data);
	}

	/**
     * 通过activity获取视图
     * @param id
     * @param intent
     * @return
     */
    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }


	public void onBtnClick(View v) {
		switch (v.getId()) {
		case R.id.text1:
			viewPager.setCurrentItem(0);
			textSelect(0);
			break;
		case R.id.text2:
			textSelect(1);
			viewPager.setCurrentItem(1);
			break;
		case R.id.text3:
			textSelect(2);
			viewPager.setCurrentItem(2);
			break;
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewPager) container).addView(list.get(position), 0);
			return list.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView(list.get(position));
		}

	}
	
	private void textSelect(int position){
		switch(position) {
		case 0:
			lastTv.setSelected(false);
			tv1.setSelected(true);
			lastTv = tv1;
			break;
		case 1:
			lastTv.setSelected(false);
			tv2.setSelected(true);
			lastTv = tv2;
			break;
		case 2:
			lastTv.setSelected(false);
//			tv3.setSelected(true);
//			lastTv = tv3;
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		((BaseActivity) manager.getActivity("0")).onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		((BaseActivity) manager.getActivity("0")).onStart();
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		((BaseActivity) manager.getActivity("0")).onStop();
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		((BaseActivity) manager.getActivity("0")).onResume();
		super.onResume();
	}
	
	
	

}
