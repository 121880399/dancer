package com.shx.dancer.common;

/**
 * Created by 邵鸿轩 on 2016/11/15.
 */
public class SystemConfig {
    /**
     * 打印日志的开关,生产版本时改为false
     */
    public static boolean LOGFLAG = true;
    public static String appName="dancer";
//    public static String BASEURL="http://192.168.8.127:80";
    public static String WEBBASEURL="http://47.104.168.161:8081/#";
    //测试地址
    public static String TEST_URL="http://192.168.1.45:80";
//    public static String TEST_URL="http://192.168.1.127:8080";
    public static String BASEURL=TEST_URL;

}
