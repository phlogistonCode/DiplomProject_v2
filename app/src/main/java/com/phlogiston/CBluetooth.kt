/**
 * Class for Bluetooth
 * @version 1.2.1
 * 14.08.2013
 */

package com.phlogiston

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Build
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class CBluetooth internal constructor(context: Context, private val mHandler: Handler) {
    private var btSocket: BluetoothSocket? = null
    private var outStream: OutputStream? = null
    private var mConnectedThread: ConnectedThread? = null

    init {
        btAdapter = BluetoothAdapter.getDefaultAdapter()
        if (btAdapter == null) {
            mHandler.sendEmptyMessage(BL_NOT_AVAILABLE)
        }
    }

    fun checkBTState() {
        if (btAdapter == null) {
            mHandler.sendEmptyMessage(BL_NOT_AVAILABLE)
        } else {
            if (btAdapter!!.isEnabled) {
                Log.d(TAG, "Bluetooth ON")
            } else {
                mHandler.sendEmptyMessage(BL_REQUEST_ENABLE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                val m = device.javaClass.getMethod("createInsecureRfcommSocketToServiceRecord", UUID::class.java)
                return m.invoke(device, MY_UUID) as BluetoothSocket
            } catch (e: Exception) {
                Log.e(TAG, "Could not create Insecure RFComm Connection", e)
            }

        }
        return device.createRfcommSocketToServiceRecord(MY_UUID)
    }

    fun BT_Connect(address: String, listen_InStream: Boolean): Boolean {
        Log.d(TAG, "...On Resume...")

        val connected: Boolean

        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            mHandler.sendEmptyMessage(BL_INCORRECT_ADDRESS)
            return false
        } else {

            val device = btAdapter!!.getRemoteDevice(address)
            try {
                btSocket = createBluetoothSocket(device)
            } catch (e1: IOException) {
                Log.e(TAG, "In BT_Connect() socket create failed: " + e1.message)
                mHandler.sendEmptyMessage(BL_SOCKET_FAILED)
                return false
            }

            if (btAdapter!!.isDiscovering) {
                btAdapter!!.cancelDiscovery()
            }

            Log.d(TAG, "...Connecting...")
            try {
                btSocket!!.connect()
                Log.d(TAG, "...Connection ok...")
            } catch (e: IOException) {
                try {
                    btSocket!!.close()
                } catch (e2: IOException) {
                    Log.e(TAG, "In BT_Connect() unable to close socket during connection failure" + e2.message)
                    mHandler.sendEmptyMessage(BL_SOCKET_FAILED)
                    return false
                }

            }

            // Create a data stream so we can talk to server.
            Log.d(TAG, "...Create Socket...")

            try {
                outStream = btSocket!!.outputStream
                connected = true
            } catch (e: IOException) {
                Log.e(TAG, "In BT_Connect() output stream creation failed:" + e.message)
                mHandler.sendEmptyMessage(BL_SOCKET_FAILED)
                return false
            }

            if (listen_InStream) {     // whether to create a thread for the incoming data (нужно ли создавать поток для входящих данных)
                mConnectedThread = ConnectedThread()
                mConnectedThread!!.start()
            }
        }
        return connected
    }

    fun btOnPause() {
        Log.d(TAG, "...On Pause...")
        if (outStream != null) {
            try {
                outStream!!.flush()
            } catch (e: IOException) {
                Log.e(TAG, "In onPause() and failed to flush output stream: " + e.message)
                mHandler.sendEmptyMessage(BL_SOCKET_FAILED)
                return
            }

        }

        if (btSocket != null) {
            try {
                btSocket!!.close()
            } catch (e2: IOException) {
                Log.e(TAG, "In onPause() and failed to close socket." + e2.message)
                mHandler.sendEmptyMessage(BL_SOCKET_FAILED)
                return
            }

        }
    }

    fun sendData(message: String) {
        val msgBuffer = message.toByteArray()

        Log.i(TAG, "Send data: $message")

        if (outStream != null) {
            try {
                outStream!!.write(msgBuffer)
            } catch (e: IOException) {
                Log.e(TAG, "In onResume() exception occurred during write: " + e.message)
                mHandler.sendEmptyMessage(BL_SOCKET_FAILED)
                return
            }

        } else
            Log.e(TAG, "Error Send data: outStream is Null")
    }

    private inner class ConnectedThread : Thread() {

        private val mmInStream: InputStream?

        init {
            var tmpIn: InputStream? = null

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = btSocket!!.inputStream
            } catch (e: IOException) {
                Log.e(TAG, "In ConnectedThread() error getInputStream(): " + e.message)
            }

            mmInStream = tmpIn
        }

        override fun run() {
            val buffer = ByteArray(256)  // buffer store for the stream
            var bytes: Int                    // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream!!.read(buffer)
                    mHandler.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget()
                } catch (e: IOException) {
                    break
                }

            }
        }
    }

    companion object {

        const val TAG = "Andruino"

        private var btAdapter: BluetoothAdapter? = null

        // SPP UUID service
        private val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        // statuses for Handler (статусы для Handler)
        const val BL_NOT_AVAILABLE = 1          // Bluetooth is not available (Bluetooth не включен)
        const val BL_INCORRECT_ADDRESS = 2     // incorrect MAC-address (неправильный MAC-адрес)
        const val BL_REQUEST_ENABLE = 3       // request enable Bluetooth (запрос на включение Bluetooth)
        const val BL_SOCKET_FAILED = 4        // socket error (ошибка SOCKET)
        const val RECIEVE_MESSAGE = 5         // receive message (приняли сообщение)
    }
}

