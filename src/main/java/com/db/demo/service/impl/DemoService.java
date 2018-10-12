package com.db.demo.service.impl;

import com.db.demo.service.IDemoService;
import com.db.mvcframework.annotation.DBService;

@DBService
public class DemoService implements IDemoService {

    public String get(String name){
        return "my name is" + name;
    }

}
