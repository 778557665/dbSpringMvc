package com.db.mvcframework.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER) //类
@Retention(RetentionPolicy.RUNTIME)  //生命周期
@Documented //可见的
public @interface DBRequestParam {

    String value() default  "";
}
