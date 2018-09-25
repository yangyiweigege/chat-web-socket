package com.chat.springboot.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否校验session中的userName
 * @author yangyiwei
 * @date 2018年7月3日
 * @time 下午5:08:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateSession {
	public boolean value() default true;
}
