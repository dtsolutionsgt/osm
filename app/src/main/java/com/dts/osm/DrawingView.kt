import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private var path = Path()
    private var currentTool = Tool.FREE_DRAWING
    private var startX = 0f
    private var startY = 0f

    enum class Tool {
        FREE_DRAWING, LINE, RECTANGLE, CIRCLE, POLYGON
    }

    fun setTool(tool: Tool) {
        currentTool = tool
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
                path.reset()
                path.moveTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                when (currentTool) {
                    Tool.FREE_DRAWING -> path.lineTo(x, y)
                    Tool.LINE -> {
                        path.reset()
                        path.moveTo(startX, startY)
                        path.lineTo(x, y)
                    }
                    Tool.RECTANGLE -> {
                        path.reset()
                        path.addRect(startX, startY, x, y, Path.Direction.CW)
                    }
                    Tool.CIRCLE -> {
                        path.reset()
                        val radius = Math.sqrt(Math.pow((x - startX).toDouble(), 2.0) + Math.pow((y - startY).toDouble(), 2.0)).toFloat()
                        path.addCircle(startX, startY, radius, Path.Direction.CW)
                    }
                    Tool.POLYGON -> {
                        path.reset()
                        path.moveTo(startX, startY)
                        path.lineTo(x, y)
                        path.lineTo(startX, y)
                        path.close()
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }
}