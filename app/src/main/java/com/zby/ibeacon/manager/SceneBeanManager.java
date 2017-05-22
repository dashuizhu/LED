package com.zby.ibeacon.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;

import com.android.internal.util.XmlUtils;
import com.smartmini.zby.testl.R;
import com.zby.ibeacon.bean.SceneModeBean;
import com.zby.ibeacon.constants.AppConstants;
import com.zby.ibeacon.util.MyResourceUtil;

/**
 * @author Administrator
 *	场景模式管理类，包括加载默认数据，
 */
public class SceneBeanManager {
	/**
	 * 读取默认的4种场景模式
	 * @param context
	 * @return
	 */
	public static List<SceneModeBean> loadDefaultScene(Context context) {
		List<SceneModeBean> list = new ArrayList<SceneModeBean>();
		try {
			XmlResourceParser parser = context.getResources().getXml(
					R.xml.scene_default);
			AttributeSet attrs = Xml.asAttributeSet(parser);
			XmlUtils.beginDocument(parser, "scenemode");

			final int depth = parser.getDepth();

			int type = -1;
			while (((type = parser.next()) != XmlPullParser.END_TAG || parser
					.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

				if (type != XmlPullParser.START_TAG) {
					continue;
				}

				TypedArray a = context.obtainStyledAttributes(attrs,
						R.styleable.scene);
				
				SceneModeBean bean = new SceneModeBean();
				bean.setRedBright(a.getInt(R.styleable.scene_brightness,0));
				bean.setMixBirght(a.getInt(R.styleable.scene_color,0));
				bean.setName(a.getString(R.styleable.scene_name));
				bean.setImage(a.getString(R.styleable.scene_image));
				bean.setDeleteable(false);
				list.add(bean);
				a.recycle();
			}
		} catch (XmlPullParserException e) {
		} catch (IOException e) {
		}	
		return list;
	}

	
	/**
	 * 修改 或者添加到倒数第二个
	 * @param list
	 * @param sbin
	 */
	public static void updateSceneMode(List<SceneModeBean> list, SceneModeBean sbin) {
		SceneModeBean bean ;
			for(int i=0; i<list.size() ; i++) {
				bean = list.get(i);
				if(sbin.getId()==bean.getId()){
					list.remove(i);
					list.add(i, sbin);
					return;
				}
			}
			//添加到倒数第二个, 最后一个为add
			list.add(list.size()-1,sbin);
	}
	
	/**
	 * 删除
	 * @param list
	 * @param sbin
	 */
	public static void deleteSceneMode(List<SceneModeBean> list, SceneModeBean sbin) {
		SceneModeBean bean ;
			for(int i=0; i<list.size() ; i++) {
				bean = list.get(i);
				if(sbin.getId()==bean.getId()){
					list.remove(i);
					return;
				}
			}
	}

	/**
	 * 获取图片
	 * @param mContext
	 * @param bean
	 * @return
	 */
	public static int getImageId(Context mContext, SceneModeBean bean) {
		if(bean.getImage().equals("")){
			return R.drawable.local_scene_1;
		} else if(bean.getImage().contains("local_scene_")) {
			int id = MyResourceUtil.getDrawableId(mContext, bean.getImage());
			if(id==0) {//本地未找到资源
				return R.drawable.local_scene_1;
			} else {//本地，找到里
				return id;
			}
		}
		return R.drawable.local_scene_1;
	}
}
