package com.zby.ibeacon.util;

public class MyDateUtils {
	
	/**
	 * �������¼��㵱�µ�����
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMaxDayByMonth(int year, int month) {
		int maxDay = 31;
		switch(month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			maxDay =31;
			break;
		
		case 4:
		case 6:
		case 9:
		case 11:
			maxDay = 30;
			break;
			
		case 2:
			if(year%4==0 && year%100 !=0) {
				maxDay = 29;
			} else {
				maxDay = 28;
			}
			break;
		}
		return maxDay;
	}

}
