package com.hotcode.test.system;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.JavaType;

@SuppressWarnings("deprecation")
public class JsonCenter {
	private static ObjectMapper json;
	
	static{
		json = new ObjectMapper();
		json.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		//忽略JSON字符串中存在而Java对象实际没有的属性
		json.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		json.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
	}
	
	public static ObjectMapper getJson(){
		return json;
	}

	/**
	 * 序列化为JSON字符串
	 */
	public static String ObjectToString(Object object) {
		try {
			return(json.writeValueAsString(object));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String ObjectToPrinter(Object object) {
		try {
			return(json.writerWithDefaultPrettyPrinter().writeValueAsString(object));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将JSON字符串转化为Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getMap(String jsonString) {
		return getObject(jsonString, Map.class);
	}
	
	public static<T> T getObject(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) return null;
		try {
			return json.readValue(jsonString, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			LogCenter.logger.error("Json解析失败");
			return null;
		}
	}
	
	/**
	 * 将JSON字符串转化为List
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {   
		return json.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
	}
	
	public static<T> List<T> getList(String jsonString, Class<T> clazz){
		try {
			JavaType javaType = getCollectionType(ArrayList.class, clazz); 
			return json.readValue(jsonString, javaType);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
