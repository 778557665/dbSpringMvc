package com.db.mvcframework.servlet;

import com.db.mvcframework.annotation.DBAutowired;
import com.db.mvcframework.annotation.DBController;
import com.db.mvcframework.annotation.DBRequestMapping;
import com.db.mvcframework.annotation.DBService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class DBDispatcherServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<String>();

    private Map<String,Object> iocMap = new HashMap<>();

    private Map<String,Method> handlerMapping = new HashMap<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //6.调用doget dopost方法，反射调用  输出结果到浏览器
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception" + Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if(handlerMapping.isEmpty()){return;}
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        requestURI = requestURI.replace(contextPath,"").replaceAll("/+","/");
        if(!this.handlerMapping.containsKey(requestURI)){
            resp.getWriter().write("404 not found");
            return;
        }

        Method method = this.handlerMapping.get(requestURI);

        Map<String, String[]> parameterMap = req.getParameterMap();

        String beanName = lowerFirstCase(method.getDeclaringClass().getSimpleName());

        method.invoke(iocMap.get(beanName),new Object[]{req,resp,parameterMap.get("name")[0]});

    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        //2.解析配置文件，扫描所有相关的类
        doScanner(contextConfig.getProperty("scanPackage"));

        //3.初始化所有相关的类，并且保存到IOC容器中
        doInstance();

        //4.完成自动化依赖注入 也就是DI
        doAutoWired();

        //5.创建handlerMapping  将URL和method 建立对应关系
        initHandlerMapping();

        System.out.println("DBmvc is init!");
    }

    private void initHandlerMapping() {
        if(iocMap.isEmpty()){return;}
        for (Map.Entry<String, Object> entry : iocMap.entrySet()) {

            Class<?> clazz = entry.getValue().getClass();
            if(!clazz.isAnnotationPresent(DBController.class)){
                continue;
            }
            String baseUrl = "";
            if(clazz.isAnnotationPresent(DBRequestMapping.class)){
                DBRequestMapping dbRequestMapping = clazz.getAnnotation(DBRequestMapping.class);
                baseUrl = dbRequestMapping.value();
            }

            Method[] method = clazz.getMethods();
            for (Method m : method) {
                if(!m.isAnnotationPresent(DBRequestMapping.class)){ continue; }
                DBRequestMapping dbRequestMapping = m.getAnnotation(DBRequestMapping.class);
                String url = ("/" + baseUrl + "/" + dbRequestMapping.value()).replaceAll("/+","/");
                handlerMapping.put(url,m);

                System.out.println("Mapping:" + url + "," + m);
            }
        }
    }

    private void doAutoWired() {
        if(iocMap.isEmpty()){return;}
        for (Map.Entry<String,Object> entry : iocMap.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if(!field.isAnnotationPresent(DBAutowired.class)){ continue; }
                DBAutowired dbAutowired = field.getAnnotation(DBAutowired.class);
                String beanName = dbAutowired.value();
                if("".equals(beanName.trim())){
                    beanName = field.getType().getName();
                }

                field.setAccessible(true);//强制赋值
                try {
                    field.set(entry.getValue(),iocMap.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    private void doInstance() {
        if(classNames.isEmpty()){return;}
        try{
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                //虽然包下可能有很多类，但是只需要给有注解的类实例化
                if(clazz.isAnnotationPresent(DBController.class)){
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    iocMap.put(beanName,clazz.newInstance());
                }else if(clazz.isAnnotationPresent(DBService.class)){
                    //1 类名首字母小写
                    //2自定义命名
                    DBService dbService = clazz.getAnnotation(DBService.class);
                    String beanName = dbService.value();
                    if("".equals(beanName.trim())){
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    iocMap.put(beanName,instance);
                    //给接口注入接口的实现类（用接口的全称作为KEY，用实现类作为值）
                    Class<?> [] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        if(iocMap.containsKey(i.getName())){
                            throw new Exception("The beanName is exists");
                        }
                        iocMap.put(i.getName(),instance);
                    }
                }else {
                    continue;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String lowerFirstCase(String simpleName) {
        char [] nameChar = simpleName.toCharArray();
        nameChar[0] += 32;
        return String.valueOf(nameChar);
    }

    private void doScanner(String scanPackage) {

        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));

        File classDir = new File(url.getFile());

        for (File file : classDir.listFiles()) {
            //可能有很多子包 递归判断
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else {
                if(!file.getName().endsWith(".class")){ continue; }
                String className =  (scanPackage + "." + file.getName().replace(".class","")).trim();
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String config) {

        //从类路径下去取得properties
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(config);

        try {
            contextConfig.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
