package com.phlogiston.gauges;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class ArcGauge extends FullGauge {

    private float sweepAngle = 240;
    private float startAngle = 150;
    private float gaugeBGWidth = 20f;

    public ArcGauge(Context context) {
        super(context);
        init();
    }

    public ArcGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ArcGauge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getGaugeBackGround().setStrokeWidth(gaugeBGWidth);
        getGaugeBackGround().setStrokeCap(Paint.Cap.ROUND);
        getGaugeBackGround().setColor(Color.parseColor("#D6D6D6"));
        getTextPaint().setTextSize(35f);
        setPadding(20f);
        setSweepAngle(sweepAngle);
        setStartAngle(startAngle);
    }
}
