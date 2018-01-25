package info.free.duangjike

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.BounceInterpolator

/**
 * Created by zhufree on 2018/1/23.
 *
 */

class FallDownView : JikeView {

    private var background: Bitmap? = null


    private var flodY = 0f
    private var dotLeft = 0f
    private var dotTop = 0f
        set(value) {
            field = value
            invalidate()
        }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        background = BitmapFactory.decodeResource(resources, R.drawable.black_sky)
        animator = ObjectAnimator.ofFloat(this, "dotTop", 0f, 900f)
        animator?.duration =2500
        animator?.interpolator = BounceInterpolator()
        animLastTime = 3
    }



    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        Log.i(mTAG, "height" + boxHeight + ", width"  +boxWidth)
        flodY = boxHeight*0.6f
        dotLeft = width.div(2) - jikeDot?.width!!.div(2f)
//        开始瞎jier画了
//        背景上半部分，占父布局0.8
        canvas?.save()
        canvas?.clipRect(0f, 0f, boxWidth, boxHeight)
        canvas?.scale(2.1f, 1.4f)  // 先拉伸一下不然不够大
        canvas?.drawBitmap(background, 0f, 0f, paint)
        canvas?.restore()

        canvas?.save()
        camera.save()
        camera.rotateX(60.toFloat())
        canvas?.translate(boxCenterX, flodY)
        camera.applyToCanvas(canvas)
        canvas?.translate((-boxCenterX), (-flodY))
        camera.restore()
        canvas?.clipRect(0f, flodY, boxWidth, boxHeight)
        canvas?.scale(2.1f, 1.4f)  // 先拉伸一下不然不够大
        canvas?.drawBitmap(background, 0f, 0f, paint)
        canvas?.restore()

        canvas?.save()
        canvas?.drawBitmap(jikeDot, dotLeft, dotTop, paint)
        canvas?.restore()
    }
}
