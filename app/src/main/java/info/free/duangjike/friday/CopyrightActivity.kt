package info.free.duangjike.friday

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import info.free.duangjike.R
import kotlinx.android.synthetic.main.activity_copyright.*

class CopyrightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_copyright)
        tv_visit_website?.setOnClickListener {
            val visitIntent = Intent()
            visitIntent.action = "android.intent.action.VIEW"
            val updateUrl = Uri.parse("http://zhongguose.com/")
            visitIntent.data = updateUrl
            startActivity(visitIntent)
        }
    }
}
