package com.lovejiaming.timemovieinkotlin.views.ownerdraw

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lovejiaming.timemovieinkotlin.R
import android.graphics.RectF
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator


/**
 * Created by choujiaming on 2017/9/7.
 */
class CustomRatingView(val ctx: Context, val attr: AttributeSet) : View(ctx, attr) {

    var mRingBkgPaint: Paint
    var mRingRealScorePaint: Paint
    var mTextPaint: Paint
    var mCurrentAngleLength: Float = 0f
    var m_sScore: String = ""

    init {
        //
        mRingBkgPaint = Paint()
        mRingBkgPaint.isAntiAlias = true
        mRingBkgPaint.style = Paint.Style.STROKE
        mRingBkgPaint.color = Color.parseColor("#d9d6c3")
        mRingBkgPaint.strokeCap = Paint.Cap.BUTT
        mRingBkgPaint.strokeWidth = 10F
        //
        mRingRealScorePaint = Paint()
        mRingRealScorePaint.isAntiAlias = true
        mRingRealScorePaint.style = Paint.Style.STROKE
        mRingRealScorePaint.color = ctx.resources.getColor(R.color.colorPrimary)
        mRingRealScorePaint.strokeCap = Paint.Cap.BUTT
        mRingRealScorePaint.strokeWidth = 10F
        //
        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.color = ctx.resources.getColor(R.color.colorPrimary)
        mTextPaint.textAlign = Paint.Align.LEFT
        mTextPaint.textSize = 25f
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //
        canvas?.let {
            //bkgring
            canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, measuredWidth / 2f - 14, mRingBkgPaint)
            //real
            val oval = RectF(14f, 14f, measuredWidth.toFloat() - 14, measuredHeight.toFloat() - 14)
            canvas.drawArc(oval, 270f, mCurrentAngleLength, false, mRingRealScorePaint)
            //draw center text
            val bounds = Rect()
            mTextPaint.getTextBounds(m_sScore, 0, m_sScore.length, bounds)
            canvas.drawText(m_sScore, measuredWidth / 2f - mTextPaint.measureText(m_sScore) / 2, (measuredWidth / 2f + bounds.height() / 2), mTextPaint)
        }
    }

    fun setRating(rating: Float) {
        m_sScore = "${(rating / 10f)}分"
        mCurrentAngleLength = (rating / 100f) * 360
        startAnimationRing(0f, mCurrentAngleLength)
    }

    private fun startAnimationRing(start: Float, end: Float) {
        val progressAnimator = ValueAnimator.ofFloat(start, end)
        progressAnimator.duration = 1500
        progressAnimator.interpolator = AnticipateOvershootInterpolator()
        progressAnimator.setTarget(mCurrentAngleLength)
        progressAnimator.addUpdateListener { animation ->
            mCurrentAngleLength = animation.animatedValue as Float
            invalidate()
        }
        progressAnimator.start()
    }
}