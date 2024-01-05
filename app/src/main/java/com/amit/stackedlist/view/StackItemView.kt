package com.amit.stackedlist.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

class StackItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var mCollapsedChildView: CollapsedViewContainer? = null
    private var mExpandedChildView: ExpandedViewContainer? = null
    private var mIsVisible: Boolean = false
    val measuredCollapsedHeight: Int
        get() = mCollapsedChildView.let {
            it?.measuredHeight ?: 0
        }
    val measuredExpandedHeight: Int
        get() = mExpandedChildView.let {
            it?.measuredHeight ?: 0
        }
    val isVisible: Boolean
        get() = mIsVisible

    init {
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        mCollapsedChildView = children.filterIsInstance<CollapsedViewContainer>().firstOrNull()
        mExpandedChildView = children.filterIsInstance<ExpandedViewContainer>().firstOrNull()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mCollapsedChildView = children.filterIsInstance<CollapsedViewContainer>().firstOrNull()
        mExpandedChildView = children.filterIsInstance<ExpandedViewContainer>().firstOrNull()
        if (mExpandedChildView == null) {
            throw RuntimeException("Expanded Child can not be null")
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
        mExpandedChildView?.measure(widthMeasureSpec, heightMeasureSpec)
        mCollapsedChildView?.measure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        setMeasuredDimension(selectedWidth, selectedHeight)
    }

    fun onLayoutCollapsedView(l: Int, t: Int, r: Int, b: Int) {
        mCollapsedChildView?.layout(l, t, r, b)
    }

    fun onLayoutExpandView(l: Int, t: Int, r: Int, b: Int) {
        mExpandedChildView?.layout(l, t, r, b)
    }
}