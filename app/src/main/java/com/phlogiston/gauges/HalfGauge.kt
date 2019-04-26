package com.phlogiston.gauges

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet

internal class HalfGauge : AbstractGauge {

    private val needleStart = 30f
    private val needleEnd = 150f
    private val startAngle = 210f
    private val sweepAngle = 120f

    private val currentNeedle: Float
        get() {
            return needleStart + (needleEnd - needleStart) / 100 * calculateValuePercentage
        }

    private val rangeValue: Paint
        get() {
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            textPaint.color = Color.GRAY
            textPaint.style = Paint.Style.FILL
            textPaint.textSize = 15f
            textPaint.textAlign = Paint.Align.CENTER
            return textPaint
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        //add BG Shadow
        getGaugeBackground().strokeWidth = 100f
        setLayerType(LAYER_TYPE_SOFTWARE, getGaugeBackground())
        //getGaugeBackground().setShadowLayer(15.0f, 0f, 5.0f, 0X50000000)

        //add Needle Shadow
        needlePaint.setShadowLayer(10f, 0f, 5.0f, 0X50000000)
        setLayerType(LAYER_TYPE_SOFTWARE, needlePaint)

        padding = 20f
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Gauge
        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 50f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)
        canvas.drawArc(getRectF(), startAngle, sweepAngle, false, getGaugeBackground())
        drawRange(canvas)
        canvas.restore()

        // Needle
        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 50f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)
        canvas.rotate(currentNeedle, rectRight / 2f, rectBottom / 2f)
        canvas.drawLine(-30f, 400f / 2f, 400f / 2f, 400f / 2f, needlePaint)
        canvas.drawOval(190f, 190f, 210f, 210f, needlePaint)
        canvas.restore()

        //draw Text Value
        drawValueText(canvas)
        //drawMinValue
        drawMinValue(canvas)
        //drawMaxValue
        drawMaxValue(canvas)
    }

    private fun drawValueText(canvas: Canvas) {
        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 50f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)
        canvas.drawText(value.toString() + "", 200f, 240f, getTextPaint())
        canvas.restore()
    }

    private fun drawMinValue(canvas: Canvas) {
        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 50f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)
        canvas.rotate(26f, 10f, 130f)
        canvas.drawText(minValue.toString() + "", 10f + padding, 130f, rangeValue)
        canvas.restore()
    }

    private fun drawMaxValue(canvas: Canvas) {
        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 50f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)
        canvas.rotate(-26f, 390f, 130f)
        canvas.drawText(maxValue.toString() + "", 390f - padding, 130f, rangeValue)
        canvas.restore()
    }

    private fun drawRange(canvas: Canvas) {
        for (range in getRanges()) {
            val startAngle = calculateStartAngle(range.from)
            val sweepAngle = calculateSweepAngle(range.from, range.to)
            canvas.drawArc(getRectF(), startAngle, sweepAngle, false, getRangePaint(range.color))
        }
    }

    private fun calculateStartAngle(from: Float): Float {
        return sweepAngle / 100 * getCalculateValuePercentage(from) + startAngle
    }

    private fun calculateSweepAngle(from: Float, to: Float): Float {
        return sweepAngle / 100 * getCalculateValuePercentage(to) - sweepAngle / 100 * getCalculateValuePercentage(from) + 0.5f

    }

    private fun getRangePaint(color: Int): Paint {
        val range = Paint()
        range.color = color
        range.isAntiAlias = true
        range.style = Paint.Style.STROKE
        range.strokeWidth = getGaugeBackground().strokeWidth
        return range
    }

}
