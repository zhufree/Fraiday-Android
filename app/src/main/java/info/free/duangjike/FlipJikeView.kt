package info.free.duangjike

import android.content.Context
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

    private var eventDownX = 0f
    private var eventDownY = 0f
    private var eventUpX = 0f
    private var eventUpY = 0f

    private var imageView: ImageView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        imageView = findViewById(R.id.image_view)
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
                        Log.i(mTAG, "move down")
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