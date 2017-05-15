package com.example.testl;

import com.smartmini.zby.testl.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingHelpActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_help);
		initBaseViews(this);
		tv_title.setText(R.string.about);
		
		TextView tv_content = (TextView) findViewById(R.id.textView_content);
		tv_content.setText(ToDBC(getString(R.string.about_content)));
	}
	
	
	public static String ToDBC(String input) {          
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {              
        if (c[i] == 12288) {                 
        c[i] = (char) 32;                  
        continue;
         }
         if (c[i] > 65280 && c[i] < 65375)
            c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }  

}
