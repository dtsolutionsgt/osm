package com.dts.osm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private var complete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val mtimer = Handler()
            val mrunner = Runnable { grantPermissions() }
            mtimer.postDelayed(mrunner, 50)
        } catch (e: Exception) {
            toastlong(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun grantPermissions() {
        try {

            if (Build.VERSION.SDK_INT >= 20) {
                if (Build.VERSION.SDK_INT > 30) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.CAMERA) === PackageManager.PERMISSION_GRANTED
                    ) {
                        startApp()
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.CAMERA
                            ), 1 )
                    }
                } else {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.CAMERA) === PackageManager.PERMISSION_GRANTED
                    ) {
                        startApp()
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.CAMERA
                            ), 1 )
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            var se=e.message
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults!!)
        try {
            if (Build.VERSION.SDK_INT > 30) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CAMERA) === PackageManager.PERMISSION_GRANTED
                ) {
                    startApp()
                } else {
                    super.finish()
                }
            } else {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CAMERA) === PackageManager.PERMISSION_GRANTED
                ) {
                    startApp()
                } else {
                    super.finish()
                }
            }
        } catch (e: java.lang.Exception) {
            var se=e.message
        }
    }

    private fun startApp() {
        try {

            /*
            if (!isAllFilesAccessGranted()) {
                grandAllFilesAccess()
                return
            }
            */

            try {
                val directory = externalMediaDirs[0]
                directory.mkdirs()
            } catch (e: Exception) {}


            val mtimer = Handler()
            val mrunner = Runnable {
                val intent = Intent(this@MainActivity, Session::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
            mtimer.postDelayed(mrunner, 50)

        } catch (e: Exception) {
            toastlong(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun isAllFilesAccessGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            true
        }
    }

    fun grandAllFilesAccess() {
        try {
            val uri = Uri.parse("package:com.dts.posprintusb")
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            startActivity(intent)
        } catch (ex: java.lang.Exception) {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            startActivity(intent)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed( { finish() }, 500)

    }

    fun toastlong(msg: String?) {
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun onResume() {
        super.onResume()
        if (complete) finish() else complete = true
    }

}