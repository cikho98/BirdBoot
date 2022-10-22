package com.webserver.test;

import java.util.Arrays;

/**
 * String中重载的split方法
 * String[] split(String s,limit l)
 */
public class SplitDemo {
    public static void main(String[] args) {
        String line = "1=2=3=4=5=6=======";
        //忽略后面所有拆分出来的空字符串
        String[] data = line.split("=");
        System.out.println(Arrays.toString(data));

        //limit:2  表达仅拆分出两项
        data = line.split("=",2);
        System.out.println(Arrays.toString(data));
        //limit:3  拆分出3项
        data = line.split("=",3);
        System.out.println(Arrays.toString(data));
        //当limit的值大于可拆分项时，仅保留所有可拆分项
        data = line.split("=",100);
        System.out.println(Arrays.toString(data));
        //如果limit=0，那么效果与一个参数的split一致。
        data = line.split("=",0);
        System.out.println(Arrays.toString(data));
        //当limit小于0时,则为应拆尽拆.将所有可拆分出的内容全部保留.
        data = line.split("=",-1);
        System.out.println(Arrays.toString(data));

    }
}
