package com.phlogiston

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.preference.PreferenceManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.lang.ref.WeakReference


class ActivityButtons : Activity() {

    private var bl: CBluetooth? = null
    private var btnLeft: Button? = null
    private var btnRight: Button? = null

    private var address: String? = null          // MAC-address from settings (MAC-адрес устройства из настроек)
    private var command1: Int = 0
    private var command2: Int = 0
    private var alterCommand1: String? = null       // Additional command symbol to send from settings (Дополнительный символ команды для отправки из настроек)
    //private String alter_command_2;  // Additional command symbol to send from settings (Дополнительный символ команды для отправки из настроек)
    //private String alter_command_3;     // Additional command symbol to send from settings (Дополнительный символ команды для отправки из настроек) (Например - звуковой сигнал)
    private var showDebug: Boolean = false       // show debug information (from settings) (отображение отладочной информации (из настроек))
    private var btIsConnect: Boolean = false // bluetooh is connected (переменная для хранения информации подключен ли Bluetooth)
    private var cmdSend: String? = null
    private var seekBar1: SeekBar? = null
    private var speedSeekBar: Int = 0

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            speedSeekBar = seekBar1!!.progress
            showTextDebug(speedSeekBar.toString())
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            cmdSend = alterCommand1 + speedSeekBar + "\r"
            showTextDebug(cmdSend!! + " sended.")
            if (btIsConnect) bl!!.sendData(cmdSend!!)
        }
    }

    private val mHandler = MyHandler(this)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        address = resources.getText(R.string.defaultMAC) as String
        command1 = Integer.parseInt(resources.getText(R.string.defaultCommand1) as String)
        command2 = Integer.parseInt(resources.getText(R.string.defaultCommand2) as String)
        alterCommand1 = resources.getText(R.string.defaultAlterCommand1) as String //(Команда дополнительно отправляемая на мк: L)
        //alter_command_2 = (String) getResources().getText(R.string.default_alter_command_2); //(Команда дополнительно отправляемая на мк: R)
        //alter_command_3 = (String) getResources().getText(R.string.default_alter_command_3);

        loadPref()

        bl = CBluetooth(this, mHandler)
        bl!!.checkBTState()

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_buttons)

        btnLeft = findViewById<View>(R.id.btn_On) as Button
        btnRight = findViewById<View>(R.id.btn_Off) as Button

        btnLeft!!.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                cmdSend = command1.toString()
                showTextDebug(cmdSend)
                if (btIsConnect) bl!!.sendData(cmdSend!!)
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Commands when the button is UP (Команды когда кнопка отжимается)
            }
            false
        }

        btnRight!!.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                cmdSend = command2.toString()
                showTextDebug(cmdSend)
                if (btIsConnect) bl!!.sendData(cmdSend!!)
            } else if (event.action == MotionEvent.ACTION_UP) {
                // Commands when the button is UP (Команды когда кнопка отжимается)
            }
            false
        }

        seekBar1 = findViewById<View>(R.id.seekBar_1) as SeekBar
        seekBar1!!.setOnSeekBarChangeListener(seekBarChangeListener)

        mHandler.postDelayed(sRunnable, 600000)
    }

    private class MyHandler(activity: ActivityButtons) : Handler() {
        private val mActivity: WeakReference<ActivityButtons> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = mActivity.get()
            if (activity != null) {
                when (msg.what) {
                    CBluetooth.BL_NOT_AVAILABLE -> {
                        Log.d(CBluetooth.TAG, "Bluetooth is not available. Exit")
                        Toast.makeText(activity.baseContext, "Bluetooth is not available", Toast.LENGTH_SHORT).show()
                        activity.finish()
                    }
                    CBluetooth.BL_INCORRECT_ADDRESS -> {
                        Log.d(CBluetooth.TAG, "Incorrect MAC address")
                        Toast.makeText(activity.baseContext, "Incorrect Bluetooth address", Toast.LENGTH_SHORT).show()
                    }
                    CBluetooth.BL_REQUEST_ENABLE -> {
                        Log.d(CBluetooth.TAG, "Request Bluetooth Enable")
                        BluetoothAdapter.getDefaultAdapter()
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        activity.startActivityForResult(enableBtIntent, 1)
                    }
                    CBluetooth.BL_SOCKET_FAILED -> Toast.makeText(activity.baseContext, "Socket failed", Toast.LENGTH_SHORT).show()
                }//activity.finish();
            }
        }
    }

    private fun showTextDebug(txtDebug: String?) {
        val textCmdSend = findViewById<View>(R.id.textViewCmdSend) as TextView
        if (showDebug) {
            textCmdSend.text = "Send: " + txtDebug!!
        } else {
            textCmdSend.text = ""
        }
    }

    private fun loadPref() {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        address = mySharedPreferences.getString("pref_MAC_address", address)        // First time we load the default value (Первый раз загружаем дефолтное значение)
        command1 = Integer.parseInt(mySharedPreferences.getString("pref_command_1", command1.toString())!!)
        command2 = Integer.parseInt(mySharedPreferences.getString("pref_command_2", command2.toString())!!)
        alterCommand1 = mySharedPreferences.getString("pref_alter_command_1", alterCommand1)
        //alter_command_2 = mySharedPreferences.getString("pref_alter_command_2", alter_command_2);
        //alter_command_3 = mySharedPreferences.getString("pref_alter_command_3", alter_command_3);
        showDebug = mySharedPreferences.getBoolean("pref_Debug", false)
    }


    override fun onResume() {
        super.onResume()
        btIsConnect = bl!!.BT_Connect(address!!, false)
    }

    public override fun onPause() {
        super.onPause()
        bl!!.btOnPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        loadPref()
    }

    companion object {
        private const val TAG = "DiplomProject_v2"

        private val sRunnable = Runnable { }
    }

}

