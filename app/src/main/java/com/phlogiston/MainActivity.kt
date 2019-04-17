/**
 * Control engine
 * @version 2.9
 * @author Popov A.Y.
 */

package com.phlogiston

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), OnClickListener {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView1.setShadowLayer(1f, 3f, 3f, Color.GRAY)

        button_gauges.setOnClickListener(this)
        button_buttons.setOnClickListener(this)
        button_about.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_gauges -> {
                val intentGauges = Intent(this, ActivityGauges::class.java)
                startActivity(intentGauges)
            }
            R.id.button_buttons -> {
                val intentButtons = Intent(this, ActivityButtons::class.java)
                startActivity(intentButtons)
            }
            R.id.button_about -> {
                val intentAbout = Intent(this, ActivityAbout::class.java)
                startActivity(intentAbout)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        intent.setClass(this@MainActivity, SetPreferenceActivity::class.java)
        startActivityForResult(intent, 0)

        return true
    }
}
