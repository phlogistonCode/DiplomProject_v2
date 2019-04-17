package com.phlogiston.gauges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;

public class HalfGauge extends AbstractGauge {

    private float needleStart = 30;
    private float needleEnd = 150;
    private float currentAngle = 30;
    private float startAngle = 210;
    private float sweepAngle = 120;
    private Integer needleAngleNext;
    private Handler handler = new Handler();


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public HalfGauge(Context context) {
        super(context);
        init();
    }

    public HalfGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HalfGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HalfGauge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //add BG Shadow
        getGaugeBackGround().setStrokeWidth(100f);
        getGaugeBackGround().setShadowLayer(15.0f, 0f, 5.0f, 0X50000000);
        setLayerType(LAYER_TYPE_SOFTWARE, getGaugeBackGround());

        //add Needle Shadow
        getNeedlePaint().setShadowLayer(10.f, 0f, 5.0f, 0X50000000);
        setLayerType(LAYER_TYPE_SOFTWARE, getNeedlePaint());

        setPadding(20f);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null)
            return;
        canvas.save();
        canvas.translate((getWidth() / 2f) - ((getRectRight() / 2f) * getScaleRatio()), getHeight() / 2f - 50f * getScaleRatio());
        canvas.scale(getScaleRatio(), getScaleRatio());
        canvas.drawArc(getRectF(), startAngle, sweepAngle, false, getGaugeBackGround());
        drawRange(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate((getWidth() / 2f) - ((getRectRight() / 2f) * getScaleRatio()), getHeight() / 2f - 50f * getScaleRatio());
        canvas.scale(getScaleRatio(), getScaleRatio());
        canvas.rotate(getNeedleAngle(), getRectRight() / 2f, getRectBottom() / 2f);
        canvas.drawLine(-30f, 400f / 2f, 400f / 2f, 400f / 2f, getNeedlePaint());
        canvas.drawOval(190f, 190f, 210f, 210f, getNeedlePaint());
        canvas.restore();


        canvas.save();
        canvas.translate((getWidth() / 2f) - ((getRectRight() / 2f) * getScaleRatio()), getHeight() / 2f - 50f * getScaleRatio());
        canvas.scale(getScaleRatio(), getScaleRatio());
        canvas.drawText(getValue() + "", 200f, 240f, getTextPaint());
        canvas.restore();


        //draw Text Value
        drawValueText(canvas);
        //drawMinValue
        drawMinValue(canvas);
        //drawMaxValue
        drawMaxValue(canvas);
    }

    private void drawValueText(Canvas canvas) {
        canvas.save();
        canvas.translate((getWidth() / 2f) - ((getRectRight() / 2f) * getScaleRatio()), getHeight() / 2f - 50f * getScaleRatio());
        canvas.scale(getScaleRatio(), getScaleRatio());
        canvas.drawText(getValue() + "", 200f, 240f, getTextPaint());
        canvas.restore();
    }

    private void drawMinValue(Canvas canvas) {
        canvas.save();
        canvas.translate((getWidth() / 2f) - ((getRectRight() / 2f) * getScaleRatio()), getHeight() / 2f - 50f * getScaleRatio());
        canvas.scale(getScaleRatio(), getScaleRatio());
        canvas.rotate(26, 10f, 130f);
        canvas.drawText(getMinValue() + "", 10f+getPadding(), 130f, getRangeValue());
        canvas.restore();
    }

    private void drawMaxValue(Canvas canvas) {
        canvas.save();
        canvas.translate((getWidth() / 2f) - ((getRectRight() / 2f) * getScaleRatio()), getHeight() / 2f - 50f * getScaleRatio());
        canvas.scale(getScaleRatio(), getScaleRatio());
        canvas.rotate(-26, 390f, 130f);
        canvas.drawText(getMaxValue() + "", 390f-getPadding(), 130f, getRangeValue());
        canvas.restore();
    }

    private void drawRange(Canvas canvas) {
        for (Range range : getRanges()) {
            float startAngle = calculateStartAngle(range.getFrom());
            float sweepAngle = calculateSweepAngle(range.getFrom(), range.getTo());
            canvas.drawArc(getRectF(), startAngle, sweepAngle, false, getRangePaint(range.getColor()));
        }
    }

    private float calculateStartAngle(double from) {
        return sweepAngle / 100 * getCalculateValuePercentage(from) + startAngle;
    }

    private float calculateSweepAngle(double from, double to) {
        return sweepAngle / 100 * getCalculateValuePercentage(to) - sweepAngle / 100 * getCalculateValuePercentage(from) + 0.5f;

    }

    public int getNeedleAngle() {
        if (needleAngleNext != null) {
            if (needleAngleNext != currentAngle) {
                if (needleAngleNext < currentAngle)
                    currentAngle--;
                else
                    currentAngle++;
                handler.postDelayed(runnable, 5);
            }
        } else {
            currentAngle = (needleEnd - needleStart) / 100 * getCalculateValuePercentage() + needleStart;
        }

        return (int) currentAngle;
    }

    @Override
    public void setValue(double value) {
        super.setValue(value);
        needleAngleNext = (int) ((needleEnd - needleStart) / 100 * getCalculateValuePercentage() + needleStart);
    }


    protected Paint getRangePaint(int color) {
        Paint range = new Paint();
        range.setColor(color);
        range.setAntiAlias(true);
        range.setStyle(Paint.Style.STROKE);
        range.setStrokeWidth(getGaugeBackGround().getStrokeWidth());
        return range;
    }

    protected Paint getRangeValue() {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.GRAY);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(15f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        return textPaint;
    }

}
