package pe.fu.im.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * Json处理实用类
 */
final public class JsonUtils {
	/**
	 * 对象转化为Json字符串
	 * 
	 * @param object
	 *            对象
	 * @return Json字符串
	 */
	public static String obj2Json(Object object) {
		return JSON.toJSONString(object);
	}

	/**
	 * Json字符串转化为Java对象
	 * 
	 * @param <T>
	 *            泛型
	 * @param clazz
	 *            Java类
	 * @param json
	 *            Json字符串
	 * @return 对象
	 */
	public static <T> T json2Obj(Class<T> clazz, String json) {
		if (json == null) {
			return null;
		}
		json = json.trim();
		int indexOf = json.indexOf("{");
		int indexOf2 = json.indexOf("[");
		if (indexOf > 0) {
			if (indexOf2 > 0) {
				if(indexOf>indexOf2){
					json = json.substring(indexOf2);
				}else{
					json = json.substring(indexOf);
				}
			} else if(indexOf2 < 0){
				json = json.substring(indexOf);
			}
		} else if (indexOf < 0) {
			if (indexOf2 > 0) {
				json = json.substring(indexOf2);
			} else if(indexOf2 < 0){
				return null;
			}
		}

		if (json.endsWith(";")) {
			json = json.substring(0, json.length() - 1);
		}

		return JSON.parseObject(json, clazz);
	}

	/**
	 * Json字符串流转换为Java对象
	 * 
	 * @param <T>
	 *            泛型
	 * @param clazz
	 *            Java类
	 * @param is
	 *            字符串流
	 * @return 对象
	 * @throws IOException
	 */
	public static <T> T jsonInputStream2Obj(Class<T> clazz, InputStream is) throws IOException {
		String json = StringUtils.inputStreamToString(is, null);
		return json2Obj(clazz, json);
	}
	
	/**
	 * Json字符串转化为Java对象
	 * 
	 * @param <T>
	 *            泛型
	 * @param clazz
	 *            Java类
	 * @param json
	 *            Json字符串
	 * @return 对象
	 */
	public static <T> List<T> json2Array(Class<T> clazz, String json) {
		if (json == null) {
			return null;
		}
		json = json.trim();
		int indexOf = json.indexOf("{");
		int indexOf2 = json.indexOf("[");
		if (indexOf > 0) {
			if (indexOf2 > 0) {
				if(indexOf>indexOf2){
					json = json.substring(indexOf2);
				}else{
					json = json.substring(indexOf);
				}
			} else if(indexOf2 < 0){
				json = json.substring(indexOf);
			}
		} else if (indexOf < 0) {
			if (indexOf2 > 0) {
				json = json.substring(indexOf2);
			} else if(indexOf2 < 0){
				return null;
			}
		}

		if (json.endsWith(";")) {
			json = json.substring(0, json.length() - 1);
		}
		
		return JSON.parseArray(json, clazz);
	}
	
	
}
