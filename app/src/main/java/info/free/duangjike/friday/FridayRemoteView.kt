package info.free.duangjike.friday

import android.widget.RemoteViews
import info.free.duangjike.R
import info.free.duangjike.Util.today
import java.util.*
import java.util.Calendar.DAY_OF_WEEK

/**
 * Created by zhufree on 2018/12/13.
 *
 */

class FridayRemoteView(packageName: String) : RemoteViews(packageName, R.layout.layout_widget) {


    fun updateDayText() {
        setTextViewText(R.id.tv_widget_date, "${getWeekDayString(today.get(DAY_OF_WEEK))} " +
                "${today.get(Calendar.YEAR)}.${today.get(Calendar.MONTH) + 1}." +
                "${today.get(Calendar.DAY_OF_MONTH)}")
        setTextViewText(R.id.tv_widget_is_friday, if (today.get(DAY_OF_WEEK) == 6) "是" else  "不是")
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
