package info.free.duangjike.friday

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import info.free.duangjike.R

/**
 * Created by zhufree on 2018/12/13.
 *
 */

class FridayWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val remoteViews = FridayRemoteView(context.packageName)
        remoteViews.updateDayText()
        val intent = Intent(context, FridayActivity::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.tv_week_day, pendingIntent)

        for (appWidgetId in appWidgetIds) {
            appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
        }
    }

//    override fun onReceive(context: Context, intent: Intent) {
//        super.onReceive(context, intent)
//    }
}
