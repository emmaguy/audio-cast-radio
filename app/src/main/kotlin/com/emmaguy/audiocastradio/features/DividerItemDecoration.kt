package com.emmaguy.audiocastradio.features

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView

class DividerItemDecoration(val divider: Drawable) : RecyclerView.ItemDecoration() {
    override fun onDrawOver(c: Canvas?, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 1..parent.childCount) {
            val child = parent.getChildAt(i - 1)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}