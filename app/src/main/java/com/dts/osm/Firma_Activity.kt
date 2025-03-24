package com.dts.osm

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dts.classes.SignatureView
import java.io.File
import java.io.FileOutputStream

class Firma_Activity: AppCompatActivity() {

    private lateinit var signatureView: SignatureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bloquear la orientación en horizontal
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_firma)

        signatureView = findViewById(R.id.signature_view)
        val btnAplicar: Button = findViewById(R.id.btn_aplicar)
        val btnGuardar: Button = findViewById(R.id.btn_guardar)

        val firmaDigital = FirmaDigital()

        btnAplicar.setOnClickListener { firmaDigital.clearSignature() }
        btnGuardar.setOnClickListener { firmaDigital.saveSignature() }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Al cerrar la Activity, regresar a la orientación vertical
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    inner class FirmaDigital {

        fun clearSignature() {
            signatureView.clearCanvas()
        }

        fun saveSignature() {
            if (signatureView.isEmpty()) {
                Toast.makeText(this@Firma_Activity, "Por favor, firma antes de guardar.", Toast.LENGTH_SHORT).show()
                return
            }

            val bitmap = signatureView.getBitmap()
            val fileName = "firma_${System.currentTimeMillis()}.png"

            try {
                // Guardar en la galería (Pictures)
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val fileGallery = File(picturesDir, fileName)
                val outputStreamGallery = FileOutputStream(fileGallery)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStreamGallery)
                outputStreamGallery.flush()
                outputStreamGallery.close()
                MediaScannerConnection.scanFile(this@Firma_Activity, arrayOf(fileGallery.absolutePath), arrayOf("image/png"), null)

                // Guardar en la carpeta privada de Android
                val androidDir = File(getExternalFilesDir(null), "FirmasApp")
                if (!androidDir.exists()) {
                    androidDir.mkdirs()
                }

                val fileAndroid = File(androidDir, fileName)
                val outputStreamAndroid = FileOutputStream(fileAndroid)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStreamAndroid)
                outputStreamAndroid.flush()
                outputStreamAndroid.close()

                Toast.makeText(this@Firma_Activity, "Firma guardada en galería y carpeta interna.", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(this@Firma_Activity, "Error al guardar la firma", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

    }

    fun doExit(view: View) {
        finish()
    }
}