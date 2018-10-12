package com.db.demo.service;


//为什么注解不能加上接 口上，因为接口是无法实例化的
public interface IDemoService {

    public String get(String name);

}
