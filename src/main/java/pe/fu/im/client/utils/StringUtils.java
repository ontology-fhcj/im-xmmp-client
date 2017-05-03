package pe.fu.im.client.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.serial.SerialException;

public class StringUtils {

	public final static String REGEX = "\\$\\{(.+?)\\}";

	/**
	 * 字符串替换
	 * 
	 * @param regex
	 * @param str
	 * @param replaceStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String stringReplace(String regex, String str, String replaceStr) {
		Pattern pat = Pattern.compile(regex);
		Matcher matcher = pat.matcher(str);
		if (matcher.find()) {
			str = matcher.replaceAll(replaceStr.replaceAll("\\\\", "/"));
		}
		return str;
	}

	public static boolean hasLength(CharSequence str) {
		return str != null && str.length() > 0;
	}

	public static boolean hasLength(String str) {
		return hasLength(((CharSequence) (str)));
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str))
			return false;
		int strLen = str.length();
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return true;

		return false;
	}

	public static boolean hasText(String str) {
		return hasText(((CharSequence) (str)));
	}

	/**
	 * Object转化为字符串
	 * 
	 * @param o
	 * @return 字符串
	 */
	public static String obj2String(Object o) {
		if (o == null) {
			return "";
		}
		return String.valueOf(o).trim();
	}

	/**
	 * 转化字符串，null以另一个字符串代替
	 * 
	 * @param value
	 *            原始字符串
	 * @param nullValue
	 *            为空时字符串
	 * @return 检查后的结果
	 */
	public static String obj2String(Object value, String nullValue) {
		return value == null ? nullValue : obj2String(value);
	}

	/**
	 * 裁剪超长字符串
	 * 
	 * @param s
	 *            待裁剪字符串
	 * @param maxLen
	 *            最大长度
	 * @return 裁剪后的字符串
	 */
	public static String limitStringLength(String s, int maxLen) {
		return s.length() > maxLen ? s.substring(0, maxLen - 1) : s;
	}

	/**
	 * Clob转换为字符串
	 * 
	 * @param clob
	 *            clob对象
	 * @return 字符串
	 * @throws IOException
	 * @throws SQLException
	 * @throws Exception
	 */
	public static String clob2String(Clob clob) throws IOException, SQLException {
		Reader inStreamDoc = null;
		try {
			inStreamDoc = clob.getCharacterStream();
			char[] tempDoc = new char[(int) clob.length()];
			inStreamDoc.read(tempDoc);
			return new String(tempDoc);
		} catch (IOException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (inStreamDoc != null) {
				inStreamDoc.close();
			}
		}
	}

	/**
	 * 
	 * 将string对象转换为Clob对象,Blob处理方式与此相同
	 * 
	 * @param str
	 * @return
	 * @throws SQLException
	 * @throws SerialException
	 */
	public static Clob string2Clob(String str) throws SerialException, SQLException {
		if (null == str)
			return null;
		return new javax.sql.rowset.serial.SerialClob(str.toCharArray());
	}

	/**
	 * 使用分隔符连接迭代器数据
	 * <p>
	 * 如： List<String> v = new ArrayList<String>();
	 * <p>
	 * v.add("A");
	 * <p>
	 * v.add("B");
	 * <p>
	 * v.add("C");
	 * <p>
	 * 则:StringUtils.join(v, ",") = "A,B,C"
	 * 
	 * @param c
	 *            迭代器
	 * @param delimiter
	 *            分隔符
	 * @return 分隔符连接的字符串
	 */
	public static String join(Iterable<? extends Object> c, String delimiter) {
		Iterator<? extends Object> itr;

		if (c == null || !(itr = c.iterator()).hasNext()) {
			return "";
		}

		StringBuilder sb = new StringBuilder(obj2String(itr.next()));
		while (itr.hasNext()) {
			sb.append(delimiter).append(obj2String(itr.next()));
		}

		return sb.toString();
	}

