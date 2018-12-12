package info.free.duangjike

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_friday.*
import android.R.attr.bitmap
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View.GONE
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException


class FridayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friday)

        val white = resources.getColor(R.color.white)

        val typeface = Typeface.createFromAsset(assets, "simsun.ttc")
        tv_is_friday?.typeface = typeface
        tv_is_friday?.paint?.isFakeBoldText = true
        tv_question?.typeface = typeface
        tv_question.post{
            tv_question.background = ThemeUtil.customShape(white,white,0,
                    tv_question.height.toFloat()/2)
        }

        tv_set_lock?.setOnClickListener {
            try {
                val wpm = getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
                Flowable.create<Bitmap>({ emitter ->
                    val bitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                            Bitmap.Config.RGB_565)
                    //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
                    val canvas = Canvas(bitmap)
                    cl_picture_container?.draw(canvas)
                    emitter.onNext(bitmap)
                    emitter.onComplete()
                }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            wpm.setBitmap(it)
                        }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
