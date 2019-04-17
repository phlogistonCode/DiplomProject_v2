package com.phlogiston.gauges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class MultiGauge extends AbstractGauge {

    private float distance = 30f;
    private float sweepAngle = 360;
    private float startAngle = 270;
    private float gaugeBGWidth = 20f;
    private double secondValue = 0;
    private double thirdValue = 0;
    private double secondMinValue = 0;
    private double thirdMinValue = 0;
    private double secondMaxValue = 100;
    private double thirdMaxValue = 100;

    private List<Range> secondRanges = new ArrayList<>();
    private List<Range> thirdRanges = new ArrayList<>();


    private RectF getSecondRect() {
        return new RectF(getRectLeft() + getPadding() + distance, getRectTop() + getPadding() + distance, getRectRight() - getPadding() - distance, getRectBottom() - getPadding() - distance);
    }

    private RectF getThirdRect() {
        return new RectF(getRectLeft() + getPadding() + distance * 2, getRectTop() + getPadding() + distance * 2, getRectRight() - getPadding() - distance * 2, getRectBottom() - getPadding() - distance * 2);
    }

    public MultiGauge(Context context) {
        super(context);
        init();
    }

    public MultiGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MultiGauge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        getGaugeBackGround().setStrokeWidth(gaugeBGWidth);
        getGaugeBackGround().setColor(Color.parseColor("#EAEAEA"));
        getTextPaint().setTextSize(35f);
        setPadding(20f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate((getWidth() / 2f) - ((getRectRight() / 2f) * getScaleRatio()), (getHeight() / 2f) - 200f * getScaleRatio());
        canvas.scale(getScaleRatio(), getScaleRatio());
        canvas.drawArc(getRectF(), startAngle, sweepAngle, false, getGaugeBackGround());
        canvas.drawArc(getSecondRect(), startAngle, sweepAngle, false, getGaugeBackGround());
        canvas.drawArc(getThirdRect(), startAngle, sweepAngle, false, getGaugeBackGround());
        canvas.restore();

        //draw gauges Ranges
        canvas.save();
        canvas.translate((getWidth() / 2f) - ((getRectRight() / 2f) * getScaleRatio()), (getHeight() / 2f) - 200f * getScaleRatio());
        canvas.scale(getScaleRatio(), getScaleRatio());

        drawValueRange(canvas, getRectF(), getMinValue(), getMaxValue(), getValue(), getRanges());
        drawValueRange(canvas, getSecondRect(), getSecondMinValue(), getSecondMaxValue(), getSecondValue(), getSecondRanges());
        drawValueRange(canvas, getThirdRect(), getThirdMinValue(), getThirdMaxValue(), getThirdValue(), getThirdRanges());

        canvas.restore();


    }


    private Paint getRangePaint(double value, List<Range> ranges) {
        Paint color = new Paint(Paint.ANTI_ALIAS_FLAG);
        color.setStrokeWidth(gaugeBGWidth);
        color.setStyle(Paint.Style.STROKE);
        color.setColor(getGaugeBackGround().getColor());
        color.setStrokeCap(Paint.Cap.ROUND);

        for (Range range : ranges) {
            if (range.getTo() <= value)
                color.setColor(range.getColor());

            if (range.getFrom() <= value && range.getTo() >= value)
                color.setColor(range.getColor());
        }
        return color;
    }

    private void drawValueRange(Canvas canvas, RectF rectF, double minValue, double maxValue, double value, List<Range> ranges) {
        float sweepAngle = calculateSweepAngle(minValue, maxValue, value);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, getRangePaint(value, ranges));
    }

    private float calculateSweepAngle(double minValue, double maxValue, double to) {
        float valuePer = getCalculateValuePercentage(minValue, maxValue, to);
        return sweepAngle / 100 * valuePer;
    }

    public double getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(double secondValue) {
        this.secondValue = secondValue;
        invalidate();
    }

    public double getThirdValue() {
        return thirdValue;
    }

    public void setThirdValue(double thirdValue) {
        this.thirdValue = thirdValue;
        invalidate();
    }

    public double getSecondMinValue() {
        return secondMinValue;
    }

    public void setSecondMinValue(double secondMinValue) {
        this.secondMinValue = secondMinValue;
    }

    public double getThirdMinValue() {
        return thirdMinValue;
    }

    public void setThirdMinValue(double thirdMinValue) {
        this.thirdMinValue = thirdMinValue;
    }

    public double getSecondMaxValue() {
        return secondMaxValue;
    }

    public void setSecondMaxValue(double secondMaxValue) {
        this.secondMaxValue = secondMaxValue;
    }

    public double getThirdMaxValue() {
        return thirdMaxValue;
    }

    public void setThirdMaxValue(double thirdMaxValue) {
        this.thirdMaxValue = thirdMaxValue;
    }

    public void addSecondRange(Range range) {
        this.secondRanges.add(range);
    }

    public void addThirdRange(Range range) {
        this.thirdRanges.add(range);
    }

    public List<Range> getSecondRanges() {
        return secondRanges;
    }

    public void setSeconRanges(List<Range> secondRanges) {
        this.secondRanges = secondRanges;
    }

    public List<Range> getThirdRanges() {
        return thirdRanges;
    }

    public void setThirdRanges(List<Range> thirdRanges) {
        this.thirdRanges = thirdRanges;
    }
}
