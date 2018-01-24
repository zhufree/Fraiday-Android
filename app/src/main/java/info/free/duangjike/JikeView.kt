package info.free.duangjike

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhufree on 2018/1/23.
 *
 */
open class JikeView : View {
    var animator: ObjectAnimator? = null
    open var mTAG = "JikeView"


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open fun startAnimation() {
        animator?.start()
    }
}