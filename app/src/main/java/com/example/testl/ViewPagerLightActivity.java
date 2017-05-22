package com.example.testl;

import java.util.List;

import com.smartmini.zby.testl.R;
import com.zby.ibeacon.agreement.CmdPackage;
import com.zby.ibeacon.agreement.CmdParse;
import com.zby.ibeacon.agreement.ConnectBroadcastReceiver;
import com.zby.ibeacon.bean.DeviceBean;
import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.manager.DeviceManager;
import com.zby.ibeacon.manager.SceneBeanManager;
import com.zby.ibeacon.util.VibrateUtils;
import com.zby.led.adapter.SceneAdapter;
import com.zby.led.sql.DeviceSqlService;
import com.zby.led.sql.SceneModeSqlService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ViewPagerLightActivity extends BaseActivity {

    private final static String TAG = ViewPagerLightActivity.class.getSimpleName();

    private SeekBar sb_brightness, sb_color;
    private TextView tv_brightness, tv_color;
    private ImageView iv_switch;

    private SceneModeSqlService sceneModeSqlService;
    private DeviceSqlService deviceSqlService;
    private List<SceneModeBean> list;
    private SceneAdapter adapter;
    private GridView gridView;

    private DeviceBean dbin;

    /**
     * seekbar 是否在滑动中
     */
    private boolean isSeekScroll = false;
    /**
     * 最后一条数据发送时间
     */
    private long lastSendTime;

    private final static int activity_scene_update = 12;

    private final static int handler_status = 113;
    private final static int handler_status_all = 114;

    @Override protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_light);
        initViews();
        initHandler();
        Log.d(TAG, "oncreate ");
        //initData();
    }

    private void initViews() {
        initBaseViews(this);
        tv_title.setText(R.string.mode_light);
        gridView = (GridView) findViewById(R.id.gridView);
        tv_brightness = (TextView) findViewById(R.id.textView_brightness);
        tv_color = (TextView) findViewById(R.id.textView_color);
        sb_brightness = (SeekBar) findViewById(R.id.seekBar_brightness);
        sb_color = (SeekBar) findViewById(R.id.seekBar_color);
        iv_switch = (ImageView) findViewById(R.id.btn_switch_light);

        deviceSqlService = new DeviceSqlService(this);
        dbin = DeviceManager.getInstance().getDeviceBean();
        sceneModeSqlService = new SceneModeSqlService(this);
        list = sceneModeSqlService.selectDefualt();
        adapter = new SceneAdapter(this, list);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                SceneModeBean bean;
                synchronized (list) {
                    bean = list.get(arg2);
                    dbin.setNowSceneId(bean.getId());
                    adapter.notifyDataSetChanged();
                    dbin.setBrightness(bean.getRedBright());
                    dbin.setColorYellow(bean.getMixBirght());
                    mHandler.sendEmptyMessage(handler_status_all);
                    dbin.writeAgreement(CmdPackage.setD1Control(bean.getRedBright(), bean.getMixBirght()));
                    //记录控制的值
                    deviceSqlService.updateOldControl(dbin);
                }
            }
        });
        //gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
        //
        //	@Override
        //	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
        //			int arg2, long arg3) {
        //		// TODO Auto-generated method stub
        //		synchronized (list) {
        //				Intent intent = new Intent(ViewPagerLightActivity.this , SceneDetailActivity.class);
        //				intent.putExtra("sceneModeBean", list.get(arg2));
        //				getParent().startActivityForResult(intent, activity_scene_update);
        //		}
        //		return true;
        //	}
        //});

        sb_brightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekScroll = false;
                sendD1ControlNoInsert();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                if (fromUser) {
                    isSeekScroll = true;
                    long now = System.currentTimeMillis();
                    if (now - lastSendTime > AppConstants.delaySeekTime) {
                        lastSendTime = now;
                        sendD1ControlNoInsert();
                    }
                }
            }
        });
        sb_color.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekScroll = false;
                sendD1Control();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                if (fromUser) {
                    isSeekScroll = true;
                    long now = System.currentTimeMillis();
                    if (now - lastSendTime > AppConstants.delaySeekTime) {
                        lastSendTime = now;
                        sendD1ControlNoInsert();
                    }
                }
            }
        });
    }

    private void initData() {
        tv_brightness.setText(getString(R.string.brightness) + ": " + dbin.getBrightness() + "%");
        tv_color.setText(getString(R.string.color) + ": " + dbin.getColorYellow() + "%");
        iv_switch.setSelected(dbin.isOnOff());
        sb_brightness.setProgress(dbin.getBrightness());
        sb_color.setProgress(dbin.getColorYellow());
        //		 if(adapter!=null) {
        //			 adapter.notifyDataSetChanged();
        //		 }
    }

    private void initHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ConnectBroadcastReceiver.Broad_Cmd:
                        switch (msg.arg1) {
                            case CmdParse.Cmd_A0_status:
                                //if(!isSeekScroll) {
                                //								 sb_brightness.setProgress(dbin.getRedBright());
                                //								 sb_color.setProgress(dbin.getMixBirght());
                                //}
                                //							 tv_brightness.setText(getString(R.string.brightness)+": "+dbin.getRedBright()+"%");
                                //							 tv_color.setText(getString(R.string.color)+": "+dbin.getColor()+"K");
                                //							 iv_switch.setSelected(dbin.isOnOff());
                                break;
                            case CmdParse.Cmd_A1_timing:
                                break;
                            case CmdParse.Cmd_A2_password:
                                break;
                            case CmdParse.Cmd_A3_name:
                                break;
                        }
                        break;
                    case handler_status:
                        tv_brightness.setText(
                                getString(R.string.brightness) + ": " + dbin.getBrightness() + "%");
                        tv_color.setText(
                                getString(R.string.color) + ": " + dbin.getColorYellow() + "%");
                        iv_switch.setSelected(dbin.isOnOff());
                        break;
                    case handler_status_all:
                        tv_brightness.setText(
                                getString(R.string.brightness) + ": " + dbin.getBrightness() + "%");
                        tv_color.setText(
                                getString(R.string.color) + ": " + dbin.getColorYellow() + "%");
                        iv_switch.setSelected(dbin.isOnOff());
                        sb_brightness.setProgress(dbin.getBrightness());
                        sb_color.setProgress(dbin.getColorYellow());
                        break;
                }
            }
        };
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        SceneModeBean sbin;
        switch (requestCode) {
            case activity_scene_update:
                if (resultCode == Activity.RESULT_OK) {//修改
                    sbin = (SceneModeBean) data.getSerializableExtra("sceneModeBean");
                    sceneModeSqlService.update(sbin);
                    synchronized (list) {
                        SceneBeanManager.updateSceneMode(list, sbin);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    public void onBtnClick(View v) {
        int value;
        switch (v.getId()) {
            case R.id.btn_brightness_add:
                value = sb_brightness.getProgress();
                value++;
                if (value > sb_brightness.getMax()) value = sb_brightness.getMax();
                sb_brightness.setProgress(value);
                sendD1Control();
                break;
            case R.id.btn_brightness_reduce:
                value = sb_brightness.getProgress();
                value--;
                if (value < 0) value = 0;
                sb_brightness.setProgress(value);
                sendD1Control();
                break;
            case R.id.btn_color_add:
                value = sb_color.getProgress();
                value++;
                if (value > sb_color.getMax()) value = sb_color.getMax();
                sb_color.setProgress(value);
                sendD1Control();
                break;
            case R.id.btn_color_reduce:
                value = sb_color.getProgress();
                value--;
                if (value < 0) value = 0;
                sb_color.setProgress(value);
                sendD1Control();

                break;
            case R.id.btn_switch_light:
                VibrateUtils.Vibrate(this, 100);
                dbin.controlSwithc();
                //dbin.setColorYellow(dbin.getOldColorYellow());
                if (dbin.isOnOff()) {
                    dbin.setBrightness(dbin.getOldBrightness());
                    dbin.setColorYellow(dbin.getOldColorYellow());
                } else {
                    dbin.setBrightness(0);
                    dbin.setColorYellow(0);
                }
                mHandler.sendEmptyMessage(handler_status_all);
                break;
        }
    }

    /**
     * 记录到数据库中
     */
    private void sendD1Control() {
        int brightness = sb_brightness.getProgress();
        int color = sb_color.getProgress();
        Log.v("tag", dbin.getDeviceAddress()
                + " bright： "
                + brightness
                + " color:"
                + color
                + " "
                + sb_color.getMax());
        dbin.setBrightness(brightness);
            dbin.setColorYellow(sb_color.getProgress());
            dbin.writeAgreement(CmdPackage.setD1Control(dbin.getBrightness(), dbin.getColorYellow()));
        mHandler.sendEmptyMessage(handler_status);
        //记录控制的值
        deviceSqlService.updateOldControl(dbin);
    }

    /**
     * 不记录到数据库中
     */
    private void sendD1ControlNoInsert() {
        int brightness = sb_brightness.getProgress();
        int color = sb_color.getProgress();
        Log.v("tag", dbin.getDeviceAddress()
                + " bright： "
                + brightness
                + " color:"
                + color
                + " "
                + sb_color.getMax());
        dbin.setBrightness(brightness);
            dbin.setColorYellow(sb_color.getProgress());
            dbin.writeAgreement(CmdPackage.setD1Control(dbin.getBrightness(), dbin.getColorYellow()));
        mHandler.sendEmptyMessage(handler_status);
    }

    @Override protected void onStart() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onstart1");
        super.onStart();
    }

    @Override protected void onStop() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onstop1");
        super.onStop();
    }

    @Override protected void onResume() {
        // TODO Auto-generated method stub
        initData();
        if (adapter != null) {
            list = sceneModeSqlService.selectDefualt();
            adapter.updateList(list);
            adapter.notifyDataSetChanged();
        }
        Log.v(TAG, "onresume1");
        super.onResume();
    }
}
