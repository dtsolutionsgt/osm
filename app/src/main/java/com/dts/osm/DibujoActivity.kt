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
import com.dts.classes.clsEnvioimagenObj
import com.dts.classes.clsOrdenfotoObj
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class DibujoActivity : PBase() {

    var imgView: ImageView? = null
    var guardarButton: ImageView? = null
    var deshacerButton: ImageView? = null

    var OrdenfotoObj: clsOrdenfotoObj? = null
    var EnvioimagenObj: clsEnvioimagenObj? = null

    var item = clsClasses.clsOrdenfoto()

    var bitmap: Bitmap? = null
    var drawingBitmap: Bitmap? = null
    var drawingCanvas: Canvas? = null

    val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 15f
    }

    var imagePath: String? = null
    val rectList = mutableListOf<RectF>()
    var currentRect: RectF? = null
    var isDrawingEnabled = true
    var rotationAngle: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_dibujo)

            super.initbase(savedInstanceState)

            imgView = findViewById(R.id.imageViewDibujo)
            guardarButton = findViewById(R.id.guardarButton)
            deshacerButton = findViewById(R.id.deshacerButton)

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
                    toast( "No se pudo cargar la imagen")
                    finish()
                }
            }

            EnvioimagenObj = clsEnvioimagenObj(this, Con!!, db!!)

            setHandlers()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

    }

    //region Events

    fun doSave(view: View) {
        saveImageAndSendToDB()
    }

    fun doUndo(view: View) {
        undoLastRectangle()
    }

    fun doExit(view: View) {
        finish()
    }

    fun setHandlers() {
        try {
            imgView?.setOnTouchListener { _, event -> drawOnImage(event) }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion


    //region Main
    private fun prepareDrawingCanvas() {
        try {
            bitmap?.let {
                drawingBitmap = it.copy(Bitmap.Config.ARGB_8888, true)
                drawingCanvas = Canvas(drawingBitmap!!)
                redrawAllRectangles()
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
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
        try {
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
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun undoLastRectangle() {
        try {
            if (rectList.isNotEmpty()) {
                rectList.removeLast()
                redrawAllRectangles()
            } else {
                Toast.makeText(this, "No hay rectángulos para deshacer", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
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

            val intent = Intent()
            intent.putExtra("imagePath", imagePath)
            setResult(RESULT_OK, intent)

            var eiitem = clsClasses.clsEnvioimagen(item?.nombre!!, 0)
            try {
                EnvioimagenObj?.add(eiitem)
            } catch (e: Exception) {
                EnvioimagenObj?.update(eiitem)
            }

            finish()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun scanMediaFile(file: File) {
        try {
            MediaScannerConnection.scanFile(
                this,
                arrayOf(file.toString()),
                null,
                null
            )
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Activity Events

    override fun onResume() {
        try {
            super.onResume()

            OrdenfotoObj?.reconnect(Con!!, db!!)
            EnvioimagenObj?.reconnect(Con!!,db!!)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}
