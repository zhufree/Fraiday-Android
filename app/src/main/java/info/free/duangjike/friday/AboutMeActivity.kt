package info.free.duangjike.friday

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about_me.*
import android.widget.Toast
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast.LENGTH_LONG
import info.free.duangjike.R
import info.free.duangjike.Util


class AboutMeActivity : AppCompatActivity() {
    private var payType = 0; // 0 wechat 1 zhi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        tv_donation_wechat?.setOnClickListener {
            payType = 0
            iv_about_me?.setImageResource(R.drawable.img_donation_wechat)
        }
        tv_donation_alipay?.setOnClickListener {
            payType = 1
            iv_about_me?.setImageResource(R.drawable.img_donation_alipay)
        }

        iv_about_me?.setOnLongClickListener {
            Util.saveBitmapFile((iv_about_me.drawable as BitmapDrawable).bitmap, "scp_donation")
            MediaScannerConnection.scanFile(this, arrayOf(Util.getAlbumStorageDir("SCP").path + "/scp_donation.jpg"),
                    null, null)
            Toast.makeText(this, "正在跳转到微信或支付宝扫一扫，请从相册选取赞赏二维码随意打赏", LENGTH_LONG).show()
            Handler().postDelayed({
                if (payType == 0) startWechatScan(this) else openAlipayScan(this)
            }, 500)
        }
    }

    private fun startWechatScan(c: Context) {
        val intent = Intent()
        intent.component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
        intent.putExtra("LauncherUI.From.Scaner.Shortcut", true)
        intent.flags = 335544320
        intent.action = "android.intent.action.VIEW"

        if (isActivityAvailable(c, intent)) {
            c.startActivity(intent)
        } else {
            Toast.makeText(c, "检测到未安装微信无法打赏，但还是感谢支持", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAlipayScan(context: Context) {
        try {
            val uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (isActivityAvailable(context, intent)) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "检测到未安装支付宝无法打赏，但还是感谢支持", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
        }

    }

    private fun isActivityAvailable(cxt: Context, intent: Intent): Boolean {
        val pm = cxt.packageManager ?: return false
        val list = pm.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list != null && list.size > 0
    }

}
