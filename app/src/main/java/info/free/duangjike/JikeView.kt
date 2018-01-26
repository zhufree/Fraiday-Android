package info.free.duangjike

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by zhufree on 2018/1/23.
 *
 */
open class JikeView : RelativeLayout {
    /*
    * ic_launcher_round 即刻圆图标
    * ic_launcher 即刻方图标
    * ic_discovertab_entrance_category 搜索图
    * ic_discovertab_entrance_custom_topic 机器人
    * ic_discovertab_entrance_daily 键盘？
    * ic_discovertab_entrance_rankinglist 奖杯
    * ic_messages_like_unselected 未点赞状态的手（灰
    * ic_messages_like_selected 点赞的手
    * ic_messages_like_selected_shining 点赞的闪光
    * ic_messages_collect_unselected 未收藏的星星（灰
    * ic_messages_collect_selected 收藏的星星
    * */
    var jikeLogo: Bitmap? = null
    var jikeDot: Bitmap? = null
    var animator: ObjectAnimator? = null
    var animatorSet = AnimatorSet()
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var camera = Camera()
    var jikeYellow = Color.parseColor("#FED710")

    var screenWidth = 0
    var screenHeight = 0
    var boxWidth = 0f
    var boxHeight = 0f
    var boxCenterX = 0f
    var boxCenterY = 0f
    var animLastTime = 0  // 动画持续时常，秒
    val animatedGifEncoder = AnimatedGifEncoder()
    var tempBitmap: Bitmap? = null
    var gifCanvas: Canvas? = null
    var byteArrayOutputStream = ByteArrayOutputStream()
    var gifFlag = false
    var gifFileName = ""

    var mTAG = "JikeView"

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        screenWidth = context.resources.displayMetrics.widthPixels
        screenHeight = context.resources.displayMetrics.heightPixels
        jikeLogo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        jikeDot = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)
        tempBitmap = Bitmap.createBitmap(screenWidth, screenWidth, Bitmap.Config.RGB_565)
        gifCanvas = Canvas(tempBitmap)
    }

    open fun startAnimation() {
        animator?.start()
        animatorSet.start()
    }

    @Throws(IOException::class)
    fun createGif(filename: String, width: Int) {
//        设置一个flag，播放动画时把时间拉长好有空处理bitmap
        gifFlag = true

        animatedGifEncoder.start(byteArrayOutputStream)//start
        animatedGifEncoder.setRepeat(0)//设置生成gif的开始播放次数，默认为1，0为立即开始播放
        animatedGifEncoder.setDelay(20) // 每帧之间的间隔，毫秒
        gifFileName = filename
        startAnimation()

        var i = 0

        val runnable = object : Runnable {
            override fun run() {
                if (i < animLastTime) {
                    gifCanvas?.save()
                    val clipTop = (screenHeight - boxHeight) + boxCenterY - width.div(2f)
                    val clipBottom = (screenHeight - boxHeight) + boxCenterY + width.div(2f)
                    gifCanvas?.translate(0f, -clipTop)
                    gifCanvas?.clipRect(0f, -clipTop, width.toFloat(), clipBottom)
                    rootView.draw(gifCanvas)
                    gifCanvas?.restore()
                    i++
                    Log.i(mTAG, "i = " + i)
                    animatedGifEncoder.addFrame(tempBitmap)
                    handler.postDelayed(this, 1000)
                } else {
                    handler.removeCallbacks(this)
                    Log.i(mTAG, "finish")
                    animatedGifEncoder.finish()//finish
                    val file = File(Environment.getExternalStorageDirectory().path + "/DuangJike")
                    if (!file.exists()) file.mkdir()
                    val path = Environment.getExternalStorageDirectory().path + "/DuangJike/" + filename + ".gif"
                    val fos = FileOutputStream(path)
                    byteArrayOutputStream.writeTo(fos)
                    byteArrayOutputStream.flush()
                    fos.flush()
                    byteArrayOutputStream.close()
                    fos.close()
                }
            }
        }
        runnable.run()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        boxWidth = width.toFloat()
        boxHeight = height.toFloat()
        boxCenterX = boxWidth.div(2f)
        boxCenterY = boxHeight.div(2f)
    }
}