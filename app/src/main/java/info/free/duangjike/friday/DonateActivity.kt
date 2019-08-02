package info.free.duangjike.friday

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_donation.*
import android.widget.Toast
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.storage.StorageManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast.LENGTH_LONG
import androidx.documentfile.provider.DocumentFile
import info.free.duangjike.R
import info.free.duangjike.Util
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class DonateActivity : AppCompatActivity() {
    private var payType = 0; // 0 wechat 1 zhi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation)
        setSupportActionBar(donation_toolbar)
        donation_toolbar?.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        donation_toolbar?.setNavigationOnClickListener { finish() }
        tv_donation_wechat?.setOnClickListener {
            payType = 0
            iv_about_me?.setImageResource(R.drawable.img_donation_wechat)
        }
        tv_donation_alipay?.setOnClickListener {
            payType = 1
            iv_about_me?.setImageResource(R.drawable.img_donation_alipay)
        }
        tv_afdian?.setOnClickListener {
            val updateIntent = Intent()
            updateIntent.action = "android.intent.action.VIEW"
            val updateUrl = Uri.parse("https://afdian.net/@zhufree")
            updateIntent.data = updateUrl
            startActivity(updateIntent)
        }

        iv_about_me?.setOnLongClickListener {
            //            Util.saveBitmapFile((iv_about_me.drawable as BitmapDrawable).bitmap, "donation")
            val sm = getSystemService(StorageManager::class.java)
            val volume = sm?.primaryStorageVolume
            volume?.createAccessIntent(Environment.DIRECTORY_PICTURES)?.also {
                startActivityForResult(it, 101)
            }

            true
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
            Toast.makeText(c, getString(R.string.uninstall_notice, "微信"), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAlipayScan(context: Context) {
        try {
            val uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (isActivityAvailable(context, intent)) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, getString(R.string.uninstall_notice, "支付宝"), Toast.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.data?.let { uri ->
            contentResolver?.takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            val df = DocumentFile.fromTreeUri(this, uri)
            val fridayPicDir = df?.findFile("Friday") // 获取文件夹

            fridayPicDir?.createFile("image/jpg", "donation.jpg")?.also {
                doAsync {
                    val pfd = contentResolver.openFileDescriptor(it.uri, "w")
                    // 打开文件流，要注意是读取还是写入，r和w，使用w会重写覆盖原文件
                    pfd?.let { p ->
                        val fileOutputStream = FileOutputStream(p.fileDescriptor)
                        (iv_about_me.drawable as BitmapDrawable).bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                        fileOutputStream.close()
                        uiThread {
                            toast(R.string.jump_notice)
                            Handler().postDelayed({
                                if (payType == 0) startWechatScan(this@DonateActivity) else openAlipayScan(this@DonateActivity)
                            }, 1000)
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
