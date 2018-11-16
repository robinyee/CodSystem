package com.example.robin.codsystem;

public class SysData {

    //是否已经获取到网络时间
    private static boolean isGetNetTime = false;

    public static boolean getTimeOk() {
        return isGetNetTime;
    }

    public static void setTimeOk(boolean a) {
        SysData.isGetNetTime = a;
    }

    //仪器是否正在运行
    private  static boolean isRun = false;
    public static boolean getIsRun() {
        return isRun;
    }

    public static void setIsRun(boolean a) {
        SysData.isRun = a;
    }

}
