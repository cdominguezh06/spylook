package com.cogu.spylook.model.utils.decorators

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.cogu.spylook.R

class SpacingItemDecoration(private val context: Context) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val vertical = context.getResources().getDimensionPixelSize(R.dimen.spacing_vertical)
        val horizontal = context.getResources().getDimensionPixelSize(R.dimen.spacing_horizontal)
        outRect.set(horizontal, vertical, horizontal, vertical)
    }
}
