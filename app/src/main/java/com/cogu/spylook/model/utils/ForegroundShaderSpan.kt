package com.cogu.spylook.model.utils

import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance

class ForegroundShaderSpan(private val shader: Shader) : CharacterStyle(), UpdateAppearance {
    override fun updateDrawState(tp: TextPaint) {
        tp.shader = shader
    }
}