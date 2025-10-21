package com.practicum.playlistmaker.player.ui.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val playImageBitmap: Bitmap?
    private val pauseImageBitmap: Bitmap?

    private var currentImageBitmap: Bitmap? = null
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private val paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
    }

    private var isPlaying = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playImageBitmap = getDrawable(R.styleable.PlaybackButtonView_playImageResId)?.toBitmap()
                pauseImageBitmap = getDrawable(R.styleable.PlaybackButtonView_pauseImageResId)?.toBitmap()

                currentImageBitmap = playImageBitmap
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val paddingLeft = paddingLeft.toFloat()
        val paddingTop = paddingTop.toFloat()
        val paddingRight = paddingRight.toFloat()
        val paddingBottom = paddingBottom.toFloat()

        imageRect = RectF(
            0f + paddingLeft,
            0f + paddingTop,
            measuredWidth.toFloat() - paddingRight,
            measuredHeight.toFloat() - paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        currentImageBitmap?.let {
            canvas.drawBitmap(it, null, imageRect, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                setPlayState(!isPlaying)
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setPlayState(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        currentImageBitmap = if (isPlaying) {
            pauseImageBitmap
        } else {
            playImageBitmap
        }

        invalidate()
    }
}