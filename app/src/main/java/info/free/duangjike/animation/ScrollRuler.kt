package info.free.duangjike.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import android.view.ViewConfiguration
import info.free.duangjike.animation.JikeView


/**
 * Created by zhufree on 2018/1/26.
 *
 */
class ScrollRuler : JikeView {
    private var totalWidth = 0f
    private var eachWidth = 0f
    private var shortHeight = 0f
    private var middleHeight = 0f
    private var startX = 0f
        set(value) {
            field = value
            if (endTouchFlag) {
                invalidate()
            }
        }

    private var eventDownX = 0f
    private var eventDownY = 0f
    private var eventUpX = 0f
    private var eventUpY = 0f
    private var lastMoveX = 0f
    private var startTouchTime = 0.toLong()
    private var endTouchTime = 0.toLong()
    private var endTouchFlag = false
    private var oneMoveDistance = 0f

    private var mActivePointerId = -1
    private var mMinFlingSpeed = 0
    private var mScroller: Scroller? = null
    var mVelocityTracker = VelocityTracker.obtain()

//    var mGesture = GestureDetector(context, this)
//    #F5F8F5
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        // 刻度50-100，每个数之间10个小刻度，一共50*10个刻度
        eachWidth = screenWidth / 80f
        totalWidth = eachWidth*500
        paint.color = Color.parseColor("#555555")
        mMinFlingSpeed = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    }


    fun onScrollEnd(distanceX: Float) {
        endTouchFlag = true
        endTouchTime = System.currentTimeMillis()
        var time = endTouchTime - startTouchTime
//        初始速度，假设一秒内减为0，则移动0.5v (px/s)
        var velocityX = distanceX/time*1000
        Log.i(mTAG, "time = " + time + ", v = " + velocityX + ", startX = " + startX + ", x = " + distanceX)
        animator = ObjectAnimator.ofFloat(this, "startX",
                startX + distanceX, startX + distanceX + velocityX/2f)
        animator?.duration = 1000
        animator?.interpolator = DecelerateInterpolator()
        animator?.start()
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                endTouchFlag = false
//                最终停下位置 startX + distanceX + velocityX/2f
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }

    override fun overScrollBy(deltaX: Int, deltaY: Int, scrollX: Int, scrollY: Int,
                              scrollRangeX: Int, scrollRangeY: Int, maxOverScrollX: Int,
                              maxOverScrollY: Int, isTouchEvent: Boolean): Boolean {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY, isTouchEvent)
    }

    private fun doFling(speed: Int) {
        if (mScroller == null) {
            return
        }
        mScroller?.fling(0,scrollY,0,speed,0,0,-500,10000);
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller == null) {
            mScroller = Scroller(context)
        }
        if (mScroller!!.computeScrollOffset()) {
            scrollTo(mScroller!!.currX, mScroller!!.currY)
            postInvalidate()
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(true)
        if (mScroller == null) {
            mScroller = Scroller(context)
        }

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                eventDownX = event.x
                eventDownY = event.y
                lastMoveX = eventDownX
                startTouchTime = System.currentTimeMillis()
                oneMoveDistance = 0f
                if (mScroller!!.isFinished) { //fling
                    mScroller!!.abortAnimation()
                }
                mActivePointerId = event.getPointerId(event.actionIndex)
            }
            MotionEvent.ACTION_UP -> {
//                onScrollEnd(oneMoveDistance)
                //当手指离开屏幕时，获得速度，作为fling的初始速度
                // mVelocityTracker.computeCurrentVelocity(1000,mMaxFlingSpeed);
                mActivePointerId = event.getPointerId(event.actionIndex)
                val initialVelocity = mVelocityTracker.getYVelocity(mActivePointerId).toInt()
                Log.i(mTAG, "v = " + initialVelocity)
                if (Math.abs(initialVelocity) > mMinFlingSpeed) {
                    // 由于坐标轴正方向问题，要加负号。
                    doFling(-initialVelocity)
                }

            }
            MotionEvent.ACTION_MOVE -> {
                val eventMoveX = event.x
                val x = eventMoveX - lastMoveX
                lastMoveX = eventMoveX
                oneMoveDistance += -x
                this.scrollBy(-x.toInt(), 0)
            }
        }
        //每次onTouchEvent处理Event时，都将event交给时间
        //测量器
        mVelocityTracker.addMovement(event)

        return true

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(totalWidth.toInt()*2, measuredHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        middleHeight = boxHeight.div(2)
        shortHeight = middleHeight.div(2)
        canvas?.save()
        canvas?.translate(-startX, 0f)
        for (i in 0..500) {
            if (i%10 == 0) {
                canvas?.drawLine(i * eachWidth, 0f, i * eachWidth, middleHeight, paint)
                paint.textSize = 60f
                when (i) {
                    0 -> canvas?.drawText((50 + i / 10).toString(), i * eachWidth + 10f, boxHeight - shortHeight, paint)
                    500 -> canvas?.drawText((50 + i / 10).toString(), i * eachWidth - 100f, boxHeight - shortHeight, paint)
                    else -> canvas?.drawText((50 + i / 10).toString(), i * eachWidth - 30f, boxHeight - shortHeight, paint)
                }
            } else {
                canvas?.drawLine(i * eachWidth, 0f, i * eachWidth, shortHeight, paint)
            }
            canvas?.translate(eachWidth, 0f)
        }
        canvas?.restore()
    }

//    override fun onShowPress(e: MotionEvent?) {
//    }
//
//    override fun onSingleTapUp(e: MotionEvent?): Boolean {
//        return true
//    }
//
//    override fun onDown(e: MotionEvent?): Boolean {
//        return true
//    }
//
//    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
//        Log.i(mTAG, "velocityX" + velocityX)
//        return true
//    }
//
//    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
//        Log.i(mTAG, "distanceX" + distanceX)
//        this.scrollBy(distanceX.toInt(), 0)
//        return true
//    }
//
//    override fun onLongPress(e: MotionEvent?) {
//    }
}