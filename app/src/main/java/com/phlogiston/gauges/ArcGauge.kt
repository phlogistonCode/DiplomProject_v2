package com.phlogiston.gauges

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet

internal class ArcGauge : FullGauge {

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
        getGaugeBackground().strokeWidth = 50f
        getGaugeBackground().color = Color.parseColor("#D6D6D6")
        //getGaugeBackground().strokeCap = Paint.Cap.ROUND
        getTextPaint().textSize = 35f
        padding = 45f
        startAngle = 157.5f
        sweepAngle = 225f
        isDrawSectorLines = true
    }
}
