package com.dts.osm

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.*
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.core.R
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream


class CameraActivity : PBase() {

    private lateinit var imageView: ImageView
    private lateinit var previewView: PreviewView
    private lateinit var btnCapture: Button
    private lateinit var btnRotate: Button
    private lateinit var btnDraw: Button
    private lateinit var btnSave: Button

    private var capturedBitmap: Bitmap? = null
    private var rotationAngle = 0f
    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private var drawingCanvas: Canvas? = null
    private var drawingBitmap: Bitmap? = null

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var cameraSelector: CameraSelector

    private var lastX = 0f
    private var lastY = 0f
    private var brushSize = 10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.dts.osm.R.layout.activity_camera)
        super.initbase(savedInstanceState)

        try {
            imageView = findViewById(com.dts.osm.R.id.imageView)
            previewView = findViewById(com.dts.osm.R.id.previewView)
            btnCapture = findViewById(com.dts.osm.R.id.btnCapture)
            btnRotate = findViewById(com.dts.osm.R.id.btnRotate)
            btnDraw = findViewById(com.dts.osm.R.id.btnDraw)
            btnSave = findViewById(com.dts.osm.R.id.btnSave)

            btnCapture.setOnClickListener { takePhoto() }
            btnRotate.setOnClickListener { rotateImage() }
            imageView.setOnTouchListener { _, event -> drawOnImage(event) }
            btnSave.setOnClickListener {
                capturedBitmap?.let {
                    saveImageToGallery(it)
                } ?: Log.e("CameraX", "No hay imagen para guardar.")
            }
            startCamera()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                imageCapture = ImageCapture.Builder().build()
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
                Log.d("CameraX", "Cámara iniciada correctamente")
            } catch (e: Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        try {
            Log.d("CameraX", "Capturando foto...")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(
                File(externalMediaDirs.first(), "photo.jpg")
            ).build()
            imageCapture.takePicture(
                outputOptions, ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        try {
                            Log.d("CameraX", "Foto capturada correctamente: ${outputFileResults.savedUri}")
                            val bitmap = BitmapFactory.decodeFile(outputFileResults.savedUri?.path)
                            capturedBitmap = bitmap
                            imageView.setImageBitmap(bitmap)
                            previewView.visibility = View.INVISIBLE
                            imageView.visibility = View.VISIBLE
                            prepareDrawingCanvas()
                            imageView.invalidate()
                        } catch (e: Exception) {
                            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("CameraX", "Error al capturar foto: ${exception.message}", exception)
                    }
                })
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun prepareDrawingCanvas() {
        try {
            capturedBitmap?.let {
                Log.d("CameraX", "Preparando lienzo para dibujo...")
                drawingBitmap = it.copy(Bitmap.Config.ARGB_8888, true)
                drawingCanvas = Canvas(drawingBitmap!!)
                imageView.setImageBitmap(drawingBitmap)
                imageView.invalidate()
                Log.d("CameraX", "Lienzo preparado y asignado al ImageView")
            } ?: Log.e("CameraX", "Error: No se pudo cargar la imagen para el lienzo.")
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private fun rotateImage() {
        try {
            capturedBitmap?.let {
                Log.d("CameraX", "Rotando imagen...")
                rotationAngle += 90f
                val matrix = Matrix().apply { postRotate(rotationAngle) }
                val rotatedBitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
                capturedBitmap = rotatedBitmap
                prepareDrawingCanvas()
                Log.d("CameraX", "Imagen rotada a $rotationAngle grados")
            } ?: Log.e("CameraX", "No hay imagen para rotar.")
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    private var rect: RectF? = null
    private var isDrawing = false
    private var lastUpdateTime: Long = 0
    private val updateInterval: Long = 50

    private fun drawOnImage(event: MotionEvent): Boolean {
        try {
            val currentTime = System.currentTimeMillis()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                    rect = RectF(lastX, lastY, lastX, lastY)
                    isDrawing = true
                    lastUpdateTime = currentTime
                    Log.d("Draw", "ACTION_DOWN - Posición inicial: ($lastX, $lastY)")
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isDrawing) {
                        val deltaX = Math.abs(event.x - lastX)
                        val deltaY = Math.abs(event.y - lastY)
                        if ((deltaX > 5 || deltaY > 5) && currentTime - lastUpdateTime >= updateInterval) {
                            val imageMatrix = imageView.imageMatrix
                            val invertedMatrix = Matrix()
                            imageMatrix.invert(invertedMatrix)
                            val coords = floatArrayOf(event.x, event.y)
                            invertedMatrix.mapPoints(coords)
                            val scaledX = coords[0]
                            val scaledY = coords[1]
                            Log.d("Draw", "ACTION_MOVE - Coordenadas: ($scaledX, $scaledY)")
                            rect?.right = scaledX
                            rect?.bottom = scaledY
                            Log.d("Draw", "ACTION_MOVE - Rectángulo actualizado: left=${rect?.left}, top=${rect?.top}, right=${rect?.right}, bottom=${rect?.bottom}")
                            paint.strokeWidth = 8f
                            drawingCanvas?.drawRect(rect!!, paint)
                            imageView.invalidate()
                            lastX = event.x
                            lastY = event.y
                            lastUpdateTime = currentTime
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isDrawing = false
                    Log.d("Draw", "ACTION_UP - Rectángulo finalizado")
                }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
        return true
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        try {
            val contentResolver: ContentResolver = contentResolver
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "edited_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            imageUri?.let {
                val outputStream = contentResolver.openOutputStream(it)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()
                    Log.d("CameraX", "Imagen guardada en la galería: $imageUri")
                }
            }
        } catch (e: IOException) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }
}