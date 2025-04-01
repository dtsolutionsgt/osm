package com.dts.osm

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.*
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import android.media.ExifInterface
import android.widget.ImageButton

class CameraActivity : PBase() {

    private lateinit var imageView: ImageView
    private lateinit var previewView: PreviewView
    private lateinit var btnCapture: Button
    private lateinit var btnRotate: Button
    private lateinit var btnDraw: Button
    private lateinit var btnSave: Button
    private lateinit var btnRetry: Button
    private lateinit var btnUndo: Button

    private var capturedBitmap: Bitmap? = null
    private var rotationAngle = 0f
    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 15f
    }
    private var drawingCanvas: Canvas? = null
    private var drawingBitmap: Bitmap? = null

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var cameraSelector: CameraSelector

    private var isDrawingEnabled = false
    private val rectList = mutableListOf<RectF>()
    private var currentRect: RectF? = null
    private var selectedRect: RectF? = null
    private var isDeleting = false
    private val deleteRadius = 50f

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
            btnRetry = findViewById(com.dts.osm.R.id.btnRetry)
            btnUndo = findViewById(com.dts.osm.R.id.btnUndo)

            btnRotate.visibility = View.GONE
            btnDraw.visibility = View.GONE
            btnSave.visibility = View.GONE
            btnRetry.visibility = View.GONE

            btnUndo.visibility = View.GONE

            btnCapture.setOnClickListener { takePhoto() }
            btnRotate.setOnClickListener { rotateImage() }
            btnDraw.setOnClickListener { toggleDrawing() }
            imageView.setOnTouchListener { _, event -> drawOnImage(event) }
            btnSave.setOnClickListener { saveImageAndRestart() }
            btnRetry.setOnClickListener { startCamera() }
            btnUndo.setOnClickListener { undoLastAction() }
            startCamera()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun toggleDrawing() {
        isDrawingEnabled = !isDrawingEnabled
        if (isDrawingEnabled) {
            Toast.makeText(this, "Modo Dibujo Activado", Toast.LENGTH_SHORT).show()
            btnUndo.visibility = View.VISIBLE
            isDeleting = false
        } else {
            Toast.makeText(this, "Modo Dibujo Desactivado", Toast.LENGTH_SHORT).show()
            btnUndo.visibility = View.GONE
            isDeleting = false
            selectedRect = null
            redrawAllRectangles()
        }
    }

    private fun startCamera() {
        try {
            Log.d("CameraX", "Iniciando startCamera()")
            previewView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            btnRotate.visibility = View.GONE
            btnDraw.visibility = View.GONE
            btnSave.visibility = View.GONE
            btnRetry.visibility = View.GONE
            btnCapture.visibility = View.VISIBLE
            btnUndo.visibility = View.GONE

            isDrawingEnabled = false
            rectList.clear()
            clearDrawingCanvas()
            selectedRect = null
            isDeleting = false
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
                Log.d("CameraX", "Casos de uso anteriores desenlazados")

                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                imageCapture = ImageCapture.Builder().build()
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture
                    )
                    Log.d("CameraX", "Cámara iniciada correctamente")
                } catch (e: Exception) {
                    msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
                }
            }, ContextCompat.getMainExecutor(this))
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
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
                            val file = File(outputFileResults.savedUri?.path)
                            val exifInterface = ExifInterface(file.absolutePath)
                            val orientation = exifInterface.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED
                            )
                            var rotation = 0f
                            when (orientation) {
                                ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90f
                                ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180f
                                ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270f
                                ExifInterface.ORIENTATION_NORMAL -> rotation = 0f
                                else -> rotation = 0f
                            }

                            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            val matrix = Matrix()
                            matrix.postRotate(rotation)
                            val rotatedBitmap = Bitmap.createBitmap(
                                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                            )
                            capturedBitmap = rotatedBitmap

                            imageView.setImageBitmap(rotatedBitmap)
                            previewView.visibility = View.INVISIBLE
                            imageView.visibility = View.VISIBLE
                            btnRotate.visibility = View.VISIBLE
                            btnDraw.visibility = View.VISIBLE
                            btnSave.visibility = View.VISIBLE
                            btnRetry.visibility = View.VISIBLE
                            btnCapture.visibility = View.GONE
                            btnUndo.visibility = View.GONE
                            prepareDrawingCanvas()
                            imageView.invalidate()
                        } catch (e: Exception) {
                            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("CameraX", "Error al capturar foto: ${exception.message}", exception)
                        msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + exception.message)
                        startCamera()
                    }
                })
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
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
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
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
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun drawOnImage(event: MotionEvent): Boolean {
        if (!isDrawingEnabled) return false
        try {
            val imageBounds = Rect()
            imageView.getDrawingRect(imageBounds)
            val drawableBounds = imageView.drawable.bounds

            val scaleX = drawableBounds.width().toFloat() / imageBounds.width()
            val scaleY = drawableBounds.height().toFloat() / imageBounds.height()

            val scaledX = event.x * scaleX
            val scaledY = event.y * scaleY

            Log.d("Draw", "Evento detectado: ${event.action}, Posición original: (${event.x}, ${event.y})")
            Log.d("Draw", "Escalado a imagen real: ($scaledX, $scaledY)")

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (isDeleting) {
                        handleDeleteTouch(scaledX, scaledY)
                    } else {
                        currentRect = RectF(scaledX, scaledY, scaledX, scaledY)
                        Log.d("Draw", "ACTION_DOWN - Nuevo rectángulo en ($scaledX, $scaledY)")
                        selectedRect = null
                        redrawAllRectangles()
                    }

                }
                MotionEvent.ACTION_MOVE -> {
                    if (isDeleting) {
                        handleDeleteTouch(scaledX, scaledY)
                    } else if (currentRect != null) {
                        currentRect!!.right = scaledX
                        currentRect!!.bottom = scaledY
                        Log.d("Draw", "ACTION_MOVE - Rectángulo actualizado: $currentRect")
                        redrawAllRectangles()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (!isDeleting && currentRect != null) {
                        rectList.add(RectF(currentRect!!))
                        Log.d("Draw", "ACTION_UP - Rectángulo guardado: $currentRect")
                        currentRect = null
                        redrawAllRectangles()
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    Log.d("Draw", "ACTION_CANCEL")
                    currentRect = null
                    redrawAllRectangles()
                }
                MotionEvent.ACTION_OUTSIDE -> {
                    Log.d("Draw", "ACTION_OUTSIDE")
                    currentRect = null
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
            capturedBitmap?.let {
                drawingBitmap = it.copy(Bitmap.Config.ARGB_8888, true)
                drawingCanvas = Canvas(drawingBitmap!!)

                Log.d("Draw", "Redibujando ${rectList.size} rectángulos")

                for (rect in rectList) {
                    Log.d("Draw", "Dibujando rectángulo: $rect")
                    drawingCanvas?.drawRect(rect, paint)
                }

                currentRect?.let {
                    Log.d("Draw", "Dibujando rectángulo en progreso: $it")
                    drawingCanvas?.drawRect(it, paint)
                }
                if (selectedRect != null) {
                    val selectPaint = Paint().apply {
                        color = Color.YELLOW
                        style = Paint.Style.STROKE
                        strokeWidth = 20f
                    }
                    drawingCanvas?.drawRect(selectedRect!!, selectPaint)
                }

                imageView.setImageBitmap(drawingBitmap)
                imageView.invalidate()
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    private fun saveImageAndRestart() {
        try {
            capturedBitmap?.let { bitmap ->
                val finalBitmap = if (isDrawingEnabled && drawingBitmap != null) {
                    drawingBitmap!!
                } else {
                    bitmap
                }
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
                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.close()
                        Log.d("CameraX", "Imagen guardada en la galería: $imageUri")
                        Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: Log.e("CameraX", "No hay imagen para guardar.")
            startCamera()
        } catch (e: IOException) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }


    private fun clearDrawingCanvas() {
        if (drawingCanvas != null) {
            drawingCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            imageView.invalidate()
        }
        drawingBitmap = null;
        drawingCanvas = null;
    }


    private fun undoLastAction() {
        if (rectList.isNotEmpty()) {
            rectList.removeLast()
            redrawAllRectangles()
        } else {
            Toast.makeText(this, "No hay rectángulos para deshacer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDeleteTouch(x: Float, y: Float) {
        Log.d("Draw", "handleDeleteTouch: x=$x, y=$y, isDeleting=$isDeleting")
        val iterator = rectList.iterator()
        var removed = false;
        while (iterator.hasNext()) {
            val rect = iterator.next()
            val centerX = rect.left + rect.width() / 2
            val centerY = rect.top + rect.height() / 2
            val distance = Math.sqrt(Math.pow((x - centerX).toDouble(), 2.0) + Math.pow((y - centerY).toDouble(), 2.0)).toFloat()

            Log.d("Draw", "  Checking rectangle: $rect, distance=$distance")
            if (distance < deleteRadius) {
                iterator.remove()
                removed = true;
                Log.d("Draw", "    Rectangle removed")
            }
        }
        if (removed) {
            redrawAllRectangles()
        }
    }
}
