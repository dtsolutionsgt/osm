package com.dts.classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.*

class SignatureView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var path = Path()
    private var paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private lateinit var bitmap: Bitmap
    private lateinit var extraCanvas: Canvas

    private var isSigned = false

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(bitmap)
        extraCanvas.drawColor(Color.WHITE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                isSigned = true
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
                extraCanvas.drawPath(path, paint)
                isSigned = true
            }
        }
        invalidate()
        return true
    }

    fun clearCanvas() {
        extraCanvas.drawColor(Color.WHITE)
        path.reset()
        isSigned = false
        invalidate()
    }

    fun getBitmap(): Bitmap = bitmap

    fun isEmpty(): Boolean {
        return !isSigned
    }
}