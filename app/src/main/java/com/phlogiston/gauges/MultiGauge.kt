package com.phlogiston.gauges

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import java.util.*

internal class MultiGauge : AbstractGauge {

    private val distance = 30f
    private val sweepAngle = 360f
    private val startAngle = 270f
    private val gaugeBGWidth = 20f
    var secondValue = 0.0f
        set(secondValue) {
            field = secondValue
            invalidate()
        }
    var thirdValue = 0.0f
        set(thirdValue) {
            field = thirdValue
            invalidate()
        }
    var secondMinValue = 0.0f
    var thirdMinValue = 0.0f
    var secondMaxValue = 100.0f
    var thirdMaxValue = 100.0f

    private var secondRanges: MutableList<Range> = ArrayList()
    private var thirdRanges: MutableList<Range> = ArrayList()


    private val secondRect: RectF
        get() = RectF(rectLeft + padding + distance, rectTop + padding + distance, rectRight - padding - distance, rectBottom - padding - distance)

    private val thirdRect: RectF
        get() = RectF(rectLeft + padding + distance * 2, rectTop + padding + distance * 2, rectRight - padding - distance * 2, rectBottom - padding - distance * 2)

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
        getGaugeBackground().color = Color.parseColor("#EAEAEA")
        getTextPaint().textSize = 35f
        padding = 20f
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 200f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)
        canvas.drawArc(getRectF(), startAngle, sweepAngle, false, getGaugeBackground())
        canvas.drawArc(secondRect, startAngle, sweepAngle, false, getGaugeBackground())
        canvas.drawArc(thirdRect, startAngle, sweepAngle, false, getGaugeBackground())
        canvas.restore()

        //draw gauges Ranges
        canvas.save()
        canvas.translate(width / 2f - rectRight / 2f * scaleRatio!!, height / 2f - 200f * scaleRatio!!)
        canvas.scale(scaleRatio!!, scaleRatio!!)

        drawValueRange(canvas, getRectF(), minValue, maxValue, value, getRanges())
        drawValueRange(canvas, secondRect, secondMinValue, secondMaxValue, secondValue, getSecondRanges())
        drawValueRange(canvas, thirdRect, thirdMinValue, thirdMaxValue, thirdValue, getThirdRanges())

        canvas.restore()


    }


    private fun getRangePaint(value: Float, ranges: List<Range>): Paint {
        val color = Paint(Paint.ANTI_ALIAS_FLAG)
        color.strokeWidth = gaugeBGWidth
        color.style = Paint.Style.STROKE
        color.color = getGaugeBackground().color
        color.strokeCap = Paint.Cap.ROUND

        for (range in ranges) {
            if (range.to <= value)
                color.color = range.color

            if (range.from <= value && range.to >= value)
                color.color = range.color
        }
        return color
    }

    private fun drawValueRange(canvas: Canvas, rectF: RectF, minValue: Float, maxValue: Float, value: Float, ranges: List<Range>) {
        val sweepAngle = calculateSweepAngle(minValue, maxValue, value)
        canvas.drawArc(rectF, startAngle, sweepAngle, false, getRangePaint(value, ranges))
    }

    private fun calculateSweepAngle(minValue: Float, maxValue: Float, to: Float): Float {
        val valuePer = getCalculateValuePercentage(minValue, maxValue, to).toFloat()
        return sweepAngle / 100 * valuePer
    }

    fun addSecondRange(range: Range) {
        this.secondRanges.add(range)
    }

    fun addThirdRange(range: Range) {
        this.thirdRanges.add(range)
    }

    private fun getSecondRanges(): List<Range> {
        return secondRanges
    }

    fun setSeconRanges(secondRanges: MutableList<Range>) {
        this.secondRanges = secondRanges
    }

    private fun getThirdRanges(): List<Range> {
        return thirdRanges
    }

    fun setThirdRanges(thirdRanges: MutableList<Range>) {
        this.thirdRanges = thirdRanges
    }
}
