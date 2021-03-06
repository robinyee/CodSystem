package com.example.robin.codsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SetupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        gotoPage();

        String ipText = getLocalIpStr(getApplicationContext());
        Resources res = getResources();
        String UrlString = "http://" + ipText + ":8080";

        //生成网址的二维码
        ImageView mImageView = (ImageView) findViewById(R.id.imageViewZXing);
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(UrlString, 70, 70);
        mImageView.setImageBitmap(mBitmap);


    }

    //获取IP地址
    public static String getLocalIpStr(Context context){
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        return  intToIpAddr(wifiInfo.getIpAddress());
    }

    private static String intToIpAddr(int ip){
        return (ip & 0xFF)+"."
                + ((ip>>8)&0xFF) + "."
                + ((ip>>16)&0xFF) + "."
                + ((ip>>24)&0xFF);
    }


    private void gotoPage(){

        //点击Home按钮
        ImageButton buttonHome = findViewById(R.id.imageButtonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, MainActivity.class) ;
                startActivity(intent) ;
            }
        });
        //点击Data按钮
        ImageButton buttonData = findViewById(R.id.imageButtonData);
        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, DataActivity.class) ;
                startActivity(intent) ;
            }
        });
        //点击Control按钮
        ImageButton buttonControl = findViewById(R.id.imageButtonControl);
        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, ControlActivity.class) ;
                startActivity(intent) ;
            }
        });
        /*//点击Setup按钮
        ImageButton buttonSetup = (ImageButton) findViewById(R.id.imageButtonSetup);
        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, SetupActivity.class) ;
                startActivity(intent) ;
            }
        });*/

    }
}
