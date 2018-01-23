package info.free.duangjike

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout

/**
 * Created by zhufree on 2018/1/23.
 *
 */

class FallDownLayout : LinearLayout {
    /*
    * ic_launcher_round 即刻圆图标
    * ic_discovertab_entrance_category 搜索图
    * ic_discovertab_entrance_custom_topic 机器人
    * ic_discovertab_entrance_daily 键盘？
    * ic_discovertab_entrance_rankinglist 奖杯*/
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var background: Bitmap? = null
    private var jikeDot: Bitmap? = null
    private var camera = Camera()
    private var animator: ObjectAnimator? = null

    private var boxWidth = 0f
    private var boxHeight = 0f
    private var flodY = 0f
    private var centerX = 0f
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
        jikeDot = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)
        background = BitmapFactory.decodeResource(resources, R.drawable.black_sky)
        animator = ObjectAnimator.ofFloat(this, "dotTop", 0f, 700f)
        animator?.duration =2500
        animator?.interpolator = BounceInterpolator()
    }



    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator?.start()
    }



    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        boxWidth = width.toFloat()
        boxHeight = height.toFloat()
        flodY = boxHeight*0.6f
        centerX = boxWidth*0.5f
        dotLeft = width.div(2) - jikeDot?.width!!.div(2f)
//        开始瞎jier画了
//        背景上半部分，占父布局0.8
        canvas?.save()
        canvas?.clipRect(0f, 0f, boxWidth, boxHeight)
        canvas?.scale(2.1f, 1.6f)  // 先拉伸一下不然不够大
        canvas?.drawBitmap(background, 0f, 0f, paint)
        canvas?.restore()

        canvas?.save()
        camera.save()
        camera.rotateX(60.toFloat())
        canvas?.translate(centerX, flodY)
        camera.applyToCanvas(canvas)
        canvas?.translate((-centerX), (-flodY))
        camera.restore()
        canvas?.clipRect(0f, flodY, boxWidth, boxHeight)
        canvas?.scale(2.1f, 1.6f)  // 先拉伸一下不然不够大
        canvas?.drawBitmap(background, 0f, 0f, paint)
        canvas?.restore()

        canvas?.save()
        canvas?.drawBitmap(jikeDot, dotLeft, dotTop, paint)
        canvas?.restore()
    }
}
