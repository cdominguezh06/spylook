package com.cogu.spylook.model.utils.textWatchers.actions

import android.text.TextUtils
import android.view.View
import android.widget.Scroller
import android.widget.TextView

class LongTextScrollerAction(
    var text: TextView,
    var startIndex: Int,
    var busqueda: String,
    var onFitOnScreen : () -> Unit
) : () -> Unit {
    companion object{
        var lastScroll = 0.0f
        var lastDistance = 0.0f
    }
    override fun invoke() {
        val lineWidth =
            text.paint.measureText(text.text.toString())
        val viewWidth = text.width

        val actualLastCharPosition = text.paint.measureText(
            text.text.toString(),
            0,
            startIndex + busqueda.length
        )
        if (lineWidth > viewWidth) {
            // Configuramos el TextView para manejar el desplazamiento
            text.setHorizontallyScrolling(true)
            text.isHorizontalScrollBarEnabled = false
            text.isSingleLine = true
            text.ellipsize = null // Desactivar truncamiento
            text.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START // Alineación desde el inicio

            val scroller = Scroller(text.context)
            text.setScroller(scroller)

            val desiredCenter = actualLastCharPosition - (viewWidth / 2f)
            val finalCenter = desiredCenter.coerceAtLeast(0f)
            val distance = finalCenter - lastScroll
            scroller.startScroll(lastScroll.toInt(), 0, distance.toInt(), 0)
            lastScroll = finalCenter
            lastDistance = distance
            text.invalidate()


        } else {
            text.isSelected = true
            text.isFocusable = true
            text.isFocusableInTouchMode = true
            text.ellipsize = TextUtils.TruncateAt.MARQUEE
            text.isHorizontalScrollBarEnabled = false
            text.setHorizontallyScrolling(false)
            text.scrollTo(0, 0)
            lastDistance = 0f
            lastScroll = 0f
            onFitOnScreen.invoke()
        }
    }
}
