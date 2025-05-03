package com.cogu.spylook.model.decorators;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.cogu.spylook.R;
import com.cogu.spylook.model.unimplemented.TextViewDecorator;

import lombok.Getter;

public class RainbowTextViewDecorator implements TextViewDecorator {
    private final Context context;
    private final int drawableResourceId;
    @Getter
    private TextView textView;
    public RainbowTextViewDecorator(Context context, TextView textView) {
        this.context = context;
        this.drawableResourceId = R.drawable.rainbow_gradient;
        this.textView = textView;
    }

    @Override
    public void apply() {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            textView.getViewTreeObserver().removeOnGlobalLayoutListener(() -> {
            });
            GradientDrawable gradientDrawable =
                    (GradientDrawable) AppCompatResources.getDrawable(context, drawableResourceId);

            // Get the gradient colors
            int[] gradientColors = gradientDrawable.getColors();
            if (gradientColors == null) {
                // Fallback colors if the drawable doesn't define them
                gradientColors = new int[]{0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF};
            }

            // Apply the gradient as a shader to the text
            LinearGradient linearGradient = new LinearGradient(
                    0, 0, textView.getWidth(), textView.getTextSize(),
                    gradientColors,
                    null,
                    Shader.TileMode.CLAMP
            );
            textView.getPaint().setShader(linearGradient);
            textView.invalidate(); // Redraw the TextView with the gradient
        });
    }
}
