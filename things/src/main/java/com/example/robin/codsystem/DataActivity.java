package com.example.robin.codsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class DataActivity extends Activity {
    private LineChartView mChartView;
    private List<PointValue> values;
    private List<Line> lines;
    private LineChartData lineChartData;
    private LineChartView lineChartView;
    private List<Line> linesList;
    private List<PointValue> pointValueList;
    private List<PointValue> points;
    private int position = 0;
    private Timer timer;
    private boolean isFinish = true;
    private Axis axisY, axisX;
    private Random random = new Random();
    private boolean hasLabelForSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        gotoPage();

        mChartView = (LineChartView) findViewById(R.id.chart);

        initView();
        timer = new Timer();


        /*
        //LineChartView chart = (LineChartView)findViewById(R.id.chart);
        //LineChartView chart = new LineChartView(context);
        //layout.addView(chart);
        Chart.setInteractive(boolean isInteractive);
        Chart.setZoomType(ZoomType zoomType);
        Chart.setContainerScrollEnabled(boolean isEnabled, ContainerScrollType type);
        ChartData.setAxisXBottom(Axis axisX);
        ColumnChartData.setStacked(boolean isStacked);
        Line.setStrokeWidth(int strokeWidthDp);
        */
        /*
        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(0, 1));
        values.add(new PointValue(1, 3));
        values.add(new PointValue(2, 2));
        values.add(new PointValue(3, 4));
        values.add(new PointValue(4, 1));
        values.add(new PointValue(5, 3));
        values.add(new PointValue(6, 2));
        values.add(new PointValue(7, 4));
        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.DKGRAY).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        LineChartView chart = (LineChartView)findViewById(R.id.chart);
        chart.setLineChartData(data);
        */
    }

    private void gotoPage(){

        //点击Home按钮
        ImageButton buttonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataActivity.this, MainActivity.class) ;
                startActivity(intent) ;
            }
        });
        /*//点击Data按钮
        ImageButton buttonData = (ImageButton) findViewById(R.id.imageButtonData);
        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataActivity.this, DataActivity.class) ;
                startActivity(intent) ;
            }
        });*/
        //点击Control按钮
        ImageButton buttonControl = (ImageButton) findViewById(R.id.imageButtonControl);
        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataActivity.this, ControlActivity.class) ;
                startActivity(intent) ;
            }
        });
        //点击Setup按钮
        ImageButton buttonSetup = (ImageButton) findViewById(R.id.imageButtonSetup);
        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataActivity.this, SetupActivity.class) ;
                startActivity(intent) ;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时添加新的点
                PointValue value1 = new PointValue(position * 1, random.nextInt(10) + 1);
                value1.setLabel("00:00");
                pointValueList.add(value1);

                float x = value1.getX();
                //根据新的点的集合画出新的线
                Line line = new Line(pointValueList);
                line.setColor(Color.GREEN);
                line.setShape(ValueShape.CIRCLE);
                line.setCubic(true);//曲线是否平滑，即是曲线还是折线
                line.setHasLabelsOnlyForSelected(hasLabelForSelected); //设置数据点可以选择

                linesList.clear();
                linesList.add(line);
                lineChartData = initDatas(linesList);
                lineChartView.setLineChartData(lineChartData);

                //根据点的横坐实时变幻坐标的视图范围
                Viewport port;
                if (x > 30) {
                    port = initViewPort(x - 30, x);
                } else {
                    port = initViewPort(0, 30);
                }
                lineChartView.setCurrentViewport(port);//当前窗口

                Viewport maPort = initMaxViewPort(x);
                lineChartView.setMaximumViewport(maPort);//最大窗口
                position++;

                lineChartView.setOnValueTouchListener(new ValueTouchListener());
            }
        }, 300, 10000);
    }

    private void initView() {
        lineChartView = (LineChartView) findViewById(R.id.chart);
        pointValueList = new ArrayList<>();
        linesList = new ArrayList<>();

        //初始化坐标轴
        axisY = new Axis();
        //添加坐标轴的名称
        axisY.setLineColor(Color.parseColor("#aab2bd"));
        axisY.setTextColor(Color.parseColor("#aab2bd"));
        axisX = new Axis();
        axisX.setLineColor(Color.parseColor("#aab2bd"));
        lineChartData = initDatas(null);
        lineChartView.setLineChartData(lineChartData);

        Viewport port = initViewPort(0, 30);
        lineChartView.setCurrentViewportWithAnimation(port);
        lineChartView.setInteractive(false);
        lineChartView.setScrollEnabled(true);
        lineChartView.setValueTouchEnabled(true);
        lineChartView.setFocusableInTouchMode(true);
        lineChartView.setViewportCalculationEnabled(false);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.startDataAnimation();

        lineChartView.setOnValueTouchListener(new ValueTouchListener());//为图表设置值得触摸事件
        lineChartView.setZoomEnabled(true);//设置是否支持缩放
        //lineChartView.setOnValueTouchListener(LineChartOnValueSelectListener touchListener);//为图表设置值得触摸事件
        lineChartView.setInteractive(true);//设置图表是否可以与用户互动
        lineChartView.setValueSelectionEnabled(true);//设置图表数据是否选中进行显示
        //lineChartView.setLineChartData(LineChartData data);//为图表设置数据，数据类型为LineChartData

        points = new ArrayList<>();
    }


    private LineChartData initDatas(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }

    /**
     * 当前显示区域
     *
     * @param left
     * @param right
     * @return
     */
    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 25;
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }

    /**
     * 最大显示区域
     *
     * @param right
     * @return
     */
    private Viewport initMaxViewPort(float right) {
        Viewport port = new Viewport();
        port.top = 25;
        port.bottom = 0;
        port.left = 0;
        port.right = right + 30;
        return port;
    }

    /**
     * 触摸监听类
     */

    private class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(DataActivity.this, "Selected: " + value, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "默认Toast样式", Toast.LENGTH_SHORT).show();
            /*
            Context context = getApplicationContext();
            CharSequence text = "Hello toast!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            */
        }
        @Override
        public void onValueDeselected() {

        }
    }

}
