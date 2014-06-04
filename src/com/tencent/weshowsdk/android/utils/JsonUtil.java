/**
 * 
 */
package com.tencent.weshowsdk.android.utils;


//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

/**
 * @author isaacxie
 * 
 */
public class JsonUtil {
	/**
	 * 从一个JSON 对象字符格式中得到一个java对象，形如： {"id" : idValue, "name" : nameValue,
	 * "aBean" : {"aBeanId" : aBeanIdValue, ...}}
	 * 
	 * @param <T>
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	/*public static <T> T getObjfromJson(String jsonString, Class<T> clazz) {
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) JSONObject.toBean(jsonObject, clazz);
	}*/

	/**
	 * 从一个JSON数组得到一个java对象数组，形如： [{"id" : idValue, "name" : nameValue}, {"id" :
	 * idValue, "name" : nameValue}, ...]
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	/*public static <T> T[] getArrayfromJson(String jsonString, Class<T> clazz) {
		JSONArray array = JSONArray.fromObject(jsonString);
		T[] obj = (T[]) new Object[array.size()];
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			obj[i] = (T) JSONObject.toBean(jsonObject, clazz);
		}
		return obj;
	}*/

	/**
	 * 从json数组中得到相应java数组
	 * 
	 * @param jsonString
	 * @return
	 */
	/*public static Object[] getObjectArrayfromJson(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}*/

	/**
	 * 从一个JSON数组得到一个java对象集合
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	/*public static <T> List<T> getListfromJson(String jsonString, Class<T> clazz) {
		JSONArray array = JSONArray.fromObject(jsonString);
		List<T> list = new ArrayList<T>();
		for (Iterator<T> iter = array.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add((T) JSONObject.toBean(jsonObject, clazz));
		}
		return list;
	}*/
}
