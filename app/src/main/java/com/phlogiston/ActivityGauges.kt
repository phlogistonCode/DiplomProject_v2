package com.phlogiston

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import com.phlogiston.gauges.ArcGauge
import com.phlogiston.gauges.HalfGauge
import com.phlogiston.gauges.Range
import kotlinx.android.synthetic.main.activity_gauges.*

class ActivityGauges : Activity() {

    private val gaugeMaxValue = 35.0f
    private val gaugeMinValue = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gauges)

        initArcGauge(arcGauge1, seekBarArcGauge1)
        initArcGauge(arcGauge2, seekBarArcGauge2)
        initArcGauge(arcGauge4, seekBarArcGauge4)
        initArcGauge(arcGauge5, seekBarArcGauge5)

        initHalfGauge(halfGauge3, seekBarHalfGauge3)
    }

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            when (seekBar.id) {
                seekBarArcGauge1.id -> {
                    arcGauge1.value = gaugeMaxValue/100 * seekBarArcGauge1.progress
                }
                seekBarArcGauge2.id -> {
                    arcGauge2.value = gaugeMaxValue/100 * seekBarArcGauge2.progress
                }
                seekBarHalfGauge3.id -> {
                    halfGauge3.value = gaugeMaxValue/100 * seekBarHalfGauge3.progress
                }
                seekBarArcGauge4.id -> {
                    arcGauge4.value = gaugeMaxValue/100 * seekBarArcGauge4.progress
                }
                seekBarArcGauge5.id -> {
                    arcGauge5.value = gaugeMaxValue/100 * seekBarArcGauge5.progress
                }
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
        }
    }

    private fun initArcGauge(gauge : ArcGauge, seekBar : SeekBar) {

        // Желтый
        val range1 = Range()
        range1.color = Color.parseColor("#E3E500")
        range1.from = 0.0f
        range1.to = 7.0f

        // Зеленый
        val range2 = Range()
        range2.color = Color.parseColor("#00b20b")
        range2.from = 7.0f
        range2.to = 27.8f

        // Желтый
        val range3 = Range()
        range3.color = Color.parseColor("#E3E500")
        range3.from = 27.8f
        range3.to = 29.0f

        // Красный
        val range4 = Range()
        range4.color = Color.parseColor("#ce0000")
        range4.from = 29.0f
        range4.to = 35.0f

        //add color ranges to gauge
        gauge.addRange(range1)
        gauge.addRange(range2)
        gauge.addRange(range3)
        gauge.addRange(range4)

        //set min max and current value
        gauge.minValue = gaugeMinValue
        gauge.maxValue = gaugeMaxValue
        gauge.value = 0.0f

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    private fun initHalfGauge(gauge : HalfGauge, seekBar: SeekBar) {
        // Желтый
        val range1 = Range()
        range1.color = Color.parseColor("#E3E500")
        range1.from = 0.0f
        range1.to = 7.0f

        // Зеленый
        val range2 = Range()
        range2.color = Color.parseColor("#00b20b")
        range2.from = 7.0f
        range2.to = 27.8f

        // Желтый
        val range3 = Range()
        range3.color = Color.parseColor("#E3E500")
        range3.from = 27.8f
        range3.to = 35.0f

        gauge.addRange(range1)
        gauge.addRange(range2)
        gauge.addRange(range3)

        //set min max and current value
        gauge.minValue = gaugeMinValue
        gauge.maxValue = gaugeMaxValue
        gauge.value = 0.0f

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }
}
