package com.db.mvcframework.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD}) //类和方法
@Retention(RetentionPolicy.RUNTIME)  //生命周期
@Documented //可见的
public @interface DBRequestMapping {

    String value() default  "";
}
