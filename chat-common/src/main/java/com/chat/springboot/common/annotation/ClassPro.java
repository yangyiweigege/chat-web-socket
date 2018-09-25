package com.chat.springboot.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ClassPro {

    String name() default "";

    String tableName() default "";
}
