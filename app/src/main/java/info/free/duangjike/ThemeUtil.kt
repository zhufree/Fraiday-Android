package info.free.duangjike

import android.graphics.drawable.GradientDrawable

/**
 * Created by zhufree on 2018/12/12.
 *
 */

object ThemeUtil {
    var scale = -1f
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(dpValue: Int): Float {
        val context = FridayApplication.context
        if (scale == -1f) {
            scale = context.resources.displayMetrics.density
        }
        return (dpValue * scale + 0.5f)
    }
    /**
     *
     * @param fillColor
     * //填充色
     * @param strokeColor
     * //边框颜色
     * @param strokeWidth
     * //边框宽度
     * @param roundRadius
     * //圆角值
     * @return
     */
    fun customShape(fillColor: Int, strokeColor: Int, strokeWidth: Int, roundRadius: Float)
            : GradientDrawable {
        val gd = GradientDrawable() // 创建drawable
        gd.setColor(fillColor)
        gd.cornerRadius = roundRadius
        gd.setStroke(strokeWidth, strokeColor)
        return gd
    }

    fun getScreenWidth(): Int {
        return FridayApplication.context.getResources().getDisplayMetrics().widthPixels
    }
    fun getScreenHeight(): Int {
        return FridayApplication.context.getResources().getDisplayMetrics().heightPixels
    }
}
