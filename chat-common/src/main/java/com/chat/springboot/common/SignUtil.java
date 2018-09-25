package com.chat.springboot.common;

import org.apache.log4j.Logger;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 密码加密工具类
 * @author yangyiwei
 * @date 2018年7月2日
 * @time 下午6:53:48
 */
public class SignUtil {
	private static Logger log = Logger.getLogger(SignUtil.class);

	/**
	 * 加密密码
	 * @param userName
	 * @param userPwd
	 * @param randomSalt
	 * @return
	 */
	public static String[] AddSalt(String userName, String userPwd, String randomSalt) {
		if (userName == null || "".equals(userName.trim())) {
			return null;
		}
		if (userPwd == null || "".equals(userPwd.trim())) {
			return null;
		}
		String algorithmName = "md5";
		String salt1 = userName;// 盐1
		String salt2 = null;// 盐2
		if (randomSalt != null && !"".equals(randomSalt.trim())) {// 如果传入了随机数，就是登录操作，验证密码是否一致
			salt2 = randomSalt;
		} else {
			salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();// 盐2:随机数
		}
		int hashIterations = 2;// 指定散列次数
		SimpleHash hash = new SimpleHash(algorithmName, userPwd, salt1 + salt2, hashIterations);
		return new String[] { hash.toHex(), salt2 };
	}
}
