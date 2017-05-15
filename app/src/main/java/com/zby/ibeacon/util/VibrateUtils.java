package com.zby.ibeacon.util;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * ��
 * @author zj
 * @d2015-5-26 @t����4:15:26
 */
public class VibrateUtils { 
	/**
	 * @param activity
	 * @param milliseconds  ����ʱ�䳤��
	 */
	public static void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	/**
	 * @param activity
	 * @param pattern �ڳ����𶯺�ȴ�3�����1�룬�ٵȴ�2�����5�룬�ٵȴ�3�����1��
     *   long[] pattern = {3000, 1000, 2000, 5000, 3000, 1000}; // OFF/ON/OFF/ON
	 * @param isRepeat �Ƿ�ѭ��
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
