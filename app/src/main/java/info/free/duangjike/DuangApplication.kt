package info.free.duangjike

import android.app.Application
import android.content.Context

/**
 * Created by zhufree on 2018/12/12.
 * 自定义activity
 */

class DuangApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
    companion object {
        lateinit var context: Context
    }
}
