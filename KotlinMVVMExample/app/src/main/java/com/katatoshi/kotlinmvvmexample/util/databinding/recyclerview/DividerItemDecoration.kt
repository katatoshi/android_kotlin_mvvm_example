package com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.katatoshi.kotlinmvvmexample.R

/**
 * RecyclerView の divider を表示するかどうかのカスタムセッター。
 */
@BindingAdapter("dividerEnabled")
fun RecyclerView.setDividerEnabled(enabled: Boolean) {
    if (enabled) {
        addDividerItemDecoration(this)
    } else {
        removeDividerItemDecoration(this)
    }
}

private fun addDividerItemDecoration(recyclerView: RecyclerView) {
    if (recyclerView.getTag(R.id.key_list_divider_item_decoration) != null) {
        return
    }

    val itemDecoration = DividerItemDecoration(recyclerView.context)
    recyclerView.addItemDecoration(itemDecoration)
    recyclerView.setTag(R.id.key_list_divider_item_decoration, itemDecoration)
}

private fun removeDividerItemDecoration(recyclerView: RecyclerView) {
    (recyclerView.getTag(R.id.key_list_divider_item_decoration) as? DividerItemDecoration)?.let {
        recyclerView.removeItemDecoration(it)
        recyclerView.setTag(R.id.key_list_divider_item_decoration, null)
    }
}

/**
 * RecyclerView 用 divider。
 */
private class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val divider: Drawable

    init {
        val typedArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        divider = typedArray.getDrawable(0)
        typedArray.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val top = child.bottom + (child.layoutParams as RecyclerView.LayoutParams).bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.set(0, 0, 0, divider.intrinsicHeight)
    }
}