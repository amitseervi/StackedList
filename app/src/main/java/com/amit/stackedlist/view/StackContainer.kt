package com.amit.stackedlist.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children

class StackContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val stackItems: MutableList<StackItemView> = mutableListOf()
    private var visibleChild = 0
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (!changed) {
            return
        }
        var top = t
        for (i in 0 until visibleChild) {
            stackItems[i].onLayoutCollapsedView(
                l = l,
                t = top,
                r = r,
                b = top + stackItems[i].measuredCollapsedHeight
            )
            top += stackItems[i].measuredCollapsedHeight
        }
        stackItems[visibleChild].onLayoutExpandView(
            l,
            top,
            r,
            top + stackItems[visibleChild].measuredExpandedHeight
        )
    }

    fun showNextChild() {
        if (visibleChild + 1 < stackItems.size) {
            visibleChild += 1
        }
        requestLayout()
    }

    fun showPreviousChild() {
        if (visibleChild - 1 >= 0) {
            visibleChild -= 1
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        stackItems.clear()
        stackItems.addAll(children.filterIsInstance<StackItemView>())
        if (stackItems.size == 0) {
            return setMeasuredDimension(0, 0)
        }
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val selectedWidth = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            0
        }

        val selectedHeight = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            0
        }

        var collapsedHeightSum = 0
        stackItems.forEach { stackChildItemView ->
            stackChildItemView.measure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(
                    maxOf(heightSize - collapsedHeightSum, 0),
                    MeasureSpec.EXACTLY
                )
            )
            collapsedHeightSum += stackChildItemView.measuredCollapsedHeight
        }

        setMeasuredDimension(selectedWidth, selectedHeight)
    }
}