package com.phlogiston.gauges

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import java.util.*

internal abstract class AbstractGauge : View {

    private var ranges: MutableList<Range> = ArrayList()

    var minValue = 0f
    var maxValue = 100f
    var padding = 0f
    private var needleColor: Paint? = null
    private var pointColor: Paint? = null
    private var gaugeBackground: Paint? = null
    private var textPaint: Paint? = null
    private var rectF: RectF? = null
    protected var rectTop = 0f
    protected var rectLeft = 0f
    protected var rectRight = 400f
    protected var rectBottom = 400f

    open var value = 0f
        set(value) {
            field = value
            invalidate()
        }

    protected val scaleRatio: Float?
        get() {
            val measuredHeight = measuredHeight
            val measuredWidth = measuredWidth
            val minSize = Math.min(measuredHeight, measuredWidth) / 1f
            val maxSize = Math.max(measuredHeight, measuredWidth) / 1f
            val f1 = minSize / 400f
            val f2 = minSize / 200f
            if (measuredWidth > measuredHeight) {
                if (f2 > f1)
                    return f1
            } else {
                return minSize / 400f

            }
            return maxSize / 400f
        }

    protected val needlePaint: Paint
        get() {
            if (needleColor == null) {
                needleColor = Paint()
                needleColor!!.color = Color.BLACK
                needleColor!!.isAntiAlias = true
                needleColor!!.style = Paint.Style.FILL_AND_STROKE
                needleColor!!.strokeWidth = 5f
            }
            return needleColor!!
        }

    protected val pointPaint: Paint
        get() {
            if (pointColor == null) {
                pointColor = Paint()
                pointColor!!.color = Color.BLACK
                pointColor!!.isAntiAlias = true
                pointColor!!.style = Paint.Style.FILL_AND_STROKE
                pointColor!!.strokeWidth = 5f
            }
            return pointColor!!
        }

    protected val calculateValuePercentage: Int
        get() = getCalculateValuePercentage(value)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        scaleRatio
    }

    protected fun getRectF(): RectF {
        if (rectF == null)
            rectF = RectF(rectLeft + padding, rectTop + padding, rectRight - padding, rectBottom - padding)
        return rectF as RectF
    }

    fun addRange(range: Range?) {
        if (range == null)
            return
        ranges.add(range)
    }

    fun getRanges(): List<Range> {
        return ranges
    }

    fun setRanges(ranges: MutableList<Range>) {
        this.ranges = ranges
    }

    protected fun getGaugeBackground(): Paint {
        if (gaugeBackground == null) {
            gaugeBackground = Paint()
            gaugeBackground!!.color = Color.parseColor("#EAEAEA")
            gaugeBackground!!.isAntiAlias = true
            gaugeBackground!!.style = Paint.Style.STROKE
            // gaugeBackground.setShadowLayer(15.0f,0f,5.0f,0X50000000);
            // setLayerType(LAYER_TYPE_SOFTWARE, gaugeBackground);
        }
        return gaugeBackground as Paint
    }

    protected fun getCalculateValuePercentage(value: Float): Int {
        return getCalculateValuePercentage(minValue, maxValue, value)
    }

    protected fun getCalculateValuePercentage(min: Float, max: Float, value: Float): Int {
        if (min >= value)
            return 0
        return if (max <= value) 100 else ((value - min) / (max - min) * 100).toInt()
    }

    protected fun getTextPaint(): Paint {
        if (textPaint == null) {
            textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            textPaint!!.color = Color.BLACK
            textPaint!!.style = Paint.Style.FILL
            textPaint!!.textSize = 25f
            textPaint!!.textAlign = Paint.Align.CENTER
        }
        return textPaint as Paint
    }

    fun setNeedleColor(color: Int) {
        needlePaint.color = color
    }
}
