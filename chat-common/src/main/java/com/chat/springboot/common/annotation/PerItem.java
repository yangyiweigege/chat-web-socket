package com.chat.springboot.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在方法参数上加此注解。
 * 校验集合内的对象属性
 * 注意：此注解依赖@ValidateServiceData(即方法体上存在该注解)
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PerItem {

    /**
     * 填写需要校验的集合 对象属性
     *
     * @return
     */
    String[] attributes() default {};
}
