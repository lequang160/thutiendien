package com.xep.thutiendien

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import com.xep.thutiendien.models.OrderModel
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

class PrinterActivity : Activity(), Runnable {
    protected val TAG = "TAG"
    private val REQUEST_CONNECT_DEVICE = 1
    private val REQUEST_ENABLE_BT = 2
    var mScan: Button? =
        null
    lateinit var mPrint: android.widget.Button
    lateinit var mDisc: android.widget.Button
    lateinit var mBluetoothAdapter: BluetoothAdapter
    private val applicationUUID = UUID
        .fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var mBluetoothConnectProgressDialog: ProgressDialog? = null
    private var mBluetoothSocket: BluetoothSocket? = null
    var mBluetoothDevice: BluetoothDevice? = null

    var mOrder: OrderModel? = null

    override fun onCreate(mSavedInstanceState: Bundle?) {
        super.onCreate(mSavedInstanceState)
        setContentView(R.layout.activity_printer)

        val bundle = intent.extras
        if (bundle != null) {
            mOrder = bundle.getParcelable("data")
        }
        if (XSharedPreferences(this, Gson()).getBluetoothDeviceId().isNotEmpty()) {
            startPrint()
        }
        showLoading()
        Handler().postDelayed({
            hideLoading()
        }, 1000);



        mScan = findViewById<View>(R.id.Scan) as Button
        mScan!!.setOnClickListener {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter == null) {
                Toast.makeText(this@PrinterActivity, "Message1", Toast.LENGTH_SHORT).show()
            } else {
                if (!mBluetoothAdapter!!.isEnabled) {
                    val enableBtIntent = Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE
                    )
                    startActivityForResult(
                        enableBtIntent,
                        REQUEST_ENABLE_BT
                    )
                } else {
                    ListPairedDevices()
                    val connectIntent = Intent(
                        this@PrinterActivity,
                        DeviceListActivity::class.java
                    )
                    startActivityForResult(
                        connectIntent,
                        REQUEST_CONNECT_DEVICE
                    )
                }
            }
        }
        mPrint = findViewById<View>(R.id.mPrint) as Button
        mPrint.setOnClickListener(View.OnClickListener {
            startPrint()
        })
        mDisc = findViewById<View>(R.id.dis) as Button
        mDisc.setOnClickListener(View.OnClickListener { if (mBluetoothAdapter != null) mBluetoothAdapter!!.disable() })
    } // onCreate

    fun startPrint() {
        val t: Thread = object : Thread() {
            override fun run() {
                try {
                    val os = mBluetoothSocket
                        ?.getOutputStream()

                    val spacerHeader =
                        """


"""
                    val headerBold =
                        """ 
Bien nhan thanh toan tien dien
"""
                    val kyThanhToan =
                        """
Ky thanh toan: ${mOrder?.date}
"""
                    val tenKHBold =
                        """
Ten KH: ${mOrder?.customerName}
"""
                    val conten1 =
                        """
Dia chi: ${mOrder?.address}
Ma KH: ${mOrder?.customerId}
"""
                    val tongtienBold =
                        """
TONG TIEN: ${mOrder?.amount} (VND)
"""
                    val conten2 =
                        """
Ngay in: ${curentDate("dd-MM-yyyy")}
Ngay thanh toan: ${curentDate()}
HD dien tu: http:cskh.evnspc.vn
Noi thanh toan
Ten cua hang: Diem thu Minh Hien
SDT: 03827088766
Dia chi: Khu Phuoc Hai
Hotline: 1900 1006 - 1900 6906
"""
                    val dathanhToanBold =
                        """
DA THANH TOAN
"""
                    val content3 =
                        """
Xac nhan cua diem giao dich
............................
............................
Cam on quy khach hen gap lai!
"""

                    val format = byteArrayOf(27, 33, 0)
                    os?.write(format)
                    os?.write(spacerHeader.toByteArray(), 0, spacerHeader.toByteArray().size)
                    // header Bold
                    format[2] = (0x8 or format.get(2).toInt()).toByte()
                    os?.write(format)
                    os?.write(headerBold.toByteArray(), 0, headerBold.toByteArray().size)
                    // ky thanh toan
                    format[2] = 0.toByte()
                    os?.write(format)
                    os?.write(kyThanhToan.toByteArray(), 0, kyThanhToan.toByteArray().size)
                    //ten kh  Bold
                    format[2] = (0x8 or format.get(2).toInt()).toByte()
                    os?.write(format)
                    os?.write(
                        deAccent(tenKHBold).toByteArray(Charsets.UTF_8),
                        0,
                        deAccent(tenKHBold).toByteArray(Charsets.UTF_8).size
                    )
                    //content 1
                    format[2] = 0.toByte()
                    os?.write(format)
                    os?.write(
                        deAccent(conten1).toByteArray(Charsets.UTF_8),
                        0,
                        deAccent(conten1).toByteArray(Charsets.UTF_8).size
                    )

                    //tong tien bold
                    format[2] = (0x8 or format.get(2).toInt()).toByte()
                    os?.write(format)
                    os?.write(
                        tongtienBold.toByteArray(Charsets.UTF_8),
                        0,
                        tongtienBold.toByteArray(Charsets.UTF_8).size
                    )

                    //content 2
                    format[2] = 0.toByte()
                    os?.write(format)
                    os?.write(
                        deAccent(conten2).toByteArray(Charsets.UTF_8),
                        0,
                        deAccent(conten2).toByteArray(Charsets.UTF_8).size
                    )

                    //da thanh toan bold
                    format[2] = (0x8 or format.get(2).toInt()).toByte()
                    os?.write(format)
                    os?.write(
                        dathanhToanBold.toByteArray(Charsets.UTF_8),
                        0,
                        dathanhToanBold.toByteArray(Charsets.UTF_8).size
                    )

                    //content 3
                    format[2] = 0.toByte()
                    os?.write(format)
                    os?.write(
                        content3.toByteArray(Charsets.UTF_8),
                        0,
                        content3.toByteArray(Charsets.UTF_8).size
                    )


                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    val gs = 29
                    os?.write(intToByteArray(gs).toInt())
                    val h = 104
                    os?.write(intToByteArray(h).toInt())
                    val n = 162
                    os?.write(intToByteArray(n).toInt())

                    // Setting Width
                    val gs_width = 29
                    os?.write(intToByteArray(gs_width).toInt())
                    val w = 119
                    os?.write(intToByteArray(w).toInt())
                    val n_width = 2
                    os?.write(intToByteArray(n_width).toInt())

                    print(deAccent(tenKHBold))
                    print(deAccent(conten1))
                } catch (e: Exception) {
                    Log.e("MainActivity", "Exe ", e)
                }
            }
        }
        t.start()
    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy()
        try {
            if (mBluetoothSocket != null) mBluetoothSocket!!.close()
        } catch (e: Exception) {
            Log.e("Tag", "Exe ", e)
        }
    }

    override fun onBackPressed() {
        try {
            if (mBluetoothSocket != null) mBluetoothSocket!!.close()
        } catch (e: Exception) {
            Log.e("Tag", "Exe ", e)
        }
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onActivityResult(
        mRequestCode: Int, mResultCode: Int,
        mDataIntent: Intent
    ) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent)
        when (mRequestCode) {
            REQUEST_CONNECT_DEVICE -> if (mResultCode == Activity.RESULT_OK) {
                val mExtra = mDataIntent.extras
                val mDeviceAddress = mExtra!!.getString("DeviceAddress")
                XSharedPreferences(this, Gson()).saveBluetoothDeviceId(mDeviceAddress!!)
                mBluetoothDevice = mBluetoothAdapter
                    .getRemoteDevice(mDeviceAddress)
                mBluetoothConnectProgressDialog = ProgressDialog.show(
                    this,
                    "Connecting...", mBluetoothDevice?.getName() + " : "
                            + mBluetoothDevice?.getAddress(), true, false
                )
                val mBlutoothConnectThread = Thread(this)
                mBlutoothConnectThread.start()
                // pairToDevice(mBluetoothDevice); This method is replaced by
                // progress dialog with thread
            }
            REQUEST_ENABLE_BT -> if (mResultCode == Activity.RESULT_OK) {
                ListPairedDevices()
                val connectIntent = Intent(
                    this@PrinterActivity,
                    DeviceListActivity::class.java
                )
                startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE)
            } else {
                Toast.makeText(this@PrinterActivity, "Message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ListPairedDevices() {
        val mPairedDevices = mBluetoothAdapter
            ?.getBondedDevices()
        if (mPairedDevices.size > 0) {
            for (mDevice in mPairedDevices) {
                Log.v(
                    TAG, "PairedDevices: " + mDevice.name + "  "
                            + mDevice.address
                )
            }
        }
    }

    override fun run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                ?.createRfcommSocketToServiceRecord(applicationUUID)
            mBluetoothAdapter.cancelDiscovery()
            mBluetoothSocket?.connect()
            mHandler.sendEmptyMessage(0)
        } catch (eConnectException: IOException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException)
            closeSocket(mBluetoothSocket)
            return
        }
    }

    private fun closeSocket(nOpenSocket: BluetoothSocket?) {
        try {
            nOpenSocket!!.close()
            Log.d(TAG, "SocketClosed")
        } catch (ex: IOException) {
            Log.d(TAG, "CouldNotCloseSocket")
        }
    }

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            mBluetoothConnectProgressDialog!!.dismiss()
            Toast.makeText(this@PrinterActivity, "DeviceConnected", Toast.LENGTH_SHORT).show()
        }
    }

    fun intToByteArray(value: Int): Byte {
        val b = ByteBuffer.allocate(4).putInt(value).array()
        for (k in b.indices) {
            println(
                "Selva  [" + k + "] = " + "0x"
                        + UnicodeFormatter.byteToHex(b[k])
            )
        }
        return b[3]
    }

    fun deAccent(str: String): String {
        return removeAccents(str)!!
    }

    fun curentDate(parttern: String = "dd-MM-yyyy HH:mm:ss"): String {
        val dateFormat = SimpleDateFormat(parttern)
        return dateFormat.format(Date())
    }

    private var mProgressLoading: KProgressHUD? = null
    public fun showLoading() {
        mProgressLoading = KProgressHUD.create(this)
        mProgressLoading?.apply {
            setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            setLabel("Please wait")
            setCancellable(false)
            setAnimationSpeed(2)
            setDimAmount(0.5f)
            show()
        }
    }

    public fun hideLoading() {
        mProgressLoading?.apply {
            Handler().postDelayed({
                dismiss()
            }, 1000)
        }
    }

}
