package com.example.testl;

import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.led.fragment.FragmentSceneList;
import com.zby.led.sql.SceneModeSqlService;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

public class DialogSceneListActivity extends Activity {

	private SceneModeSqlService sceneSqlService;
	private List<SceneModeBean> list;
	private FragmentSceneList fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_scene_list);
		initViews();
	}

	private void initViews() {
		sceneSqlService = new SceneModeSqlService(this);
		list = sceneSqlService.selectAll();

		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		fragment = FragmentSceneList.getInstance(list,new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SceneModeBean bin = list.get(arg2);
				Intent intent = getIntent();
				intent.putExtra("sceneModeBean", bin);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		},  getIntent().getIntExtra("sceneId", -1));
		transaction.replace(R.id.layout_fragment, fragment);
		transaction.commit();
	}

	public void btn_back(View v) {
		finish();
	}
}
