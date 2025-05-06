package com.cogu.spylook.model.utils.decorators

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.cogu.spylook.R
import com.cogu.spylook.model.unimplemented.TextViewDecorator
import lombok.Getter

class RainbowTextViewDecorator(
    private val context: Context,
    @field:Getter private val textView: TextView
) : TextViewDecorator {
    private val drawableResourceId: Int = R.drawable.rainbow_gradient

    override fun apply() {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener {
            val gradientDrawable =
                AppCompatResources.getDrawable(context, drawableResourceId) as GradientDrawable?

            // Get the gradient colors
            var gradientColors = gradientDrawable!!.getColors()
            if (gradientColors == null) {
                // Fallback colors if the drawable doesn't define them
                gradientColors =
                    intArrayOf(-0x10000, -0x100, -0xff0100, -0xff0001, -0xffff01, -0xff01)
            }

            // Apply the gradient as a shader to the text
            val linearGradient = LinearGradient(
                0f, 0f, textView.width.toFloat(), textView.textSize,
                gradientColors,
                null,
                Shader.TileMode.CLAMP
            )
            textView.paint.setShader(linearGradient)
            textView.invalidate() // Redraw the TextView with the gradient
        })
    }
}
