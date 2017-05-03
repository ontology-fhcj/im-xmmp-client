package pe.fu.im.client.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtils {

	// 时间戳格式("yyyy-MM-dd HH:mm:ss.SSS")
	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	// 日期时间24小时制格式("yyyy-MM-dd HH:mm:ss")
	public static final String DATE_TIME24_FORMAT = "yyyy-MM-dd HH:mm:ss";
	// 日期格式("yyyy-MM-dd")
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	// 时间24小时制格式("HH:mm:ss")
	public static final String TIME24_FORMAT = "HH:mm:ss";

	/**
	 * 日期字符串（yyyy-MM-dd 格式）转化为日期
	 * 
	 * @param dateTime
	 *            yyyy-MM-dd格式时间串
	 * @return 时间对象
	 * @throws ParseException
	 */
	public static Date converToDate(String dateTime) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		return df.parse(dateTime);
	}

	/**
	 * 日期字符串转化为日期
	 * 
	 * @param dateTime
	 *            时间串
	 * @param format
	 *            格式
	 * @return 时间对象
	 * @throws ParseException
	 */
	public static Date converToDate(String dateTime, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(dateTime);
	}

	/**
	 * 将日期格式化为字符串（yyyy-MM-dd格式）
	 * 
	 * @param date
	 * @return 格式化结果
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DATE_FORMAT);
	}

	/**
	 * 将日期格式化为字符串，自定义格式
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String formatDate(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	public static String formatDate(Long date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	/**
	 * 将当前日期格式化为字符串（yyyy-MM-dd格式）
	 * 
	 * @return 格式化结果
	 */
	public static String formatCurrentDate() {
		return formatCurrentDate(DATE_FORMAT);
	}

	/**
	 * 使用自定义格式格式化当前日期
	 * 
	 * @param dateFormat
	 *            输出显示的时间格式
	 * @return 格式化结果
	 */
	public static String formatCurrentDate(String dateFormat) {
		return formatDate(new Date(), dateFormat);
	}
	
	
	/**
	 * 截取某一时间戳 为yyyy-mm-dd
	 * @param dateTime
	 * @return
	 * @throws ParseException
	 */
	public static Date cutTimestampToDate(String dateTime) throws ParseException{
		Date date = converToDate(dateTime, DATE_TIME24_FORMAT);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 0);
		return calendar.getTime();
		
	}
	
	/**
	 * 获取当前日期前一天
	 * 
	 * @return
	 */
	public static String lastDay() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return formatDate(date);
	}

	/**
	 * 获取当前日期前n天的日期
	 * 
	 * @param n
	 * @return
	 */
	public static String prevDay(int n) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -n);
		date = calendar.getTime();
		return formatDate(date);
	}

	/**
	 * 获取当前日期前一周
	 * 
	 * @return
	 */
	public static String lastWeek() {
		return prevDay(7);
	}

	/**
	 * 获取当前时间allMonth月的日期
	 * 
	 * @param n
	 * @return
	 */
	public static String prevMonth(int n) {
		Date date = new Date();
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
		int month = Integer.parseInt(new SimpleDateFormat("MM").format(date)) - n;
		int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
		if (month <= 0) {
			int yearFlag = (month * (-1)) / 12 + 1;
			int monthFlag = (month * (-1)) % 12;
			year -= yearFlag;
			month = monthFlag * (-1) + 12;
		} else if (day > 28) {
			if (month == 2) {
				if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
					day = 29;
				} else
					day = 28;
			} else if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
				day = 30;
			}
		}
		String y = year + "";
		String m = "";
		String d = "";
		if (month < 10)
			m = "0" + month;
		else
			m = month + "";
		if (day < 10)
			d = "0" + day;
		else
			d = day + "";

		return y + "-" + m + "-" + d;
	}

	/**
	 * 获取当前年
	 */
	public static String getCurrentYear() {
		Date date = new Date();
		return new SimpleDateFormat("yyyy").format(date);
	}

	/**
	 * 是否在今天
	 * 
	 * @param date
	 * @return
	 */
	public static Boolean isToday(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 是否在今天
	 * 
	 * @param dateTime
	 * @return
	 * @throws ParseException 
	 */
	public static Boolean isToday(String dateTime) throws ParseException {
		Date date = converToDate(dateTime);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {
			return true;
		} else {
			return false;
		}
	}

}
