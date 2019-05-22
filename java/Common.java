package com.hotcode.test.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javazoom.upload.MultipartFormDataRequest;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.util.Comparators;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

public class Common {
	/**
	 * 判断一个String是不是为空
	 */
	public static boolean isEmptyString(String s){
		return StringUtils.isEmpty(s);
	}
	
	/**
	 * 获取String类型的参数值
	 */
	public static String formatString(String s) {
		return s==null?"":s;
	}

	public static String getValue(String strName, HttpServletRequest request) {
		return formatString(request.getParameter(strName));
	}

	public static String getValue(String strName, MultipartFormDataRequest request) {
		return formatString(request.getParameter(strName));
	}

	/**
	 * 获取Integer类型的参数值
	 */
	public static Integer formatInteger(String s) {
		try {
			Integer i = Integer.parseInt(s);
			return i;
		} catch (Exception ex) {
			return 0;
		}
	}

	public static Integer getInteger(String strName, HttpServletRequest request) {
		return formatInteger(request.getParameter(strName));
	}

	public static Integer getInteger(String strName, MultipartFormDataRequest mrequest) {
		return formatInteger(mrequest.getParameter(strName));
	}
	
	/**
	 * 获取Long类型的参数值
	 */
	public static Long formatLong(String s) {
		try {
			Long i = Long.parseLong(s);
			return i;
		} catch (Exception ex) {
			return 0L;
		}
	}

	public static Long getLong(String strName, HttpServletRequest request) {
		return formatLong(request.getParameter(strName));
	}

	public static Long getLong(String strName, MultipartFormDataRequest mrequest) {
		return formatLong(mrequest.getParameter(strName));
	}
	
	/**
	 * 获取Double类型
	 */
	public static Double formatDouble(String s){
		try {
			Double i = Double.parseDouble(s);
			return i;
		} catch (Exception ex) {
			return 0.0;
		}
	}
	
	public static Double getDouble(String strName, HttpServletRequest request) {
		return formatDouble(request.getParameter(strName));
	}
	
	public static Double getDouble(String strName, MultipartFormDataRequest mrequest) {
		return formatDouble(mrequest.getParameter(strName));
	}
	
	/**
	 * 获取Float类型
	 */
	public static Float formatFloat(String s){
		try {
			Float i = Float.parseFloat(s);
			return i;
		} catch (Exception ex) {
			return 0f;
		}
	}

	/**
	 * 获取页码
	 */
	public static int getPage(HttpServletRequest request) {
		Integer i = getInteger("intPage", request);
		if (i > 1) {
			return i.intValue();
		} else {
			return 1;
		}
	}

	/**
	 * 以指定格式转换小数 ",###.00"
	 */
	public static String formatNumber(double d, String format){
		DecimalFormat f = new DecimalFormat(format);
		return f.format(d);
	}
	
