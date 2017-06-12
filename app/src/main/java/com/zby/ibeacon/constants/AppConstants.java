package com.zby.ibeacon.constants;

public class AppConstants {
	
	/**
	 * 是否是无硬件演示版
	 */
	public final static boolean isDemo  = false;
	
	/**
	 * seekbar 滑动间隔时间 ， 单位毫秒
	 */
	public final static int delaySeekTime = 50;
	
	/**
	 * 数据间的间隔时间 单位毫秒
	 */
	public final static int delayTime = 200;
	
	/**
	 * 密码校验超时时间  单位毫秒
	 */
	public final static int PasswordCheckTimeOut = 3000;
	
	/**
	 * 字符串发送到硬件 交互过程中 的  编码格式，
	 */
	public final static String charSet = "GBK";

	//public final static int Color_max = 6510;
	//public final static int Color_min = 2715;
	//public final static int Color_step = 15;
	//public static final int Color_range = 253; // ( max-min) / step

	/**
	 * 名字的最长字节数
	 */
	public static int name_maxLength = 18;
	
	/**
	 * 密码长度
	 */
	public static int password_length = 6;
	
	/**
	 * GridView默认一行显示个数
	 */
	public static int gridView_numColums = 3;
	
	//默认值
	public static int default_brightness = 50;
	public static int default_colorYellow = 100;

	/**
	 * 默认list值
	 */
	public static int default_scene_size = 2;
	/**
	 * 最大定时个数
	 */
	public static int TIMING_MAX_SIZE = 10;
}
