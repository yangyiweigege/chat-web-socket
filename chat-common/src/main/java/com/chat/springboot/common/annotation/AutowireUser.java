package com.chat.springboot.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 若存在该注解，将会从本地线程中提取用户信息
 * @author yangyiwei
 * @date 2018年7月3日
 * @time 下午5:08:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutowireUser {
	
	/**
	 * 填写令牌字段 如token ?token=xxxxx
	 * @return
	 */
	public String value() default "token";

}
