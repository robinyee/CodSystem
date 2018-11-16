package com.example.robin.codsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class DataActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        gotoPage();

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
        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(0, 1));
        values.add(new PointValue(1, 3));
        values.add(new PointValue(2, 2));
        values.add(new PointValue(3, 4));

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        LineChartView chart = (LineChartView)findViewById(R.id.chart);
        chart.setLineChartData(data);
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
}
