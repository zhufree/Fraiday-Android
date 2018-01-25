package info.free.duangjike

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Created by zhufree on 2018/1/23.
 *
 */
open class JikeView : RelativeLayout {
    /*
    * ic_launcher_round 即刻圆图标
    * ic_launcher 即刻方图标
    * ic_discovertab_entrance_category 搜索图
    * ic_discovertab_entrance_custom_topic 机器人
    * ic_discovertab_entrance_daily 键盘？
    * ic_discovertab_entrance_rankinglist 奖杯
    * ic_messages_like_unselected 未点赞状态的手（灰
    * ic_messages_like_selected 点赞的手
    * ic_messages_like_selected_shining 点赞的闪光
    * ic_messages_collect_unselected 未收藏的星星（灰
    * ic_messages_collect_selected 收藏的星星
    * */
    var jikeLogo: Bitmap? = null
    var jikeDot: Bitmap? = null
    var animator: ObjectAnimator? = null
    var animatorSet = AnimatorSet()
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var camera = Camera()

    var boxWidth = 0f
    var boxHeight = 0f
    var boxCenterX = 0f
    var boxCenterY = 0f

    var mTAG = "JikeView"

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        jikeLogo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        jikeDot = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)
    }

    open fun startAnimation() {
        animator?.start()
        animatorSet.start()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        boxWidth = width.toFloat()
        boxHeight = height.toFloat()
        boxCenterX = boxWidth.div(2f)
        boxCenterY = boxHeight.div(2f)
    }
}