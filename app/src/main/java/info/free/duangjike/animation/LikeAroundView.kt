package info.free.duangjike.animation

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import info.free.duangjike.R

/**
 * Created by zhufree on 2018/1/25.
 *
 */
class LikeAroundView : JikeView {
    private var dotLeft = 0f
    private var dotTop = 0f
    private var handLeft = 0f
    private var handTop = 0f
    private var rotateDegree = 0
        set(value) {
            field = value
            if (hands.contains(value)) {
                invalidate()
            }
        }
    private var curDegree = 0

    private var likeHand : Bitmap? = null
    private var shining : Bitmap? = null
    private var hands : IntArray = intArrayOf(0, 20, 40, 60, 80, 100, 120,
            140, 160, 180, 200, 220, 240,
            260, 280, 300, 320, 340, 360)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        shining = BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected_shining)
        likeHand = BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected)
        animator = ObjectAnimator.ofInt(this, "rotateDegree", 0, 360)
        animator?.duration = 3000
        animator?.interpolator = LinearInterpolator()
        animLastTime = 18
    }

    override fun startAnimation() {
        rotateDegree = 0
        if (gifFlag) {
            animator?.duration = 18000
        } else {
            animator?.duration = 3000
        }
        super.startAnimation()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        dotLeft = boxCenterX - jikeDot?.width!!.div(2)
        dotTop = boxCenterY - jikeDot?.height!!.div(2)
        handLeft = boxCenterX - likeHand?.width!!.div(2)
        handTop = boxCenterY - jikeDot?.width!!.toFloat() - likeHand?.width!!.toFloat() - 20

        canvas?.save()
        canvas?.scale(2f, 2f, boxCenterX, boxCenterY)
        canvas?.drawBitmap(jikeDot, dotLeft, dotTop, paint)
        canvas?.restore()

        curDegree = rotateDegree
        hands.filter {
            it <= curDegree
        }.forEach {
            canvas?.save()
            canvas?.rotate(it.toFloat(), boxCenterX, boxCenterY)
            canvas?.drawBitmap(likeHand, handLeft, handTop, paint)
            canvas?.drawBitmap(shining, handLeft + 7, handTop - 30, paint)
            canvas?.restore()
        }
    }
}
