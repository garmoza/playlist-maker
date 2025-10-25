package com.practicum.playlistmaker.player.ui.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.practicum.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val playImageDrawable: Drawable?
    private val pauseImageDrawable: Drawable?

    private var currentImageDrawable: Drawable? = null
    private val drawableBounds = Rect()

    private var isPlaying = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playImageDrawable = getDrawable(R.styleable.PlaybackButtonView_playImageResId)
                pauseImageDrawable = getDrawable(R.styleable.PlaybackButtonView_pauseImageResId)

                currentImageDrawable = playImageDrawable
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawableBounds.set(
            paddingLeft,
            paddingTop,
            measuredWidth - paddingRight,
            measuredHeight - paddingBottom
        )

        playImageDrawable?.bounds = drawableBounds
        pauseImageDrawable?.bounds = drawableBounds
        currentImageDrawable?.bounds = drawableBounds
    }

    override fun onDraw(canvas: Canvas) {
        currentImageDrawable?.draw(canvas)
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

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun setPlayState(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        currentImageDrawable = if (isPlaying) {
            pauseImageDrawable
        } else {
            playImageDrawable
        }

        invalidate()
    }
}