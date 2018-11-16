package com.example.robin.codsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.things.device.TimeManager;
import com.google.android.things.pio.PeripheralManager;
import com.instacart.library.truetime.TrueTime;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private ImageButton btnStartup;
    private String statusMsg;
    private TextView textViewStatus;
    private TextView textViewDate;
    private SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm");
    private Date curDate =  new Date(System.currentTimeMillis());
    private TimeManager timeManager = TimeManager.getInstance();
    private boolean isGetNetTime = SysData.getTimeOk();
    private boolean isRun = SysData.getIsRun();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //启动后台服务
        Intent intent = new Intent(this, SysService.class);
        Bundle bundle = new Bundle();
        startService(intent);
        Log.i("MainActivity", "启动后台服务");

        //第一次运行获取网络时间
        if(!SysData.getTimeOk()){
            setSysTime();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity", "程序已经运行");

        //显示实时时间
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        //接收系统时间广播,ACTION_TIME_TICK-60s发送一次，ACTION_TIME_CHANGED-系统时间改变
        registerReceiver(mTimeRefreshReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        registerReceiver(mTimeRefreshReceiver, new IntentFilter(Intent.ACTION_TIME_CHANGED));
        textViewDate.setText(formatter.format(curDate)); //显示当前时间

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        statusMsg = "系统待机";
        textViewStatus.setText(statusMsg);
        Log.i("MainActivity", "状态="+statusMsg);

        startup(); //启动测定流程
        gotoPage(); //页面跳转
    }

    /*
    //TimeManager timeManager = TimeManager.getInstance();
    // Use 24-hour time
    //timeManager.setTimeFormat(TimeManager.FORMAT_24);
    // Set time zone to Eastern Standard Time
    //timeManager.setTimeZone("Asia/Shanghai");
    // Set clock time to noon
    //Calendar calendar = Calendar.getInstance();
    //calendar.set(Calendar.MILLISECOND, 0);
    //calendar.set(Calendar.SECOND, 0);
    //calendar.set(Calendar.MINUTE, 0);
    //calendar.set(Calendar.HOUR_OF_DAY, 12);
    //long timeStamp = calendar.getTimeInMillis();
    //timeManager.setTime(timeStamp);
    //timeManager.setTime(date.getTime());
    */
    //系统时间同步为网络时间
    private void setSysTime()
    {
        Log.i("MainActivity", "准备获取网络时间");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Date date = null; //new Date(System.currentTimeMillis());  //初始时间从系统获取
                int isOK = 0;
                int max = 10;
                do{
                    try {
                        TrueTime.build().initialize();
                        date = TrueTime.now();
                        Log.i("MainActivity", "获取网络时间成功");
                        Log.i("MainActivity", "网络时间："+date.toString());
                        Log.i("MainActivity", "网络时间："+date.getTime());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(date != null){
                        timeManager.setTime(date.getTime());
                        isOK = max;
                        isGetNetTime = true;
                        SysData.setTimeOk(isGetNetTime);
                    }else {
                        isOK = isOK + 1;
                    }
                    try {
                        Thread.sleep(10000);  // 线程暂停10秒，单位毫秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while(isOK < max || isGetNetTime == false);

                timeManager.setTimeFormat(TimeManager.FORMAT_24); //设置24小时格式
                timeManager.setTimeZone("Asia/Shanghai"); //设置时区
            }
        }).start();

    }

    //启动测定流程
    private void startup(){
        //点击Startup按钮
        ImageButton buttonStartup = (ImageButton) findViewById(R.id.startup);
        if (!isRun) {
            buttonStartup.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        } else {
            buttonStartup.setImageResource(R.drawable.ic_stop_black_24dp);
        }
        //buttonStartup.setBackgroundResource(R.drawable.ic_stop_black_24dp);
        //buttonStartup.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
        buttonStartup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRun) {
                    showPlayDialog();
                } else {
                    showStopDialog();
                }
            }
        });
    }
    //按下启动测定时显示对话框
    private void showPlayDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder altDialog = new AlertDialog.Builder(MainActivity.this);
        altDialog.setIcon(R.drawable.ic_warning_black_24dp);
        altDialog.setTitle("提示");
        altDialog.setMessage("要启动测定程序吗？");
        altDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        statusMsg = "启动测定程序";
                        textViewStatus.setText(statusMsg);
                        isRun = true;
                        SysData.setIsRun(isRun);
                        ImageButton buttonStartup = (ImageButton) findViewById(R.id.startup);
                        buttonStartup.setImageResource(R.drawable.ic_stop_black_24dp);
                        Log.i("MainActivity", "状态="+statusMsg);
                    }
                });
        altDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        /*
                        statusMsg = "系统待机";
                        textViewStatus.setText(statusMsg);
                        isRun = false;
                        SysData.setIsRun(isRun);
                        Log.i("MainActivity", "状态="+statusMsg);
                        */
                    }
                });
        // 显示
        altDialog.show();
    }

    //按下停止按钮时显示对话框
    private void showStopDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder altDialog = new AlertDialog.Builder(MainActivity.this);
        altDialog.setIcon(R.drawable.ic_warning_black_24dp);
        altDialog.setTitle("提示");
        altDialog.setMessage("要停止测定程序吗？");
        altDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        statusMsg = "停止测定程序";
                        textViewStatus.setText(statusMsg);
                        isRun = false;
                        SysData.setIsRun(isRun);
                        ImageButton buttonStartup = (ImageButton) findViewById(R.id.startup);
                        buttonStartup.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        Log.i("MainActivity", "状态="+statusMsg);
                    }
                });
        altDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        /*
                        statusMsg = "系统待机";
                        textViewStatus.setText(statusMsg);
                        isRun = false;
                        SysData.setIsRun(isRun);
                        Log.i("MainActivity", "状态="+statusMsg);
                        */
                    }
                });
        // 显示
        altDialog.show();
    }

    //接收系统时间广播
    private BroadcastReceiver mTimeRefreshReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                setTime(getSystemTime());
            }
            if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
                setTime(getSystemTime());
            }
        }
    };

    //显示时间到页面
    private void setTime(CharSequence strDate){
        textViewDate.setText(strDate.toString());
    }

    //获取系统广播时间
    private CharSequence getSystemTime() {
        long sysTime = System.currentTimeMillis();
        return formatter.format(sysTime);
    }

    private void gotoPage(){
        /*
        //点击Home按钮
        ImageButton buttonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class) ;
                startActivity(intent) ;
            }
        });*/
        //点击Data按钮
        ImageButton buttonData = (ImageButton) findViewById(R.id.imageButtonData);
        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DataActivity.class) ;
                startActivity(intent) ;
            }
        });
        //点击Control按钮
        ImageButton buttonControl = (ImageButton) findViewById(R.id.imageButtonControl);
        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ControlActivity.class) ;
                startActivity(intent) ;
            }
        });
        //点击Setup按钮
        ImageButton buttonSetup = (ImageButton) findViewById(R.id.imageButtonSetup);
        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetupActivity.class) ;
                startActivity(intent) ;
            }
        });

    }

}
