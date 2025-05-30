package com.cogu.spylook.model.utils.textWatchers.actions

import android.text.TextUtils
import android.widget.Scroller
import android.widget.TextView

class LongTextScrollerAction(
    var text: TextView,
    var startIndex: Int,
    var busqueda: String
) : () -> Unit {
    companion object{
        var lastScroll = 0.0f
    }
    override fun invoke() {
        val lineWidth =
            text.paint.measureText(text.text.toString())
        val viewWidth = text.width
        val startLine = text.layout.getLineForOffset(lastScroll.toInt())
        val endLine = text.layout.getLineForOffset(lastScroll.toInt() + viewWidth)

        // Obtener el índice del último carácter visible en la última línea visible
        val visibleText = text.layout.getOffsetForHorizontal(
            endLine.coerceAtMost(text.layout.lineCount - 1), // Validar línea máxima
            lastScroll + viewWidth                      // Última posición horizontal visible
        ).coerceAtMost(text.text.length)

        val endCharPosition = visibleText.coerceAtMost(text.text.length)
        val startCharPosition = visibleText.coerceAtLeast(0)
        val actualLastCharPosition =
            text.paint.measureText(text.text.toString(), 0, startIndex + busqueda.length - 1)
        if (lineWidth > viewWidth) {

            // Configuramos el Scroller para manejar el desplazamiento
            text.setHorizontallyScrolling(true)
            text.isHorizontalScrollBarEnabled = false
            text.isSingleLine = true
            text.ellipsize =
                null // Desactivar truncamiento
            text.textAlignment =
                TextView.TEXT_ALIGNMENT_VIEW_START // Alineación desde el inicio

            // Creando y asignando Scroller
            val scroller = Scroller(text.context)
            text.setScroller(scroller)

            if (actualLastCharPosition < startCharPosition || actualLastCharPosition > endCharPosition) {
                scroller.startScroll(lastScroll.toInt(), 0, (actualLastCharPosition -lastScroll).toInt(), 0)
                lastScroll = text.x
            }
            text.invalidate()

        } else {
            text.isSelected = true
            text.isFocusable = true
            text.isFocusableInTouchMode = true
            text.ellipsize = TextUtils.TruncateAt.MARQUEE
            text.isHorizontalScrollBarEnabled = false
            text.setHorizontallyScrolling(false)
            text.scrollTo(0, 0)

        }
    }
}