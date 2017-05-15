package com.example.testl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.manager.SceneBeanManager;
import com.zby.ibeacon.util.MyResourceUtil;

public class MainActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int id = MyResourceUtil.getDrawableId(this, "btn_add");
		int id2 = MyResourceUtil.getDrawableId(this, "btn_addxadf");
		System.out.println("id " + id + " " + id2 + " " + R.drawable.btn_add);
	}

}
