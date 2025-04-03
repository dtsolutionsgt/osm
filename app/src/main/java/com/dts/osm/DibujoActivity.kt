package com.dts.osm

import android.content.Intent
import android.graphics.*
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dts.base.clsClasses
import com.dts.classes.clsOrdenfotoObj
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class DibujoActivity : PBase() {

    var item = clsClasses.clsOrdenfoto()
    var OrdenfotoObj: clsOrdenfotoObj? = null
    private var imgView: ImageView? = null
    private var bitmap: Bitmap? = null
    private var drawingBitmap: Bitmap? = null
    private var drawingCanvas: Canvas? = null
    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 15f
    }

    private var imagePath: String? = null
    private val rectList = mutableListOf<RectF>()
    private var currentRect: RectF? = null
    private var isDrawingEnabled = true
    var rotationAngle: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dibujo)
        super.initbase(savedInstanceState)

        val guardarButton = findViewById<ImageView>(R.id.guardarButton)
        val deshacerButton = findViewById<ImageView>(R.id.deshacerButton)


        imgView = findViewById(R.id.imageViewDibujo)
        imagePath = intent.getStringExtra("imagePath")
        rotationAngle = intent.getFloatExtra("rotationAngle", 0f)

        if (imagePath != null) {
            val file = File(imagePath!!)
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.absolutePath)

                if (rotationAngle != 0f) {
                    val matrix = Matrix()
                    matrix.postRotate(rotationAngle)
                    bitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap!!.width, bitmap!!.height, matrix, true)
                }

                prepareDrawingCanvas()
            } else {
                Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        imgView?.setOnTouchListener { _, event -> drawOnImage(event) }

        guardarButton.setOnClickListener {
            saveImageAndSendToDB()
        }

        deshacerButton.setOnClickListener {
            undoLastRectangle()
        }
    }

    //region Events

    fun doExit(view: View) {
        finish()
    }

    //endregion


    //region Main
    private fun prepareDrawingCanvas() {
        bitmap?.let {
            drawingBitmap = it.copy(Bitmap.Config.ARGB_8888, true)
            drawingCanvas = Canvas(drawingBitmap!!)
            redrawAllRectangles()
        }
    }

    private fun drawOnImage(event: MotionEvent): Boolean {
        if (!isDrawingEnabled) return false

        try {
            val imgWidth = imgView?.width?.toFloat() ?: 0f
            val imgHeight = imgView?.height?.toFloat() ?: 0f

            val bitmapWidth = bitmap?.width?.toFloat() ?: 1f
            val bitmapHeight = bitmap?.height?.toFloat() ?: 1f


            val scaleX = imgWidth / bitmapWidth
            val scaleY = imgHeight / bitmapHeight

            val scaledX = event.x / scaleX
            val scaledY = event.y / scaleY

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentRect = RectF(scaledX, scaledY, scaledX, scaledY)
                }
                MotionEvent.ACTION_MOVE -> {
                    currentRect?.right = scaledX
                    currentRect?.bottom = scaledY
                    redrawAllRectangles()
                }
                MotionEvent.ACTION_UP -> {
                    currentRect?.let {
                        rectList.add(RectF(it))
                        currentRect = null
                    }
                    redrawAllRectangles()
                }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
        return true
    }


    private fun redrawAllRectangles() {
        drawingBitmap?.let {
            drawingCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            drawingCanvas?.drawBitmap(bitmap!!, 0f, 0f, null)

            for (rect in rectList) {
                drawingCanvas?.drawRect(rect, paint)
            }

            currentRect?.let {
                drawingCanvas?.drawRect(it, paint)
            }

            imgView?.setImageBitmap(drawingBitmap)
        }
    }

    private fun undoLastRectangle() {
        if (rectList.isNotEmpty()) {
            rectList.removeLast()
            redrawAllRectangles()
        } else {
            Toast.makeText(this, "No hay rectángulos para deshacer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageAndSendToDB() {
        try {
            if (drawingBitmap == null) {
                Toast.makeText(this, "No hay imagen para guardar", Toast.LENGTH_SHORT).show()
                return
            }

            val file = File(imagePath!!)
            FileOutputStream(file).use { out ->
                drawingBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            scanMediaFile(file)

            Toast.makeText(this, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show()

            val intent = Intent()
            intent.putExtra("imagePath", imagePath)
            setResult(RESULT_OK, intent)

            finish()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun scanMediaFile(file: File) {
        MediaScannerConnection.scanFile(
            this,
            arrayOf(file.toString()),
            null,
            null
        )
    }

    //endregion
}
