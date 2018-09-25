package com.chat.springboot.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 常量值判断工具
 * @author yangyiwei
 * @date 2018年7月5日
 * @time 上午10:06:28
 */
public class StringUtils {

	private final static String tag = "&lt;img src=&quot;";
	public static String str;
	public static final String EMPTY_STRING = "";
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
			"E", "F" };
	public static final String DATA_TYPE_URL = "?&";// URL格式
	public static final String DATA_TYPE_COMMON = ",.;!@#$%^&*()_-=+";// 普通格式

	public static void main(String[] args) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 截取倒数第几个后面的字符串
	 *
	 * @param str
	 * @param num
	 * @return
	 */
	public static String getSubStr(String str, int num) {
		String result = "";
		int i = 0;
		while (i < num) {
			int lastFirst = str.lastIndexOf('/');
			result = str.substring(lastFirst) + result;
			str = str.substring(0, lastFirst);
			i++;
		}
		return result.substring(1);
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 转换字节数组为16进制字串
	 *
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	/**
	 * 空字符串检验
	 *
	 * @param :str
	 * @return
	 */
	public static boolean isBlank(final Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String) {
			String data = obj.toString();
			if (data == null || data.trim().equals("") || data.trim().equalsIgnoreCase("null")) {
				return true;
			} else {
				return false;
			}
		} else if (obj instanceof List) {
			List data = (List) obj;
			if (data == null || data.size() == 0) {
				return true;
			} else {
				return false;
			}
		} else if (obj instanceof Set) {
			Set data = (Set) obj;
			if (data == null || data.size() == 0) {
				return true;
			} else {
				return false;
			}
		} else if (obj instanceof Integer) {
			Integer data = (Integer) obj;
			if (data == null) {
				return true;
			} else {
				return false;
			}
		} else if (obj instanceof String[]) {
			String[] data = (String[]) obj;
			if (data == null || data.length == 0) {
				return true;
			} else {
				return false;
			}
		} else if (obj instanceof Double) {
			Double data = (Double) obj;
			if (data == null) {
				return true;
			} else {
				return false;
			}
		} else if (obj instanceof StringBuffer) {
			StringBuffer data = (StringBuffer) obj;
			if (data == null || data.length() == 0) {
				return true;
			} else {
				return false;
			}
		} else if (obj instanceof StringBuilder) {
			StringBuilder data = (StringBuilder) obj;
			if (data == null || data.length() == 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 判断是否是空字符串, 不为空返回 true
	 *
	 * @param :str
	 * @return
	 */
	public static boolean isNotBlank(final Object obj) {
		return !isBlank(obj);
	}

	/**
	 * 都不为空 返回true
	 * @param objects
	 * @return
	 */
	public static boolean isNotBlankAnd(Object... objects) {
		if (isBlank(objects)) {
			return false;
		}
		int count = 0;
		for (Object obj : objects) {
			if (isBlank(obj)) {
				return false;
			}
			count++;
		}
		// 不为空的数量等于总数量
		if (count == objects.length) {
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * 说   明: 只要其中一个为空，就返回TRUE
	 * &#64;param objects
	 * &#64;return
	 * 创建者: 杨乙伟
	 * 日   期: 2014-6-25下午4:02:48
	 * </pre>
	 */
	public static boolean isBlankOr(Object... objects) {
		for (Object obj : objects) {
			if (isBlank(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 去掉字符串 中空格
	 *
	 * @param :text
	 * @return text
	 */
	public static String delSpace(String str) {
		str = str.replace(" ", "");
		return str;
	}

	/**
	 * 验证邮箱号输入是否合法
	 *
	 * @param :strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/**
	 * 验证手机号输入是否合法
	 * 
	 * @param strMobile
	 * @return
	 */
	public static boolean isMobile(String strMobile) {
		Pattern p = Pattern.compile("^[1][0-9][0-9]{9}$");
		Matcher m = p.matcher(strMobile);
		return m.matches();
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 转换字符串编码为UTF-8
	 * 
	 * @param txt
	 *            字符串参数
	 * @return 转换后的字符串
	 */
	public static String toUTF8(String txt) {
		String reStr = null;
		try {
			if (txt == null)
				reStr = null;
			else
				reStr = new String(txt.getBytes("ISO8859-1"), "UTF-8");
		} catch (Exception ex) {
			return null;
		}
		return reStr;
	}

	public static String format(String txt, String value1, String value2) {
		String v1 = txt.replace("@", value1);
		String reStr = v1.replace("$", value2);
		return reStr;
	}

	/**
	 * <pre>
	 * 说   明:  产生唯一且不重复的随机数
	 * &#64;param tag
	 * &#64;return
	 * 创建者: 杨乙伟
	 * 日   期: 2014-8-27下午1:50:30
	 * </pre>
	 */
	synchronized public static String getRandomNo(String tag) {
		if (isBlank(tag))
			tag = "";
		String randomNo = "";
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		randomNo = tag + ((int) (Math.random() * 100)) + Long.parseLong(date);
		return randomNo;
	}

	/**
	 * 将空字符串转换
	 * 
	 * @param obj
	 * @return
	 */
	public static String getNullToStr(String obj) {
		if (isBlank(obj)) {
			return "";
		} else {
			return obj;
		}
	}

	public static String getNullToStr(Object obj) {
		if (isBlank(obj)) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * <pre>
	 * 说   明:  去掉斜杠
	 * &#64;param method  STRINGUTIL
	 * &#64;return
	 * 创建者: 杨乙伟
	 * 日   期: 2014-10-29下午2:51:50
	 * </pre>
	 */
	public static String replaceSlash(String method) {
		String slashMethod = method;
		boolean isSlash = false;
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < slashMethod.length(); i++) {
			char s = slashMethod.charAt(i);
			if (isSlash) {
				if (s == '/') {
					continue;
				} else {
					isSlash = false;
					stringBuilder.append(s);
				}
			} else {
				if (s == '/') {
					isSlash = true;
				}
				stringBuilder.append(s);
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * <pre>
	 * 说       明: 截取指定长度的字符串
	 * &#64;param str  STRINGUTIL
	 * &#64;param count
	 * &#64;return
	 * &#64;throws Exception
	 * 涉及版本: V1.0.0 
	 * 创  建  者: 杨乙伟
	 * 日       期: 2015-7-7上午11:42:04
	 * </pre>
	 */
	public static String substring(String str, int count) throws Exception {

		if (str == null || str.equals("") || count <= 0) {

			return null;
		}
		count = count > str.length() ? str.length() : count;
		return str.substring(0, count);
	}

	/**
	 * <pre>
	 * 说       明: 截取指定长度的字符串,并加上后缀
	 * &#64;param str  STRINGUTIL
	 * &#64;param count
	 * &#64;param suffix
	 * &#64;return
	 * &#64;throws Exception
	 * 涉及版本: V1.0.0 
	 * 创  建  者: 杨乙伟
	 * 日       期: 2015-7-7下午1:20:26
	 * </pre>
	 */
	public static String substring(String str, int count, String suffix) throws Exception {

		return substring(str, count) + suffix;
	}

	/**
	 * <pre>
	 * 说       明: 获取指定类包名路径
	 * &#64;param cls : 类
	 * &#64;param replaceChar ：要替换的字符串
	 * &#64;param retainPackageLevel ：指定保留包的层级,如:只保留前面2级,后面的不需要
	 * &#64;return  STRINGUTIL
	 * &#64;throws Exception
	 * 涉及版本: V1.0.0 
	 * 创  建  者: 杨乙伟
	 * 日       期: 2015-7-9上午10:50:52
	 * </pre>
	 */
	public static String getPackageName(Class cls, String replaceChar, Integer retainPackageLevel) throws Exception {

		if (cls == null) {
			return null;
		}
		if (retainPackageLevel != null && retainPackageLevel <= 0) {

			return null;
		}
		String packageName = "";
		Integer size = 0;
		if (replaceChar == null) {

			packageName = cls.getPackage().getName();
			size = packageName.replace(".", "/").split("/").length;
		} else {

			if (replaceChar.trim().equals("*")) {
				return null;

			}
			packageName = cls.getPackage().getName().replace(".", replaceChar);
			size = packageName.split(replaceChar).length;
		}

		if (retainPackageLevel != null) {

			retainPackageLevel = retainPackageLevel > size ? size : retainPackageLevel;
			List<String> list = null;
			if (replaceChar != null) {

				list = Arrays.asList(packageName.split(replaceChar));// string 转
																		// list
			} else {
				list = Arrays.asList(packageName.replace(".", "/").split("/"));// string
																				// 转
																				// list
			}

			String tempListStr = list.subList(0, retainPackageLevel).toString();// 截取指定范围集合数据
			packageName = tempListStr.substring(1, tempListStr.length() - 1);// 去掉数据首尾[]
			if (replaceChar != null) {

				packageName = packageName.replace(", ", replaceChar);
			} else {
				packageName = packageName.replace(", ", ".");
			}
		}
		return packageName;
	}

	/**
	 * <pre>
	 * 说       明: 去掉最后一个指定字符  STRINGUTILS
	 * &#64;param sb
	 * &#64;param lastChar
	 * &#64;return
	 * &#64;throws Exception
	 * 涉及版本: V1.0.0 
	 * 创  建  者:杨乙伟
	 * 日       期: 2015-3-29下午12:59:25
	 * </pre>
	 */
	public static StringBuffer resplaceLastCharSb(StringBuffer sb, String lastChar) throws Exception {
		if (sb == null || sb.length() == 0 || lastChar == null || lastChar.trim().equals("")) {
			return null;
		}
		if (sb.substring(sb.length() - 1, sb.length()).equals(lastChar)) {
			return new StringBuffer(sb.substring(0, sb.length() - 1));
		}
		return new StringBuffer(sb);
	}

	/**
	 * <pre>
	 * 说   明: 去掉最后一个字符  STRINGUTILS
	 * 创建者: 杨乙伟
	 * 修改者:
	 * 日   期: 2014-5-26 下午03:31:11
	 * 
	 * &#64;param sb
	 * &#64;return
	 * &#64;throws Exception
	 * </pre>
	 */
	public static StringBuffer resplaceSb(String sb) throws Exception {
		if (sb == null || sb.length() == 0) {
			return null;
		}
		sb = sb.substring(0, sb.length() - 1);
		return new StringBuffer(sb);
	}

	/**
	 * <pre>
	 * 说       明:   STRINGUTILS
	 * &#64;param sb
	 * &#64;return
	 * &#64;throws Exception
	 * 涉及版本: 
	 * 创  建  者: 杨乙伟
	 * 日       期: 2015-9-23上午11:55:39
	 * </pre>
	 */
	public static StringBuilder resplaceSb(StringBuilder sb) throws Exception {
		if (sb == null || sb.length() == 0) {
			return new StringBuilder("");
		}
		sb = sb.delete(sb.length() - 1, sb.length());
		return new StringBuilder(sb);
	}

	public static StringBuffer resplaceSb(StringBuffer sb) throws Exception {
		if (sb == null || sb.length() == 0) {
			return new StringBuffer("");
		}
		sb = sb.delete(sb.length() - 1, sb.length());
		return new StringBuffer(sb);
	}

	/**
	 * <pre>
	 * 说       明: 删除最后面的指定字符  STRINGUTILS
	 * &#64;param str
	 * &#64;param deleteChar
	 * &#64;return
	 * &#64;throws Exception
	 * 涉及版本: V1.0.0 
	 * 创  建  者: 陈林林(Vickey)
	 * 日       期: 2015-3-2下午1:32:27
	 * </pre>
	 */
	public static String deleteChar(String str, String deleteChar) throws Exception {
		if (str == null || str.length() < 2) {
			return str;
		}
		String start = str.substring(0, str.lastIndexOf(deleteChar));
		String end = str.substring(str.lastIndexOf(deleteChar) + 1, str.length());
		return start + end;
	}

	/**
	 * <pre>
	 * 说       明:   STRINGUTILS
	 * &#64;param html
	 * &#64;return
	 * 涉及版本: 
	 * 创  建  者: 陈林林(Vickey)
	 * 日       期: 2015-9-23上午11:56:19
	 * </pre>
	 */
	// HTML代码反转
	public static String escapeHtmlWithJSON(String html) {
		return html.replace("\"", "\\\"");
	}

	/**
	 * <pre>
	 * 说   明:  字符数组转为STRING  STRINGUTILS
	 * 
	 * &#64;param ch
	 * &#64;return 创建者: 陈    林(Vickey)
	 * 日   期: 2014-7-18下午5:49:31
	 * </pre>
	 */
	public static String convertArray(char[] ch) {
		if (ch == null) {
			return null;
		}
		return new String(ch);
	}

	/**
	 * <pre>
	 * 说   明: 去掉最后一个字符  STRINGUTIL
	 * 创建者: 陈林林(Vickey)
	 * 修改者:
	 * 日   期: 2014-5-26 下午03:31:11
	 * 
	 * &#64;param sb
	 * &#64;return
	 * &#64;throws Exception
	 * </pre>
	 */
	public static String resplaceStr(String sb) throws Exception {
		if (sb == null || sb.length() == 0) {
			return sb;
		}
		sb = sb.substring(0, sb.length() - 1);
		return sb;
	}

	/**
	 * <pre>
	 * 说       明: 给密码加盐
	 * &#64;param userName
	 * &#64;param userPwd
	 * &#64;return
	 * 涉及版本: V1.0.0 
	 * 创  建  者: 陈林林(Vickey)
	 * 日       期: 2015-1-30上午11:11:03
	 * </pre>
	 */
	public static String[] AddSalt(String userName, String userPwd, String randomSalt) {
		if (StringUtils.isBlankOr(userName, userPwd)) {
			return null;
		}
		String algorithmName = "md5";
		String salt1 = userName;// 盐1
		String salt2 = null;
		if (!StringUtils.isBlank(randomSalt)) {// 如果传入了随机数，就是登录操作，验证密码是否一致
			salt2 = randomSalt;
		} else {
			salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();// 盐2:随机数
		}
		int hashIterations = 2;// 指定散列次数
		SimpleHash hash = new SimpleHash(algorithmName, userPwd, salt1 + salt2, hashIterations);
		return new String[] { hash.toHex(), salt2 };
	}

	/**
	 * <pre>
	 * 说       明: 数字比较
	 * &#64;param first
	 * &#64;param second
	 * &#64;return
	 * 涉及版本: 
	 * 创  建  者: 陈林林(Vickey)
	 * 日       期: 2015-11-11下午5:25:13
	 * </pre>
	 */
	public static boolean equalsInt(int first, int second) {
		return first > second;
	}

	/**
	 * <pre>
	 * 说       明: 不忽略大小写比较字符串
	 * &#64;param first
	 * &#64;param second
	 * &#64;return
	 * 涉及版本: 
	 * 创  建  者: 陈林林(Vickey)
	 * 日       期: 2015-11-11下午5:25:03
	 * </pre>
	 */
	public static boolean equals(String first, String second) {
		if (first == null || second == null) {
			return false;
		}
		return first.equals(second);
	}

	/**
	 * <pre>
	 * 说       明: 忽略大小写比较字符串
	 * &#64;param first
	 * &#64;param second
	 * &#64;return
	 * 涉及版本: 
	 * 创  建  者: 陈林林(Vickey)
	 * 日       期: 2015-11-11下午5:24:45
	 * </pre>
	 */
	public static boolean equalsIgnoreCase(String first, String second) {
		if (first == null || second == null) {
			return false;
		}
		return first.equalsIgnoreCase(second);
	}

	/**
	 * <pre>
	 * 说       明: 根据特殊符号的字符串进行拼接
	 * &#64;param data
	 * &#64;param appendChar
	 * &#64;param appendValue
	 * &#64;return
	 * 涉及版本: 
	 * 创  建  者: 陈林林(Vickey)
	 * 日       期: 2015-12-4下午2:16:11
	 * </pre>
	 */
	public static String getAppendValue(String data, String appendChar, String appendValue) {
		if (StringUtils.isBlank(data) || data.length() == 0) {
			data = "";
		}

		if (DATA_TYPE_URL.indexOf(appendChar) != -1) {

			if (data.indexOf("?") == -1) {// 没有?

				if (appendValue.length() > 1 && appendValue.substring(0, 1).equals("?")) {
					return data + appendValue;
				}
				return data + "?" + appendValue;
			} else if (data.substring(data.length() - 1, data.length()).equals("?")) {
				return data + appendValue;
			} else if (!data.substring(data.length() - 1, data.length()).equals("&")) {
				if (appendValue.length() > 1 && appendValue.substring(0, 1).equals("?")) {
					return data + "&" + appendValue.substring(1, appendValue.length());
				}
				return data + "&" + appendValue;
			}
			return data + appendValue;
		} else if (DATA_TYPE_COMMON.indexOf(appendChar) != -1) {

			if (data.lastIndexOf(appendChar) != data.length() - 1) {// 不存在

				return data + appendChar + appendValue;
			}
			return data + appendValue;
		}
		return null;
	}

	/**
	 * <pre>
	 * 说       明: 银行卡脱敏处理，指定长度，指定字段
	 * &#64;param source 源数据
	 * &#64;param replaceChar 替换的字段
	 * &#64;param saveCount 保存数量
	 * &#64;param displayCount 显示数量
	 * &#64;param intervalCount 间隔数量
	 * &#64;return
	 * &#64;throws Exception
	 * 涉及版本: 
	 * 创  建  者: 陈林林(Vickey)
	 * 日       期: 2016-3-2下午8:11:44
	 * </pre>
	 */
	public static String replaceChar(String source, String replaceChar, Integer saveCount, Integer displayCount,
			Integer intervalCount) throws Exception {

		if (StringUtils.isBlankOr(source, saveCount, displayCount, intervalCount)) {
			return null;
		}
		replaceChar = replaceChar == null ? "*" : replaceChar;
		source = source.length() > saveCount ? source.substring(source.length() - saveCount, source.length()) : source;
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= displayCount - source.length(); i++) {
			sb.append(replaceChar);
			if (i % intervalCount == 0) {
				sb.append(" ");// 每固定间隔加一个空格
			}
		}
		return sb.append(source).toString();
	}

	/**
	 * <pre>
	 * 说       明: 
	 * 涉及版本: V3.1.0  
	 * 创  建  者: Vickey
	 * 日       期: 2016年4月29日上午9:32:18
	 * Q    Q: 308053847
	 * </pre>
	 */
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <pre>
	 * 说       明: 
	 * 涉及版本: V3.1.0  
	 * 创  建  者: Vickey
	 * 日       期: 2016年4月29日上午9:32:22
	 * Q    Q: 308053847
	 * </pre>
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	
	
	

	/**
	 * <pre>
	 * 说       明: 解压到指定目录 
	 * 涉及版本: V3.0.0  
	 * 创  建  者: 杨乙伟
	 * 日       期: 2017年6月8日下午1:24:21
	 * </pre>
	 */
	public static void unZipFiles(String zipPath, String descDir) throws Exception {
		deleteDir(new File(descDir));
		unZipFiles(new File(zipPath), descDir);
	}

	/**
	 * <pre>
	 * 说       明: 删除空目录
	 * 涉及版本: V3.0.0  
	 * 创  建  者:杨乙伟
	 * 日       期: 2017年6月19日上午9:29:23
	 * </pre>
	 */
	private static void doDeleteEmptyDir(String dir) throws Exception {
		boolean success = (new File(dir)).delete();
		if (success) {
			System.out.println("Successfully deleted empty directory: " + dir);
		} else {
			System.out.println("Failed to delete empty directory: " + dir);
		}
	}

	/**
	 * <pre>
	 * 说       明: 递归删除目录下的所有文件及子目录下所有文件
	 * 涉及版本: V3.0.0  
	 * 创  建  者: 杨乙伟
	 * 日       期: 2017年6月19日上午9:29:15
	 * </pre>
	 */
	public static boolean deleteDir(File dir) throws Exception {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * <pre>
	 * 说       明: 解压文件到指定目录
	 * 涉及版本: V3.0.0  
	 * 创  建  者: 杨乙伟
	 * 日       期: 2017年6月8日下午1:24:30
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	public static void unZipFiles(File zipFile, String descDir) throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {

			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			;
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				in.close();
				continue;
			}
			// 输出文件路径信息

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
	}

	/**
	 * <pre>
	 * 说       明: 从REQUEST中获取JSON数据
	 * 涉及版本: V3.0.0  
	 * 创  建  者: 杨乙伟
	 * 日       期: 2017年12月1日上午9:31:13
	 * </pre>
	 */
	public String getJsonDataForRequest(HttpServletRequest request) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		return stringBuilder.toString();
	}

	

	
}
