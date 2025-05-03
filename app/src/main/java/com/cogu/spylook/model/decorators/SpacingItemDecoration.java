package com.cogu.spylook.model.decorators;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cogu.spylook.R;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    public SpacingItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int vertical = context.getResources().getDimensionPixelSize(R.dimen.spacing_vertical);
        int horizontal = context.getResources().getDimensionPixelSize(R.dimen.spacing_horizontal);
        outRect.set(horizontal, vertical, horizontal, vertical);
    }
}
