package info.free.duangjike.friday

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import info.free.duangjike.R
import kotlinx.android.synthetic.main.activity_copyright.*

class CopyrightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_copyright)

        setSupportActionBar(copyright_toolbar)
        copyright_toolbar?.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        copyright_toolbar?.setNavigationOnClickListener { finish() }
        tv_visit_website?.setOnClickListener {
            val visitIntent = Intent()
            visitIntent.action = "android.intent.action.VIEW"
            val updateUrl = Uri.parse("http://zhongguose.com/")
            visitIntent.data = updateUrl
            startActivity(visitIntent)
        }
    }
}
