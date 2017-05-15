package com.example.testl;

import java.util.ArrayList;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.util.MyImage;
import com.zby.ibeacon.util.MyImage.ScalingLogic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

/**
 * <p>
 * Description: 引导图
 * </p>
 * 
 * @author zhujiang
 * @date 2014-5-28
 */
public class SettingGuideViewActivity extends Activity {

	private static final String TAG = "GuideViewActivity";

	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ViewGroup group;
	private ImageView imageView;
	//private ImageView[] imageViews;

	// 滑动到边上时结束
	// private ImageView imageView_guideOver;

	/**
	 * 是否从设置界面的 帮助跳转过来的
	 */
	private boolean isFormHelp = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide);
		 //imageViews = new ImageView[pageViews.size()];
		// group是R.layou.main中的负责包裹小圆点的LinearLayout.
		group = (ViewGroup) findViewById(R.id.layout_viewGroup);

		viewPager = (ViewPager) findViewById(R.id.guidePages);
		// for (int i = 0; i < pageViews.size(); i++) {
		// imageView = new ImageView(GuideViewActivity.this);
		// imageView.setLayoutParams(new LayoutParams(30,30));
		// imageView.setPadding(50, 10, 50, 10);
		// imageViews[i] = imageView;
		// if (i == 0) {
		// //默认选中第一张图
		// imageViews[i].setBackgroundResource(R.drawable.green_point);
		// }else {
		// imageViews[i].setBackgroundResource(R.drawable.gray_point);
		// }
		// group.addView(imageViews[i]);
		// }
		//
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initView();
				handler.sendEmptyMessage(1);
			}}).start();

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				viewPager.setAdapter(new GuidePageAdapter());
				viewPager
						.setOnPageChangeListener(new GuidePageChangeListener());
				break;
			}
		}
	};

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// TODO Auto-generated method stub

		pageViews = new ArrayList<View>();

		// View view1 = getLayoutInflater().inflate(R.layout.guide1, null);
		// View view2 = getLayoutInflater().inflate(R.layout.guide2, null);
		// View view3 = getLayoutInflater().inflate(R.layout.guide3, null);

		ImageView view3 = new ImageView(this);
		// view4.setBackgroundResource(R.drawable.guide);
		view3.setBackgroundDrawable(MyImage
				.decodeFileBitmapDrawable(getResources(), R.drawable.guide1,
						400, 300, new ScalingLogic()));

		ImageView view4 = new ImageView(this);
		// view4.setBackgroundResource(R.drawable.guide4);
		view4.setBackgroundDrawable(MyImage
				.decodeFileBitmapDrawable(getResources(), R.drawable.guide2,
						400, 300, new ScalingLogic()));

		ImageView view5 = new ImageView(this);
		// view5.setBackgroundResource(R.drawable.guide5);
		view5.setBackgroundDrawable(MyImage
				.decodeFileBitmapDrawable(getResources(), R.drawable.guide3,
						400, 300, new ScalingLogic()));

		ImageView view6 = new ImageView(this);
		// view6.setBackgroundResource(R.drawable.guide1);
		view6.setBackgroundDrawable(MyImage
				.decodeFileBitmapDrawable(getResources(), R.drawable.guide4,
						400, 300, new ScalingLogic()));
		pageViews.add(view3);
		pageViews.add(view4);
		pageViews.add(view5);
		pageViews.add(view6);

		// imageView_guideOver = (ImageView)
		// findViewById(R.id.imageView_guideOver);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(pageViews!=null) {
			for(int i=0 ;i<pageViews.size() ; i++) {
				View v = pageViews.get(i);
				v.destroyDrawingCache();
			}
			pageViews.clear();
		}
		super.onDestroy();
	}

	/** 指引页面Adapter */
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	int isScrolledCount = 0;

	/**
	 * 指引页面改监听器, 小圆点显示正处于第几个页面
	 */
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			// 最后一页划不动。
			// 进入最后一页，arg0 会出现一次
			// 然后滑动，arg0 就会多次出现
			if (arg0 == (pageViews.size()-1 )) {
				isScrolledCount++;
				if (isScrolledCount > 1) {// 表示着界面往右滑动了
					finish();
				}
			} else {
				isScrolledCount = 0;
			}
		}

		@Override
		public void onPageSelected(int arg0) {
			// 将结束按钮显示出来
		}

	}

}