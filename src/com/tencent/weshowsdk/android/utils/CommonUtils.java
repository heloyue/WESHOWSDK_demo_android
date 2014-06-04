/**
 * 
 */
package com.tencent.weshowsdk.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author isaacxie
 * 
 */
public class CommonUtils {

	/**
	 * @des 得到config.properties配置文件中的所有配置
	 * @return Properties对象
	 */
	public static Properties getConfig() {
		Properties props = new Properties();
		InputStream in = CommonUtils.class
				.getResourceAsStream("/config/config.properties");
		try {
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("工具包异常", "获取配置文件异常");
			e.printStackTrace();
		}
		return props;
	}

	/**
	 * 判断当前网络是否可用
	 * 
	 * @param activity
	 *            当前Acitivity对象
	 * @return 可用返回true 否则返回false
	 * 
	 * */
	public static boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		} else {
			NetworkInfo info[] = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 存储缓存数据
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            存储数据特定的键
	 * @param value
	 *            存储的数据 （String类型）
	 */
	public static void saveSharePersistent(Context context, String key,
			String value) {
		SharePersistent mSharePersistent = SharePersistent.getInstance();
		mSharePersistent.put(context, key, value);
	}

	/**
	 * 存储缓存数据
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            存储数据特定的键
	 * @param value
	 *            存储的数据 （long类型）
	 */
	public static void saveSharePersistent(Context context, String key,
			long value) {
		SharePersistent mSharePersistent = SharePersistent.getInstance();
		mSharePersistent.put(context, key, value);
	}
}
