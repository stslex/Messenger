package st.slex.messenger.ui.auth.phone

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class TextDrawable(private val text: String, private val size: Float) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = PhoneHelper.EMOJI_COLOR
        textSize = size
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) = canvas.drawText(
        text,
        0,
        text.length,
        bounds.centerX().toFloat(),
        bounds.centerY().toFloat() - ((paint.descent() + paint.ascent()) / 2),
        paint
    )

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT
}