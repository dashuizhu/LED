package com.zby.ibeacon.util;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * 震动
 * @author zj
 * @d2015-5-26 @t下午4:15:26
 */
public class VibrateUtils { 
	/**
	 * @param activity
	 * @param milliseconds  整栋时间长度
	 */
	public static void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	/**
	 * @param activity
	 * @param pattern 在程序起动后等待3秒后，振动1秒，再等待2秒后，振动5秒，再等待3秒后，振动1秒
     *   long[] pattern = {3000, 1000, 2000, 5000, 3000, 1000}; // OFF/ON/OFF/ON
	 * @param isRepeat 是否循环
	 */
	public static void Vibrate(final Activity activity, long[] pattern,boolean isRepeat) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}
	
	public static void cancel(Activity activity) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.cancel();
	}
}
