package com.example.robin.codsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

public class ControlActivity extends Activity {

    //设置输入输出引脚
    private static final String GPIO_OUT_D2 = "BCM17";  //D2进样阀开关量输出
    private Gpio mGpioOutD2;
    private static final String GPIO_OUT_D3 = "BCM18";  //D3排空阀开关量输出
    private Gpio mGpioOutD3;
    private static final String GPIO_OUT_D4 = "BCM27";  //D4微量泵开关量输出
    private Gpio mGpioOutD4;
    private static final String GPIO_OUT_D5 = "BCM22";  //D7加热开关量输出
    private Gpio mGpioOutD5;
    private static final String GPIO_OUT_D7 = "BCM24";  //D7加热开关量输出
    private Gpio mGpioOutD7;
    private static final String GPIO_OUT_D8 = "BCM21";  //D8LED灯开关量输出
    private Gpio mGpioOutD8;
    private static final String GPIO_OUT_M1 = "BCM20";  //M1步进电机启动
    private Gpio mGpioOutM1;
    private static final String GPIO_OUT_M2 = "BCM16";  //M1步进电机启动
    private Gpio mGpioOutM2;

    private static final int PUMP_D4 = 114;
    private static final int PUMP_S1 = 121;
    private static final int PUMP_S2 = 122;
    private static final int PUMP_S3 = 123;
    private static final int PUMP_S4 = 124;
    private static final int PUMP_S6 = 126;

    private static final int PUMP_STAR = 101;   //线程间通信信息，微量泵启动
    private static final int PUMP_STOP = 102;   //线程间通信信息，微量泵停止

    Switch switchName;
    Switch switchD1, switchD2, switchD3, switchD4, switchD5, switchD6, switchD7, switchD8;
    Switch switchS1, switchS2, switchS3, switchS4, switchS5, switchS6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        gotoPage();

