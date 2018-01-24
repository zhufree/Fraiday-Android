package info.free.duangjike

import android.animation.ObjectAnimator
import android.content.Context
import android.drm.DrmStore
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.widget.ImageView
import kotlin.math.abs

/**
 * Created by zhufree on 2018/1/24.
 *
 */
class FlipJikeView : JikeView {
    private var jikeDot: Bitmap? = null
    private var dotLeft = 0f
    private var dotTop = 0f

    private var eventDownX = 0f
    private var eventDownY = 0f
    private var eventUpX = 0f
    private var eventUpY = 0f

    private var imageView: ImageView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        jikeDot = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)
//        animator = ObjectAnimator.ofFloat(this, "dotTop", 0f, 900f)
//        animator?.duration =2500
//        animator?.interpolator = BounceInterpolator()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        imageView = findViewById(R.id.image_view)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        dotLeft = boxCenterX - jikeDot?.width!!.div(2f)
        dotTop = boxCenterY - jikeDot?.height!!.div(2f)

//        canvas?.save()
//        canvas?.drawBitmap(jikeDot, dotLeft, dotTop, paint)
//        canvas?.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        Log.i(mTAG, "event.x = " + event?.x + ", event.y = " + event?.y)
        requestDisallowInterceptTouchEvent(true)

        when (event?.action) {
            ACTION_DOWN -> {
                eventDownX = event.x
                eventDownY = event.y
            }
            ACTION_UP -> {
                eventUpX = event.x
                eventUpY = event.y
                val x = eventUpX - eventDownX
                val y = eventUpY - eventDownY
                var round = 0
//                Log.i(mTAG, "x = " + x + ", y = " + y)
                if (abs(x) > abs(y)) {
//                    纵向
                    round = (abs(x)/100).toInt()
                    if (x > 50) {
                        //->
                        imageView?.animate()?.rotationYBy(180f*round)
                        Log.i(mTAG, "move right")
                    } else if (x < -50) {
                        //<-
                        imageView?.animate()?.rotationYBy(-180f*round)
                        Log.i(mTAG, "move left")
                    }
                } else {
                    round = (abs(y)/100).toInt()
                    if (y > 50) {
                        //down
                        imageView?.animate()?.rotationXBy(-180f*round)
                        Log.i(mTAG, "move down" + imageView)
                    } else if (y < -50){
                        // up
                        imageView?.animate()?.rotationXBy(180f*round)
                        Log.i(mTAG, "move up")
                    }
                }
            }
        }
        return true
    }
}