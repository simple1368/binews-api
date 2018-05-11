package ksd.binews.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 公共工具类
 * 
 * @author Administrator
 *
 */
public class PublicUtil {
	private static final String MD5_KEY = "cvz12bbn231hj13dfcg20170306";

	/**
	 * 生成32位UUid
	 * 
	 * @return
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();

		return uuid.toString().replace("-", "").substring(0, 32);
	}
	
	public static int getRandom() {
		return (int)((Math.random() * 9 + 1) * 10000);
	}

	/**
	 * 获取当前时间戳
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 将集合拼接成字符串 ,隔开
	 */
	public static String listSplltString(List<Map<String, Object>> list, String key) {
		String key_value = "";
		if (list == null)
			return key_value;
		String key_id = "";
		for (Map<String, Object> map : list) {
			key_id = PublicUtil.mapObjectToString(map, key);
			if (key_id != "") {
				if (key_value != "") {
					key_value += ",";
				}
				key_value += key_id;
			}
		}
		return key_value;
	}

	/**
	 * 将map<string,object> 转成 string 类型
	 */
	public static String mapObjectToString(Map<String, Object> map, String key) {
		if (map == null) {
			return "";
		}

		String value = "";
		if (map.containsKey(key) && map.get(key) != null) {
			value = map.get(key).toString();
		}
		return value;
	}

	/**
	 * 根据map的value获取map的key
	 * 
	 * @param map
	 * @param value
	 * @return
	 */
	public static String getKey(Map<String, String> map, String value) {
		String key = "";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				key = entry.getKey();
			}
		}
		return key;
	}

	/**
	 * obj转map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> ConvertObjToMap(Object obj) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		if (obj == null)
			return null;
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				try {
					Field f = obj.getClass().getDeclaredField(fields[i].getName());
					f.setAccessible(true);
					Object o = f.get(obj);
					if ("serialVersionUID".equals(f.getName()) || o == null) {
						continue;
					}
					reMap.put(fields[i].getName(), o);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return reMap;
	}


	/**
	 * 密码加密
	 * 
	 * @param password
	 * @return
	 */
	public static String password(String password) {
		return MD5SecurityUtil.MD5Encode(password, MD5_KEY);
	}

}
