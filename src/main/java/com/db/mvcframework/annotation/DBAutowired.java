package com.db.mvcframework.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD) //字段
@Retention(RetentionPolicy.RUNTIME)  //生命周期
@Documented //可见的
public @interface DBAutowired {

    String value() default  "";
}
