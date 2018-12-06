package com.example.robin.codsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ControlActivity extends Activity {

    //设置输入输出引脚
    private static final String GPIO_OUT_D1 = "BCM17";
    private Gpio mGpioOutD1;

    /*
    //输入和输出GPIO引脚名称
    private static final String GPIO_IN_NAME = "BCM21";
    private static final String GPIO_OUT_NAME = "BCM5";

    //输入和输出Gpio
    private Gpio mGpioIn;
    private Gpio mGpioOut;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        gotoPage();

        PeripheralManager manager = PeripheralManager.getInstance();
        try {
            mGpioOutD1 = manager.openGpio(GPIO_OUT_D1);
            mGpioOutD1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //开关按钮状态改变时，输出D1引脚.
        Switch switchD1 = (Switch) findViewById(R.id.SwitchD1);
        switchD1.setChecked(true);
        switchD1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                try {
                    mGpioOutD1.setValue(isChecked);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        /*
        PeripheralManager manager = PeripheralManager.getInstance();
        List<String> portList = manager.getGpioList();
        if (portList.isEmpty()) {
            Log.i(TAG, "这个设备没有GPIO端口可用");
        } else {
            Log.i(TAG, "可用端口：" + portList);
        }

        TextView gpioMsg = (TextView) findViewById(R.id.gpioList);
        gpioMsg.setText("可用GPIO端口："+portList);
        */
        /*
        PeripheralManager manager = PeripheralManager.getInstance();
        try {
            //打开并设置输入Gpio，监听输入信号变化（开关按钮的开关）
            mGpioIn = manager.openGpio(GPIO_IN_NAME);
            mGpioIn.setDirection(Gpio.DIRECTION_IN);
            mGpioIn.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mGpioIn.setActiveType(Gpio.ACTIVE_HIGH);
            mGpioIn.registerGpioCallback(mGpioCallback);

            //打开并设置输出Gpio
            mGpioOut = manager.openGpio(GPIO_OUT_NAME);
            mGpioOut.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }


    /*
    private GpioCallback mGpioCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try {
                //当按开关按钮的时候，改变输出Gpio的信号，从而控制LED灯的亮和灭
                mGpioOutD1.setValue(!mGpioOutD1.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
        }
    };
    */

    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭Gpio
        if (mGpioIn != null) {
            try {
                mGpioIn.unregisterGpioCallback(mGpioCallback);
                mGpioIn.close();
                mGpioIn = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mGpioOut != null) {
            try {
                mGpioOut.close();
                mGpioOut = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    */

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
}
