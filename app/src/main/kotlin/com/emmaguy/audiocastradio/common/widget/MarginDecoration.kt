package com.emmaguy.audiocastradio.common.widget

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class MarginDecoration(val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(margin, margin, margin, margin)
    }
}