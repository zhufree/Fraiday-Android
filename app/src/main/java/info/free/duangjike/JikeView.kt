package info.free.duangjike

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * Created by zhufree on 2018/1/23.
 *
 */
open class JikeView : RelativeLayout {
    /*
    * ic_launcher_round 即刻圆图标
    * ic_discovertab_entrance_category 搜索图
    * ic_discovertab_entrance_custom_topic 机器人
    * ic_discovertab_entrance_daily 键盘？
    * ic_discovertab_entrance_rankinglist 奖杯
    * */
    var animator: ObjectAnimator? = null
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var camera = Camera()

    var boxWidth = 0f
    var boxHeight = 0f
    var boxCenterX = 0f
    var boxCenterY = 0f


    open var mTAG = "JikeView"


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open fun startAnimation() {
        animator?.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        boxWidth = width.toFloat()
        boxHeight = height.toFloat()
        boxCenterX = boxWidth.div(2f)
        boxCenterY = boxHeight.div(2f)
    }
}