	/**
	 * 格式转换:Double转String，指定小数点后位数
	 */
	public static String formatNumber(double d, int min, int max) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(max);
		format.setMinimumFractionDigits(min);
		return format.format(d);
	}

	/**
	 * 格式转换:Double转String，指定小数点后位数
	 */
	public static String formatNumber(double d, int n) {
		return formatNumber(d, n, n);
	}

	/**
	 * 传递UTF8编码信息
	 */
	public static String encode(String s) throws Exception {
		return URLEncoder.encode(s, "utf-8");
	}

	public static String decode(String s) {
		try {
			s = new String(s.getBytes("ISO-8859-1"), "utf-8");
		} catch (Exception ex) {
			s = "";
		}
		return s;
	}

	public static String formatUTF8(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= '\377') {
				sb.append(c);
			} else {
				byte b[];
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	public static String getDecodeValue(String strName, HttpServletRequest request) {
		return decode(getValue(strName, request));
	}

	public static String getDecodeValue(String strName,	MultipartFormDataRequest request) {
		return decode(getValue(strName, request));
	}

	/**
	 * 获取多选框的值
	 */
	public static String getValues(String strName, HttpServletRequest request) {
		String strValue[] = request.getParameterValues(strName);
		
		String s = "";
		if (strValue == null) {
			return "";
		} else {
			for (int i = 0; i < strValue.length; i++) {
				if (!strValue[i].trim().equals("")) {
					if (s.equals("")) {
						s = strValue[i].trim();
					} else {
						s = s + "," + strValue[i].trim();
					}
				}
			}
			return s;
		}
	}

	/**
	 * 连接字符串,s为总字符,s1为要连接字符,s2连接串
	 */
	public static String append(String s, String s1, String s2) {
		if (s.equals("")) {
			s = s1;
		} else {
			s = s + s2 + s1;
		}
		return s;
	}

	public static String append(String s, String s1) {
		return append(s, s1, ",");
	}
	
	/**
	 * 将n1转换成n2长度的字符串
	 */
	public static String getStr(int n1, int n2) {
		String str = n1 + "";
		if (str.length() < n2) {
			int nlen = n2 - str.length();
			for (int i = 0; i < nlen; i++) {
				str = "0" + str;
			}
		}
		return str;
	}

	/**
	 * 查看s1是否包含s2,s1为s3隔开的字符串
	 */
	public static boolean find(String s1, String s2, String s3) {
		if (s1 == null) {
			return false;
		}
		if (s2 == null) {
			s2 = "";
		}

		String[] s = s1.split(s3);
		boolean b = false;
		for (int i = 0; i < s.length; i++) {
			if (s[i].equals(s2)) {
				b = true;
				break;
			}
		}
		return b;
	}

	public static boolean find(String s1, String s2) {
		return find(s1, s2, ",");
	}

	/**
	 * 字符截取，双字节字符标示为2
	 */
	public static String getTitle(String strTitle, int n) {
		Pattern p = Pattern.compile("[^\\x00-\\xff]"); // 双字节字符正则
		int nlen = 0;
		String tmpStr = "";

		for (int i = 0; i < strTitle.length(); i++) {
			String str = strTitle.substring(i, i + 1);
			Matcher m = p.matcher(str);
			if (m.matches()) {
				nlen += 2;
			} else {
				nlen += 1;
			}

			tmpStr += str;

			if (nlen >= n) {
				break;
			}
		}

		if (!tmpStr.equals(strTitle))
			return tmpStr + "...";
		else
			return tmpStr;
	}
	
	/**
	 * 获取长度为size的随机字符串
	 */
	public static String getRandomString(int size) {
		char[] c = { 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D',
				'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M' };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}
	
	public static String getRandomInt(int size) {
		char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}
	
	/**
	 * 生成min到max的随机数，包含min和max
	 */
	public static int getRandom(int min, int max){
		if(max <= min)
			return min;
		Random r = new Random();
		return min + r.nextInt(max-min+1);
	}
	
	/**
	 * 获取HTML编码
	 */
	public static String getHtml(String s) {
		s = s.replace("  ", "　");
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		s = s.replace("\r\n", "<br>");
		return s;
	}

	/**
	 * 时间函数
	 */
	public static Date formatDate(String dateString, String dateFormat){
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		try {
			return df.parse(dateString);
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static String formatDate(Date date, String dateFormat){
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}
	
	public static String FormatFullDate(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date FormatFullDate(String dateString) {
		return formatDate(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	public static String FormatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}
	
	public static Date FormatDate(String dateString) {
		return formatDate(dateString, "yyyy-MM-dd");
	}
	
	public static String getNow() {
		return FormatFullDate(new Date());
	}
	
	public static String getDate() {
		return FormatDate(new Date());
	}
	
	/**
	 * 随机生成文件名
	 */
	public static String getFileName() {
		Calendar c = Calendar.getInstance();
		String str = getStr(c.get(Calendar.YEAR), 4);
		str = str + getStr(c.get(Calendar.MONTH) + 1, 2);
		str = str + getStr(c.get(Calendar.DATE), 2);
		str = str + getStr(c.get(Calendar.HOUR_OF_DAY), 2);
		str = str + getStr(c.get(Calendar.MINUTE), 2);
		str = str + getStr(c.get(Calendar.SECOND), 2);
		str = str + getStr(c.get(Calendar.MILLISECOND), 3);
		str = str + getRandomInt(3);
		return str;
	}

	/**
	 * 获取session中传递的message信息
	 */
	public static String getMsg(HttpServletRequest request) {
		return getMsg(request, "");
	}

	public static String getMsg(HttpServletRequest request, String defaultValue) {
		String s = "";
		if (request.getSession().getAttribute("msg") != null) {
			s = request.getSession().getAttribute("msg").toString();
			request.getSession().removeAttribute("msg");
		}
		if (s.equals("")) {
			s = defaultValue;
		}
		return s;
	}
	
	public static String getCookieMsg(HttpServletRequest request, HttpServletResponse response, String defaultValue) throws Exception{
		String s = getCookie(request, "msg");
		if (s == null || s.equals("")) {
			s = defaultValue;
		}
		setCookie(response, "msg", null, 0);
		return s;
	}
	
	/**
	 * 设置cookie
	 */
	public static void setCookie(HttpServletResponse response, String name,	String value, Integer t) throws UnsupportedEncodingException {
		if(value != null)
			value = URLEncoder.encode(value, "utf-8");
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(t);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	/**
	 * 获取cookie
	 */
	public static String getCookie(HttpServletRequest request, String name) throws UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();
		String value = null;

		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name))
					value = URLDecoder.decode(cookies[i].getValue(),"utf-8");
			}
		}

		return value;
	}
	
	/**
	 * 正则表达式获取某一段内容
	 */
	public static String getMatherGroupDoTall(String s, int i, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			if (matcher.groupCount() >= i) {
				return matcher.group(i);
			}
		}
		return null;
	}

	/**
	 * 判断是否为邮件地址
	 */
	public static boolean isEmail(String str) {
		Pattern p = Pattern.compile("^[\\w\\-\\.]+@[\\w\\-\\.]+(\\.\\w+)+$"); // 正则表达式
		Matcher m = p.matcher(str); // 操作的字符串
		Boolean b = m.matches();
		return b;
	}

	/**
	 * 获取IP
	 */
	public static String getIp(HttpServletRequest request){
		String ip = request.getHeader("X-Real-IP");
		if(ip == null || ip.equals("") || ip.equals("127.0.0.1")){
			ip = request.getRemoteAddr();
		}
		if(ip == null || ip.equals(""))
			ip = "127.0.0.1";
		
		return ip;
	}

	/**
	 * 获取当前访问的绝对路径
	 */
	@SuppressWarnings("deprecation")
	public static String getRealPath(HttpServletRequest request) {
		String url = request.getRealPath("").replace("\\", "/");
		String uri = request.getServletPath();
		uri = uri.substring(0, uri.lastIndexOf("/"));

		return url + uri;
	}

	/**
	 * 获取根路径
	 */
	@SuppressWarnings("deprecation")
	public static String getRootPath(HttpServletRequest request) {
		return request.getRealPath("").replace("\\", "/");
	}
	
	/**
	 * 获取主机名
	 */
	public static String getServerName(HttpServletRequest request) {
		String serverName = request.getServerName().toLowerCase();
		return serverName;
	}

	/**
	 * 获取Domain信息
	 */
	public static String getUrlDomain(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder(request.getScheme());
		sb.append("://").append(getServerName(request));
		if (request.getServerPort() != 80) {
			sb.append(':').append(request.getServerPort());
		}
		return sb.toString();
	}
	
	/**
	 * 获取URL的相对路径
	 */
	public static String getFullPath(HttpServletRequest request){
		StringBuilder sb = new StringBuilder(request.getRequestURI());
		if(request.getQueryString() != null)
			sb.append("?").append(request.getQueryString());
		
		return sb.toString();
	}
	
	/**
	 * 获取URL的完整路径
	 */
	public static String getAbsoluteUrl(HttpServletRequest request) {
		return getUrlDomain(request) + getFullPath(request);
	}
	
	public static String getEncodedAbsoluteUrl(HttpServletRequest request) {
		try {
			return encode(getAbsoluteUrl(request));
		} catch (Exception e) {
			return getAbsoluteUrl(request);
		}
	}
	
	/**
	 * Map依据value进行排序
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map sortByValue(Map map, final boolean reverse){  
        List list = new LinkedList(map.entrySet());  
        Collections.sort(list, new Comparator(){  
            public int compare(Object o1, Object o2) {  
                if(reverse){  
                    return -((Comparable)((Map.Entry)o1).getValue()).compareTo(((Map.Entry)o2).getValue());  
                }  
                return ((Comparable)((Map.Entry)o1).getValue()).compareTo(((Map.Entry)o2).getValue());  
            }  
        });
  
        Map result = new LinkedHashMap();  
        for (Iterator it = list.iterator(); it.hasNext();) {  
        	Map.Entry entry = (Map.Entry) it.next();  
        	result.put(entry.getKey(), entry.getValue());  
        }  
        return result;
	}
	public static <T> List<T> sortList(List<T> list, String sortstring, final boolean reverse ){
		if(list.size()>0){
			@SuppressWarnings("rawtypes")
			Class classtype = list.get(0).getClass();
			PropertyDescriptor pd = null;
			try {
				pd = new PropertyDescriptor(sortstring, classtype);
			} catch (IntrospectionException e1) {
				e1.printStackTrace();
			}
			final Method readMehod = pd.getReadMethod();//获得读方法
			Collections.sort(list,new Comparator<T>() {
				@Override
				public int compare(T o1, T o2) {
					int b = 0;
					try {
						b = Double.valueOf(readMehod.invoke(o1).toString()).compareTo(Double.valueOf(readMehod.invoke(o2).toString()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					if(reverse){
						return -b;
					}else{
						return b;
					}
				}
			});
		}
		return list;
	}
	/**
	 * 生成Select语句
	 */
	public static String buildSelect(String key, Integer value, Map<Integer, String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='" + key + "'>");
		for(Integer option : map.keySet()) {
			if(option.equals(value))
				sb.append("<option value='"+option+"' selected>").append(map.get(option)).append("</option>");
			else
				sb.append("<option value='"+option+"'>").append(map.get(option)).append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	/**
	 * 生成Select语句,value为string类型的select
	 */
	public static String buildSelect(String key, String value, Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='" + key + "'>");
		for(String option : map.keySet()) {
			if(option.equals(value))
				sb.append("<option value='"+option+"' selected>").append(map.get(option)).append("</option>");
			else
				sb.append("<option value='"+option+"'>").append(map.get(option)).append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}  
	/**
	 * 根据日期获得所在周的日期 
	 * 
	 */
    public static List<Date> dateToWeek(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
        int b = calendar.get(Calendar.DAY_OF_WEEK);
        Date returnDate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = date.getTime() - (b-1) * 24 * 3600000;
        for (int i = 1; i <= 7; i++) {
        	returnDate = new Date();
        	returnDate.setTime(fTime + (i * 24 * 3600000));
            list.add(i-1, returnDate);
        }
        return list;
    }
    
    /*
     * 计算年龄
     */
    public static int getAge(String birth) throws Exception {
    	Date birthDay = formatDate(birth, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();  
  
        if (cal.before(birthDay)) {  
            throw new IllegalArgumentException(  
                    "The birthDay is before Now.It's unbelievable!");  
        }  
        int yearNow = cal.get(Calendar.YEAR);  
        int monthNow = cal.get(Calendar.MONTH);  
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
        cal.setTime(birthDay);  
  
        int yearBirth = cal.get(Calendar.YEAR);  
        int monthBirth = cal.get(Calendar.MONTH);  
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  
  
        int age = yearNow - yearBirth;  
  
        if (monthNow <= monthBirth) {  
            if (monthNow == monthBirth) {  
                if (dayOfMonthNow < dayOfMonthBirth) age--;  
            }else{  
                age--;  
            }  
        }  
        return age;  
    }  
    /*
     * 以1开头11位数字验证手机号
     */
    
    public static boolean isPhonenumber(String phone){
    	if(StringUtils.isNotEmpty(phone)){
    		Pattern pattern = Pattern.compile("^1[0-9]{10}$");
    		Matcher matcher = pattern.matcher(phone);
    		return matcher.matches();
    	}else{
    		return false;
    	}
    }
    
    /*
     * 获得当前日期的前一天
     */
    public static String getPreDay(){
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return formatDate(calendar.getTime(), "yyyy-MM-dd");
    }
    
    /*
     * 获得当月的第一天
     */
    public static String getFirstDay(){
    	//获取当前月第一天：
        Calendar calendar = Calendar.getInstance();    
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return formatDate(calendar.getTime(),"yyyy-MM-dd");
    }
    
    /*
     * 获得当月的最后一天
     */
    public static String getLastDay(){
    	Calendar calendar = Calendar.getInstance();    
    	calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(calendar.getTime(),"yyyy-MM-dd");
    }
    
    public static String getPercent(Double num1,Double num2 ){
    	if(num2==0){
    		return "0%";
    	}else if(num1/num2>=1){
    		return "100%";
    	}else if(num1/num2 ==0 ){
    		return "0";
    	}else{
    		DecimalFormat df = new DecimalFormat("##%");
    		double f = num1/num2;
    		return df.format(f);
    	}
    }
    
    public static Integer getMP3PlayTime(String filePath){
		try {
			MP3File f = (MP3File)AudioFileIO.read(new File(filePath));
			MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
			if(audioHeader.getTrackLength()<1){
				return 1;
			}
			return audioHeader.getTrackLength();
		} catch (Exception e) {
			return 1;
		} 
    }
    
   /*
    * 获取时间间隔（天数）
    */
    public static long getdays(Date day1, Date day2){
    	long date1 = Common.formatDate(Common.FormatDate(day1), "yyyy-MM-dd").getTime();
    	long date2 = Common.formatDate(Common.FormatDate(day2), "yyyy-MM-dd").getTime();
    	long day = (date1-date2)/1000/3600/24;
    	if(day<0){
    		day = (-day);
    	}
    	return day;
    }
    /* 获取随机工具
     * 
     */
    public static ThreadLocalRandom getRandom() {  
        return ThreadLocalRandom.current();
    }  
    
    /*
     * 获得一个[0,max)之间的随机整数。 
     */  
    public static int getRandomint(int max) {  
        return getRandom().nextInt(max);  
    }  
      
    /*
     * 获得一个[min, max]之间的随机整数 
     */  
    public static int getRandomint(int min, int max) {  
        return getRandom().nextInt(max-min+1) + min;  
    }  
  
    /*
     * 获得一个[0,max)之间的长整数。  
     */  
    public static long getRandomLong(long max) {  
        return getRandom().nextLong(max);  
    }  
      
    /*
     * 从数组中随机获取一个元素 
     */  
    public static <E> E getRandomElement(E[] array){  
        return array[getRandomint(array.length)];         
    }  
      
    /*
     * 从list中随机取得一个元素 
     */  
    public static <E> E getRandomElement(List<E> list){  
        return list.get(getRandomint(list.size()));  
    }  
      
    /*
     * 从set中随机取得一个元素 
     */  
    public static <E> E getRandomElement(Set<E> set){  
        int rn = getRandomint(set.size());  
        int i = 0;  
        for (E e : set) {  
            if(i==rn){  
                return e;  
            }  
            i++;  
        }  
        return null;  
    }  
      
    /*
     * 从map中随机取得一个key 
     */  
    public static <K, V> K getRandomKeyFromMap(Map<K, V> map) {  
        int rn = getRandomint(map.size());  
        int i = 0;  
        for (K key : map.keySet()) {  
            if(i==rn){  
                return key;  
            }  
            i++;  
        }  
        return null;  
    }  
      
    /*
     * 从map中随机取得一个value  
     */  
    public static <K, V> V getRandomValueFromMap(Map<K, V> map) {  
        int rn = getRandomint(map.size());  
        int i = 0;  
        for (V value : map.values()) {  
            if(i==rn){  
                return value;  
            }  
            i++;  
        }  
        return null;  
    }  
      
}