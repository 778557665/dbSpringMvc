package com.db.demo.mvcaction;

import com.db.demo.service.IDemoService;
import com.db.mvcframework.annotation.DBAutowired;
import com.db.mvcframework.annotation.DBController;
import com.db.mvcframework.annotation.DBRequestMapping;
import com.db.mvcframework.annotation.DBRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@DBController
@DBRequestMapping("/demo")
public class DemoAction {

    @DBAutowired
    private IDemoService demoService;

    @DBRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @DBRequestParam("name") String name){
        String result = demoService.get(name);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
