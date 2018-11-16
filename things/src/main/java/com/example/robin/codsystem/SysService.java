package com.example.robin.codsystem;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;


class MyServer extends NanoHTTPD {

    public MyServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>CODMn分析仪</title>\n" +
                "<script>document.createElement(\"myHero\")</script>\n" +
                "<style>\n" +
                "myHero {\n" +
                "\tdisplay: block;\n" +
                "\tbackground-color: #ddd;\n" +
                "\tpadding: 50px;\n" +
                "\tfont-size: 30px;\n" +
                "} \n" +
                "</style> \n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<h1>NS210型COD</h1>\n" +
                "\n" +
                "<p>欢迎您使用COD控制系统！</p>\n" +
                "\n" +
                "<myHero><button type=\"button\" onclick=\"alert('启动测试')\">启动分析</button>\n" +
                "<button type=\"button\" onclick=\"alert('停止分析')\">停止分析</button></myHero>\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        return newFixedLengthResponse(builder.toString());
    }
}

public class SysService extends Service {
    public SysService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //Log.i("MainActivity", "启动后台服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyServer myServer = new MyServer(8080);
        try {
            myServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

}
