package com.magic.gestureviewflipper.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import com.magic.gestureviewflipper.R

class GestureViewFlipper : ViewFlipper, GestureDetector.OnGestureListener {
    private var ctx: Context? = null
    private var gestureDetector: GestureDetector? = null
    private lateinit var flipperFocusChangedListener: FlipperFocusChangedListener

    constructor(mContext: Context?) : super(mContext) {}
    constructor(mContext: Context?, attrs: AttributeSet?) : super(mContext, attrs) {
        this.ctx = mContext
        gestureDetector = GestureDetector(mContext, this)
        isLongClickable = true
        setOnTouchListener { v, event -> gestureDetector!!.onTouchEvent(event) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        return gestureDetector!!.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        gestureDetector!!.onTouchEvent(ev)
        super.dispatchTouchEvent(ev)
        return true
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e2.y - e1.y > 30) {
            //滑动开始时，暂停轮播
            stopFlipping()
            isAutoStart = false
            //从上向下滑动
            inAnimation = AnimationUtils.loadAnimation(
                ctx,
                R.anim.banner_notice_down_in
            )
            outAnimation = AnimationUtils.loadAnimation(
                ctx,
                R.anim.banner_notice_down_out
            )
            showPrevious()
            //手动滑动两秒后重新自动轮播
            delay(0, 2000, "upToDown")
            return true
        }
        if (e1.y - e2.y > 30) {
            //滑动开始时，暂停轮播
            stopFlipping()
            isAutoStart = false
            //从下向上滑动
            inAnimation = AnimationUtils.loadAnimation(
                ctx,
                R.anim.banner_notice_up_in
            )
            outAnimation = AnimationUtils.loadAnimation(
                ctx,
                R.anim.banner_notice_up_out
            )
            showNext()
            //手动滑动两秒后重新自动轮播
            delay(0, 2000, "downToUp")
            return true
        }
        return true
    }

    fun playFlipping() {
        inAnimation = AnimationUtils.loadAnimation(
            ctx,
            R.anim.banner_notice_up_in
        )
        outAnimation = AnimationUtils.loadAnimation(
            ctx,
            R.anim.banner_notice_up_out
        )
        isAutoStart = true
        flipInterval = 2000
        startFlipping()
    }

    override fun showNext() {
        super.showNext()
        flipperFocusChangedListener.onFlipperChanged(displayedChild)
    }

    override fun showPrevious() {
        super.showPrevious()
        flipperFocusChangedListener.onFlipperChanged(displayedChild)
    }

    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {}

    interface FlipperFocusChangedListener {
        fun onFlipperChanged(index: Int)
    }

    fun setOnFocusChangedListener(flipperFocusChangedListener: FlipperFocusChangedListener) {
        this.flipperFocusChangedListener = flipperFocusChangedListener
    }

    fun delay(tag: Int, timeMillis: Long, params: Any?) {
        handler.removeMessages(tag)
        val msg = handler.obtainMessage()
        msg.obj = params
        msg.what = tag
        handler.sendMessageDelayed(msg, timeMillis)
    }

    private val handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                val what = msg.obj as String
                if (what == "upToDown") {
                    inAnimation = AnimationUtils.loadAnimation(
                        ctx,
                        R.anim.banner_notice_up_in
                    )
                    outAnimation = AnimationUtils.loadAnimation(
                        ctx,
                        R.anim.banner_notice_up_out
                    )
                    showNext()
                    playFlipping()
                } else if (what == "downToUp") {
                    showNext()
                    playFlipping()
                }
            }
        }
    }
}