	/**
	 * 数组转化为连接器字符串
	 * <p>
	 * 如:StringUtils.join(new String[]{"A","B","C"}, ",") = "A,B,C"
	 * 
	 * @param <T>
	 * 
	 * @param arrO
	 *            数组
	 * @param delimiter
	 *            分割符
	 * @return 增加了分隔符的字符串
	 */
	public static <T> String join(T[] arrO, String delimiter) {
		int arrSize;

		if ((arrSize = arrO.length) == 0) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder(String.valueOf(arrO[0]));
			for (int i = 1; i < arrSize; i++) {
				sb.append(delimiter).append(arrO[i]);
			}

			return sb.toString();
		}
	}

	/**
	 * 字符串转换为数组
	 * 
	 * @param source
	 *            源字符串
	 * @param delimiter
	 *            分隔符
	 * @return 字符串数组,如果源字符串为空或null，则返回String[0]
	 */
	public static String[] toArray(String source, String delimiter) {
		if (isEmpty(source)) {
			return new String[0];
		} else {
			return source.split(delimiter);
		}
	}

	/**
	 * 
	 * @param source
	 * @param delimiter
	 * @return
	 */
	public static List<String> toList(String source, String delimiter) {
		List<String> list = new ArrayList<String>();
		for (String _s_ : toArray(source, delimiter)) {
			list.add(_s_);
		}
		return list;
	}

	/**
	 * 判断是否为空或空白
	 * 
	 * @param value
	 *            值
	 * @return 结果
	 */
	public static boolean isEmpty(Object value) {
		if (value == null || "".equals(obj2String(value))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 建立字符串数组
	 * 
	 * @param str
	 *            字符串
	 * @param count
	 *            数组元素个数
	 * @return 数组，包含count个str
	 */
	public static String[] getStringArray(String str, int count) {
		String[] result = new String[count];

		for (int i = 0; i < count; i++) {
			result[i] = str;
		}

		return result;
	}

	/**
	 * 建立字符数组
	 * 
	 * @param ch
	 *            字符
	 * @param count
	 *            数组元素个数
	 * @return 数组，包含count个ch
	 */
	public static char[] getCharArray(char ch, int count) {
		char[] result = new char[count];

		for (int i = 0; i < count; i++) {
			result[i] = ch;
		}

		return result;
	}

	/**
	 * Iso字符串转化为Utf8字符串
	 * 
	 * @param str
	 *            字符串
	 * @return 转码后的字符串
	 */
	public static String isoToUtf8(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * 以指定字符填补字符串的左边
	 * 
	 * @param source
	 *            源字符串
	 * @param padChar
	 *            指定字符
	 * @param length
	 *            目标长度
	 * @return 填充后的字符串
	 */
	public static String padLeft(String source, char padChar, int length) {
		if (source.length() > length) {
			return source;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.getCharArray(padChar, length - source.length()));
		sb.append(source);

		return sb.toString();
	}

	/**
	 * 以指定字符填补字符串的右边
	 * 
	 * @param source
	 *            源字符串
	 * @param padChar
	 *            指定字符
	 * @param length
	 *            目标长度
	 * @return 填充后的字符串
	 */
	public static String padRight(String source, char padChar, int length) {
		if (source.length() > length) {
			return source;
		}

		StringBuilder sb = new StringBuilder(source);
		sb.append(StringUtils.getCharArray(padChar, length - source.length()));

		return sb.toString();
	}

	/**
	 * 修剪字符串
	 * 
	 * @param source
	 *            原始字符串
	 * @return 修剪后的字符串
	 */
	public static String trim(String source) {
		return source == null ? "" : source.trim();
	}

	/**
	 * 去掉字符串中所有空格
	 * 
	 * @param source
	 * @return
	 */
	public static String trimAllSpace(String source) {
		return source == null ? "" : source.replaceAll(" ", "");
	}

	/**
	 * 修剪左边字符
	 * 
	 * @param source
	 *            源字符串
	 * @param trimChar
	 *            修剪字符
	 * @return 修剪后的字符串
	 */
	public static String trimLeft(String source, char trimChar) {
		for (int i = 0, length = source.length(); i < length; i++) {
			if (source.charAt(i) != trimChar) {
				return source.substring(i);
			}
		}

		return "";
	}

	/**
	 * 修剪右边字符
	 * 
	 * @param source
	 *            源字符串
	 * @param trimChar
	 *            修剪字符
	 * @return 修剪后的字符串
	 */
	public static String trimRight(String source, char trimChar) {
		for (int size = source.length(), i = size - 1; i >= 0; i--) {
			if (source.charAt(i) != trimChar) {
				return source.substring(0, i + 1);
			}
		}

		return "";
	}

	/**
	 * 字符串驼峰表示法
	 * 
	 * @param source
	 *            源字符串
	 * @return 驼峰表示字符串
	 */
	public static String camelize(String source) {
		String str = source;
		if (str.startsWith("_")) {
			str = StringUtils.firstUpper(str);
		}

		String[] arrS = source.split("_");
		int len = arrS.length;

		if (len == 1)
			return arrS[0];

		String camelized = arrS[0];

		for (int i = 1; i < len; i++) {
			camelized += String.valueOf(arrS[i].charAt(0)).toUpperCase() + arrS[i].substring(1);
		}

		return camelized;
	}

	/**
	 * 使字符串首字母大写
	 * 
	 * @param source
	 *            源字符串
	 * @return 字符串
	 */
	public static String firstUpper(String source) {
		return String.valueOf(source.charAt(0)).toUpperCase() + source.substring(1);
	}

	/**
	 * 首字母小写
	 * 
	 * @param source
	 *            源字符串
	 * @return 字符串
	 */
	public static String firstLower(String source) {
		return String.valueOf(source.charAt(0)).toLowerCase() + source.substring(1);
	}

	/**
	 * 清除所有空格
	 * 
	 * @param source
	 *            源字符串
	 * @return 清除空格后的字符串
	 */
	public static String eraseAllSpace(String source) {
		if (isEmpty(source)) {
			return "";
		}
		return source.replaceAll("\\s*", "");
	}

	/**
	 * 获取Reader内容
	 * 
	 * @param reader
	 *            reader对象
	 * @return Reader内容字符串
	 * @throws IOException
	 */
	public static String readerToString(Reader reader) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader br = null;
		char[] buf = new char[1024];
		int r = -1;
		try {
			br = new BufferedReader(reader);
			while ((r = br.read(buf)) != -1) {
				result.append(buf, 0, r);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (br != null)
				br.close();
		}
		return result.toString();
	}

	/**
	 * InputStream 转换为字符串，默认以UTF-8编码
	 * 
	 * @param is
	 *            InputStream输入流
	 * @return 字符串
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream is, String charsetName) throws IOException {
		if (StringUtils.isEmpty(charsetName)) {
			charsetName = "UTF-8";
		}
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream(1024);
			byte[] data = new byte[1024];
			int n = -1;
			while ((n = is.read(data)) != -1) {
				os.write(data, 0, n);
			}
			os.flush();
			return new String(os.toByteArray(), charsetName);
		} catch (IOException e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null)
				os.close();
		}
	}

	public static String bytes2HexString(byte[] b) {
		String ret = "";
		AssertUtils.notNull(b);
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex;
		}
		return ret;
	}
}
