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
    private var mIsExpanded: Boolean = false
    val measuredCollapsedHeight: Int
        get() = mCollapsedChildView.let {
            it?.measuredHeight ?: 0
        }

    fun setState(visible: Boolean, expanded: Boolean) {
        mIsVisible = visible
        mIsExpanded = expanded

        mExpandedChildView?.let { v ->
            if (visible && expanded) {
                v.visibility = View.VISIBLE
            } else {
                v.visibility = View.GONE
            }
        }

        mCollapsedChildView?.let { v ->
            if (visible && !expanded) {
                v.visibility = View.VISIBLE
            } else {
                v.visibility = View.GONE
            }
        }
    }

    fun showCollapsedView() {
        mCollapsedChildView?.visibility = View.VISIBLE
    }

    fun showExpandedView() {
        mExpandedChildView?.visibility = View.VISIBLE
    }

    fun hideCollapsedView() {
        mCollapsedChildView?.visibility = View.GONE
    }

    fun hideExpandedView() {
        mExpandedChildView?.visibility = View.GONE
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val leftPosition = paddingLeft
        val rightPosition = r - l - paddingRight
        val topPosition = paddingTop
        val bottomPosition = b - t - paddingBottom
        if (leftPosition > rightPosition) {
            return
        }
        if (topPosition > bottomPosition) {
            return
        }

        mExpandedChildView?.let { expandedViewContainer ->
            expandedViewContainer.layout(
                leftPosition,
                topPosition,
                leftPosition + expandedViewContainer.measuredWidth,
                topPosition + expandedViewContainer.measuredHeight
            )
        }
        mCollapsedChildView?.let { collapsedViewContainer ->
            collapsedViewContainer.layout(
                leftPosition, topPosition,
                leftPosition + collapsedViewContainer.measuredWidth,
                topPosition + collapsedViewContainer.measuredWidth
            )
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mCollapsedChildView = children.filterIsInstance<CollapsedViewContainer>().firstOrNull()
        mExpandedChildView = children.filterIsInstance<ExpandedViewContainer>().firstOrNull()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
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

        val horizontalPadding = paddingRight + paddingLeft
        val verticalPadding = paddingTop + paddingBottom
        mExpandedChildView?.measure(
            MeasureSpec.makeMeasureSpec(
                maxOf(
                    widthSize - horizontalPadding,
                    0
                ), MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(
                maxOf(
                    heightSize - verticalPadding,
                    0
                ), MeasureSpec.EXACTLY
            ),
        )
        mCollapsedChildView?.measure(
            MeasureSpec.makeMeasureSpec(
                maxOf(
                    widthSize - horizontalPadding,
                    0
                ), MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        setMeasuredDimension(selectedWidth, selectedHeight)
    }

    fun setAnimationValue(v: Float) {
        val collapsedAlpha: Float
        val expandedAlpha: Float
        val translationYFactor: Float
        if (v > 1) {
            collapsedAlpha = v - 1f
            expandedAlpha = 2f - v
            translationYFactor = 0f // keep it at the top
        } else {
            collapsedAlpha = 0f
            expandedAlpha = v
            translationYFactor = 1f - v
        }
        mCollapsedChildView?.let { collapsedViewContainer ->
            collapsedViewContainer.alpha = collapsedAlpha
        }
        mExpandedChildView?.let { expandedViewContainer ->
            expandedViewContainer.alpha = expandedAlpha
        }
        this.translationY = measuredHeight * translationYFactor
    }
}