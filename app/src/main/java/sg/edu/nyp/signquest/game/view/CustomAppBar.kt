package sg.edu.nyp.signquest.game.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.RemoteViews

@RemoteViews.RemoteView
class CustomAppBar(context: Context, attributeSet: AttributeSet): FrameLayout(context, attributeSet) {


    private val path: Path by lazy {
        Path().apply {
            moveTo(0F, 0F)
            lineTo(0.0F, height * 0.85F)

            quadTo(width / 2.0F, height.toFloat(), width.toFloat(), height * 0.85F)
            lineTo(width.toFloat(), 0F)
            close()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        with(canvas){
            val save = save()
            clipPath(path)
            super.dispatchDraw(canvas)
            restoreToCount(save)
        }
    }
}