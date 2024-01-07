package com.amit.stackedlist.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.children
import com.amit.stackedlist.R
import kotlin.math.roundToInt

class StackContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val stackItems: MutableList<StackItemView> = mutableListOf()
    private var animationDuration: Long
    private var mCurrentAnimator: ValueAnimator? = null
    private var mCurrentItemIndex = 0
    private var mOnPositionChangeCallback: OnPositionChangeCallback? = null

    init {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.StackItemView, 0, 0)
        animationDuration = try {
            typedArray.getInteger(R.styleable.StackItemView_stackAnimationDuration, 500).toLong()
        } catch (e: Exception) {
            500
        }
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
        var collapsedStackHeight = 0
        stackItems.forEach { stackItemView ->
            stackItemView.layout(
                leftPosition,
                topPosition + collapsedStackHeight,
                leftPosition + stackItemView.measuredWidth,
                topPosition + collapsedStackHeight + stackItemView.measuredHeight
            )
            collapsedStackHeight += stackItemView.measuredCollapsedHeight
        }
    }

    fun showNextChild(): Boolean {
        val currentIndex = mCurrentItemIndex
        if (currentIndex + 1 == stackItems.size) {
            return false
        }
        mCurrentAnimator?.let { currentAnimator ->
            if (currentAnimator.isRunning) {
                return false
            }
        }
        val animator = ValueAnimator.ofFloat(0f, 1f)
        mCurrentAnimator = animator
        animator.duration = animationDuration
        animator.addUpdateListener {
            val animationValue: Float = it.animatedValue as Float
            stackItems[currentIndex].setAnimationValue(1f + animationValue)
            stackItems[currentIndex + 1].setAnimationValue(animationValue)
        }
        animator.doOnStart {
            stackItems[currentIndex + 1].showExpandedView()
            stackItems[currentIndex].showCollapsedView()
            stackItems[currentIndex + 1].visibility = View.VISIBLE
        }
        animator.doOnEnd {
            stackItems[currentIndex].hideExpandedView()
            stackItems[currentIndex + 1].hideCollapsedView()
            stackItems[currentIndex].onCollapsed()
            stackItems[currentIndex + 1].onExpanded()
            mCurrentItemIndex = currentIndex + 1
            notifyPositionChange()
        }
        animator.start()
        return true
    }

    private fun notifyPositionChange() {
        mOnPositionChangeCallback?.onPositionChange(mCurrentItemIndex)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null) {
            return super.onInterceptTouchEvent(ev)
        }
        val currentView = stackItems.getOrNull(mCurrentItemIndex) ?: return false
        val currentViewHeight = currentView.measuredHeight
        if (ev.y >= measuredHeight - currentViewHeight) {
            println("Touch are in visible child")
            return false
        }
        return true
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        ev ?: return false
        val currentView = stackItems.getOrNull(mCurrentItemIndex) ?: return false
        val currentViewHeight = currentView.measuredHeight
        if (ev.y >= measuredHeight - currentViewHeight) {
            return false
        }
        if (ev.action == MotionEvent.ACTION_DOWN) {
            showPreviousChild(getCollapsedIndexForTouchY(ev.y.roundToInt()))
            return true
        }
        return false
    }

    private fun getCollapsedIndexForTouchY(y: Int): Int {
        var ans = 0
        stackItems.forEachIndexed { index, view ->
            if (y < measuredHeight - view.measuredHeight) {
                return ans
            }
            ans = index
        }
        return ans
    }

    fun getCurrentStackPosition(): Int {
        return mCurrentItemIndex
    }

    fun showPreviousChild(): Boolean {
        val currentIndex = mCurrentItemIndex
        if (currentIndex == 0) {
            return false
        }
        mCurrentAnimator?.let { currentAnimator ->
            if (currentAnimator.isRunning) {
                return false
            }
        }
        val animator = ValueAnimator.ofFloat(1f, 0f)
        mCurrentAnimator = animator
        animator.duration = animationDuration
        animator.addUpdateListener {
            val animationValue: Float = it.animatedValue as Float
            stackItems[currentIndex].setAnimationValue(animationValue)
            stackItems[currentIndex - 1].setAnimationValue(1 + animationValue)
        }
        animator.doOnStart {
            stackItems[currentIndex - 1].showExpandedView()
        }
        animator.doOnEnd {
            stackItems[currentIndex - 1].hideCollapsedView()
            stackItems[currentIndex].hideExpandedView()
            stackItems[currentIndex].visibility = View.GONE
            stackItems[currentIndex].onHidden()
            stackItems[currentIndex - 1].onExpanded()
            mCurrentItemIndex = currentIndex - 1
            notifyPositionChange()
        }
        animator.start()
        return true
    }

    fun showPreviousChild(index: Int): Boolean {
        val currentIndex = mCurrentItemIndex
        if (currentIndex == 0) {
            return false
        }
        if (index < 0) {
            return false
        }
        if (index >= currentIndex) {
            return false
        }
        mCurrentAnimator?.let { currentAnimator ->
            if (currentAnimator.isRunning) {
                return false
            }
        }
        val animator = ValueAnimator.ofFloat(1f, 0f)
        mCurrentAnimator = animator
        animator.duration = animationDuration
        animator.addUpdateListener {
            val animationValue: Float = it.animatedValue as Float
            for (i in (index + 1)..currentIndex) {
                stackItems[i].setAnimationValue(animationValue)
            }
            stackItems[index].setAnimationValue(1 + animationValue)
        }
        animator.doOnStart {
            stackItems[index].showExpandedView()
        }
        animator.doOnEnd {
            stackItems[index].hideCollapsedView()
            stackItems[index].onExpanded()
            for (i in (index + 1)..currentIndex) {
                stackItems[i].hideExpandedView()
                stackItems[i].visibility = View.GONE
                stackItems[i].onHidden()
            }
            mCurrentItemIndex = index
            notifyPositionChange()
        }
        animator.start()
        return true
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        stackItems.clear()
        stackItems.addAll(children.filterIsInstance<StackItemView>())
        for (i in 1 until stackItems.size) {
            stackItems[i].visibility = View.GONE
        }
        applyStateToChild()
    }

    private fun applyStateToChild() {
        if (stackItems.isEmpty()) {
            return
        }
        if (mCurrentItemIndex >= stackItems.size) {
            return
        }
        stackItems[mCurrentItemIndex].setState(true, true)
        for (i in 0 until mCurrentItemIndex) {
            stackItems[i].setState(true, false)
        }
        for (i in (mCurrentItemIndex + 1) until stackItems.size) {
            stackItems[i].setState(false, false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
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
        val horizontalPadding = paddingRight + paddingLeft
        val verticalPadding = paddingTop + paddingBottom
        stackItems.forEach { stackChildItemView ->
            stackChildItemView.measure(
                MeasureSpec.makeMeasureSpec(
                    maxOf(widthSize - horizontalPadding, 0),
                    MeasureSpec.EXACTLY
                ),
                MeasureSpec.makeMeasureSpec(
                    maxOf(heightSize - verticalPadding - collapsedHeightSum, 0),
                    MeasureSpec.EXACTLY
                )
            )
            collapsedHeightSum += stackChildItemView.measuredCollapsedHeight
        }
        setMeasuredDimension(selectedWidth, selectedHeight)
    }

    fun setPositionChangeCallback(onPositionChangeCallback: OnPositionChangeCallback) {
        this.mOnPositionChangeCallback = onPositionChangeCallback
    }

    interface OnPositionChangeCallback {
        fun onPositionChange(position: Int)
    }
}