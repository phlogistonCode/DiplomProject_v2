package com.phlogiston.gauges

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet

internal open class FullGauge : AbstractGauge {

    protected var isDrawSectorLines: Boolean = true
    protected var sweepAngle = 360f
    protected var startAngle = 270f
    private var gaugeBGWidth = 40f
    protected var sectorLineLength = 60f
    protected var sectorLineWidth = 2f

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
        getGaugeBackground().strokeWidth = gaugeBGWidth
        getTextPaint().textSize = 35f
        padding = 20f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 200f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)
        canvas.drawArc(getRectF(), startAngle, sweepAngle, false, getGaugeBackground())
        drawValueRange(canvas)
        canvas.restore()

        //drawText
        drawValueText(canvas)
        if (isDrawSectorLines) drawSectorLines(canvas)
    }


    private fun drawValueText(canvas: Canvas) {
        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 220f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)
        canvas.drawText(value.toString() + "", 200f, 240f, getTextPaint())
        canvas.restore()
    }

    private fun drawSectorLines(canvas: Canvas) {
        for (range in getRanges()) {
            if (range!=getRanges().last()) {
                canvas.save()
                canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 200f * scaleRatio!!)
                canvas.scale(scaleRatio!!, scaleRatio!!)
                canvas.drawArc(getRectF(), calculatePosition(range), sectorLineWidth, false, getSectorLineColor())
                canvas.restore()
            }
        }
    }

    private fun getSectorLineColor(): Paint {
        val color = Paint(Paint.ANTI_ALIAS_FLAG)
        color.strokeWidth = sectorLineLength
        color.style = Paint.Style.STROKE
        color.color = Color.parseColor("#000000")

        return color
    }

    private fun getRangePaint(value: Float): Paint {
        val color = Paint(Paint.ANTI_ALIAS_FLAG)
        color.strokeWidth = gaugeBGWidth
        color.style = Paint.Style.STROKE
        color.color = getGaugeBackground().color
        //color.strokeCap = Paint.Cap.ROUND

        for (range in getRanges()) {
            if (range.to <= value)
                color.color = range.color

            if (range.from <= value && range.to >= value)
                color.color = range.color
        }
        return color
    }

    private fun drawValueRange(canvas: Canvas) {
        val sweepLength = calculateSweepLength(value)
        canvas.drawArc(getRectF(), startAngle, sweepLength, false, getRangePaint(value))
    }

    private fun calculatePosition(range: Range) : Float {
        return startAngle + (sweepAngle * range.to / maxValue)
    }

    private fun calculateSweepLength(value: Float) : Float {
        return sweepAngle * value / maxValue
    }
}
