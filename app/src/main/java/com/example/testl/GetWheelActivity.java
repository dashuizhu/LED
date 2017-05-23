package com.example.testl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.util.MyDateUtils;
import com.zby.ibeacon.view.wheel.NumericWheelAdapter;
import com.zby.ibeacon.view.wheel.OnWheelChangedListener;
import com.zby.ibeacon.view.wheel.WheelService;
import com.zby.ibeacon.view.wheel.WheelView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * <p>Description: 选择执行的定时时间</p>
 * @author zhujiang
 * @date 2014-7-31
 */
public class GetWheelActivity extends BaseActivity {
	
	private final static String TAG = GetWheelActivity.class.getSimpleName();
	
	private TextView tv_title;
	
	//wheel 时间
	private WheelView wheelView_year, wheelView_month, wheelView_day, wheelView_hour, wheelView_minute;
	private final int wheelTextSize = 7;
	private LinearLayout layout_hour ,layout_minute, layout_year, layout_month, layout_day;
	private int maxHour =23, minHour=0, maxMinute =59, minMinute =0, maxMonth =12, minMonth=1, maxDay,  minDay=1,  maxYear =2030, minYear=2015;
	
	//handler
	private final int InitWheelView = 3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.get_wheel_activity);
		
//		Window window = getWindow();
//		window.setGravity(Gravity.BOTTOM);
//		//背景阴影
//		WindowManager.LayoutParams lp2 = getWindow().getAttributes();
//		lp2.dimAmount = 0.5f;
//		getWindow().setAttributes(lp2);
//		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		
		initView();
		initWheelViewThread();
		//initListener();
	}
	

	private void initView() {
		layout_year = (LinearLayout) findViewById(R.id.layout_year);
		layout_month = (LinearLayout) findViewById(R.id.layout_month);
		layout_day = (LinearLayout) findViewById(R.id.layout_day);
		layout_hour = (LinearLayout) findViewById(R.id.layout_hour);
		layout_minute = (LinearLayout) findViewById(R.id.layout_minute);
		tv_title = (TextView) findViewById(R.id.textView_title);
	}
	
	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case InitWheelView:
				addWheelView();
				initListener();
				initValue();
				break;
			default:
				break;
			}
		};
	};
	
	//等待oncreate完成， 给layout高度
	private void initWheelViewThread() {
		new Thread() {
			@Override
			public void run() {
				int height = 0;
				while (height == 0) {
					if(layout_hour != null){
						height = layout_hour.getHeight();
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				h.sendEmptyMessage(InitWheelView);
				super.run();
			}
		}.start();
	}
	
	
	
	private void addWheelView() {
//		if (tunableTime == 0) {
//			maxHour = hour;
//			minHour = hour;
//			hour = 0;
//		} else {
//			hour = hour - minHour;
//		}
//		updateNowHour();
		
		//设置字体最大值，
//		int textSize = layout_hour.getHeight()/ wheelTextSize;
//		int maxSize = 20 + (int) (20* phone_density);
//		if(textSize>maxSize) textSize=maxSize;
//		wheelView_hour.setAdapter(new NumericWheelListAdapter(items));
//		setHour(value);
//		wheelView_hour.setCyclic(false);
//		wheelView_hour.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		layout_hour.addView(wheelViwe_hour);
		
		int size = layout_hour.getHeight() / wheelTextSize; 
		
		wheelView_hour = WheelService.getBinInt(this, maxHour, minHour, 1, "时", size, phone_density);
		layout_hour.addView(wheelView_hour);
		wheelView_hour.setTextColor(getResources().getColor(R.color.text_red), getResources().getColor(R.color.line_between));
		//wheelView_hour.setCenterDrawableSourceId(R.drawable.img_layout_wheel);
		
		wheelView_minute = WheelService.getBinInt(this, maxMinute, minMinute, 0, "分", size,  phone_density);
		layout_minute.addView(wheelView_minute);
		
		wheelView_year = WheelService.getBinInt(this, maxYear, minYear, 0, "年", size,  phone_density);
		layout_year.addView(wheelView_year);
		
		wheelView_month = WheelService.getBinInt(this, maxMonth, minMonth, 0, "月", size,  phone_density);
		layout_month.addView(wheelView_month);
		
		wheelView_day = WheelService.getBinInt(this, maxDay, minDay, 0, "日", size,  phone_density);
		layout_day.addView(wheelView_day);
	}
	
	
	
	private void initListener() {
		wheelView_year.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				if(!wheel.isScrolling()) {
					onDayMaxChanged(minYear+wheelView_year.getCurrentItem(), minMonth+ wheelView_month.getCurrentItem());
				}
				//nowHour = newValue + minHour;
				//listenerDay(nowHour);
			}
		});
		wheelView_month.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(!wheel.isScrolling()) {
					onDayMaxChanged(minYear+wheelView_year.getCurrentItem(), minMonth+ wheelView_month.getCurrentItem());
				}
				//listenerDay(nowMonth);
			}
		});
		
//		second_wheelView.addChangingListener(new OnWheelChangedListener() {
//
//			@Override
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				nowSecond = newValue + minSecond;
//				//listenerDay(nowMonth);
//			}
//		});
		
		
	}

	private void initValue() {
		int year, month , day , hour, minute;
		Intent mIntent= getIntent();
		Date d = new Date();
		//D。getYear是以1900为0
		year = mIntent.getIntExtra("year", d.getYear()+1900);
		//getMonth是以0开始
		month = mIntent.getIntExtra("month", d.getMonth()+1);
		day = mIntent.getIntExtra("day", d.getDate());
		hour = mIntent.getIntExtra("hour", d.getHours());
		minute = mIntent.getIntExtra("minute", d.getMinutes());

		wheelView_year.setCurrentItem(year-minYear);
		wheelView_month.setCurrentItem(month-minMonth);
		wheelView_day.setCurrentItem(day-minDay);
		wheelView_hour.setCurrentItem(hour-minHour);
		wheelView_minute.setCurrentItem(minute-minMinute);
	}
	
	
	
	public void btn_confirm(View view){
		
		int year , month ,day , hour , minute;
		year = wheelView_year.getCurrentItem()+minYear;
		month = wheelView_month.getCurrentItem()+minMonth;
		day = wheelView_day.getCurrentItem()+minDay;
		hour = wheelView_hour.getCurrentItem()+minHour;
		minute = wheelView_minute.getCurrentItem()+minMinute;
		
		Log.v(TAG, "now select date: " +year + "/" +month+"/" +day +" " + hour+":"+minute);
		
		Intent intent=new Intent();
		intent.putExtra("year", year);
		intent.putExtra("month", month);
		intent.putExtra("day", day);
		intent.putExtra("hour", hour);
		intent.putExtra("minute", minute);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	
	
	public void btn_back(View view){
		finish();
	}


	/**
	 * 年 月 改变导致  日的范围改变
	 * @param year
	 * @param month
	 */
	private void onDayMaxChanged(int year, int month) {
		int max = MyDateUtils.getMaxDayByMonth(year, month);
		
		//获得当前的选中项
		int currentItems = wheelView_day.getCurrentItem();
		//如果当月的 日期 最大值 ， 发生了变化
		if(max!= wheelView_day.getAdapter().getItemsCount()) {
			//修改adapter的范围
			wheelView_day.setAdapter(new NumericWheelAdapter(minDay, max, "%02d"));
			//如果之前的选中day  大于  这个月的最大天数， 修改值， （比如 ，之前是选中31号， 但新的只有30号， 就修改为选中30号） 
			if(currentItems>=max) {
				wheelView_day.setCurrentItem(max-minDay);
			}
		}
	}
	
}
