package com.lovejiaming.timemovieinkotlin.views.ownerdraw

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by xiaoxin on 2017/8/30.
 */
class CustomLabelView(ctx: Context, val attr: AttributeSet) : View(ctx, attr) {
    //框
    val rectPaint: Paint by lazy {
        Paint()
    }
    //文字
    val textPaint: Paint by lazy {
        Paint()
    }
    lateinit var m_sText: String

    init {
        rectPaint.isAntiAlias = true
        rectPaint.color = Color.BLACK
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 7F
        //
        textPaint.isAntiAlias = true
        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL
        textPaint.strokeWidth = 32f
        textPaint.textSize = 32F
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), rectPaint)
        //
        val bounds = Rect()
        textPaint.getTextBounds(m_sText, 0, m_sText.length, bounds)
        canvas?.drawText(m_sText, measuredWidth / 2 - (textPaint.measureText(m_sText) / 2), (measuredHeight / 2 + bounds.height() / 2).toFloat(), textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    fun setText(text: String) {
        this.m_sText = text
        invalidate()
    }

    fun setColor(color: Int) {
        textPaint.color = color
        rectPaint.color = color
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        setColor(Color.parseColor("#bf360c"))
        return super.onTouchEvent(event)
    }
}