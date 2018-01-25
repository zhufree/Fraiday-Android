package info.free.duangjike

import android.animation.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet

/**
 * Created by zhufree on 2018/1/24.
 * 仿flipBoard翻页效果
 */
class FlipBoardView : JikeView {
    private var jikeLogo: Bitmap? = null
    private var dotLeft = 0f
    private var dotTop = 0f
    var flopDegree1 = 0f
        set(value) {
            field = value
            invalidate()
        }
    var flopDegree2 = 0f
        set(value) {
            field = value
            invalidate()
        }
    var rotateDegree = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var animator1: ObjectAnimator? = null
    private var animator2: ObjectAnimator? = null
    private var animator3: ObjectAnimator? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        jikeLogo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
//        第一段，右侧向上叠起
//        第二段，保持该角度旋转270
//        第三段，上侧向上叠起，下侧保持之前的状态，两边都叠起
        animator1 = ObjectAnimator.ofFloat(this, "flopDegree1", 0f, -45f)
        animator2 = ObjectAnimator.ofFloat(this, "rotateDegree", 0f, 270f)
        animator3 = ObjectAnimator.ofFloat(this, "flopDegree2", 0f, 45f)
        animator1?.duration = 500
        animator2?.duration = 2000
        animator3?.duration = 500
        animatorSet.playSequentially(animator1, animator2, animator3)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }


            override fun onAnimationEnd(animation: Animator?) {
                val runnable = object : Runnable {
                    override fun run() {
                        flopDegree1 = 0f
                        flopDegree2 = 0f
                        rotateDegree = 0f
                        invalidate()
                    }
                }
                handler.postDelayed(runnable, 1000)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        dotLeft = boxCenterX - jikeLogo?.width!!.div(2)
        dotTop = boxCenterY - jikeLogo?.height!!.div(2)

        // 固定的半边
        canvas?.save()
        camera.save()
        camera.rotateZ(rotateDegree)
        camera.rotateY(flopDegree2)
        canvas?.translate(boxCenterX, boxCenterY)
        camera.applyToCanvas(canvas)
        canvas?.translate(-boxCenterX, -boxCenterY)
        camera.restore()
        canvas?.clipRect(0f, 0f, boxCenterX, boxHeight)
//        canvas?.clipRect(0f, 0f, boxWidth, boxHeight)
        canvas?.rotate(rotateDegree, boxCenterX, boxCenterY)
        canvas?.drawBitmap(jikeLogo, dotLeft, dotTop, paint)
        canvas?.restore()

        // 旋转的半边
        canvas?.save()
        camera.save()
        camera.rotateZ(rotateDegree)
        camera.rotateY(flopDegree1)
        canvas?.translate(boxCenterX, boxCenterY)
        camera.applyToCanvas(canvas)
        canvas?.translate(-boxCenterX, -boxCenterY)
        camera.restore()
        canvas?.clipRect(boxCenterX, 0f, boxWidth, boxHeight)
        canvas?.rotate(rotateDegree, boxCenterX, boxCenterY)
        canvas?.drawBitmap(jikeLogo, dotLeft, dotTop, paint)
        canvas?.restore()
    }
}