package com.webserver.core;

import com.webserver.annoations.Controller;
import com.webserver.annoations.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类维护请求路径与Controller处理方法的对应关系
 */
public class HandlerMapping {
    private static Map<String, Method> mapping = new HashMap<>();
    static{
        initMapping();
    }
    private static void initMapping(){
        try {
            //启动类所在的目录
            File root = new File(
                BirdBootApplication.primarySource.getResource(".").toURI()
            );
            //定位启动类所在目录中的子目录controller
            File dir = new File(root,"controller");
            if(!dir.exists()){//如果controller目录不存在则直接返回
                return;
            }
            File[] subs = dir.listFiles(f->f.getName().endsWith(".class"));
            for(File sub : subs){
                String fileName = sub.getName();
                String className = fileName.substring(0,fileName.indexOf("."));
                //获取启动类的包名
                String packageName = BirdBootApplication.primarySource
                                     .getPackage().getName();
                className = packageName+".controller."+className;
                Class cls = Class.forName(className);
                if(cls.isAnnotationPresent(Controller.class)){
                    Method[] methods = cls.getDeclaredMethods();
                    for(Method method : methods){
                        if(method.isAnnotationPresent(RequestMapping.class)){
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            String value = rm.value();//方法注解中的参数(该方法处理的请求路径)
                            mapping.put(value,method);
                        }
                    }
                }
            }
        } catch (Exception e) {
            //处理业务如果出现了异常应当设置response响应500类错误
        }
    }

    /**
     * 根据给定的请求路径获取处理该请求的某个Controller中的对应方法
     * @param path
     * @return
     */
    public static Method getMethod(String path){
        return mapping.get(path);
    }

    public static void main(String[] args) {
        System.out.println(mapping);
        Method method = getMethod("/regUser");
        System.out.println(method);
    }
}
