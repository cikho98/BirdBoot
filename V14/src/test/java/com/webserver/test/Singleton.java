package com.webserver.test;

/**
 * 单例模式 Java23种设计模式之一；
 * 使用单例模式定义的类，全局只能有一个实例
 */
public class Singleton {
    private static Singleton instance = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance(){
        return instance;
    }
}
