package com.phlogiston

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import com.phlogiston.gauges.ArcGauge
import com.phlogiston.gauges.Range
import kotlinx.android.synthetic.main.activity_gauges.*

class ActivityGauges : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gauges)

        initGauge(arcGauge1, seekBarArcGauge1)
        initGauge(arcGauge2, seekBarArcGauge2)
        initGauge(arcGauge3, seekBarArcGauge3)
        initGauge(arcGauge4, seekBarArcGauge4)
        initGauge(arcGauge5, seekBarArcGauge5)
    }

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            when (seekBar.id) {
                seekBarArcGauge1.id -> {
                    arcGauge1.value = seekBarArcGauge1.progress.toDouble()
                }
                seekBarArcGauge2.id -> {
                    arcGauge2.value = seekBarArcGauge2.progress.toDouble()
                }
                seekBarArcGauge3.id -> {
                    arcGauge3.value = seekBarArcGauge3.progress.toDouble()
                }
                seekBarArcGauge4.id -> {
                    arcGauge4.value = seekBarArcGauge4.progress.toDouble()
                }
                seekBarArcGauge5.id -> {
                    arcGauge5.value = seekBarArcGauge5.progress.toDouble()
                }
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
        }
    }

    private fun initGauge(gauge : ArcGauge, seekBar : SeekBar) {
        val range1 = Range()
        range1.color = Color.parseColor("#E3E500")
        range1.from = 0.0
        range1.to = 50.0

        val range2 = Range()
        range2.color = Color.parseColor("#00b20b")
        range2.from = 50.0
        range2.to = 100.0

        val range3 = Range()
        range3.color = Color.parseColor("#E3E500")
        range3.from = 100.0
        range3.to = 150.0

        val range4 = Range()
        range4.color = Color.parseColor("#ce0000")
        range4.from = 150.0
        range4.to = 200.0

        //add color ranges to gauge
        gauge.addRange(range1)
        gauge.addRange(range2)
        gauge.addRange(range3)
        gauge.addRange(range4)

        //set min max and current value
        gauge.minValue = 0.0
        gauge.maxValue = 200.0
        gauge.value = 0.0

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }
}
