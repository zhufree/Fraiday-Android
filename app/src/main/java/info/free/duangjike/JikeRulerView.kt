package info.free.duangjike

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet

/**
 * Created by zhufree on 2018/1/26.
 *
 */
class JikeRulerView : JikeView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        paint.strokeWidth = 2f
        paint.color = Color.parseColor("#333333")
    }




    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
//        画中间的线
        canvas?.save()
        paint.color = jikeYellow
        paint.strokeWidth = 5f
        canvas?.drawLine(screenWidth/2f, boxHeight - 300f, screenWidth/2f, boxHeight, paint)
        canvas?.restore()
    }
}
