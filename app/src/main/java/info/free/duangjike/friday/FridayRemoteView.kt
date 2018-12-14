package info.free.duangjike.friday

import android.widget.RemoteViews
import info.free.duangjike.R
import info.free.duangjike.Util
import java.util.Calendar.DAY_OF_WEEK

/**
 * Created by zhufree on 2018/12/13.
 *
 */

class FridayRemoteView(packageName: String) : RemoteViews(packageName, R.layout.layout_widget) {

    fun updateDayText() {
        setTextViewText(R.id.tv_week_day, getWeekDayString(Util.today.get(DAY_OF_WEEK)))
    }

    private fun getWeekDayString(day: Int): String {
        return when (day) {
            1 -> "日"
            2 -> "一"
            3 -> "二"
            4 -> "三"
            5 -> "四"
            6 -> "五"
            7 -> "六"
            else -> "五"
        }
    }
}
