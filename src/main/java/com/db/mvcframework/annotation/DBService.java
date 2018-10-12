package com.db.mvcframework.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE) //类
@Retention(RetentionPolicy.RUNTIME)  //生命周期
@Documented //可见的
public @interface DBService {

    String value() default  "";
}