        PeripheralManager manager = PeripheralManager.getInstance();
        try {
            mGpioOutD2 = manager.openGpio(GPIO_OUT_D2);
            mGpioOutD2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);  //初始化为高电平，低电平输出开关量
            mGpioOutD3 = manager.openGpio(GPIO_OUT_D3);
            mGpioOutD3.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);  //初始化为高电平，低电平输出开关量
            mGpioOutD4 = manager.openGpio(GPIO_OUT_D4);
            mGpioOutD4.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);  //初始化为高电平，低电平输出开关量
            mGpioOutD5 = manager.openGpio(GPIO_OUT_D5);
            mGpioOutD5.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);  //初始化为高电平，低电平输出开关量
            mGpioOutD7 = manager.openGpio(GPIO_OUT_D7);
            mGpioOutD7.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);  //初始化为高电平，低电平输出开关量
            mGpioOutD8 = manager.openGpio(GPIO_OUT_D8);
            mGpioOutD8.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);  //初始化为低电平，高电平输出开关量，电路板错误。
            mGpioOutM1 = manager.openGpio(GPIO_OUT_M1);
            mGpioOutM1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);  //初始化为高电平，低电平输出开关量
            mGpioOutM2 = manager.openGpio(GPIO_OUT_M2);
            mGpioOutM2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);  //初始化为高电平，低电平输出开关量

        } catch (IOException e) {
            e.printStackTrace();
        }

        //开关按钮状态改变时，输出D2引脚.
        switchD2 = (Switch) findViewById(R.id.SwitchD2);
        switchD2.setChecked(false);
        switchD2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                try {
                    mGpioOutD2.setValue(!isChecked); //输出板低电压驱动
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //开关按钮状态改变时，输出D3引脚.
        switchD3 = (Switch) findViewById(R.id.SwitchD3);
        switchD3.setChecked(false);
        switchD3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                try {
                    mGpioOutD3.setValue(!isChecked); //输出板低电压驱动
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //开关按钮状态改变时，输出D4引脚.
        switchD4 = (Switch) findViewById(R.id.SwitchD4);
        switchD4.setChecked(false);
        switchD4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked) {
                    PumpRunnable myThread = new PumpRunnable();
                    myThread.setName(PUMP_D4);
                    myThread.setNum(94);
                    Thread thread = new Thread(myThread);
                    thread.start();
                }
            }
        });

        //开关按钮状态改变时，输出D5引脚.
        switchD5 = (Switch) findViewById(R.id.SwitchD5);
        switchD5.setChecked(false);
        switchD5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                try {
                    mGpioOutD5.setValue(!isChecked); //输出板低电压驱动
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //开关按钮状态改变时，输出D8引脚.
        switchD8 = (Switch) findViewById(R.id.SwitchD8);
        switchD8.setChecked(false);
        switchD8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                try {
                    mGpioOutD8.setValue(isChecked); //输出板低电压驱动
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //进水样
        switchS1 = (Switch) findViewById(R.id.SwitchS1);
        switchS1.setChecked(false);
        switchS1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked) {
                    MotorRunnable myThread = new MotorRunnable();
                    myThread.setName(PUMP_S1);
                    myThread.setT(5);
                    myThread.setNum(100000);
                    Thread thread = new Thread(myThread);
                    thread.start();
                }
            }
        });

        //加硫酸试剂
        switchS2 = (Switch) findViewById(R.id.SwitchS2);
        switchS2.setChecked(false);
        switchS2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked) {
                    PumpRunnable myThread = new PumpRunnable();
                    myThread.setName(PUMP_S2);
                    myThread.setNum(94);
                    Thread thread = new Thread(myThread);
                    thread.start();
                }
            }
        });

        //加高锰酸钾试剂
        switchS3 = (Switch) findViewById(R.id.SwitchS3);
        switchS3.setChecked(false);
        switchS3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked) {
                    PumpRunnable myThread = new PumpRunnable();
                    myThread.setName(PUMP_S3);
                    myThread.setNum(188);
                    Thread thread = new Thread(myThread);
                    thread.start();
                }
            }
        });

        //加草酸钠试剂
        switchS4 = (Switch) findViewById(R.id.SwitchS4);
        switchS4.setChecked(false);
        switchS4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked) {
                    PumpRunnable myThread = new PumpRunnable();
                    myThread.setName(PUMP_S4);
                    myThread.setNum(188);
                    Thread thread = new Thread(myThread);
                    thread.start();
                }
            }
        });

        //消解
        switchS5 = (Switch) findViewById(R.id.SwitchS5);
        switchS5.setChecked(false);
        switchS5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked) {
                    //pumpNum = 94;
                    //new Thread(new PumpRunnable()).start();
                }
            }
        });

        //滴定
        switchS6 = (Switch) findViewById(R.id.SwitchS6);
        switchS6.setChecked(false);
        switchS6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked) {
                    PumpRunnable myThread = new PumpRunnable();
                    myThread.setName(PUMP_S6);
                    myThread.setNum(400);
                    Thread thread = new Thread(myThread);
                    thread.start();
                }
            }
        });

    }



    private void gotoPage(){

        //点击Home按钮
        ImageButton buttonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlActivity.this, MainActivity.class) ;
                startActivity(intent) ;
            }
        });
        //点击Data按钮
        ImageButton buttonData = (ImageButton) findViewById(R.id.imageButtonData);
        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlActivity.this, DataActivity.class) ;
                startActivity(intent) ;
            }
        });
        /*//点击Control按钮
        ImageButton buttonControl = (ImageButton) findViewById(R.id.imageButtonControl);
        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlActivity.this, ControlActivity.class) ;
                startActivity(intent) ;
            }
        });*/
        //点击Setup按钮
        ImageButton buttonSetup = (ImageButton) findViewById(R.id.imageButtonSetup);
        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlActivity.this, SetupActivity.class) ;
                startActivity(intent) ;
            }
        });

    }

    //微量泵状态处理信息
    @SuppressLint("HandlerLeak")
    private Handler pumpHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PUMP_D4:
                    switchName = switchD4;
                    break;
                case PUMP_S1:
                    switchName = switchS1;
                    break;
                case PUMP_S2:
                    switchName = switchS2;
                    break;
                case PUMP_S3:
                    switchName = switchS3;
                    break;
                case PUMP_S4:
                    switchName = switchS4;
                    break;
                case PUMP_S6:
                    switchName = switchS6;
                    break;
                case PUMP_STAR:
                    switchName.setChecked(true);
                    break;
                case PUMP_STOP:
                    switchName.setChecked(false);
                    break;
            }
        }
    };

    //微量泵加液线程
    private class PumpRunnable implements Runnable{

        private int id;
        public void setName(int id)
        {
            this.id = id;
        }

        private int num;
        public void setNum(int num)
        {
            this.num = num;
        }

        public void run() {
            try {
                pumpHandler.sendEmptyMessage(id);
                pumpHandler.sendEmptyMessage(PUMP_STAR);
                for (int i = 0; i < num; i++) {
                    mGpioOutD4.setValue(false);
                    Thread.sleep(150); // 休眠1秒
                    mGpioOutD4.setValue(true);
                    Thread.sleep(350); // 休眠1秒
                }
                pumpHandler.sendEmptyMessage(PUMP_STOP);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    //微量泵加液线程
    private class MotorRunnable implements Runnable{

        private int id;
        public void setName(int id)
        {
            this.id = id;
        }

        private int t;
        public void setT(int t)
        {
            this.t = t;
        }

        public void setNum(int num)
        {
            this.num = num;
        }

        private int num;
        public void run() {
            try {
                pumpHandler.sendEmptyMessage(id);
                pumpHandler.sendEmptyMessage(PUMP_STAR);

                //mGpioOutD7.setValue(true);
                for (int i = 0; i < num; i++) {
                    mGpioOutM1.setValue(false);
                    mGpioOutM2.setValue(false);
                    Thread.sleep(t); // 保持时间
                    mGpioOutM1.setValue(true);
                    mGpioOutM2.setValue(true);
                    Thread.sleep(t); // 保持时间
                }
                //mGpioOutD7.setValue(false);
                pumpHandler.sendEmptyMessage(PUMP_STOP);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

    }
}



