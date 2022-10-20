package com.webserver.test;

import java.util.Arrays;

public class SplitDemo {
    public static void main(String[] args) {
        String line ="1=2=3=4=5=6============";
        String[] data = line.split("=");
        System.out.println(Arrays.toString(data));


//        limit:2 表达仅拆出两项
        data= line.split("=",2);
        System.out.println(Arrays.toString(data));

//        拆出三项
        data= line.split("=",3);
        System.out.println(Arrays.toString(data));

        //当limit的值大于可拆分时，仅保留所有可拆分项；
        data= line.split("=",200);
        System.out.println(Arrays.toString(data));

        //如果lmit
        data= line.split("=",0);
        System.out.println(Arrays.toString(data));

        data= line.split("=",-1);
        System.out.println(Arrays.toString(data));

    }
}
