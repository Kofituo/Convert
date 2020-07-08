package com.otuolabs.unitconverter.miscellaneous

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan

//borrowed
class RedUnderline(resources: Resources, color: Int, lineWidth: Int, waveSize: Int) :
    DynamicDrawableSpan(ALIGN_BASELINE) {
    private var width = 0
    private var lineWidth: Int
    private var waveSize: Int
    private var color: Int

    constructor(resources: Resources) : this(resources, Color.RED, 1, 3)

    override fun getSize(
        paint: Paint, text: CharSequence?,
        start: Int, end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        width = paint.measureText(text, start, end).toInt()
        return width
    }

    override fun getDrawable(): Drawable? = null

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val mPaint = Paint(paint)
        mPaint.color = color
        mPaint.strokeWidth = lineWidth.toFloat()
        val doubleWaveSize = waveSize * 2
        var i = x.toInt()
        while (i < x + width) {
            canvas.drawLine(
                i.toFloat(), bottom.toFloat(),
                (i + waveSize).toFloat(), (bottom - waveSize).toFloat(), mPaint
            )
            canvas.drawLine(
                (i + waveSize).toFloat(),
                (bottom - waveSize).toFloat(),
                (i + doubleWaveSize).toFloat(),
                bottom.toFloat(),
                mPaint
            )
            i += doubleWaveSize
        }
        canvas.drawText(text?.subSequence(start, end).toString(), x, y.toFloat(), mPaint)
    }

    init {
        // Get the screen's density scale
        val scale: Float = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        this.lineWidth = (lineWidth * scale + 0.5f).toInt()
        this.waveSize = (waveSize * scale + 0.5f).toInt()
        this.color = color
    }
}