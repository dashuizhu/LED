package com.zby.led.help;

import java.io.UnsupportedEncodingException;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.constants.AppConstants;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class ValueCheckHelp {
	
	/**
	 * 检查输入框的名字是否正确， 带了toast提示
	 * @param et
	 * @param mContext
	 * @return
	 */
	public static boolean helpValueCheck(EditText et, Context mContext){
		String name= et.getText().toString().trim();
		if(name.equals("")) {
			Toast.makeText(mContext, mContext.getString(R.string.input_name), Toast.LENGTH_LONG).show();
			et.requestFocus();
			return false;
		}
		try {
			if(name.getBytes(AppConstants.charSet).length>AppConstants.name_maxLength) {
				Toast.makeText(mContext, mContext.getString(R.string.name_too_long), Toast.LENGTH_LONG).show();
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
